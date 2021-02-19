package com.javartisan.app;


import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RateLimiterTests {

    public static void main(String[] args) {
        RateLimiter rateLimiter = new RateLimiter(TimeUnit.SECONDS);
        int SIZE = 500;
        CountDownLatch countDownLatch = new CountDownLatch(SIZE);
        for (int i = 0; i < SIZE; i++) {

            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean acquire = rateLimiter.acquire(100, 100);
                if (!acquire) {
                    System.out.println(acquire);
                }else{
                    System.out.println("acquire true");
                }

            }).start();
            countDownLatch.countDown();
        }

    }
}
