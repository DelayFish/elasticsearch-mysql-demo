package com.example.elasticmysqldemo.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @TableId(type = IdType.AUTO)
    private Integer vid;
    private Integer uid;
    private String title;
    private String descr;
    private String tags;
}
