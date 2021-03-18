package top.kyokoswork.manga_reptile.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.kyokoswork.manga_reptile.entities.Chapter;
import top.kyokoswork.manga_reptile.entities.Manga;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MangaUtil {
    /**
     * 获取漫画
     *
     * @param page 漫画页面
     * @return 漫画对象
     */
    public static Manga getManga(HtmlPage page) {
        // 解析页面
        String xml = page.asXml();
        Document document = Jsoup.parse(xml);

        // 获取漫画名
        Element nameEl = document.getElementsByClass("title fs-md").get(0);
        String name = nameEl.ownText();
        // 获取漫画作者
        Element authorEl = document.getElementsByClass("fs-xs mar-top-20").get(0);
        String author = authorEl.ownText().substring(4);

        // 获取章节id、章节名
        List<Chapter> chapters = new ArrayList<>();
        String content = xml.substring(xml.indexOf("aid="), xml.indexOf("return"));
        // 章节id下标
        int aidIndex = content.indexOf("aid=");
        // 章节名下标
        int titleIndex = content.indexOf("\"");
        // 截取章节信息
        while (aidIndex != -1) {
            Chapter chapter = new Chapter();
            chapter.setId(content.substring(aidIndex + 4, aidIndex + 11));
            chapter.setName(content.substring(titleIndex + 1,
                    content.indexOf("\"", content.indexOf("\"") + 1)));
            chapters.add(chapter);

            int i = content.indexOf("aid=", 1);
            // if - 截取完毕
            if (i == -1) break;
            content = content.substring(i);
            aidIndex = content.indexOf("aid=");
            titleIndex = content.indexOf("\"");
        }
        // 按章节id排序 注意:由于补档存在,不一定是正确的章节顺序
        chapters.sort(Comparator.comparing(Chapter::getId));

        Manga manga = new Manga();
        manga.setChapters(chapters).setName(name).setAuthor(author);

//        DomNodeList<DomElement> divs = page.getElementsByTagName("div");
//        for (DomElement div : divs) {
//            String attribute = div.getAttribute("class");
//            if (Objects.equals(attribute, "item-box")) {
//                try {
//                    System.out.println(div.getTextContent());
//                    div.click();
//
//                    System.out.println(webClient.getWebWindows().size());
//                    return null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        return manga;
    }

    /**
     * 获取章节
     *
     * @param page 章节页面
     * @return 章节对象
     */
    public static Chapter getChapter(HtmlPage page) {
        // 解析页面
        String xml = page.asXml();
        Document document = Jsoup.parse(xml);
        // 获取章节名
        Element nameEl = document.getElementsByClass("article-title").get(0);
        String name = nameEl.ownText();
        // 获取漫画容器元素
        Element element = document.getElementById("article-main-contents");
        // 获取漫画内容元素
        Elements elements = element.getElementsByTag("img");
        // 获取内容
        List<String> images = new ArrayList<>();
        for (Element el : elements) {
            String attr = el.getElementsByTag("img").eq(0).attr("src");
            if (attr != null) images.add(attr);
        }

        return new Chapter().setName(name).setImages(images);
    }

    /**
     * 获取HTML页面
     *
     * @param url 页面链接
     * @return HTML页面
     */
    public static HtmlPage getHtmlPage(String url) {
        // 获取浏览器对象
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        // 启用Ajax
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        WebClientOptions options = webClient.getOptions();
        // 关闭css
        options.setCssEnabled(false);
        // 状态码异常不抛
        options.setThrowExceptionOnFailingStatusCode(false);
        // JS异常不抛
        options.setThrowExceptionOnScriptError(false);
        // 关闭本地ActiveX
        options.setActiveXNative(false);
        // 使用JS
        options.setJavaScriptEnabled(false);
        options.setDownloadImages(false);

//        options.setTimeout(30000);
//        options.setRedirectEnabled(true);
//        options.setUseInsecureSSL(true);
//        webClient.setJavaScriptTimeout(30000);
//        webClient.setRefreshHandler(new ImmediateRefreshHandler());

        HtmlPage page = null;
        try {
            // 获取页面
            page = webClient.getPage(url);
            // 设置JS超时时间
            webClient.waitForBackgroundJavaScript(30000);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }

        return page;
    }
}
