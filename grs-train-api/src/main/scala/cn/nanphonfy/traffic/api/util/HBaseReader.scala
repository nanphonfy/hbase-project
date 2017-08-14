package cn.nanphonfy.traffic.api.util

import java.io.Serializable
import java.util

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.filter.ColumnRangeFilter
import org.apache.hadoop.hbase.util.Bytes
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.util.Try

class HBaseReader(val tableName: String) {

  private val logger = LoggerFactory.getLogger("")

  // Hbase连接参数
  val conf = HBaseConfiguration.create()

  private var connection: HConnection = null

  private def getConnection() = {
    if (connection == null || connection.isClosed) {
      connection = HConnectionManager.createConnection(conf)
    }
    connection
  }


  // 查询接口
  def Query(rows: Iterable[String]): Iterable[String] = Query(rows, Seq("value")).map(_.head)

  /**
    * 查询接口
    *
    * @param rows    行键
    * @param columns 列键
    * @return 对应值的列表
    */

  def Query(rows: Iterable[String], columns: Seq[String]): Iterable[Seq[String]] = {
    val table = getConnection().getTable(tableName)

    val gets = new util.ArrayList[Get]()
    rows.foreach(rowKey => {
      val get = new Get(rowKey.getBytes)
      columns.foreach(column => get.addFamily(column.getBytes))
      gets.add(get)
    })

    table.get(gets).map(result => {
      if (result.isEmpty) columns.map(col => "")
      else columns.map(col => {
        val v = result.getValue(col.getBytes, null);
        if (v == null) "" else new String(v)
      })
    })
  }

  def Query(startRow: String, stopRow: String, columns: Seq[String]): Iterable[Seq[String]] = {
    val table = getConnection().getTable(tableName)
    val scan = new Scan()
    scan.setStartRow(startRow.getBytes())
    scan.setStopRow(stopRow.getBytes())

    val rss: ResultScanner = table.getScanner(scan)
    rss.map(result => if (result.isEmpty) columns.map(col => "")
    else columns.map(col => {
      val v = result.getValue(col.getBytes, null);
      if (v == null) "" else new String(v)
    }))
  }

  def QueryTime(startRow: String, stopRow: String, columns: Seq[String]): Iterable[Seq[String]] = {
    val table = getConnection().getTable(tableName)
    val scan = new Scan()
    scan.setStartRow(startRow.getBytes())
    scan.setStopRow(stopRow.getBytes())

    val rss: ResultScanner = table.getScanner(scan)
    rss.map(result => if (result.isEmpty) columns.map(col => "")
    else columns.map(col => {
      val v = result.getValue(col.getBytes, null);
      if (v == null) ""
      else {
        println("!", new String(v) + "!" + (Bytes.toString(result.list()(0).getRow).split(" ")(0).split(":")(1) + " " + Bytes.toString(result.list()(0).getRow).split(" ")(1)))
        //      (!,1,22.280303955078125,0,0.0,1,22.280304,0,0.0,0,0.0,0,0.0,0,0.0!16-09-11 22:05:00)
        //      (!,2,12.259829711914062,1,12.433049,1,12.0,0,0.0,0,0.0,0,0.0,0,0.0!16-09-11 22:10:00)
        new String(v) + "!" + (Bytes.toString(result.list()(0).getRow).split(" ")(0).split(":")(1) + " " + Bytes.toString(result.list()(0).getRow).split(" ")(1))
      }
    }))
  }

  def QueryTimeHis(startRow: String, stopRow: String, columns: Seq[String]): Iterable[Seq[String]] = {
    val table = getConnection().getTable(tableName)
    val scan = new Scan()
    scan.setStartRow(startRow.getBytes())
    scan.setStopRow(stopRow.getBytes())

    val rss: ResultScanner = table.getScanner(scan)
    rss.map(result => if (result.isEmpty) columns.map(col => "")
    else columns.map(col => {
      val v = result.getValue(col.getBytes, null);
      if (v == null) ""
      else {
        new String(v) + "!" + (Bytes.toString(result.list()(0).getRow).split(" ")(0).split(":")(1).substring(2) + " " + Bytes.toString(result.list()(0).getRow).split(" ")(1))
      }
    }))
  }

