package com.zgc123.www.com.twoSevenFiveUU.www;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;

public class startMain {

    public static void main(String[] args) {
        start("https://www.275uu.com/htm/", "J:/tempSaveFile2");
    }

    /**
     * 开始方法
     *
     * @param url          地址
     * @param saveFilePath 保存文件的路径
     */
    public static void start(String url, String saveFilePath) {

        Header[] headers = new Header[]{};
        String charset = "utf-8";

        // 获取标题栏
        String homePage = TSFuuCrawice.getPage(url, charset, headers, 1);
        final Elements rowInfos = Jsoup.parse(homePage).select(".menu");

        // 图片区
        Element element = rowInfos.get(2);
        url = "https://www.275uu.com";

        long now = System.currentTimeMillis();
        groupGoingByTitle(url, saveFilePath, headers, charset, element);
        long now2 = System.currentTimeMillis();
        System.out.println("groupGoingByTitle花费的时间:" + (now2 - now));
    }


    private static void groupGoingByTitle(final String url, final String saveFilePath, final Header[] headers, final String charset, final Element element) {

        final String titleName = TSFuuCrawice.getPageInfos(element, "li a", TSFuuCrawice.SELECT_TYPE_TEXT, null).get(0);
        final List<String> titleTypeAbsoluteUrls = TSFuuCrawice.getPageInfos(element, "li a", TSFuuCrawice.SELECT_TYPE_ATTA, "href");
        final List<String> titleTypeNames = TSFuuCrawice.getPageInfos(element, "li a", TSFuuCrawice.SELECT_TYPE_TEXT, null);
        System.out.println("主题:" + titleName);

        /**
         * 自拍偷拍
         */
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        long now = System.currentTimeMillis();
        groupGoingByType(url, saveFilePath, headers, charset, titleName, titleTypeAbsoluteUrls.get(1), titleTypeNames.get(1));
        long now2 = System.currentTimeMillis();
        System.out.println("groupGoingByType花费的时间:" + (now2 - now));
//            }
//        }).start();


        System.out.println("-------------------------------------");
    }

    private static void groupGoingByType(String url, String saveFilePath, Header[] headers, String charset, String titleName, String titleTypeAbsoluteUrl, String titleTypeName) {
        System.out.println("标题:" + titleTypeName);
        String titleTypeUrl = url + titleTypeAbsoluteUrl;
        System.out.println("分类url:" + titleTypeUrl);

        // 获取页数
        String typePage = TSFuuCrawice.getPage(titleTypeUrl, "utf-8", headers, 10);
        int pageNum = new TSFuuCrawice().getPageNum(typePage, ".pageList a", TSFuuCrawice.SELECT_TYPE_ATTA, "href");

        // 遍历该类型所有页面
        for (int pageIndex = 1; pageIndex < pageNum + 1; pageIndex++) {

            long now = System.currentTimeMillis();
            GroupGoingByTypePage(url, saveFilePath, headers, charset, titleName, titleTypeName, titleTypeUrl, pageIndex);
            long now2 = System.currentTimeMillis();
            System.out.println("GroupGoingByTypePage花费的时间:" + (now2 - now));
        }
    }

    private static void GroupGoingByTypePage(String url, String saveFilePath, Header[] headers, String charset, String titleName, String titleTypeName, String titleTypeUrl, int pageIndex) {
        String typePageUrl = titleTypeUrl + pageIndex + ".htm";
        String typePagePage = TSFuuCrawice.getPage(typePageUrl, charset, headers, 15);
        // 该漫画的名称
        List<String> names = TSFuuCrawice.getPageInfos(typePagePage, ".textList>li>a", TSFuuCrawice.SELECT_TYPE_TEXT, null);
        // 该漫画的地址
        List<String> infoUrls = TSFuuCrawice.getPageInfos(typePagePage, ".textList>li>a", TSFuuCrawice.SELECT_TYPE_ATTA, "href");

        for (int infoIndex = 0; infoIndex < names.size(); infoIndex++) {
            String InfoName = names.get(infoIndex);
            String infoPage = TSFuuCrawice.getPage(url + infoUrls.get(infoIndex), charset, headers, 10);
            List<String> fileUrls = TSFuuCrawice.getPageInfos(infoPage, ".picContent img", TSFuuCrawice.SELECT_TYPE_ATTA, "src");

            String localFileUrl = saveFilePath + "/" + titleName + "/" + titleTypeName + "/" + InfoName;

            for (String fileUrl : fileUrls) {
                String substring = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
                stringStringHashMap.put("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
                stringStringHashMap.put("Accept-Encoding", "gzip, deflate");
                stringStringHashMap.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
                stringStringHashMap.put("Connection", "Connection");
                stringStringHashMap.put("Host", "img.581gg.com");
                stringStringHashMap.put("Referer", "https://www.275uu.com");
                try {

                    long now = System.currentTimeMillis();
                    TSFuuCrawice.saveJpgImgFile(fileUrl, localFileUrl, substring, stringStringHashMap);
                    long now2 = System.currentTimeMillis();
                    System.out.println("saveJpgImgFile花费的时间:" + (now2 - now));
                } catch (Exception e) {
                    System.out.println(localFileUrl + "的存储时，出现异常");
                    e.printStackTrace();
                }
            }
        }
    }
}
