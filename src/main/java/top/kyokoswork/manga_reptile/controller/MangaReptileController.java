package top.kyokoswork.manga_reptile.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kyokoswork.manga_reptile.entities.Chapter;
import top.kyokoswork.manga_reptile.entities.Manga;
import top.kyokoswork.manga_reptile.enums.StateE;
import top.kyokoswork.manga_reptile.service.IMangaReptileService;
import top.kyokoswork.manga_reptile.utils.RespResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/manga")
@CrossOrigin
public class MangaReptileController {
    @Resource
    private IMangaReptileService mangaReptileService;

    private HttpSession session;

    /**
     * 获取漫画章节
     *
     * @param url 漫画地址
     * @return 章节地址
     */
    @GetMapping("/chapters")
    private RespResult<Manga> getManga(HttpSession httpSession, String url) {
        Manga manga = mangaReptileService.mangaDetail(url);
        // 存入manga到session
        session = httpSession;
        session.setAttribute("manga", manga);
        return new RespResult<>(manga);
    }

    @GetMapping("/downloadManga")
    private RespResult<String> downloadManga(String siteUrl) {
        // 从session获取漫画
        Manga manga = (Manga) session.getAttribute("manga");
        String downloadUrl = mangaReptileService.downloadManga(manga, siteUrl);
        return new RespResult<>(downloadUrl);
    }

    /**
     * 获取章节内容
     *
     * @param url 章节地址
     * @return 章节对象
     */
    @GetMapping("/detail")
    private RespResult<Chapter> chapterDetail(HttpSession httpSession, String url) {

        Chapter chapter = mangaReptileService.chapterDetail(url);
        // 存入chapter到session
        session = httpSession;
        session.setAttribute("chapter", chapter);

        return new RespResult<>(chapter);
    }

    /**
     * 下载章节
     *
     * @param siteUrl 本页面地址
     * @return ZIP下载链接
     */
    @GetMapping("/downloadChapter")
    private RespResult<String> downloadChapter(String siteUrl) {
        // 从session获取chapter
        Chapter chapter = (Chapter) session.getAttribute("chapter");

        // 获取图片链接
        List<String> images = chapter.getImages();
        // 还未获取漫画详情
        if (images == null) return new RespResult<>(StateE.DETAILS_ERROR);
        // 返回下载链接
        String downloadUrl = mangaReptileService.downloadChapter(chapter, siteUrl);

        return new RespResult<>(downloadUrl);
    }
}
