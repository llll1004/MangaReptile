package top.kyokoswork.manga_reptile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageUtil implements Runnable {

    private final int i;
    private final String url;
    private final InputStream[] ins;

    public ImageUtil(int i, String url, InputStream[] ins) {
        this.i = i;
        this.url = url;
        this.ins = ins;
    }

    @Override
    public void run() {
        System.out.println("获取第 " + i + "张...");
        InputStream is = null;
        try {
            URL u = new URL(url);
            URLConnection hu = u.openConnection();
            hu.connect();

            is = hu.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        ins[i] = is;
        System.out.println("第 " + i + "张获取成功");
    }
}
