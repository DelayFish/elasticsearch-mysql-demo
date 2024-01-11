package com.example.elasticmysqldemo.testES;

import com.alibaba.fastjson2.JSON;
import com.example.elasticmysqldemo.ElasticMysqlDemoApplication;
import com.example.elasticmysqldemo.controller.VideoController;
import com.example.elasticmysqldemo.mapper.VideoMapper;
import com.example.elasticmysqldemo.pojo.Video;
import com.example.elasticmysqldemo.pojo.es.VideoDoc;
import com.example.elasticmysqldemo.service.esVideoService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@SpringBootTest(classes = ElasticMysqlDemoApplication.class)
public class testElasticsearch {
    @Autowired
    private VideoController controller;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private esVideoService es;
    @Test
    public void testGetDocumentById() throws IOException {
        GetRequest request = new GetRequest("video", "9");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String json = response.getSourceAsString();
        System.out.println(json);
    }

    @Test
    public void testGetDocumentByKeyword() throws IOException {
        List<Map<String, Object>> list = es.getVideoByKeyword("原神", 0);
        System.out.println(list);
    }

    @Test
    public void testBulkRequest() throws IOException {
        List<Video> videos = videoMapper.selectList(null);

        System.out.println(videos);

        BulkRequest request = new BulkRequest();
        for (Video video: videos) {
            VideoDoc videoDoc =  new VideoDoc(video);
            request.add(new IndexRequest("video")
                    .id(videoDoc.getVid().toString())
                    .source(JSON.toJSONString(videoDoc), XContentType.JSON));
        }

        client.bulk(request, RequestOptions.DEFAULT);
    }

    @Test
    public void testGetSuggestByKeyword() throws IOException {
        List<String> list = es.getSuggestion("jh");
        System.out.println(list);
    }
}
