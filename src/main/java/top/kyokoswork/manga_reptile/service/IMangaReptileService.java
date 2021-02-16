package top.kyokoswork.manga_reptile.service;

import top.kyokoswork.manga_reptile.entities.Chapter;
import top.kyokoswork.manga_reptile.entities.Manga;

public interface IMangaReptileService {
    /**
     * 获取漫画
     *
     * @param url 漫画地址
     * @return 漫画对象
     */
    Manga mangaDetail(String url);

    /**
     * 下载漫画
     *
     * @param manga 漫画地址
     * @param siteUrl 本页面地址
     * @return ZIP下载链接
     */
    String downloadManga(Manga manga, String siteUrl);

    /**
     * 获取章节
     *
     * @param url 章节地址
     * @return 章节对象
     */
    Chapter chapterDetail(String url);

    /**
     * 下载章节
     *
     * @param chapter 章节地址
     * @param siteUrl 本页面地址
     * @return ZIP下载链接
     */
    String downloadChapter(Chapter chapter, String siteUrl);
}
