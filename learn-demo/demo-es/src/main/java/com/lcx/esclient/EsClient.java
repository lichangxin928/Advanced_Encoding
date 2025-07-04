package com.lcx.esclient;

import com.lcx.constant.EsIndex;
import org.apache.http.HttpHost;


import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;


/**
 * @author : lichangxin
 * @create : 2024/6/13 14:02
 * @description
 *
 * 对岗位的理解：
 *
 *
 *
 */
//@SpringBootTest
public class EsClient {


    private RestHighLevelClient client;

    @BeforeEach
    void setUp() {
        this.client =new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.1.150", 9200, "http")));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }


    @Test
    void testCreateHotelIndex() throws IOException {

        IndexRequest index = new IndexRequest("hotel");
        index.source(EsIndex.HOTEL_INDEX, XContentType.JSON);
        IndexResponse indexResponse = client.index(index, RequestOptions.DEFAULT);
    }

}












