package com.javartisan.app;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IRateLimiter {

    private AtomicInteger consumedTokenCnt = new AtomicInteger();

    private AtomicLong lastFillTokenTimeInMillSeconds = new AtomicLong();

    //时间单位
    private Long timeUnitInMillSecond;

    public IRateLimiter() {
        this(TimeUnit.SECONDS);
    }

    public IRateLimiter(TimeUnit unit) {
        switch (unit) {

            case SECONDS:
                timeUnitInMillSecond = 1000L;
                break;
            case MINUTES:
                timeUnitInMillSecond = 1000L * 60;
            default:
                throw new RuntimeException("TimeUnit not support!");
        }
    }


    public void refillToken(int burstCnt, int countPerTimeUnit) {

        long lastFillTime = lastFillTokenTimeInMillSeconds.get();
        long currentTimeMillis = System.currentTimeMillis();
        long timeDiff = currentTimeMillis - lastFillTime;
        // 产生token的数量
        long generateTokenCnt = timeDiff / timeUnitInMillSecond * countPerTimeUnit;
        if (generateTokenCnt > 0) {

            //产生这些token花费的时间
            long generateTokenCntCostTime = generateTokenCnt / countPerTimeUnit * timeUnitInMillSecond;
            /*
             * 注意：
             * long nextFillTime = currentTimeMillis + generateTokenCntCostTime 与 long nextFillTime = lastFillTime == 0 ? currentTimeMillis : currentTimeMillis + generateTokenCntCostTime;区别：
             * 前者表示生成的token是系统启动也就是0到 currentTimeMillis + generateTokenCntCostTime 时间段产生的token
             * 后者表示生成的token是系统启动也就是0到 currentTimeMillis   时间段产生的token
             * 差别在于：
             *  0到currentTimeMillis + generateTokenCntCostTime时间之间token数量的差别
             */
            long nextFillTime = lastFillTime == 0 ? currentTimeMillis : currentTimeMillis + generateTokenCntCostTime;
            if (lastFillTokenTimeInMillSeconds.compareAndSet(lastFillTime, nextFillTime)) { // 此处不需要使用while循环，如果失败了证明有其他线程在执行该操作因此可以由其他线程执行

                while (true) { // 一旦if (lastFillTokenTimeInMillSeconds.compareAndSet(lastFillTime, nextFillTime)) 执行成功，此时必须执行成功
                    int tokenConsumedCnt = consumedTokenCnt.get();
                    // 由于burstCnt是每次传参，因此burstCnt可能会变小，因此需要去min操作，得到的结果也就是最多可以弥补的token数量（最多可以回血数）
                    int maximumTokenToAdd = Math.min(tokenConsumedCnt, burstCnt);
                    int newConsumeCnt = Math.max(0, tokenConsumedCnt - maximumTokenToAdd);
                    if (consumedTokenCnt.compareAndSet(tokenConsumedCnt, newConsumeCnt)) {
                        return;
                    }
                }
            }

        }
    }

    public boolean acquire(int burstCnt, int countPerTimeUnit) {
        if (burstCnt < 0 || countPerTimeUnit < 0) {
            throw new RuntimeException("burstCnt and countPerTimeUnit must bigger zero.");
        }
        refillToken(burstCnt, countPerTimeUnit);
        return consumeToken(burstCnt);
    }

    private boolean consumeToken(int burstCnt) {
        while (true) {
            int tokenConsumedCnt = consumedTokenCnt.get();
            if (tokenConsumedCnt >= burstCnt) {
                return false;
            }
            if (consumedTokenCnt.compareAndSet(tokenConsumedCnt, tokenConsumedCnt + 1)) {
                return true;
            }
        }
    }


}