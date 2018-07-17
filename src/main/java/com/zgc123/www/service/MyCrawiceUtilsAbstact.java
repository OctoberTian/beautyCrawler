package com.zgc123.www.service;

import com.zgc123.www.utils.HttpClientPoolUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 爬虫可扩展工具类（可以扩展成适合每个网站的抽象类）
 *
 * @author clever4zgc
 */
public abstract class MyCrawiceUtilsAbstact {

    /**
     * 标签属性
     */
    public final static int SELECT_TYPE_ATTA = 1;
    /**
     * 标签文本
     */
    public final static int SELECT_TYPE_TEXT = 2;

    /**
     * 获取页面
     *
     * @param url      资源ID
     * @param charset  解析字符类型
     * @param headers  所有文件头
     * @param maxCount 最大链接失败次数
     * @return html页面字符串
     */
    public static String getPage(String url, String charset, Header[] headers, int maxCount) {
        return _getPage(url, charset, headers, maxCount, 0);
    }

    /**
     * 获取页数
     * 最好根据网站实际情况来重写或重载该方法
     * 该方法只是简单的根据获取到的元素数量来判断页数
     *
     * @param htmlPage       html页面字符串
     * @param jsoupSelectStr 用来筛选标签的选择器（根据jsoup的选择器正则来写）
     * @param type           类型，1.标签属性 2.标签内容 3.标签名
     * @param attaName       属性名称，不需要的话可以填null
     * @return 页数
     */
    public abstract int getPageNum(String htmlPage, String jsoupSelectStr, int type, String attaName);


    /**
     * 页面筛选后的标签的多个属性值或者多个内容
     *
     * @param htmlPage       html页面字符串
     * @param jsoupSelectStr 用来筛选标签的选择器（根据jsoup的选择器正则来写）
     * @param type           类型，1.标签属性 2.标签内容 3.标签名
     * @param attaName       属性名称，不需要的话可以填null
     * @return 多个属性值或者多个内容
     */
    public static List<String> getPageInfos(String htmlPage, String jsoupSelectStr, int type, String attaName) {
        Elements select = Jsoup.parse(htmlPage).select(jsoupSelectStr);
        List<String> infos = _getInfos(select, attaName, type);
        return infos;
    }

    /**
     * 页面筛选后的属性值或者内容
     *
     * @param jsoupSelectStr 用来筛选标签的选择器（根据jsoup的选择器正则来写）
     * @param type           类型，1.标签属性 2.标签内容
     * @param attaName       属性名称，不需要的话可以填null
     * @return 属性值或者内容
     */
    public static List<String> getPageInfos(Element element, String jsoupSelectStr, int type, String attaName) {
        Elements select = element.select(jsoupSelectStr);
        List<String> infos = _getInfos(select, attaName, type);
        return infos;
    }


    /**
     * 页面筛选后的属性值或者内容
     * 最好根据网站实际情况来重写或重载该方法
     *
     * @param jsoupSelectStr 用来筛选标签的选择器（根据jsoup的选择器正则来写）
     * @param type           类型，1.标签属性 2.标签内容
     * @param attaName       属性名称，不需要的话可以填null
     * @return 属性值或者内容
     */
    public static List<List<String>> getPageInfos(Elements element, String jsoupSelectStr, int type, String attaName) {
        List<List<String>> allInfos = new ArrayList<>();
        for (int i = 0; i < element.size(); i++) {
            List<String> infos = getPageInfos(element.get(i), jsoupSelectStr, type, attaName);
            allInfos.add(infos);
        }
        return allInfos;
    }


    /**
     * 保存文件到本地
     *
     * @param jpgImgUrl
     * @param localFileUrl
     * @throws Exception
     */
    public static void saveJpgImgFile(String jpgImgUrl, String localFileUrl, String localFileName,Map<String,String> headers) throws Exception {
        System.out.println("图片地址：" + jpgImgUrl + ",存放路径：" + localFileUrl + ",文件名:" + localFileName);

        File file = new File(localFileUrl);
        if (!file.exists()) {
            file.mkdirs();
        }

        String localFilePath = localFileUrl + "/" + localFileName;

        File localFile = new File(localFilePath);
        if(localFile.exists()){
            // 已存在跳过
            System.out.println("这文件我有了，跳过他："+ localFileUrl);
            return;
        }
        File saveFileUrl = new File(localFilePath);
        if (!saveFileUrl.exists()) {
            saveFileUrl.createNewFile();
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet(jpgImgUrl);
        for (String key : headers.keySet()) {
            String value = headers.get(key);
            request.setHeader(key, value);
        }

        long now = System.currentTimeMillis();
        InputStream content = httpclient.execute(request).getEntity().getContent();
        long now2 = System.currentTimeMillis();
        System.out.println("InputStream花费的时间:"+ (now2 - now));

        long now3 = System.currentTimeMillis();
        byte[] bytes = readInputStream(content);
        long now4 = System.currentTimeMillis();
        System.out.println("InputStream花费的时间:"+ (now4 - now3));

        long now5 = System.currentTimeMillis();
        FileUtils.writeByteArrayToFile(saveFileUrl, bytes);
        long now6 = System.currentTimeMillis();
        System.out.println("writeByteArrayToFile花费的时间:"+ (now4 - now3));


        request.clone();
        httpclient.close();
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


    /**
     * 获取页面信息(用于被内部调用)
     * 对httpClient的get请求调用并升级功能的封装
     *
     * @param url      资源ID
     * @param charset  解析字符类型
     * @param maxCount 最大链接失败次数
     * @param headers  所有文件头
     * @param count    连接次数
     * @return HTML页面
     */
    private static String _getPage(String url, String charset, Header[] headers, int maxCount, int count) {
        // httpClient线程池工具类中封装的get请求
        String execute = HttpClientPoolUtil.execute(url, charset, headers);
        // 递归和循环都可以，但是还是用递归好
        if (count == maxCount) {
            return "";
        }
        if (!"".equals(execute)) {
            return execute;
        }
        return _getPage(charset, url, headers, maxCount, ++count);
    }

    /**
     * 获取标签信息
     *
     * @param type
     * @param attaName
     * @param select
     * @return
     */
    protected static List<String> _getInfos(Elements select, String attaName, int type) {
        List<String> infos = new ArrayList<>();
        int index = 0;
        switch (type) {
            case 1:
                for (Element element1 : select) {
                    infos.add(element1.attr(attaName));
                }
                break;
            case 2:
                for (Element element1 : select) {
                    infos.add(element1.text());
                }
                break;
            default:
                for (Element element1 : select) {
                    infos.add(element1.text());
                }
                break;
        }
        return infos;
    }

    /**
     * 获取标签信息
     *
     * @param type
     * @param attaName
     * @param select
     * @return
     */
    protected static String _getInfos(Element select, String attaName, int type) {
        switch (type) {
            case 1:
                return select.attr(attaName);
            case 2:
                return select.text();
            default:
                return select.text();
        }
    }


}
