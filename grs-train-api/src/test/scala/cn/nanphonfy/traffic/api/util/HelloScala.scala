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
  val tableNameOld = "GdRoadOld";
  val tableNameNew = "GdRoadNew";

  def main(args: Array[String]) {
    var begin = System.currentTimeMillis()
    newBusRoadsSortInfoQueryTest()
    var end = System.currentTimeMillis()
    println("【新流量、速度top10排序】耗时：", (end - begin), "ms")

    begin = System.currentTimeMillis()
    oldBusRoadsSortInfoQueryTest()
    end = System.currentTimeMillis()
    println("【旧流量、速度top10排序】耗时：", (end - begin), "ms")

    begin = System.currentTimeMillis()
    newBusRoadsStatInfoQueryTest()
    end = System.currentTimeMillis()
    println("【新道路流量查询】耗时：", (end - begin), "ms")

    begin = System.currentTimeMillis()
    oldBusRoadsStatInfoQueryTest()
    end = System.currentTimeMillis()
    println("【旧道路流量查询】耗时：", (end - begin), "ms")
  }

  /**
    * 流量、速度top10排序
    */
  def newBusRoadsSortInfoQueryTest(): Unit = {
    val brsiq = new BusRoadsSortInfoQuery(tableNameNew)
    println(brsiq.QuerySecond(List("1022143", "1022144"), "17-08-02 05:00:00", "17-08-02 23:00:00"))
    //    println(brsiq.Query(List("1022143", "1022144"), "16-09-11 05:00:00", "16-09-11 23:00:00"))
  }

  /**
    * 道路流量查询
    */
  def newBusRoadsStatInfoQueryTest(): Unit = {
    val brsiq = new RoadStatInfoQuery(tableNameNew)
    //    println(brsiq.Query(List("1018556","1018559"),"16-12-10 12:00:00","16-12-10 14:30:00"))
    println(brsiq.QuerySecond(List("6902", "1041194", "", "1013963"), "17-08-02 07:00:00", "17-08-02 15:59:00"))
  }

  /**
    * 流量、速度top10排序
    */
  def oldBusRoadsSortInfoQueryTest(): Unit = {
    val brsiq = new BusRoadsSortInfoQuery(tableNameOld)
    println(brsiq.Query(List("1022143", "1022144"), "16-09-11 05:00:00", "16-09-11 23:00:00"))
  }

  /**
    * 道路流量查询
    */
  def oldBusRoadsStatInfoQueryTest(): Unit = {
    val brsiq = new RoadStatInfoQuery(tableNameOld)
    println(brsiq.Query(List("1018556", "1018559"), "16-12-10 12:00:00", "16-12-10 14:30:00"))
  }

  def test0(): Unit = {
    println(columns)
    println(columns.map(col => ""))
    val result = "1,52.5,0,0.0,0,0.0,1,52.5,0,0.0,0,0.0,0,0.0"
    //columns.map(col => { val v = result ; if (v == null) "" else {new String(v)+"!"+result.split(" ")(0).split(":")(1)+" "+result.split(" ")(1)}})
    val res = hbase_today.scanByColumnRangeFilter("0001022143:17-08-01 05:00:00", "0001022143:17-08-02 23:00:00", Seq("value"))
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
