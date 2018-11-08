package cn.nanphonfy;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析oracle表为domain对象
 */
public class Oracle2DomainTest {

   public static void main(String args[]) {
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            String A="1.txt";
            String B="1-xml.txt";
//            String BASE="D:\\Users\\zhengsr001\\Desktop\\";
            String BASE="C:\\Users\\NAN\\Desktop\\data\\";
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
                line = line.replace(" ","|").toLowerCase();
                String[] lineArr = line.split("\\|");
                String[] arr = new String[3];
                int row=0;
                for(int i=0;i<lineArr.length;i++){
                    if(!"".equals(lineArr[i])){
                        if(!"y".equals(lineArr[i])){
                            arr[row] = lineArr[i];
                            row++;
                        }
                        if(row == 3){
                            break;
                        }
                    }
                }

                String field = transferFormat(arr[0]);
                String type = arr[1];
                String common = arr[2];
                //System.out.println(field+type+common);
                System.out.print(arr[0]+",");
                String newLine = "";
                if(type != null){
                    if (type.contains("varchar") || type.contains("char")) {
                        newLine = transferFiled("String",field);
                    }else if(type.contains("integer")){
                        newLine = transferFiled("Integer",field);
                    }else if(type.contains("date")){
                        newLine = transferFiled("Date",field);
                    }
                    out.write(String.format("/**%s**/",common)+"\n"+newLine); // \r\n即为换行
                    out.newLine();
                    out.flush(); // 把缓存区内容压入文件
                }
            }

            out.close(); // 最后记得关闭文件
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String transferFiled(String type,String field){
        StringBuffer sb = new StringBuffer();
        sb.append("private ").append(type).append(" ").append(field).append(";");
        return sb.toString();
    }

    private static String transferFormat(String field){
       StringBuffer sb = new StringBuffer();
       for (int i=0;i<field.length();i++){
           if(field.charAt(i) == '_'){
               char temp = field.charAt(i+1);
               Character ch=new Character(temp);
               temp=ch.toUpperCase(temp);
               sb.append(temp);
               i++;
           }else {
               sb.append(field.charAt(i));
           }
       }
       return sb.toString();
    }
}
