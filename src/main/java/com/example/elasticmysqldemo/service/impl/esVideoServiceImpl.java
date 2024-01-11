package com.example.elasticmysqldemo.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.elasticmysqldemo.mapper.VideoMapper;
import com.example.elasticmysqldemo.pojo.Video;
import com.example.elasticmysqldemo.pojo.es.VideoDoc;
import com.example.elasticmysqldemo.service.esVideoService;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class esVideoServiceImpl implements esVideoService  {
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private RestHighLevelClient client;
    @Override
    public void insertDocByVid(Integer vid) {
        try {
            Video video = videoMapper.selectById(vid);
            VideoDoc videoDoc = new VideoDoc(video);

            // 1. 准备 request
            IndexRequest request = new IndexRequest("video").id(videoDoc.getVid().toString());
            // 2. 准备 DSL
            request.source(JSON.toJSONString(videoDoc), XContentType.JSON);
            // 3. 发送请求
            client.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteDocByVid(Integer vid) {
        try {
            // 1. request
            DeleteRequest request = new DeleteRequest("video", vid.toString());
            // 2. execute
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getVideoByKeyword(String keyword, Integer pageNum) {
        try {
            if (pageNum <= 0) pageNum = 0;
            // 条件搜索
            SearchRequest searchRequest = new SearchRequest("video");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // 分页
            searchSourceBuilder.from(pageNum).size(10);
            // 精确匹配
            MatchQueryBuilder match = QueryBuilders.matchQuery("title", keyword);
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
            searchSourceBuilder.query(match).timeout(new TimeValue(60, TimeUnit.SECONDS));

            // 高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title").requireFieldMatch(false)
                            .preTags("<span style='color: red'>").postTags("</span>");
            searchSourceBuilder.highlighter(highlightBuilder);

            searchRequest.source(searchSourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            System.out.println("resp: " + response);

            // 解析结果
            ArrayList<Map<String, Object>> list = new ArrayList<>();
            for (SearchHit hit: response.getHits().getHits()) {
                // 解析高亮字段
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField title = highlightFields.get("title");
                Map<String, Object> map = hit.getSourceAsMap(); // 原来的结果
                if (title != null) {
                    Text[] fragments = title.fragments();
                    String highlightTitle = Arrays.toString(fragments);
                    map.put("title", highlightTitle); // 用高亮替换原来的
                }
                list.add(map);
            }
            return list;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getSuggestion(String key) {
        try {
            SearchRequest request = new SearchRequest("video");
            request.source().suggest(new SuggestBuilder()
                    .addSuggestion("keyword_suggest",
                            SuggestBuilders.completionSuggestion("suggestion")
                                    .prefix(key)
                                    .skipDuplicates(true)
                                    .size(10)));

            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            Suggest suggest = response.getSuggest();
            CompletionSuggestion keyword_suggest = suggest.getSuggestion("keyword_suggest");
            List<CompletionSuggestion.Entry.Option> options = keyword_suggest.getOptions();

            List<String> suggestions = new ArrayList<>();
            for (CompletionSuggestion.Entry.Option option: options) {
                String text = option.getText().toString();
                suggestions.add(text);
            }

            return suggestions;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
