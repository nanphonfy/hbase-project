package cn.nanphonfy.traffic.api.util

/**
  * Created by nanphonfy on 2017/8/13.
  */
object HBaseReaderTest {
  def main(args: Array[String]) {
    scanByColumnRangeFilterTest()
    //    test0()
        test1()
  }

  /**
    * 测试新rowkey和高表转宽表的方法
    */
  def scanByColumnRangeFilterTest(): Unit = {
    val hBaseReader = new HBaseReader("GdRoadNew")
    val rs = hBaseReader.scanByColumnRangeFilter("0000000429:17-08-01 07:00:00", "0000000429:17-08-02 15:59:00", List("value"))
    println(rs)
  }

  def test0(): Unit = {
    val r = List("value").map(col => "")
    println(r)
  }

  /**
    * 旧接口
    */
  def test1(): Unit = {
    val hBaseReader = new HBaseReader("GdRoadOld")
    println(hBaseReader.Query("0000020212:16-09-12 00:00:00", "0000020212:16-09-12 17:40:00", List("value")))
  }
}
