package top.kyokoswork.manga_reptile.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class Manga implements Serializable {
    /**
     * 漫画名
     */
    private String name;
    /**
     * 漫画作者
     */
    private String author;
    /**
     * 章节
     */
    private List<Chapter> chapters;
}
