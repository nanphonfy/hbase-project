package cn.nanphonfy;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengshaorong on 2018/11/9.
 */
public class Oracle2InsertXml {
    public static void main(String[] args) {
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            String A="1.txt";
            String B="1-xml.txt";
            String BASE="D:\\Users\\zhengsr001\\Desktop\\";
//            String BASE="C:\\Users\\NAN\\Desktop\\data\\";
            /* 读入TXT文件 */
            String pathname = BASE+A; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言

            /* 写入Txt文件 */
            File writename = new File(BASE+B); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));

            Map<String,Integer> policyMap=new HashMap<String, Integer>();
            String line = "";

            while ((line=br.readLine()) != null) {
                if(line.contains("private")){

                }
                //out.write(String.format("/**%s**/",common)+"\n"+newLine); // \r\n即为换行
                out.newLine();
                out.flush(); // 把缓存区内容压入文件
            }

            out.close(); // 最后记得关闭文件
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