  /**
    * 新设计的rowkey和高表改宽表的查询方法
    */
  def scanByColumnRangeFilter(startRowOld: String, stopRowOld: String, columns: Seq[String]): mutable.MutableList[Iterable[String]] = {
    //0000987741:16-11-26 20:10:00
    //long最大值减去该值，变为stopRow，故需要将日期减1
    val time1 = StringUtil.subOndDay(startRowOld.substring(11))
    val time2 = stopRowOld.substring(11)
    val roadId: String = startRowOld.substring(0, 11)

    val start: Long = StringUtil.getTimestamp(time1, StringUtil.PATTERN_yy_MM_dd)
    val end: Long = StringUtil.getTimestamp(time2, StringUtil.PATTERN_yy_MM_dd)

    //时间片的范围
    val h_m_s1: String = startRowOld.substring(20)
    val timeslice1: Int = StringUtil.getTimesLice(h_m_s1)
    val minColumn: String = StringUtil.getQualifier(timeslice1)

    val h_m_s2: String = stopRowOld.substring(20)
    val timeslice2: Int = StringUtil.getTimesLice(h_m_s2)
    val maxColumn: String = StringUtil.getQualifier(timeslice2)

    //key的范围
    val startRow: String = roadId + StringUtil.getLongMaxSubTimestamp(end)
    val stopRow: String = roadId + StringUtil.getLongMaxSubTimestamp(start)

    val table = getConnection().getTable(tableName)
    val scan = new Scan()
    scan.setStartRow(startRow.getBytes)
    scan.setStopRow(stopRow.getBytes)

    val minColumnlnclusive: Boolean = true
    val maxColumnlnclusive: Boolean = true
    val filter: ColumnRangeFilter = new ColumnRangeFilter(Bytes.toBytes(minColumn), minColumnlnclusive, Bytes.toBytes(maxColumn), maxColumnlnclusive)
    // 设置过滤器
    scan.setFilter(filter)

    val rss: ResultScanner = table.getScanner(scan)
    val list = new mutable.MutableList[String] //存储查询出来的所有结果集

    rss.map(result => if (result.isEmpty) columns.map(col => "")
    else columns.map(col => {
      for (keyValue <- result.raw) {
        list += Bytes.toString(keyValue.getValue) + "!" + Bytes.toString(keyValue.getQualifier) + "|"
      }
      list
    }
    ))
    //println(list)
    val temp = list.map(r => r.split("\\|,").toIterable)
    //println(temp)
    temp
  }

  def tempBak(): Unit ={
    //    val temp = rss.map(result =>
    //      if (result.isEmpty) columns.map(col => "")
    //      else columns.map(col => {
    //        if ("value".eq(col)) {
    //          result.raw().map(keyValue => (Bytes.toString(keyValue.getValue) + "!" + Bytes.toString(keyValue.getQualifier) + "|"));
    //        }
    //        else ""
    //      }))

    //    val columnss = Seq("001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "018", "019", "020", "021", "022", "023", "024", "025", "026", "027", "028", "029", "030", "031", "032", "033", "034", "035", "036", "037", "038", "039", "040", "041", "042", "043", "044", "045", "046", "047", "048", "049", "050", "051", "052", "053", "054", "055", "056", "057", "058", "059", "060", "061", "062", "063", "064", "065", "066", "067", "068", "069", "070", "071", "072", "073", "074", "075", "076", "077", "078", "079", "080", "081", "082", "083", "084", "085", "086", "087", "088", "089", "090", "091", "092", "093", "094", "095", "096", "097", "098", "099", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161", "162", "163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173", "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190", "191", "192", "193", "194", "195", "196", "197", "198", "199", "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213", "214", "215", "216", "217", "218", "219", "220", "221", "222", "223", "224", "225", "226", "227", "228", "229", "230", "231", "232", "233", "234", "235", "236", "237", "238", "239", "240", "241", "242", "243", "244", "245", "246", "247", "248", "249", "250", "251", "252", "253", "254", "255", "256", "257", "258", "259", "260", "261", "262", "263", "264", "265", "266", "267", "268", "269", "270", "271", "272", "273", "274", "275", "276", "277", "278", "279", "280", "281", "282", "283", "284", "285", "286", "287")
    //    rss.map(result => if (result.isEmpty) columns.map(col => "")
    //    else columnss.map(col => {
    //      val v = result.getValue("value".getBytes, col.getBytes);
    //      if (v == null) ""
    //      else {
    //        new String(v) + "!" + col
    //      }
    //    }))
  }
}

object HBaseReader {
  /**
    * 检查Hbase的连接是否成功
    *
    * @return 是否成功
    */
  def Check(): Boolean = {
    Try {
      val conf = HBaseConfiguration.create()
      HBaseAdmin.checkHBaseAvailable(conf)
    }.isSuccess
  }

  def main(args: Array[String]) {
    println(Check())
  }
}