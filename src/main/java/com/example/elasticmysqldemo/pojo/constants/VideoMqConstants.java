package com.example.elasticmysqldemo.pojo.constants;

public class VideoMqConstants {
    // 交换机名称
    public static final String EXCHANGE_NAME = "video.topic";
    // 修改和新增 video 的队列
    public static final String INSERT_QUEUE_NAME = "video.insert.queue";
    // 删除队列
    public static final String DELETE_QUEUE_NAME = "video.delete.queue";

    // RoutingKey
    public static final String INSERT_KEY = "video.insert";
    public static final String DELETE_KEY = "video.delete";
}
