package org.siabt.traffic.api

import cn.nanphonfy.traffic.api.controller.query.{BusRoadsSortInfoQuery, RoadStatInfoQuery}
import cn.nanphonfy.traffic.api.util.HBaseReader

/**
  * Created by nanphonfy on 2017/4/25.
  */
object HelloScala {
  private val types = List("total", "bus", "taxi", "truck", "coach", "car", "other")
  private val columns = types.flatMap(vtype => List(vtype + "_volume", vtype + "_speed"))
  val hbase_today = new HBaseReader("GdRoadNew")
  //  val tableName = "GdRoadOld";
  val tableName = "GdRoadNew";

  def main(args: Array[String]) {
    test0()
        BusRoadsSortInfoQueryTest()
  }

  def BusRoadsSortInfoQueryTest(): Unit = {
    val brsiq = new BusRoadsSortInfoQuery(tableName)
    var begin = System.currentTimeMillis()
    println(brsiq.Query(List("1022143", "1022144"), "17-08-02 05:00:00", "17-08-02 23:00:00"))
//        println(brsiq.Query(List("1022143", "1022144"), "16-09-11 05:00:00", "16-09-11 23:00:00"))
    var end = System.currentTimeMillis()
    println("耗时：", (end - begin), "ms")
  }

  def BusRoadsStatInfoQueryTest(): Unit = {
    val brsiq = new RoadStatInfoQuery(tableName)
    var begin = System.currentTimeMillis()
    //    println(brsiq.Query(List("1018556","1018559"),"16-12-10 12:00:00","16-12-10 14:30:00"))
    println(brsiq.Query(List("6902", "1041194", "", "1013963"), "17-07-12 07:00:00", "17-07-13 15:59:00"))
    var end = System.currentTimeMillis()
    println("耗时：", (end - begin) / 1000)
  }

  def test0(): Unit = {
    println(columns)
    println(columns.map(col => ""))
    val result = "1,52.5,0,0.0,0,0.0,1,52.5,0,0.0,0,0.0,0,0.0"
    //columns.map(col => { val v = result ; if (v == null) "" else {new String(v)+"!"+result.split(" ")(0).split(":")(1)+" "+result.split(" ")(1)}})
    val res = hbase_today.scanByColumnRangeFilter("0001022143:17-08-01 05:00:00", "0001022143:17-08-02 23:00:00",Seq("value"))
    println(res)
  }

  def test1(): Unit = {
//    print(List.range("001","288"))
    for (i <- List.range(1, 288)) {
      if (i < 10)
        print("\"00" + i + "\",")
      else if (i < 100)
        print("\"0" + i + "\",")
      else
        print("\"" + i + "\",")
    }
  }
}
