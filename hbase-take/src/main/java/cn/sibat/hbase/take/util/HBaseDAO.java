package cn.sibat.hbase.take.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

/**
 * Created by nanphonfy on 2017/8/12.
 * http://www.aboutyun.com/thread-8930-1-1.html
 * http://pangjiuzala.github.io/2015/08/18/HBase%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86-6-%E6%89%AB%E6%8F%8F%E6%93%8D%E4%BD%9C%E4%BB%8B%E7%BB%8D/
 */
public class HBaseDAO {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(HBaseDAO.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Configuration config = null;
    private static HConnection conn = null;

    static {
        try {
            config = HBaseConfiguration.create();
            conn = HConnectionManager.createConnection(config);
        } catch (IOException e) {
            logger.error("HBaseDAO init error!\n{}", e.getMessage());
        }
    }

    /**
     * 表不存在则新建
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public static boolean tableExists(String tableName) throws IOException {
        HBaseAdmin admin = new HBaseAdmin(config);
        if (!admin.tableExists(tableName)) {// whether the table is
            HTableDescriptor table = new HTableDescriptor(tableName);
            table.addFamily(new HColumnDescriptor("value"));
            admin.createTable(table);
            return false;
        }
        return true;
    }

    /**
     * 旧方法的插入
     *
     * @param values
     * @return
     */
    public static boolean insert(String tableName, List<String> values) {
        HTable table = null;
        try {
            table = new HTable(config, Bytes.toBytes(tableName));
            //            connection = ConnectionFactory.createConnection(config);
            //            table = (HTable) connection.getTable(TableName.valueOf(tableName));
            table.setAutoFlushTo(false);
            table.setWriteBufferSize(534534534);//设置得太大，可缩小
            ArrayList<Put> puts = new ArrayList<>();
            for (String row : values) {
                try {
                    String[] arr = row.split("\\|");
                    String key = arr[0];
                    String value = arr[1];
                    Put put = new Put(Bytes.toBytes(key));
                    put.add(Bytes.toBytes("value"), Bytes.toBytes(""), Bytes.toBytes(value));
                    puts.add(put);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //插入数据
            table.put(puts);
            //提交
            table.flushCommits();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 旧方法的插入，原始数据版本2
     *
     * @param values
     * @return
     */
    public static boolean insertSecond(String tableName, List<String> values) {
        try {
            tableExists(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HTableInterface htable = null;
        try {
            htable = conn.getTable(tableName);
            htable.setAutoFlushTo(false);
            htable.setWriteBufferSize(534534534);//设置得太大，可缩小
            ArrayList<Put> puts = new ArrayList<>();
            for (String row : values) {
                try {
                    String key = row.substring(0, StringUtil.ROW_KEY_SIZE - 1);
                    String value = row.substring(StringUtil.ROW_KEY_SIZE);
                    long timestamp = StringUtil.getTimestamp(key.substring(11));//历史时间戳
                    Put put = new Put(Bytes.toBytes(key));
                    put.add(Bytes.toBytes("value"), Bytes.toBytes(""), timestamp, Bytes.toBytes(value));
                    puts.add(put);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //插入数据
            htable.put(puts);
            //提交
            htable.flushCommits();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            hTableClose(htable);
        }
    }

    /**
     * 新设计的rowkey和列名
     *
     * @param tableName
     * @param values
     * @return
     */

    public static boolean insertNew(String tableName, List<String> values) {
        try {
            tableExists(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HTableInterface htable = null;
        try {
            htable = conn.getTable(tableName);
            htable.setAutoFlushTo(false);
            htable.setWriteBufferSize(534534534);//设置得太大，可缩小
            ArrayList<Put> puts = new ArrayList<>();
            for (String row : values) {
                try {
                    String[] arr = row.split("\\|");
                    String key = arr[0];
                    String date = key.substring(11, 19);//得到日期
                    long recentTimestamp = (Long.MAX_VALUE - StringUtil.getTimestamp(date, StringUtil.PATTERN_yy_MM_dd)) / 100000;
                    String newKey = key.substring(0, 11) + recentTimestamp;//道路id+:
                    //String newKey = key.replace(subStr, date.substring(2));//替换该日期

                    String time = key.substring(20);//06:05:00 得到时间
                    int timeslice = StringUtil.getTimesLice(time);//得到时间片
                    String qualifier = StringUtil.getQualifier(timeslice);//得到列名
                    long timestamp = StringUtil.getTimestamp(key.substring(11));//得到所有时间
                    String value = arr[1];

                    Put put = new Put(Bytes.toBytes(newKey));
                    put.add(Bytes.toBytes("value"), Bytes.toBytes(qualifier), timestamp, Bytes.toBytes(value));
                    puts.add(put);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //插入数据
            htable.put(puts);
            //提交
            htable.flushCommits();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            hTableClose(htable);
        }
    }

    /**
     * 新设计的rowkey和列名，第二版
     *
     * @param tableName
     * @param values
     * @return
     */

    public static boolean insertNewSecond(String tableName, List<String> values) {
        try {
            tableExists(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        HTable table = null;
        HTableInterface htable = null;
        try {
//            table = new HTable(config, Bytes.toBytes(tableName));
            htable = conn.getTable(tableName);
            htable.setAutoFlushTo(false);
            htable.setWriteBufferSize(534534534);//设置得太大，可缩小
            ArrayList<Put> puts = new ArrayList<>();
            for (String row : values) {
                try {
                    String key = row.substring(0, StringUtil.ROW_KEY_SIZE - 1);
                    String value = row.substring(StringUtil.ROW_KEY_SIZE);
                    long timestamp = StringUtil.getTimestamp(key.substring(11), StringUtil.PATTERN_yy_MM_dd);//历史时间戳

                    long recentTimestamp = (Long.MAX_VALUE - timestamp) / 100000;//5*60*1000 舍弃5个零
                    String newKey = key.substring(0, 11) + recentTimestamp;//'道路id:'
                    //String newKey = key.replace(subStr, date.substring(2));//替换该日期

                    String time = key.substring(20);//06:05:00 得到时间
                    int timeslice = StringUtil.getTimesLice(time);//得到时间片
                    String qualifier = StringUtil.getQualifier(timeslice);//得到列名

                    Put put = new Put(Bytes.toBytes(newKey));
                    put.add(Bytes.toBytes("value"), Bytes.toBytes(qualifier), timestamp, Bytes.toBytes(value));
                    puts.add(put);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //插入数据
            htable.put(puts);
            //提交
            htable.flushCommits();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            hTableClose(htable);
        }
    }

    /**
     * 一个月模拟多个月的测试方法
     *
     * @param tableName
     * @param values
     * @param date
     * @return
     */
    public static boolean insertWhile(String tableName, List<String> values, String date) {
        HTable table = null;
        try {
            tableExists(tableName);
            table = new HTable(config, Bytes.toBytes(tableName));
            table.setAutoFlushTo(false);
            table.setWriteBufferSize(534534534);
            ArrayList<Put> puts = new ArrayList<>();
            for (String row : values) {
                try {
                    String[] arr = row.split("\\|");
                    String key = arr[0];
                    String subStr = key.substring(11, 19);//日期
                    String newKey = key.replace(subStr, date.substring(2));//替换该日期
                    //                    StringUtil.getTimestamp("17-04-12 06:05:00");
                    long timestamp = StringUtil.getTimestamp(newKey.substring(11));
                    String value = arr[1];
                    Put put = new Put(Bytes.toBytes(newKey));
                    put.add(Bytes.toBytes("value"), Bytes.toBytes(""), timestamp, Bytes.toBytes(value));
                    puts.add(put);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //插入数据
            table.put(puts);
            //提交
            table.flushCommits();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean check() {
        try {
            HBaseAdmin.checkHBaseAvailable(config);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 该方法可改进
     *
     * @param tableName
     * @param startTime
     * @param endTime
     * @throws ParseException
     * @throws FileNotFoundException
     */
    public static void queryAllByTime(String tableName, String startTime, String endTime) throws ParseException, FileNotFoundException {
        File file = new File(startTime.split(" ")[0] + ".txt");
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file))
        );
        /////////////////////////////////////////////////////////////
//        HTable table;
        HTableInterface htable = null;
        ResultScanner rs = null;
        try {
//            table = new HTable(config, Bytes.toBytes(tableName));
            htable = conn.getTable(tableName);
            Scan scan = new Scan();

            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            long start = startDate.getTime();
            long end = endDate.getTime();

            scan.setTimeRange(start, end);
            rs = htable.getScanner(scan);
            int count = 0;
            for (Result r : rs) {
                String rowString = Bytes.toString(r.getRow());
                String valueString = Bytes.toString(r.getValue(Bytes.toBytes("value"), null));

                String line = rowString + "|" + valueString;
                writer.write(line);
                writer.flush();
                writer.newLine();

                if (count % 10000 == 0) {
                    System.out.println("finish number is :" + count);
                }
                count++;
            }
            System.out.println("total:" + count);

            writer.close();
            System.out.println("finish");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 取出起始行的记录
     *
     * @param tableName
     * @param startRow
     * @param endRow
     * @return
     */
    public static ResultScanner scanByStartRowAndEndRow(String tableName, String startRow, String endRow) {
//        HTable table;
        HTableInterface htable = null;
        ResultScanner rs = null;
        try {
//            table = new HTable(config, Bytes.toBytes(tableName));
            htable = conn.getTable(tableName);
            Scan scan = new Scan();
            scan.setStartRow(startRow.getBytes());
            scan.setStopRow(endRow.getBytes());
            rs = htable.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            hTableClose(htable);
        }
        return rs;
    }

    /**
     * 取出rowKey的表格前缀
     *
     * @param tableName
     * @param rowPrifix
     * @param time
     * @return
     * @throws Exception
     */
    public static ResultScanner scanByPrefixFilterAndColumn(String tableName,
                                                            String rowPrifix, String time) throws Exception {
        HTableInterface htable = null;
        ResultScanner rs = null;
        try {
//            table = new HTable(config, Bytes.toBytes(tableName));
            htable = conn.getTable(tableName);
            Scan scan = new Scan();
            Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                    new SubstringComparator(rowPrifix));
            // 设置过滤器
            scan.setFilter(filter);
            rs = htable.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            hTableClose(htable);
        }
        return rs;
    }

    /**
     * 取列名的时间片的范围（按照字典顺序）
     *
     * @param tableName
     * @param minColumn
     * @param maxColumn
     * @return
     * @throws Exception
     */
    public static ResultScanner scanByColumnRangeFilter(String tableName, String startRowOld, String stopRowOld, String minColumn, String maxColumn) throws ParseException {
        //long最大值减去该值，变为stopRow，故需要将日期减1
        String time1 = StringUtil.subOndDay(startRowOld.substring(11));
        String time2 = stopRowOld.substring(11);
        String roadId = startRowOld.substring(0, 11);

        long start = StringUtil.getTimestamp(time1, StringUtil.PATTERN_yy_MM_dd);
        long end = StringUtil.getTimestamp(time2, StringUtil.PATTERN_yy_MM_dd);

        //时间片的范围
        String h_m_s1 = startRowOld.substring(20);
        int timeslice1 = StringUtil.getTimesLice(h_m_s1);
        minColumn = StringUtil.getQualifier(timeslice1);

        String h_m_s2 = stopRowOld.substring(20);
        int timeslice2 = StringUtil.getTimesLice(h_m_s2);
        maxColumn = StringUtil.getQualifier(timeslice2);

        //key的范围
        String startRow  = roadId + StringUtil.getLongMaxSubTimestamp(end);
        String stopRow = roadId + StringUtil.getLongMaxSubTimestamp(start);

        HTableInterface htable = null;
        ResultScanner rs = null;
        try {
            htable = conn.getTable(tableName);
            Scan scan = new Scan();
            scan.setStartRow(startRow.getBytes());
            scan.setStopRow(stopRow.getBytes());

            boolean minColumnlnclusive = true;
            boolean maxColumnlnclusive = true;
            ColumnRangeFilter filter = new ColumnRangeFilter(Bytes.toBytes(minColumn), minColumnlnclusive, Bytes.toBytes(maxColumn), maxColumnlnclusive);
            // 设置过滤器
            scan.setFilter(filter);

            rs = htable.getScanner(scan);

        } catch (IOException e) {
            logger.error("scanByColumnRangeFilter error...\n{}", e.getMessage());
        } finally {
            hTableClose(htable);
        }
        return rs;
    }

    /**
     * 根据rowkey的精确范围，找到所有值
     *
     * @param tableName
     * @param rowKey
     * @param minColumn
     * @param maxColumn
     * @return
     * @throws Exception
     */
    public static Result getByColumnRangeFilter(String tableName, String rowKey, String minColumn, String maxColumn) throws Exception {
        Result r = null;
        HTableInterface htable = null;
        try {
            htable = conn.getTable(tableName);
            Get get = new Get(Bytes.toBytes(rowKey));

            boolean minColumnlnclusive = true;
            boolean maxColumnlnclusive = true;
            ColumnRangeFilter filter = new ColumnRangeFilter(Bytes.toBytes(minColumn), minColumnlnclusive, Bytes.toBytes(maxColumn), maxColumnlnclusive);
            // 设置过滤器
            get.setFilter(filter);

            r = htable.get(get);
        } catch (IOException e) {
            logger.error("getByColumnRangeFilter error...{}", e.getMessage());
        } finally {
            hTableClose(htable);
        }
        return r;
    }

    /**
     * 关闭表连接
     *
     * @param htable
     */
    private static void hTableClose(HTableInterface htable) {
        if (htable != null) {
            try {
                htable.close();
            } catch (IOException e) {
                logger.error("HTableInterface close error...\n{}", e.getMessage());
            }
        }
    }
}