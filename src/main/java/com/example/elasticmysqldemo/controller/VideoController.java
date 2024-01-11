package com.example.elasticmysqldemo.controller;

import com.example.elasticmysqldemo.pojo.constants.VideoMqConstants;
import com.example.elasticmysqldemo.service.VideoService;
import com.example.elasticmysqldemo.service.esVideoService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class VideoController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private VideoService videoService;

    @Autowired
    private esVideoService es;

    @PostMapping("/add/")
    public void addVideo(@RequestBody Map<String, String> map) {
        Integer uid = Integer.valueOf(map.get("uid"));
        String title = map.get("title");
        String descr = map.get("descr");
        String tags = map.get("tags");

        Integer vid = videoService.addVideo(uid, title, descr, tags);

        // 消息队列只通知 vid 减轻内存压力
        rabbitTemplate.convertAndSend(VideoMqConstants.EXCHANGE_NAME, VideoMqConstants.INSERT_KEY, vid);
    }

    @PostMapping("/delete/{vid}")
    public void deleteVideByVid(@PathVariable("vid") Integer vid) {
        videoService.deleteVideo(vid);

        rabbitTemplate.convertAndSend(VideoMqConstants.EXCHANGE_NAME, VideoMqConstants.DELETE_KEY, vid);
    }

    @PostMapping("/update/")
    public void updateVideoByVid(@RequestBody Map<String, String> map) {
        Integer vid = Integer.valueOf(map.get("vid"));
        Integer uid = Integer.valueOf(map.get("uid"));
        String title = map.get("title");
        String descr = map.get("decsr");
        String tags = map.get("tags");

        videoService.updateVideo(vid, uid, title, descr, tags);

        rabbitTemplate.convertAndSend(VideoMqConstants.EXCHANGE_NAME, VideoMqConstants.INSERT_KEY, vid);
    }

    @GetMapping("/parse/{keyword}/{pageNum}")
    public List<Map<String, Object>> getVideoByKeyword(@PathVariable("keyword") String keyword, @PathVariable("pageNum") String Num) {
        System.out.println("keyword:" + keyword + " " + "pageNum:" + Num);
        Integer pageNum = Integer.valueOf(Num);
        return es.getVideoByKeyword(keyword, pageNum);
    }

}
