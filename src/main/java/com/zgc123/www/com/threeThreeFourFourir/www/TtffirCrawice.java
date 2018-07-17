package com.zgc123.www.com.threeThreeFourFourir.www;

import com.zgc123.www.service.MyCrawiceUtilsAbstact;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class TtffirCrawice extends MyCrawiceUtilsAbstact {

    @Override
    public int getPageNum(String htmlPage, String jsoupSelectStr, int type, String attaName) {
        Elements select = Jsoup.parse(htmlPage).select(jsoupSelectStr);
        if (select == null||select.size()<1){
            return 0;
        }
        Element element = select.get(select.size()-1);
        String s = _getInfos(element, attaName, type);
        return Integer.parseInt(s.substring(0, s.lastIndexOf(".")));
    }


}
