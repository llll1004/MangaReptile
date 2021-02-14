package top.kyokoswork.manga_reptile.service;

import top.kyokoswork.manga_reptile.entities.Chapter;

import java.util.List;

public interface IMangaReptileService {
    /**
     * 获取漫画章节
     *
     * @param url 漫画地址
     * @return 漫画章节
     */
    List<Chapter> getChapters(String url);

    /**
     * 获取章节
     *
     * @param url 章节地址
     * @return 章节对象
     */
    Chapter chapterDetail(String url);

    /**
     * 下载漫画
     *
     * @param chapter 漫画内容地址
     * @param siteUrl 本页面地址
     * @return ZIP下载链接
     */
    String download(Chapter chapter, String siteUrl);
}
