package com.example.elasticmysqldemo.service;

import com.example.elasticmysqldemo.pojo.Video;

import java.util.List;

public interface VideoService {
    Integer addVideo(Integer uid, String title, String descr, String tags);
    void deleteVideo(Integer vid);
    void updateVideo(Integer vid, Integer uid, String title, String descr, String tags);
    List<Video> getVideo();

    Video getVideoByVid(Integer vid);
}
