package top.kyokoswork.manga_reptile.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ZipUtil;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.kyokoswork.manga_reptile.entities.Chapter;
import top.kyokoswork.manga_reptile.service.IMangaReptileService;
import top.kyokoswork.manga_reptile.utils.ImageUtil;
import top.kyokoswork.manga_reptile.utils.MangaUtil;

import java.io.InputStream;
import java.util.List;

@Service
public class MangaReptileService implements IMangaReptileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MangaReptileService.class);

    /**
     * 获取漫画章节
     *
     * @param url 漫画地址
     * @return 漫画章节
     */
    @Override
    public List<Chapter> getChapters(String url) {
        HtmlPage page = MangaUtil.getHtmlPage(url);
        MangaUtil.getManga(page);
        return null;
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
     * 下载漫画压缩包
     *
     * @param chapter 漫画章节
     * @param siteUrl 本页面地址
     * @return ZIP下载链接
     */
    @Override
    public String download(Chapter chapter, String siteUrl) {
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
        for (int i = 0; i < ins.length; i++) {
            ThreadUtil.execAsync(new ImageUtil(i, urls.get(i), ins));
        }

        // TODO 更改线程执行完毕判断
        LOGGER.info("输入流获取完毕");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.info("尝试压缩文件...");
        ZipUtil.zip(FileUtil.touch("file:/home/download/" + chapter.getName() + ".zip"), paths, ins);
        LOGGER.info("压缩文件成功");

        return siteUrl + chapter.getName() +".zip";
    }
}
