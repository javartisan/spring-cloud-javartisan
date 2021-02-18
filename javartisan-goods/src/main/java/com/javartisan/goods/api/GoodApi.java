package com.javartisan.goods.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * name 指定对应的应用名称
 */
@FeignClient(name = "good-service-app", contextId = "GoodApi")
public interface GoodApi {

    @RequestMapping("/good/get")
    public String get(@RequestParam("name") String name);
}
