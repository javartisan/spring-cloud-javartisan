package com.javartisan.app;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rate limiter implementation is based on token bucket algorithm. There are two parameters:
 * <ul>
 * <li>
 *     burst size - maximum number of requests allowed into the system as a burst
 * </li>
 * <li>
 *     average rate - expected number of requests per second (RateLimiters using MINUTES is also supported)
 * </li>
 * </ul>
 *
 * @author Tomasz Bak
 */
public class RateLimiter {

    private final long rateToMsConversion;

    /**
     * 当前时间窗口消费的token数量
     */
    private final AtomicInteger consumedTokens = new AtomicInteger();

    // 上次fill token时间戳
    private final AtomicLong lastRefillTime = new AtomicLong(0);

    @Deprecated
    public RateLimiter() {
        this(TimeUnit.SECONDS);
    }

    public RateLimiter(TimeUnit averageRateUnit) {
        switch (averageRateUnit) {
            case SECONDS:
                rateToMsConversion = 1000;
                break;
            case MINUTES:
                rateToMsConversion = 60 * 1000;
                break;
            default:
                throw new IllegalArgumentException("TimeUnit of " + averageRateUnit + " is not supported");
        }
    }

    /**
     * @param burstSize   - 允许作为突发事件进入系统的最大请求数
     * @param averageRate - 预期的每秒请求数（新版本也支持使用分钟为单位）
     * @return
     */
    public boolean acquire(int burstSize, long averageRate) {
        return acquire(burstSize, averageRate, System.currentTimeMillis());
    }

    public boolean acquire(int burstSize, long averageRate, long currentTimeMillis) {
        if (burstSize <= 0 || averageRate <= 0) { // Instead of throwing exception, we just let all the traffic go
            return true;
        }

        refillToken(burstSize, averageRate, currentTimeMillis);
        return consumeToken(burstSize);
    }

    /**
     * @param burstSize   - 允许作为突发事件进入系统的最大请求数
     * @param averageRate - 预期的每秒/分的请求数（取决于构造器中的TimeUnit参数,本示例以秒作为单位进行源码解读）
     * @return
     */
    private void refillToken(int burstSize, long averageRate, long currentTimeMillis) {
        long refillTime = lastRefillTime.get(); // 上次填充token时间
        long timeDelta = currentTimeMillis - refillTime; //本次fill token时间差

        // timeDelta * averageRate / rateToMsConversion使用交换律转化为： (timeDelta/ rateToMsConversion) * averageRate 更容易理解，这么做目的是缩小误差
        long newTokens = timeDelta * averageRate / rateToMsConversion; // 计算当前可以产生token的数量

        if (newTokens > 0) {
            // newTokens * rateToMsConversion / averageRate 转为 (newTokens/averageRate)*rateToMsConversion 更容易理解
            // (newTokens/averageRate)*rateToMsConversion 计算生成newTokens数量的token需要的截止时间
            //拆开的理解：newTokens/averageRate 计算生成newTokens个token需要花费的秒数, (newTokens/averageRate)*rateToMsConversion也就是生成newTokens个token需要花费的毫秒数
            long newRefillTime = refillTime == 0 ? currentTimeMillis : refillTime + newTokens * rateToMsConversion / averageRate;// 设置下一次refillToken时间
            if (lastRefillTime.compareAndSet(refillTime, newRefillTime)) {
                while (true) {
                    //当前消费的个数
                    int currentLevel = consumedTokens.get();
                    // 取有效的当前消费的个数
                    int adjustedLevel = Math.min(currentLevel, burstSize); // In case burstSize decreased
                    // 弥补一些已经消费的token(就是游戏里面的`回血`操作)
                    int newLevel = (int) Math.max(0, adjustedLevel - newTokens); // adjustedLevel - newTokens如果大于0则表示新产生的token个数不足以将已消费的token清0，此时设置adjustedLevel - newTokens，否则清0
                    if (consumedTokens.compareAndSet(currentLevel, newLevel)) {
                        //设置到消费的token中
                        return;
                    }
                }
            }
        }
    }

    private boolean consumeToken(int burstSize) {
        int i = 0;
        while (true) {
            System.out.println("consumeToken = " + ++i);
            int currentLevel = consumedTokens.get();
            if (currentLevel >= burstSize) {
                return false;
            }
            if (consumedTokens.compareAndSet(currentLevel, currentLevel + 1)) {
                return true;
            }
        }
    }

    public void reset() {
        consumedTokens.set(0);
        lastRefillTime.set(0);
    }
}