package cn.sibat.hbase.take.util;

import cn.sibat.hbase.take.AbstractTest;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanphonfy on 2017/8/12.
 */
public class UtilTest extends AbstractTest {
    private long start = 0;

    @Before
    public void before() {
        start = System.currentTimeMillis();
    }

    @After
    public void after() {
        logger.info("cost:{} ms", System.currentTimeMillis() - start);
        logger.info("cost:{} ", StringUtil.getDuration(System.currentTimeMillis() - start));
    }

    /**
     * 测试传入目录，得到该目录的所有文件
     */
    @Test
    public void getFilesTest() {
        FilesUtil filesUtil = new FilesUtil();
        List<String> filesPath = filesUtil.getFiles("F:\\nanphonfy\\code\\storm-kafka-examples");
        logger.info(filesPath.toString());
        List<String> fileNames = new ArrayList<>();
        for (String filePath : filesPath) {
            try {
                String[] arr = filePath.split("\\\\");
                fileNames.add(arr[arr.length - 1]);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        logger.info(fileNames.toString());
    }

    /**
     * 测试原始数据，如何切割key和value
     */
    @Test
    public void getRowKeyAndValue() {
//        String str = "0000047489:16-12-24 18:40:00,1,34.876094818115234,0,0.0,0,0.0,1,34.876095,0,0.0,0,0.0,0,0.0";
        String str = "0000047489:16-12-25 15:00:00,1,30.498327255249023,0,0.0,0,0.0,1,30.498327,0,0.0,0,0.0,0,0.0";
        int rowKeySize = "0000047489:16-12-24 18:40:00,".length();
        logger.info("rowKey:{}", str.substring(0, rowKeySize - 1));
        logger.info("value:{}", str.substring(rowKeySize));
        logger.info("time:{}", str.substring(0, rowKeySize - 1).substring(11));
        logger.info("roadId:{}", str.substring(0, 11));
    }

    /**
     * 测试新设计的rowkey和高表改宽表
     */
    @Test
    public void newDesignDataTest() throws ParseException {
        String row = "0000047489:16-12-25 18:00:00,1,30.498327255249023,0,0.0,0,0.0,1,30.498327,0,0.0,0,0.0,0,0.0";
        String key = row.substring(0, StringUtil.ROW_KEY_SIZE - 1);
        String value = row.substring(StringUtil.ROW_KEY_SIZE);
        logger.info("timestamp:{}", key.substring(11));//timestamp:16-12-25 18:00:00
        logger.info("timestamp:{}", key.substring(11, 19));//timestamp:16-12-25
        long timestamp = StringUtil.getTimestamp(key.substring(11), StringUtil.PATTERN_yy_MM_dd);//历史时间戳
        long newTimestamp = StringUtil.getTimestamp(key.substring(11, 19), StringUtil.PATTERN_yy_MM_dd);//历史时间戳

        long recentTimestamp = (Long.MAX_VALUE - timestamp) / 100000;//5*60*1000 舍弃5个零
        String newKey = key.substring(0, 11) + recentTimestamp;//'道路id:'

        String time = key.substring(20);//06:05:00 得到时间
        int timeslice = StringUtil.getTimesLice(time);//得到时间片
        String qualifier = StringUtil.getQualifier(timeslice);//得到列名

        logger.info("rowkey:{}", newKey);
        logger.info("value:{},qualifier:{}", newKey, qualifier);
        logger.info("timestamp:{}", timestamp);
        logger.info("newTimestamp:{}", newTimestamp);
    }

    /**
     * 扫描单日的数据
     *
     * @throws Exception
     */
    @Test
    public void getByColumnRangeFilterTest() throws Exception {
        Result result = HBaseDAO.getByColumnRangeFilter("GdRoadNew", "0000000429:92233705353379", "001", "288");
        logger.info("rowKey:{}", Bytes.toString(result.getRow()));
        for (KeyValue keyValue : result.raw()) {
            logger.info("rowKey:{},value:{}", Bytes.toString(keyValue.getQualifier()), Bytes.toString(keyValue.getValue()));
        }
    }

    /**
     * 扫描指定日期的数据
     *
     * @throws Exception
     */
    @Test
    public void scanByColumnRangeFilterTest() throws Exception {
        //0000000428:92233705352515,0000000428:92233705353379
//        起始行包括在内，而终止行时不包括在内。一般用区间表示法表示为[startRow,stopRow),所以开始日期需要减一天
        long start = StringUtil.getTimestamp("17-08-00", StringUtil.PATTERN_yy_MM_dd);
        long end = StringUtil.getTimestamp("17-08-02", StringUtil.PATTERN_yy_MM_dd);
        String roadId = "0000000429";
        String startRow = roadId + ":" + StringUtil.getLongMaxSubTimestamp(end);
        String endRow = roadId + ":" + StringUtil.getLongMaxSubTimestamp(start);

        logger.info("startRow:{},endRow:{}", startRow, endRow);

//        ResultScanner rs = HBaseDAO.scanByColumnRangeFilter("GdRoadNew", "0000000428:92233705352515", "0000000428:92233705353379", "001", "288");
        ResultScanner rs = HBaseDAO.scanByColumnRangeFilter("GdRoadNew", startRow, endRow, "001", "288");
        for (Result r : rs) {
            logger.info("======================================");
            logger.info("rowkey:{}", Bytes.toString(r.getRow()));
            for (KeyValue keyValue : r.raw()) {
//                logger.info("rowkey:{}", Bytes.toString(keyValue.getRow()));
                logger.info("qualifier:{},value:{}", Bytes.toString(keyValue.getQualifier()), Bytes.toString(keyValue.getValue()));
            }
        }
    }
}
