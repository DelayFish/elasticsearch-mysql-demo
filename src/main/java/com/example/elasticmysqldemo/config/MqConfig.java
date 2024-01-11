package com.example.elasticmysqldemo.config;

import com.example.elasticmysqldemo.pojo.constants.VideoMqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
    /**
     * @durable 是否需要消息持久化
     * 这里创建一个 topic 交换机
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(VideoMqConstants.EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue insertQueue() {
        return new Queue(VideoMqConstants.INSERT_QUEUE_NAME, true);
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(VideoMqConstants.DELETE_QUEUE_NAME, true);
    }

    // 绑定队列和交换机的关系
    @Bean
    public Binding insertQueueBinding() {
        return BindingBuilder.bind(insertQueue()).to(topicExchange()).with(VideoMqConstants.INSERT_KEY);
    }

    @Bean
    public Binding deleteQueueBinding() {
        return BindingBuilder.bind(deleteQueue()).to(topicExchange()).with(VideoMqConstants.DELETE_KEY);
    }
}
