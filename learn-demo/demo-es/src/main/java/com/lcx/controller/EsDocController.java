package com.lcx.controller;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : lichangxin
 * @create : 2024/6/26 17:26
 * @description
 */

@RestController
@RequestMapping("/doc")
public class EsDocController {

    @Autowired
    RestHighLevelClient restHighLevelClient;


}
