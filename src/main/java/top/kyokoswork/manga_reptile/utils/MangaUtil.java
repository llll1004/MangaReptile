package top.kyokoswork.manga_reptile.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.kyokoswork.manga_reptile.entities.Chapter;
import top.kyokoswork.manga_reptile.entities.Manga;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MangaUtil {
    private static WebClient webClient;

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
        Elements itemBoxes = document.getElementsByClass("item-box");

        System.out.println(xml);

        for (Element itemBox : itemBoxes) {
            System.out.println(itemBox.ownText());
        }

        DomNodeList<DomElement> divs = page.getElementsByTagName("div");
        for (DomElement div : divs) {
            String attribute = div.getAttribute("class");
            if (Objects.equals(attribute, "item-box")) {
                try {
                    System.out.println(div.getTextContent());
                    div.click();

                    System.out.println(webClient.getWebWindows().size());
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
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
        webClient = new WebClient(BrowserVersion.CHROME);
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
        options.setJavaScriptEnabled(true);

        HtmlPage page = null;
        try {
            // 获取页面
            page = webClient.getPage(url);
            // 设置JS超时时间
            webClient.waitForBackgroundJavaScript(30000);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            webClient.close();
        }

        return page;
    }
}
