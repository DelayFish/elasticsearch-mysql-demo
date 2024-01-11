package com.example.elasticmysqldemo.service;

import java.util.List;
import java.util.Map;

public interface esVideoService {
    void insertDocByVid(Integer vid);
    void deleteDocByVid(Integer vid);

    List<Map<String, Object>> getVideoByKeyword(String keyword, Integer pageNum);

    List<String> getSuggestion(String key);
}
