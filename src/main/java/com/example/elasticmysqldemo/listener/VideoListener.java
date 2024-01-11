package com.example.elasticmysqldemo.listener;

import com.example.elasticmysqldemo.pojo.constants.VideoMqConstants;
import com.example.elasticmysqldemo.service.esVideoService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class VideoListener {
    @Autowired
    private esVideoService es;

    /**
     * 监听视频的新增或者修改业务
     * 只需要传 vid， 然后通过 mysql 查出全部信心并写入到 es 中
     * @param vid 视频的 vid 作为唯一标识
     */
    @RabbitListener(queues = VideoMqConstants.INSERT_QUEUE_NAME)
    public void listenVideoInsertAndUpdate(Integer vid) {
        es.insertDocByVid(vid);
    }

    @RabbitListener(queues = VideoMqConstants.DELETE_QUEUE_NAME)
    public void listenVideoDelete(Integer vid) {
        es.deleteDocByVid(vid);
    }
}
