package com.javartisan.goods.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "SERVER-SERVICE", contextId = "GoodApi")
public interface GoodApi {

    @RequestMapping("/good/get")
    public String get(String name);
}
