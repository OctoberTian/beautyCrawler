package com.zgc123.www;

import com.zgc123.www.utils.HttpClientPoolUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        try {
            // 获取标题栏小姐姐类型分组
            final String charset = "gbk";
            String url = "http://www.mm131.com";
            String s = sendGet(charset, null, url,100,0);
            Document parse = Jsoup.parse(s);
            final Elements nav = parse.select(".nav ul li a");

//            // 遍历标题栏小姐姐类型分组(只有静态或不可改变量才可以带入线程中)
//            for (int titleIndex = 1; titleIndex < nav.size()-1; titleIndex++) {
//
//
//
//
//
//            }

            // 7个线程就够了，手机下载每秒大概1.5M左右。
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String titleName = nav.get(1).text();
                        String titleUrl = nav.get(1).attr("href");
                        String titlePage = sendGet(charset, null, titleUrl,50,0);
                        // 分头行动
                        groupGoing(charset, titleName, titleUrl, titlePage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String titleName = nav.get(2).text();
                        String titleUrl = nav.get(2).attr("href");
                        String titlePage = sendGet(charset, null, titleUrl,50,0);
                        // 分头行动
                        groupGoing(charset, titleName, titleUrl, titlePage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String titleName = nav.get(3).text();
                        String titleUrl = nav.get(3).attr("href");
                        String titlePage = sendGet(charset, null, titleUrl,50,0);
                        // 分头行动
                        groupGoing(charset, titleName, titleUrl, titlePage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String titleName = nav.get(4).text();
                        String titleUrl = nav.get(4).attr("href");
                        String titlePage = sendGet(charset, null, titleUrl,50,0);
                        // 分头行动
                        groupGoing(charset, titleName, titleUrl, titlePage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String titleName = nav.get(5).text();
                        String titleUrl = nav.get(5).attr("href");
                        String titlePage = sendGet(charset, null, titleUrl,50,0);
                        // 分头行动
                        groupGoing(charset, titleName, titleUrl, titlePage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String titleName = nav.get(6).text();
                        String titleUrl = nav.get(6).attr("href");
                        String titlePage = sendGet(charset, null, titleUrl,50,0);
                        // 分头行动
                        groupGoing(charset, titleName, titleUrl, titlePage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void groupGoing(String charset, String titleName, String titleUrl, String titlePage) throws Exception {
        //获取该小姐姐分组的页数
        Elements changePageEle = Jsoup.parse(titlePage).select(".page-en");
        Element endPage = changePageEle.get(changePageEle.size() - 1);
        String endPageHref = endPage.attr("href");
        int pageNum = Integer.parseInt(endPageHref.substring(endPageHref.lastIndexOf("_") + 1, endPageHref.indexOf(".")));

        // 遍历每页小姐姐分组的页面
        for (int titlePageIndex = 1; titlePageIndex < pageNum+1; titlePageIndex++) {
            String meinvIndexPage = null;
            if (titlePageIndex == 1) {
                meinvIndexPage = titleUrl;
            } else {
                meinvIndexPage = titleUrl + endPageHref.substring(0, endPageHref.lastIndexOf("_")) + "_" + titlePageIndex + endPageHref.substring(endPageHref.lastIndexOf("."));
            }
            // 获取页面上的每一个小姐姐的专栏地址
            String meinvAllPage = sendGet(charset, null, meinvIndexPage,20,0);
            if ("".equals(meinvIndexPage)) {
                System.out.println("总是失败,直接跳过:"+meinvIndexPage);
            }
            Elements select = Jsoup.parse(meinvAllPage).select(".list-left dd a");

            // 遍历每一个小姐姐的专栏
            for (Element pageIndexEle : select) {
                String fileNameUrl = "F:/little older sister/";
                fileNameUrl += titleName + "/";
                String meinvUrl = pageIndexEle.attr("href");
                if (pageIndexEle.select("img").size() < 1) {
                    // 这不是小姐姐的专栏,这是页码,不要它.
                    continue;
                }
                String meinvName = pageIndexEle.select("img").get(0).attr("alt");
                System.out.println(meinvUrl);
                System.out.println(meinvName);
                fileNameUrl += meinvName;

                // 获取专栏小姐姐的展示页数
                String meinvHomePage = sendGet(charset, null, meinvUrl,5,0);
                if ("".equals(meinvHomePage)) {
                    System.out.println("总是失败,直接跳过:"+meinvUrl);
                }
                Document meinvHomePageParse = Jsoup.parse(meinvHomePage);
                Elements select1 = meinvHomePageParse.select(".content-page .page-en");
                int pageSize = select1.size();

                // 遍历这个小姐姐的所有专栏页面（每页一张小姐姐的个人秀）
                for (int meinvPageIndex = 1; meinvPageIndex < pageSize+ 1; meinvPageIndex++) {
                    String meinvIndexPageUrl = null;
                    if (meinvPageIndex == 1) {
                        meinvIndexPageUrl = meinvUrl;
                    } else {
                        int i1 = meinvUrl.lastIndexOf(".");
                        meinvIndexPageUrl = meinvUrl.substring(0, i1) + "_" + meinvPageIndex + meinvUrl.substring(i1);
                    }

                    // 赶紧保存好。。。干巴爹
                    String s1 = sendGet(charset, null, meinvIndexPageUrl,3,0);
                    if ("".equals(s1)) {
                        System.out.println("总是失败,直接跳过:"+meinvIndexPageUrl);
                        continue;
                    }
                    Document document = Jsoup.parse(s1);
                    Element alt = document.select(".content-pic a img").get(0);
                    String imgUrl = alt.attr("src");
                    String filePath = fileNameUrl.concat("/" + meinvPageIndex + ".jpg");
                    if (new File(filePath).exists()) {
                        // 图片已经存在了
                        System.out.println(filePath + ",已存在该小姐姐.跳过咯...");
                        continue;
                    }
                    saveJpgImgFile(imgUrl, fileNameUrl, meinvPageIndex + "");
                }
            }
        }
    }

    /**
     * 发送请求并获取返回信息的字符串
     *
     * @param charset
     * @param uri
     * @return
     * @throws IOException
     */
    private static String sendGet(String charset, String param, String uri,int maxCount,int count) {
        String execute = HttpClientPoolUtil.execute(uri, charset,new Header[]{});

        if (!"".equals(execute)){
            return execute;
        }

        if (count == maxCount){
            return "";
        }
        return sendGet(charset,param,uri,maxCount,++count);
    }

    /**
     * 保存文件到本地
     *
     * @param jpgImgUrl
     * @param localFileUrl
     * @throws Exception
     */
    private static void saveJpgImgFile(String jpgImgUrl, String localFileUrl, String localFileName) throws Exception {
        System.out.println("图片地址：" + jpgImgUrl + ",存放路径：" + localFileUrl + ",文件名:" + localFileName);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet(jpgImgUrl);
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
        request.setHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        request.setHeader("Accept-Encoding", "gzip, deflate");
        request.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        request.setHeader("Connection", "Connection");
        request.setHeader("Host", "img1.mm131.me");
        request.setHeader("Referer", "http://www.mm131.www/qingchun/3091_2.html");
        File file = new File(localFileUrl);
        if (!file.exists()) {
            file.mkdirs();
        }
        File saveFileUrl = new File(localFileUrl + "/" + localFileName + ".jpg");
        if (!saveFileUrl.exists()) {
            saveFileUrl.createNewFile();
        }
        InputStream content = httpclient.execute(request).getEntity().getContent();
        byte[] bytes = readInputStream(content);
        FileUtils.writeByteArrayToFile(saveFileUrl, bytes);
    }

    /**
     * 流-> 字节数组
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }


}
