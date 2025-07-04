package com.lcx.controller;

import com.lcx.common.Result;
import com.lcx.constant.EsIndex;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author : lichangxin
 * @create : 2024/6/6 10:05
 * @description
 */

@RequestMapping("/index")
@RestController
public class EsIndexController {

    @Autowired
    RestHighLevelClient restHighLevelClient;


    @PutMapping("/createIndex/{index}")
    public  Result<CreateIndexResponse> createIndex(@PathVariable("index") String index) throws IOException {
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(new CreateIndexRequest(index), RequestOptions.DEFAULT);
        return Result.ok(createIndexResponse);
    }

    @DeleteMapping("/deleteIndex/{index}")
    public Result<AcknowledgedResponse> deleteIndex(@PathVariable("index") String index) throws IOException {
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
        return Result.ok(acknowledgedResponse);
    }

    @GetMapping("/indices")
    public Result<String[]> indices(@RequestParam(required = false) String index) throws IOException {
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(new GetIndexRequest(StringUtils.isBlank(index) ? "*" : index), RequestOptions.DEFAULT);
        return Result.ok(getIndexResponse.getIndices());
    }





}
