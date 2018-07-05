package com.zgc123.www.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author: ZGC
 * @Date Created in 下午 1:45 2018/7/5
 */
public class PropertiesHander {
    public static Properties properties = null;

    public static String getPropertiesValue(String key) {
        try {
            if (properties == null) {
                properties = new Properties();
                String path = PropertiesHander.class.getResource("/job_task.properties").getFile();
                FileInputStream in = new FileInputStream(new File(path));
                properties.load(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }
}
