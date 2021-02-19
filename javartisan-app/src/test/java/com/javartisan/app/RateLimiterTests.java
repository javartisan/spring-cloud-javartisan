package com.javartisan.app;


import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class RateLimiterTests {

    public static void main(String[] args) {
        RateLimiter rateLimiter = new RateLimiter(TimeUnit.SECONDS);
        for (int i = 0; i < 10; i++) {

            boolean acquire = rateLimiter.acquire(5, 2);

            System.out.println(LocalDateTime.now() + "  " + acquire);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
