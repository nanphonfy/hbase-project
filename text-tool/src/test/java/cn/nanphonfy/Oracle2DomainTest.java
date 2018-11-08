package cn.nanphonfy;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ����oracle��Ϊdomain����
 */
public class Oracle2DomainTest {

   public static void main(String args[]) {
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw
            String A="1.txt";
            String B="1-xml.txt";
//            String BASE="D:\\Users\\zhengsr001\\Desktop\\";
            String BASE="C:\\Users\\NAN\\Desktop\\data\\";
            /* ����TXT�ļ� */
            String pathname = BASE+A; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // ����һ������������reader
            BufferedReader br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������

            /* д��Txt�ļ� */
            File writename = new File(BASE+B); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�
            writename.createNewFile(); // �������ļ�
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
                    out.write(String.format("/**%s**/",common)+"\n"+newLine); // \r\n��Ϊ����
                    out.newLine();
                    out.flush(); // �ѻ���������ѹ���ļ�
                }
            }

            out.close(); // ���ǵùر��ļ�
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
