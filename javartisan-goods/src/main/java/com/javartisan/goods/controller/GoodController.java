package com.javartisan.goods.controller;

import com.javartisan.goods.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/good")
public class GoodController {

    @Autowired
    private GoodService goodService;

    @RequestMapping("/get")
    public String get(String name) {
        return goodService.get(name);
    }
}
