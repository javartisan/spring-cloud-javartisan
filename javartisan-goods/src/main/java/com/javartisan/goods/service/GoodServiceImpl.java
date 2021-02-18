package com.javartisan.goods.service;

import org.springframework.stereotype.Service;

@Service
public class GoodServiceImpl implements GoodService {

    @Override
    public String get(String name) {
        return "参数name：" + name;
    }
}
