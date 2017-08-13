package cn.nanphonfy.traffic.api.controller.query

import java.text.SimpleDateFormat
import java.util.Date

import cn.nanphonfy.traffic.api.model.RoadInfo
import cn.nanphonfy.traffic.api.util.HBaseReader

class QueryManager(tableName: String) {
  val hbase_today = new HBaseReader(tableName)

  private val types = List("total", "bus", "taxi", "truck", "coach", "car", "other")
  private val columns = types.flatMap(vtype => List(vtype + "_volume", vtype + "_speed"))
  private val interval = 5 //分钟

  /**
    * 对时间进行每5分钟取整操作
    *
    * @param time 时间
    * @return 取整后的时间
    */
  def hash(time: String) = {
    val m = time.substring(12, 14).toInt / interval * interval
    if (m >= 10) time.substring(0, 12) + m + ":00" else time.substring(0, 12) + "0" + m + ":00"
  }

  /**
    * 获取从开始时间到结束时间中间经过的时间列表
    *
    * @param start_time 起始时间
    * @param end_time   结束时间
    * @return 时间列表
    */
  private def GetTimeList(start_time: String, end_time: String): List[String] = {
    val start = hash(start_time)
    val end = hash(end_time)

    if (start == end) {
      List(start)
    } else {
      val sf = new SimpleDateFormat("yy-MM-dd HH:mm:ss")
      val startTime = sf.parse(start).getTime
      val endTime = sf.parse(end).getTime
      val step = interval * 60 * 1000
      val time = new Date()
      startTime.to(endTime, step).map(timestamp => {
        time.setTime(timestamp);
        sf.format(time)
      }).toList
    }
  }

  /**
    * 获取道路数据
    *
    * @param roads 道路列表
    * @param start 起始时间
    * @param end   结束时间
    * @return 道路列表在对应时间内的所有数据
    */
  def GetRoadsData(roads: Iterable[String], start: String, end: String): Iterable[(String, Iterable[RoadInfo])] = {
    //填充道路ID到10位数
    val filled_roads = roads.map(road => ("0" * (10 - road.length)) + road)

    //从HBASE获取数据
    val data: Iterable[Iterable[RoadInfo]] = filled_roads.map(road => {
      val sf = new SimpleDateFormat("yy-MM-dd")

//      var results = hbase_today.QueryTime(road + ":" + start, road + ":" + end, Seq("value", "vehicles")) //List(List(...),...)
            var results = hbase_today.scanByColumnRangeFilter(road + ":" + start, road + ":" + end, Seq("value", "vehicles")) //List(MutableList(...))
      println("QueryManager GetRoadsData:" + results)

      val timeList = results.filter(!_.head.equals("")).map(result => {
        result.head.split("!")(1)
      }).toList
      results = results.map(result => {
        Seq(result.head.split("!")(0), result.last.split("!")(0))
      })
      (timeList zip results).map { case (time, result) => {
        if (result.head.isEmpty) {
          RoadInfo.Empty(road, time)
        } else {
          RoadInfo(road, time, result.head, result.last)
        }
      }
      }
    })

    filled_roads zip data
  }

  /**
    * 获取道路数据,第二版
    *
    * @param roads 道路列表
    * @param start 起始时间
    * @param end   结束时间
    * @return 道路列表在对应时间内的所有数据
    */
  def GetRoadsDataSecond(roads: Iterable[String], start: String, end: String): Iterable[(String, Iterable[RoadInfo])] = {
    //填充道路ID到10位数
    val filled_roads = roads.map(road => ("0" * (10 - road.length)) + road)

    //从HBASE获取数据
    val data: Iterable[Iterable[RoadInfo]] = filled_roads.map(road => {
      val sf = new SimpleDateFormat("yy-MM-dd")

      var results = hbase_today.scanByColumnRangeFilter(road + ":" + start, road + ":" + end, Seq("value")) //List(MutableList(...))

      println("QueryManager GetRoadsDataSecond:" + results.filter(!_.equals("")))

      val timeList = results.filter(!_.equals("")).map(result => {
        if (!"".equals(result))
          result.map(r => r.toString.split("!")(1))
      }).toList
      results = results.map(result => {
        Seq(result.head.toString.split("!")(0), result.last.toString.split("!")(0))
      })
      (timeList zip results).map { case (time, result) => {
        if (result.head.toString.isEmpty) {
          RoadInfo.Empty(road, time.toString)
        } else {
          RoadInfo(road, time.toString, result.head.toString, result.last.toString)
        }
      }
      }
    })

    filled_roads zip data
  }

  def GetRoadAverage(road: String, start: String, end: String) = {
    val data = GetRoadsData(List(road), start, end)

    val (_, recs) = data.head
    val valid_speeds = recs.map(_.total_speed).filter(n => !n.isInfinity && !n.isNaN)
    val valid_volume = recs.map(_.total_volume).filter(n => !n.isInfinity && !n.isNaN)

    val speed = valid_speeds.sum / valid_speeds.size
    val volume = valid_volume.sum / valid_volume.size

    speed + "," + volume
  }
}
