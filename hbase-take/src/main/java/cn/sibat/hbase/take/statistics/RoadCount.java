package cn.sibat.hbase.take.statistics;

import cn.sibat.hbase.take.HBaseDAO;

import java.io.*;
import java.util.*;

/**
 * Created by nanphonfy on 2017/8/9.
 */
public class RoadCount {
    public static void main(String[] args) throws IOException {
        Map<String, Integer> roadIdsMap = new LinkedHashMap<>();
        Map<String, LinkedHashMap<String, Integer>> hoursRoadIdsMap = new LinkedHashMap<>();

        String path = "F:\\code\\172.20.104.248-svn\\code\\tos\\trunk\\hbase-tool\\2017-07-12.txt\\";
        String fileName = "2017-07-12.txt";
//        String path = args[0];//"/home/public/zhengshaorong/backup/"
//        String fileName = args[1]; //"2017-08-08.txt"

        long start = System.currentTimeMillis();
        FileReader fileReader = new FileReader(path + fileName);
        BufferedReader reader = new BufferedReader(fileReader);
        String str;
        List<String> list = new ArrayList<>();
        int lineNum = 0;
        // Read all lines
        while ((str = reader.readLine()) != null) {
            try {
                String[] arr = str.split(":");
                String roadId = arr[0];

                arr = str.split("\\|");
                String key = arr[0];
                String time = key.substring(11).substring(9, 11);//小时

                if (roadIdsMap.get(roadId) == null) {
                    roadIdsMap.put(roadId, 1);
                } else {
                    int count = roadIdsMap.get(roadId);
                    roadIdsMap.put(roadId, ++(count));
                }

                if (hoursRoadIdsMap.get(time) == null) {
                    LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
                    hoursRoadIdsMap.put(time, map);
                    hoursRoadIdsMap.get(time).put(roadId, 1);
                } else {
                    if (hoursRoadIdsMap.get(time).get(roadId) == null) {
                        hoursRoadIdsMap.get(time).put(roadId, 1);
                    } else {
                        int count = hoursRoadIdsMap.get(time).get(roadId);
                        hoursRoadIdsMap.get(time).put(roadId, ++count);
                    }
                }
            } catch (Exception e) {
                System.out.println("出错!!!!!!");
                e.printStackTrace();
            }

        }
        BufferedWriter bw = null;

        FileOutputStream fos = new FileOutputStream(path + fileName + ".analy");
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        bw = new BufferedWriter(osw);// 把filewriter的写法写成FileOutputStream形式

        for (String hour : hoursRoadIdsMap.keySet()) {
            for (String roadId : hoursRoadIdsMap.get(hour).keySet()) {
                writeToFile(hour + " " + roadId + " " + hoursRoadIdsMap.get(hour).get(roadId), bw);
            }
        }

        fos = new FileOutputStream(path + fileName + ".analy2");
        osw = new OutputStreamWriter(fos, "UTF-8");
        bw = new BufferedWriter(osw);// 把filewriter的写法写成FileOutputStream形式
        for (String roadId : roadIdsMap.keySet()) {
            writeToFile(roadId + " " + roadIdsMap.get(roadId), bw);
        }
        bw.close();
        osw.close();
        fos.close();
    }

    public static void writeToFile(String content, BufferedWriter bw) throws IOException {
        try {
            bw.write(content);
            bw.newLine();
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
