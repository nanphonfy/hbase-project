package cn.sibat.hbase.take.util;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAVA遍历文件夹中的所有文件
 *
 * @author nanphonfy
 */
public class FilesUtil {
    private static Logger log = Logger.getLogger(FilesUtil.class);
    private List<String> absolutePaths = new LinkedList<>();

    /*
     * 通过递归得到某一路径下所有的目录及其文件(记得必须得传目錄，不能传文件)
     */
    public List<String> getFiles(String filePath) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        if (files == null) {// 如果传进来的是文件，而不是目�?
            absolutePaths.add(filePath);
            return absolutePaths;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                getFiles(file.getAbsolutePath());
            } else {// 默认为没有目录，只有文件
                absolutePaths.add(file.getAbsolutePath().toString());
            }
        }
        return absolutePaths;
    }
}
