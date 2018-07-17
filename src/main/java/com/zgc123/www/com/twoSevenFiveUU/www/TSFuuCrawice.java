package com.zgc123.www.com.twoSevenFiveUU.www;

import com.zgc123.www.service.MyCrawiceUtilsAbstact;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TSFuuCrawice extends MyCrawiceUtilsAbstact{

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
