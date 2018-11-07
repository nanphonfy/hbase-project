package cn.nanphonfy.util;

import java.io.*;

/**
 * 写文件的工具
 * @author nanphonfy(南风zsr)
 * @date 2018/11/7
 */
public class TextFileUtil {
    //输出流变量
    private BufferedWriter bufferedWriter;

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    public TextFileUtil(String filePath) {
        try {
            File newFile = new File(filePath);
            // 建立一个输入流对象reader
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(newFile));
            // 建立一个对象，它把文件内容转成计算机能读懂的语言
            bufferedWriter = new BufferedWriter(outputStreamWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 传入字符串，到输出流写入文件
     *
     * @param line
     * @param bufferedWriter
     */
    public void writeLine(String line, BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     * @param bufferedWriter
     */
    public void releaseBufferedWriter(BufferedWriter bufferedWriter) {
        if(bufferedWriter != null){
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
