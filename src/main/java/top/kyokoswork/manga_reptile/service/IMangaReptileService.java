package top.kyokoswork.manga_reptile.service;

import top.kyokoswork.manga_reptile.entities.Chapter;
import top.kyokoswork.manga_reptile.entities.Manga;

import javax.websocket.Session;

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
     * @param manga     漫画地址
     * @param siteUrl   本页面地址
     * @param wsSession WebSocket Session
     * @return ZIP下载链接
     */
    String downloadManga(Manga manga, String siteUrl, Session wsSession);

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
     * @param chapter   章节地址
     * @param siteUrl   本页面地址
     * @param wsSession WebSocket Session
     * @return ZIP下载链接
     */
    String downloadChapter(Chapter chapter, String siteUrl, Session wsSession);

    /**
     * 检查是否已缓存
     *
     * @param chapterName 文件名
     * @param siteUrl     本页面地址
     * @return 文件路径
     */
    String checkCache(String chapterName, String siteUrl);
}
