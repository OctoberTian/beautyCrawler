package com.zgc123.www.com.threeThreeFourFourir.www;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;

/**
 * 开始运行的主方法
 */
public class startMain {

    public static void main(String[] args) {
       start("https://www.4455yb.com/", "J:/tempSaveFile");
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
        String homePage = TtffirCrawice.getPage(url, charset, headers, 1);
        final Elements rowInfos = Jsoup.parse(homePage).select(".menu");

        // 图片区
        Element element = rowInfos.get(0);
        groupGoingByTitle(url, saveFilePath, headers, charset, element);
    }

    private static void groupGoingByTitle(final String url,final String saveFilePath,final Header[] headers,final String charset,final Element element) {
        final String titleName = TtffirCrawice.getPageInfos(element, "dt a", TtffirCrawice.SELECT_TYPE_TEXT, null).get(0);
        final List<String> titleTypeAbsoluteUrls = TtffirCrawice.getPageInfos(element, "dd a", TtffirCrawice.SELECT_TYPE_ATTA, "href");
        final List<String> titleTypeNames = TtffirCrawice.getPageInfos(element, "dd a", TtffirCrawice.SELECT_TYPE_TEXT, null);
        System.out.println("主题:" + titleName);

        /**
         * 卡通动漫
         */
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                groupGoingByType(url, saveFilePath, headers, charset, titleName, titleTypeAbsoluteUrls, titleTypeNames, 0);
//            }
//        }).start();

        /**
         * 亚洲图片
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                groupGoingByType(url, saveFilePath, headers, charset, titleName, titleTypeAbsoluteUrls, titleTypeNames, 1);
            }
        }).start();

        /**
         * 欧美图片
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                groupGoingByType(url, saveFilePath, headers, charset, titleName, titleTypeAbsoluteUrls, titleTypeNames, 2);
            }
        }).start();

        /**
         * 偷拍自拍
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                groupGoingByType(url, saveFilePath, headers, charset, titleName, titleTypeAbsoluteUrls, titleTypeNames, 3);
            }
        }).start();

        /**
         * 乱论淑女
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                groupGoingByType(url, saveFilePath, headers, charset, titleName, titleTypeAbsoluteUrls, titleTypeNames, 4);
            }
        }).start();

        /**
         * 精品套图
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                groupGoingByType(url, saveFilePath, headers, charset, titleName, titleTypeAbsoluteUrls, titleTypeNames, 5);
            }
        }).start();

        /**
         * 同性美图
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                groupGoingByType(url, saveFilePath, headers, charset, titleName, titleTypeAbsoluteUrls, titleTypeNames, 6);
            }
        }).start();

        /**
         * 美腿丝袜
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                groupGoingByType(url, saveFilePath, headers, charset, titleName, titleTypeAbsoluteUrls, titleTypeNames, 7);
            }
        }).start();


        System.out.println("-------------------------------------");
    }

    private static void groupGoingByType(String url, String saveFilePath, Header[] headers, String charset, String titleName, List<String> titleTypeAbsoluteUrls, List<String> titleTypeNames, int titleTypeIndex) {
        String titleTypeName = titleTypeNames.get(titleTypeIndex);
        System.out.println("标题:" + titleTypeName);
        String titleTypeAbsoluteUrl = titleTypeAbsoluteUrls.get(titleTypeIndex);
        String titleTypeUrl = url + titleTypeAbsoluteUrl;
        System.out.println("分类url:" + titleTypeUrl);

        // 获取页数
        String typePage = TtffirCrawice.getPage(titleTypeUrl, "utf-8", headers, 10);
        int pageNum = new TtffirCrawice().getPageNum(typePage, ".page a", TtffirCrawice.SELECT_TYPE_ATTA, "href");

        // 遍历该类型所有页面
        for (int pageIndex = 1; pageIndex < pageNum + 1; pageIndex++) {
            GroupGoingByTypePage(url, saveFilePath, headers, charset, titleName, titleTypeName, titleTypeUrl, pageIndex);
        }
    }

    private static void GroupGoingByTypePage(String url, String saveFilePath, Header[] headers, String charset, String titleName, String titleTypeName, String titleTypeUrl, int pageIndex) {
        String typePageUrl = titleTypeUrl + pageIndex + ".htm";
        String typePagePage = TtffirCrawice.getPage(typePageUrl, charset, headers, 15);
        // 该漫画的名称
        List<String> names = TtffirCrawice.getPageInfos(typePagePage, ".news_list li a", 2, null);
        // 该漫画的地址
        List<String> infoUrls = TtffirCrawice.getPageInfos(typePagePage, ".news_list li a", 1, "href");

        for (int infoIndex = 0; infoIndex < names.size(); infoIndex++) {
            String InfoName = names.get(infoIndex);
            String infoPage = TtffirCrawice.getPage(url + infoUrls.get(infoIndex), charset, headers, 10);
            List<String> fileUrls = TtffirCrawice.getPageInfos(infoPage, ".news img", TtffirCrawice.SELECT_TYPE_ATTA, "src");

            String localFileUrl = saveFilePath + "/" + titleName + "/" + titleTypeName + "/" + InfoName;

            for (String fileUrl : fileUrls) {
                String substring = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                try {
                    TtffirCrawice.saveJpgImgFile(fileUrl, localFileUrl, substring, stringStringHashMap);
                } catch (Exception e) {
                    System.out.println(localFileUrl + "的存储时，出现异常");
                    e.printStackTrace();
                }
            }
        }
    }
}
