package com.example.elasticmysqldemo.pojo.es;

import com.example.elasticmysqldemo.pojo.Video;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class VideoDoc {
    private Integer vid;
    private Integer uid;
    private String title;
    private String descr;
    private String tags;

    private List<String> suggestion;

    public VideoDoc(Video video) {
        this.vid = video.getVid();
        this.uid = video.getUid();
        this.title = video.getTitle();
        this.descr = video.getDescr();
        this.tags = video.getTags();

        // 假设 tags 按照 / 分隔
        if (this.tags.contains("/")) {
            // 将 tags 切割
            String []arr = this.tags.split("/");
            this.suggestion = new ArrayList<>();
            this.suggestion.add(this.descr);
            Collections.addAll(this.suggestion, arr);
        } else {
            this.suggestion = Arrays.asList(this.tags, this.descr);
        }
    }
}
