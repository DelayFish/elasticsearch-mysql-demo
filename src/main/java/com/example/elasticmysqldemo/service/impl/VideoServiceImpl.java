package com.example.elasticmysqldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.elasticmysqldemo.mapper.VideoMapper;
import com.example.elasticmysqldemo.pojo.Video;
import com.example.elasticmysqldemo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Slf4j
@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoMapper videoMapper;

    @Override
    public Integer addVideo(Integer uid, String title, String descr, String tags) {
        try {
            Video video = new Video(null, uid, title, descr, tags);
            videoMapper.insert(video);
            return video.getVid();
        } catch (Exception e) {
            System.out.println("插入错误");
        }
        return null;
    }

    @Override
    public void deleteVideo(Integer vid) {
        try {
            QueryWrapper<Video> wrapper = new QueryWrapper<>();
            wrapper.eq("vid", vid);
            videoMapper.delete(wrapper);
        } catch (Exception e) {
            System.out.println("删除错误");
        }
    }

    @Override
    public void updateVideo(Integer vid, Integer uid, String title, String descr, String tags) {
        try {
            Video video = new Video(vid, uid, title, descr, tags);
            videoMapper.updateById(video);
        } catch (Exception e) {
            System.out.println("更新错误");
        }
    }

    @Override
    public List<Video> getVideo() {
        return videoMapper.selectList(null);
    }

    @Override
    public Video getVideoByVid(Integer vid) {
        try {
            return videoMapper.selectById(vid);
        } catch (Exception e) {
            System.out.println("单条查询失败");
        }
        return null;
    }
}

