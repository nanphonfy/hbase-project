package cn.nanphonfy;

import cn.nanphonfy.domain.HealthQuestionnaireClassification;
import cn.nanphonfy.util.FastJsonUtil;
import cn.nanphonfy.util.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 该类用于解析非excel文件
 * @author nanphonfy(南风zsr)
 * @date 2018/11/5
 */
public class Test3 {
    public static void main(String[] args) throws IOException {
        //File newFile = new File("C:\\Users\\NAN\\Desktop\\data\\其他.txt");
        File newFile = new File("D:\\code\\text-code\\其他.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(newFile)); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = "";
        List<HealthQuestionnaireClassification> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            String[] arr = line.split("、");
            for(int i=1;i<arr.length;i++){
                HealthQuestionnaireClassification  obj= new HealthQuestionnaireClassification();
                obj.setParentId(arr[0]);
                obj.setId(StringUtil.getSequenceId(arr[0],i,-1));
                obj.setClassification(StringUtil.removeDigital(arr[i]));
                obj.setSequence(String.valueOf(i));

                list.add(obj);
            }
        }
        System.out.println(FastJsonUtil.toJson(list));
    }
}
