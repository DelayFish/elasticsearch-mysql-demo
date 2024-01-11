package com.example.elasticmysqldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.elasticmysqldemo.pojo.Video;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface VideoMapper extends BaseMapper<Video> {
}
