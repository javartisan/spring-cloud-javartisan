package com.javartisan.app;

import com.javartisan.goods.api.GoodApi;
import com.javartisan.goods.api.SensitiveGoodApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
public class GoodsController {

    @Autowired
    private GoodApi goodApi;

    @Autowired
    private SensitiveGoodApi sensitiveGoodApi;

    @RequestMapping("get")
    public String get(@RequestParam(value = "name") String name) {
        String value = goodApi.get(name);
        System.out.println("goodApi = " + value);
        return value;
    }

    @RequestMapping("get2")
    public String get2(@RequestParam(value = "name") String name) {
        String value = sensitiveGoodApi.get(name);
        System.out.println("sensitiveGoodApi = " + value);
        return value;
    }
}
