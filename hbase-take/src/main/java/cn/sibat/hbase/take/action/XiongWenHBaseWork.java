package cn.sibat.hbase.take.action;

import cn.sibat.hbase.take.util.FilesUtil;
import cn.sibat.hbase.take.util.HBaseDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanphonfy on 2017/7/27.
 * 该类用于插入原始数据的数据源
 * 设计了新的rowkey和列名
 * shell脚本示例:nohup java -cp hbase-take-new-1.0-SNAPSHOT-jar-with-dependencies.jar cn.sibat.hbase.take.action.XiongWenHBaseWork GdRoadNew /home/public/zhengshaorong/backup/ 2017-08-01.txt >test.log &
 */
public class XiongWenHBaseWork {
    public static void main(String[] args) throws IOException, ParseException {
        String tableName = args[0];
        String path = args[1];//"/home/public/zhengshaorong/backup/"
        String fileName = args[2]; //"2017-08-08.txt"

        if (!HBaseDAO.tableExists(tableName)) {
            System.out.println("------------->创建表");
        }

        long start = System.currentTimeMillis();
        FileReader fileReader = new FileReader(path + fileName);
        BufferedReader reader = new BufferedReader(fileReader);
        String str;
        List<String> list = new ArrayList<>();
        int lineNum = 0;
        // Read all lines
        while ((str = reader.readLine()) != null) {
            try {
                if (lineNum == 40000) {
                    HBaseDAO.insertNew(tableName, list);
                    list = new ArrayList<>();
                    lineNum = 0;
                }
                list.add(str);
                lineNum++;
            } catch (Exception e) {
                System.out.println("出错，丢失40000行!!!!!!");
                e.printStackTrace();
            }
        }

        if (list != null && list.size() != 0) {
            HBaseDAO.insertNew(tableName, list);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }
}
