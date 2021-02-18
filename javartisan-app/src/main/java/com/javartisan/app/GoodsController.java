package com.javartisan.app;

import com.javartisan.goods.api.GoodApi;
import com.javartisan.goods.api.SensitiveGoodApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(value = "api")
public class GoodsController {

    @Autowired
    private GoodApi goodApi;

    @Autowired
    private SensitiveGoodApi sensitiveGoodApi;

    @RequestMapping("get")
    public String get() {
        String value = goodApi.get("" + new Random().nextInt(100));
        System.out.println("goodApi = " + value);
        return value;
    }

    @RequestMapping("get2")
    public String get2() {
        String value = sensitiveGoodApi.get("" + new Random().nextInt(100));
        System.out.println("sensitiveGoodApi = " + value);
        return value;
    }
}
