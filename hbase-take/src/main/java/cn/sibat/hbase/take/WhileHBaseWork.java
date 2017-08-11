package cn.sibat.hbase.take;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanphonfy on 2017/7/27.
 * 用其中几天的数据模拟多天的数据,一般为一个月一个jar包
 * shell脚本
 * nohup java -cp hbase-tool-MN-SNAPSHOT-jar-with-dependencies.jar cn.sibat.hbase.tool.WhileHBaseWork 12 31 2017-07- 1 31 2017-03- >test.log &
 */
public class WhileHBaseWork {
    public static void main(String[] args) throws IOException, ParseException {
        int startCountR = StringUtil.getInt(args[0], 0);//读取文本开始日
        int temp = startCountR;
        int endCountR = StringUtil.getInt(args[1], 0);//读取文本结束日
        String startDateR = args[2];//2017-07-//读取的文本

        int dayM = StringUtil.getInt(args[3], 0);//模拟开始日
        int endDayM = StringUtil.getInt(args[4], 0);//模拟结束日
        String modDateM = args[5];//2017-04-//模拟日期

        while (startCountR <= endCountR && dayM <= endDayM) {
            String fileName = startDateR + StringUtil.getDay(startCountR) + ".txt";
            String date = modDateM + StringUtil.getDay(dayM);
//            FileReader fileReader = new FileReader("F:\\code\\172.20.104.248-svn\\code\\tos\\trunk\\hbase-tool\\2017-07-12.txt\\"+fileName);
            FileReader fileReader = new FileReader("/home/public/zhengshaorong/backup/" + fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String str;
            List<String> list = new ArrayList<>();
            int lineNum = 0;
            long start = System.currentTimeMillis();
            // Read all lines
            while ((str = reader.readLine()) != null) {
                try {
                    if (lineNum == 40000) {
                        HBaseDAO.insertWhile("GdRoad", list, date);
                        list = new ArrayList<>();
                        lineNum = 0;
                    }
                    list.add(str);
                    lineNum++;
                } catch (Exception e) {
                    System.out.println("出错啦，损失40000行!!!!!!");
                    e.printStackTrace();
                }
            }

            if (list != null && list.size() != 0) {
                HBaseDAO.insert("zsr_test", list);
            }
            System.out.println(System.currentTimeMillis() - start + "ms");
            startCountR++;
            dayM++;
            if (startCountR > endCountR) {
                startCountR = temp;
            }
        }
    }
}
