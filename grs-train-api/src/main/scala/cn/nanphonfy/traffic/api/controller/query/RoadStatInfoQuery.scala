package cn.nanphonfy.traffic.api.controller.query

import argonaut.Argonaut._
import argonaut.Json
import cn.nanphonfy.traffic.api.model.RoadStat

class RoadStatInfoQuery(tableName:String) extends Queryable{
  /**
   * 获取道路列表在某个时间段的统计信息
   * @param roads 道路ID列表
   * @param start 起始时间
   * @param end 结束时间
   * @return JSON数据
   */
  override def Query(roads: Iterable[String], start: String, end: String): String = {
    val qm=new QueryManager(tableName)
    val data = qm.GetRoadsData(roads,start,end)

    //封装成JSON数据
    val results = data.map{case(road,records) => {
      //统计早晚高峰信息
      // 7:00 ~ 9:00
      val days = records.groupBy(_.time.substring(0,8)).toList.sortBy(_._1)

      val peak = days.map{case(day,recs) => {

        val morning =   recs.filter(r => r.time.startsWith(day + " 07") || r.time.startsWith(day + " 08"))
        val evening = recs.filter(r => r.time >= day + " 17:30" && r.time <= day + " 19:30")
        val nonpeak = recs.filterNot(r => r.time >= day + " 17:30" && r.time <= day + " 19:30").filterNot(r => r.time.startsWith(day + " 07") || r.time.startsWith(day + " 08"))

        Json("day" -> jString(day),
          "morning" -> new RoadStat(road,morning).toJson(),
          "evening" -> new RoadStat(road,evening).toJson(),
          "nonpeak" -> new RoadStat(road,nonpeak).toJson()
        )
      }}.toList

      val average = new RoadStat(road , records).toJson()

      Json("road" -> jString(road),
        "stat" -> average,
        "peak" -> jArray(peak)
        //, "times" -> jArray(records.map(_.toJson))
      )
    }}

    jArray(results.toList).toString()
  }
}
