package com.javartisan.app;

import com.javartisan.goods.api.GoodApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(value = "api")
public class GoodsController {

    @Autowired
    private GoodApi goodApi;

    @RequestMapping("get")
    public String get() {
        return goodApi.get("" + new Random().nextInt(100));
    }
}
