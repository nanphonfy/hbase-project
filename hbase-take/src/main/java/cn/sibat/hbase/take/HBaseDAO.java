package cn.sibat.hbase.take;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class HBaseDAO {
    private final static Logger logger = LoggerFactory.getLogger(HBaseDAO.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static HBaseConfiguration hbaseConfig = null;
    private static Configuration config = null;

    static {
        config = HBaseConfiguration.create();
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
//        Connection connection = null;
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
//        Connection connection = null;
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
     * 一个月模拟多个月的测试方法
     *
     * @param tableName
     * @param values
     * @param date
     * @return
     */
    public static boolean insertWhile(String tableName, List<String> values, String date) {
//        Connection connection = null;
        HTable table = null;
        try {
            tableExists(tableName);
            table = new HTable(config, Bytes.toBytes(tableName));
//            connection = ConnectionFactory.createConnection(config);
//            table = (HTable) connection.getTable(TableName.valueOf(tableName));
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
        HTable table;
        ResultScanner rs = null;
        try {
            table = new HTable(config, Bytes.toBytes(tableName));
            Scan scan = new Scan();

            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            long start = startDate.getTime();
            long end = endDate.getTime();

            scan.setTimeRange(start, end);
            rs = table.getScanner(scan);
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
        HTable table;
        ResultScanner rs = null;
        try {
            table = new HTable(config, Bytes.toBytes(tableName));
            Scan scan = new Scan();
            scan.setStartRow(startRow.getBytes());
            scan.setStopRow(endRow.getBytes());
            rs = table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        HTable table;
        ResultScanner rs = null;
        try {
            table = new HTable(config, Bytes.toBytes(tableName));
            Scan scan = new Scan();
            Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                    new SubstringComparator(rowPrifix));
            // 设置过滤器
            scan.setFilter(filter);
            rs = table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
    public static ResultScanner scanByColumnRangeFilter(String tableName, String minColumn, String maxColumn) throws Exception {
        HTable table;
        ResultScanner rs = null;
        try {
            table = new HTable(config, Bytes.toBytes(tableName));
            Scan scan = new Scan();
            boolean minColumnlnclusive = true;
            boolean maxColumnlnclusive = true;
            ColumnRangeFilter filter = new ColumnRangeFilter(Bytes.toBytes("001"), minColumnlnclusive, Bytes.toBytes("100"), maxColumnlnclusive);
            // 设置过滤器
            scan.setFilter(filter);
            rs = table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return rs;
    }
}