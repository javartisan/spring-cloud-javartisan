package guava;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class GuavaRateLimiter {

    public static void main(String[] args) {

        RateLimiter rateLimiter = RateLimiter.create(2.0);

        // 带有 Warmup功能的限流器
        //RateLimiter rateLimiter2 = RateLimiter.create(2.0, 1, TimeUnit.SECONDS);

        for (int i = 0; i < 10; i++) {
            double costTime = rateLimiter.acquire();
            System.out.println("costTime = " + costTime);
        }
    }
}
