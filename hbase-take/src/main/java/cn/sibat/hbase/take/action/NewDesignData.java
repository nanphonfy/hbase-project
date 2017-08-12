package cn.sibat.hbase.take.action;

import cn.sibat.hbase.take.util.FilesUtil;
import cn.sibat.hbase.take.util.HBaseDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by nanphonfy on 2017/8/12.
 * 重新设计rowkey和列名，高表改成宽表
 * shell脚本如下：
 * ps aux|grep hbase-take-new-1.0-SNAPSHOT-jar-with-dependencies.jar|awk '{print $2}'|xargs kill -9
 * nohup java -cp hbase-take-new-1.0-SNAPSHOT-jar-with-dependencies.jar cn.sibat.hbase.take.action.NewDesignData GdRoadNew /home/public/hbaseOptimize/File/ > newTest.log &
 */
public class NewDesignData {
    private static final Logger logger = LoggerFactory.getLogger(NewDesignData.class);

    public static void main(String[] args) throws IOException, ParseException {
        final String tableName = args[0];
        final String directory = args[1];//"/home/public/zhengshaorong/backup/"

        logger.info("tableName:{} directory:{}", tableName, directory);

        if (!HBaseDAO.tableExists(tableName)) {
            logger.info("------------->创建表:{}", tableName);
        }

        FilesUtil filesUtil = new FilesUtil();
        List<String> filesPath = filesUtil.getFiles(directory);
        List<String> fileNames = new ArrayList<>();
        for (String filePath : filesPath) {
            try {
                String[] arr = filePath.split("/");
                fileNames.add(arr[arr.length - 1]);
            } catch (Exception e) {
                logger.error("--------------->get filePath error...{}", e.getMessage());
            }
        }

        logger.info("fileNames:{}", fileNames);

        // 使用线程池多线程处理，优化
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 3, 500, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        for (final String fileName : fileNames) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        hbaseTaking(tableName, directory, fileName);
                    } catch (IOException e) {
                        logger.error("---------->hbase inserting error!--------->{}", e.getMessage());
                    }
                }
            });

            try {
                Thread.sleep(100000);//休眠100秒
            } catch (InterruptedException e) {
                logger.error("Thread error...{}", e.getMessage());
            }
        }

    }

    /**
     * 读取文本切割，批量插入hbase
     *
     * @param tableName
     * @param directory
     * @param fileName
     * @throws FileNotFoundException
     */
    private static void hbaseTaking(String tableName, String directory, String fileName) throws IOException {
        FileReader fileReader = new FileReader(directory + fileName);
        BufferedReader reader = new BufferedReader(fileReader);
        String str;
        List<String> list = new ArrayList<>();
        int lineNum = 0;//对行计数，批量插入
        long start = System.currentTimeMillis();
        // Read all lines
        while ((str = reader.readLine()) != null) {
            try {
                if (lineNum == 40000) {
                    HBaseDAO.insertNewSecond(tableName, list);
                    list = new ArrayList<>();
                    lineNum = 0;
                }
                list.add(str);
                lineNum++;
            } catch (Exception e) {
                logger.error("error occurred when hbase inserting... {}", e.getMessage());
            }
        }

        if (list != null && list.size() != 0) {
            HBaseDAO.insertNewSecond(tableName, list);
        }
        logger.info("insert hbase table: {} ,fileName:{}", tableName, fileName);
        logger.info("time consuming:{} ms", (System.currentTimeMillis() - start));
    }
}
