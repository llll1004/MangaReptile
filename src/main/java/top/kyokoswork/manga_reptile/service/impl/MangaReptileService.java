package top.kyokoswork.manga_reptile.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.core.util.ZipUtil;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.kyokoswork.manga_reptile.entities.Chapter;
import top.kyokoswork.manga_reptile.entities.Manga;
import top.kyokoswork.manga_reptile.service.IMangaReptileService;
import top.kyokoswork.manga_reptile.utils.ChapterUtil;
import top.kyokoswork.manga_reptile.utils.ImageUtil;
import top.kyokoswork.manga_reptile.utils.MangaUtil;
import top.kyokoswork.manga_reptile.utils.WebSocketUtil;

import javax.websocket.Session;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

@Service
public class MangaReptileService implements IMangaReptileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MangaReptileService.class);

    /**
     * 获取漫画
     *
     * @param url 漫画地址
     * @return 漫画对象
     */
    @Override
    public Manga mangaDetail(String url) {
        HtmlPage page = MangaUtil.getHtmlPage(url);
        return MangaUtil.getManga(page);
    }

    /**
     * 下载漫画
     *
     * @param manga     漫画地址
     * @param siteUrl   本页面地址
     * @param wsSession WebSocket Session
     * @return ZIP下载链接
     */
    @Override
    public String downloadManga(Manga manga, String siteUrl, Session wsSession) {
        List<Chapter> chapters = manga.getChapters();

        // 设置章节名
        String[] chapterNames = new String[chapters.size()];
        for (int i = 0; i < chapters.size(); i++) {
            chapterNames[i] = chapters.get(i).getName() + ".zip";
        }

        // 获取章节地址
        String[] chapterUrls = new String[chapters.size()];
        for (int i = 0; i < chapters.size(); i++) {
            WebSocketUtil.sendMessage(wsSession, "wsMsg:获取章节中 " + (i + 1) + "/" + chapters.size());
            String url = checkCache(chapters.get(i).getName(), siteUrl);
            System.out.println(url);
            String s;
            if (url == null) {
                url = "https://www.lightnovel.us/detail/" + chapters.get(i).getId();
                chapters.get(i).setImages(chapterDetail(url).getImages());
                s = downloadChapter(chapters.get(i), siteUrl, null);
                s = URLUtil.encode(s);
            } else {
                s = URLUtil.encode(url);
            }
            chapterUrls[i] = s;
        }

        // 获取输入流
        InputStream[] ins = new InputStream[chapters.size()];
        Vector<Integer> flag = new Vector<>();
        for (int i = 0; i < ins.length; i++) {
            ThreadUtil.execAsync(new ChapterUtil(i, chapterUrls[i], ins, flag));
        }

        // 线程执行完毕判断
        while (flag.size() != chapters.size()) {
            try {
                Thread.sleep(1000);
                System.out.println(flag.size() + "/" + chapters.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LOGGER.info("尝试压缩文件...");
        WebSocketUtil.sendMessage(wsSession, "wsMsg:打包漫画中...");
        ZipUtil.zip(FileUtil.touch("file:/home/download/" + manga.getName() + ".zip"),
                chapterNames, ins);
        LOGGER.info("压缩文件成功");
        WebSocketUtil.sendMessage(wsSession, "wsMsg:打包完毕");

        return siteUrl + manga.getName() + ".zip";
    }

    /**
     * 获取章节
     *
     * @param url 章节地址
     * @return 章节对象
     */
    @Override
    public Chapter chapterDetail(String url) {
        // 获取页面
        HtmlPage page = MangaUtil.getHtmlPage(url);
        // 返回章节对象
        return MangaUtil.getChapter(page);
    }

    /**
     * 下载章节压缩包
     *
     * @param chapter   章节
     * @param siteUrl   本页面地址
     * @param wsSession WebSocket Session
     * @return ZIP下载链接
     */
    @Override
    public String downloadChapter(Chapter chapter, String siteUrl, Session wsSession) {
        List<String> urls = chapter.getImages();
        // 定义图片数量及图片名
        String[] paths = new String[urls.size()];
        for (int i = 0; i < paths.length; i++) {
            // TODO 修改图片名
            paths[i] = i + ".jpg";
        }
        LOGGER.info("图片名称创建完毕");
        // 获取所有图片输入流
        InputStream[] ins = new InputStream[urls.size()];
        Vector<Integer> flag = new Vector<>();
        for (int i = 0; i < ins.length; i++) {
            ThreadUtil.execAsync(new ImageUtil(i, urls.get(i), ins, flag));
        }

        // 线程执行完毕判断
        while (flag.size() != urls.size()) {
            try {
                Thread.sleep(1000);
                WebSocketUtil.sendMessage(wsSession,
                        "wsMsg:获取图片中 " + flag.size() + "/" + urls.size());
                System.out.println(flag.size() + "/" + urls.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("输入流获取完毕");

        LOGGER.info("尝试压缩文件...");
        WebSocketUtil.sendMessage(wsSession, "wsMsg:打包章节中...");
        System.out.println(chapter.getName());
        ZipUtil.zip(FileUtil.touch("file:/home/download/" + chapter.getName() + ".zip"), paths, ins);
        LOGGER.info("压缩文件成功");
        WebSocketUtil.sendMessage(wsSession, "wsMsg:打包完毕");


        return siteUrl + chapter.getName() + ".zip";
    }

    /**
     * 检查是否已缓存
     *
     * @param chapterName 文件名
     * @param siteUrl     本页面地址
     * @return 文件路径
     */
    @Override
    public String checkCache(String chapterName, String siteUrl) {
        if (FileUtil.exist("file:/home/download/" + chapterName + ".zip")) {
            return siteUrl + chapterName + ".zip";
        }

        return null;
    }
}
