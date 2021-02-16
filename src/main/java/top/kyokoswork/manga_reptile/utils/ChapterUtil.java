package top.kyokoswork.manga_reptile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class ChapterUtil implements Runnable{
    private final int i;
    private final String url;
    private final InputStream[] ins;
    private final Vector<Integer> flag;

    public ChapterUtil(int i, String url, InputStream[] ins, Vector<Integer> flag) {
        this.i = i;
        this.url = url;
        this.ins = ins;
        this.flag = flag;
    }

    @Override
    public void run() {
        System.out.println("获取第 " + i + " 章...");
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
        flag.add(1);
        System.out.println("第 " + i + " 章获取成功");
    }
}
