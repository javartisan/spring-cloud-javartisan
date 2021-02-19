package com.javartisan.goods.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

@Service
public class GoodServiceImpl implements GoodService {

    @Override
    @HystrixCommand(fallbackMethod = "errorHandler")
    public String get(String name) {
        if ("error".equalsIgnoreCase(name)) {
            int i = 1 / 0;
        }
        String value = "参数name：" + name;
        return value;
    }

    // 熔断方法需要与原始对应方法参数保持一致
    public String errorHandler(String name) {
        return name + " errorHandler服务器繁忙：稍后再试!";
    }

    // 熔断方法需要与原始对应方法参数保持一致,否则报错：fallback method wasn't found: errorHandler([class java.lang.String])
    public String errorHandler() {
        return " errorHandler服务器繁忙：稍后再试!";
    }

    public String errorHandler(Object name) {
        return name + " errorHandler服务器繁忙：稍后再试!";
    }
}
