package top.kyokoswork.manga_reptile.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class Chapter implements Serializable {
    /**
     * 章节地址id
     */
    private String id;
    /**
     * 章节名
     */
    private String name;
    /**
     * 图片地址
     */
    private List<String> images;
}
