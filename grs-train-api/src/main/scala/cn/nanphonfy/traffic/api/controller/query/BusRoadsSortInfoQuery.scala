package cn.nanphonfy.traffic.api.controller.query

import java.text.SimpleDateFormat
import java.util.Date

import argonaut.Argonaut._
import argonaut.Json
import cn.nanphonfy.traffic.api.model.RoadStatCrossFlow
import cn.nanphonfy.traffic.api.util.BusRoads

/**
  * 对固定道路截面公交流量的查询
  */
class BusRoadsSortInfoQuery(tableName: String) extends Queryable {
  override def Query(roads: Iterable[String], start: String, end: String): String = {
    val roads = BusRoads.roads.flatMap(road => road.RoadsWithDirection.map { case (direction, roadsList) => {
      val name = road.name
      val qm = new QueryManager(tableName)
      val data = qm.GetRoadsData(roadsList, start, end)
      //      val data = qm.GetRoadsDataSecond(roadsList, start, end)//新版
      //      println(data)
      val stat = new RoadStatCrossFlow(name, data.flatMap(_._2))
      //      println(stat.toJson())/by zsr
      if (stat.isEmpty) {
        (name, direction, 0.0, 0.0)
      } else {
        (name, direction, stat.bus_volume, stat.bus_speed)
      }
    }
    })

    val dealroads = roads.filter(_._4 < 100)

    //    println(dealroads.size,roads.size)//test by zsr

    val sort_speed_roads = dealroads.sortBy(_._4)
    val sort_volume_roads = dealroads.sortBy(_._3)
    //    val sort_speed_roads = roads.sortBy(_._4)
    //    val sort_volume_roads = roads.sortBy(_._3)

    val speed_top_10 = sort_speed_roads.reverse.take(10)
    val speed_last_10 = sort_speed_roads.take(10)

    val volume_top_10 = sort_volume_roads.reverse.take(10)
    val volume_last_10 = sort_volume_roads.take(10)

    val toJson = (x: (String, String, Double, Double)) => Json("name" -> jString(x._1), "direction" -> jString(x._2), "volume" -> jNumber(x._3).get, "speed" -> jNumber(if (x._4.isNaN) 0 else x._4).get)

    Json(
      "speed_top_10" -> jArray(speed_top_10.map(toJson)),
      "speed_last_10" -> jArray(speed_last_10.map(toJson)),
      "volume_top_10" -> jArray(volume_top_10.map(toJson)),
      "volume_last_10" -> jArray(volume_last_10.map(toJson))
    ).toString()
  }

  override def QuerySecond(roads: Iterable[String], start: String, end: String): String = {
    val roads = BusRoads.roads.flatMap(road => road.RoadsWithDirection.map { case (direction, roadsList) => {
      val name = road.name
      val qm = new QueryManager(tableName)
      //      val data = qm.GetRoadsData(roadsList, start, end)
      val data = qm.GetRoadsDataSecond(roadsList, start, end) //新版
      //      println(data)
      val stat = new RoadStatCrossFlow(name, data.flatMap(_._2))
      //      println(stat.toJson())/by zsr
      if (stat.isEmpty) {
        (name, direction, 0.0, 0.0)
      } else {
        (name, direction, stat.bus_volume, stat.bus_speed)
      }
    }
    })

    val dealroads = roads.filter(_._4 < 100)

    //    println(dealroads.size,roads.size)//test by zsr

    val sort_speed_roads = dealroads.sortBy(_._4)
    val sort_volume_roads = dealroads.sortBy(_._3)
    //    val sort_speed_roads = roads.sortBy(_._4)
    //    val sort_volume_roads = roads.sortBy(_._3)

    val speed_top_10 = sort_speed_roads.reverse.take(10)
    val speed_last_10 = sort_speed_roads.take(10)

    val volume_top_10 = sort_volume_roads.reverse.take(10)
    val volume_last_10 = sort_volume_roads.take(10)

    val toJson = (x: (String, String, Double, Double)) => Json("name" -> jString(x._1), "direction" -> jString(x._2), "volume" -> jNumber(x._3).get, "speed" -> jNumber(if (x._4.isNaN) 0 else x._4).get)

    Json(
      "speed_top_10" -> jArray(speed_top_10.map(toJson)),
      "speed_last_10" -> jArray(speed_last_10.map(toJson)),
      "volume_top_10" -> jArray(volume_top_10.map(toJson)),
      "volume_last_10" -> jArray(volume_last_10.map(toJson))
    ).toString()
  }
}
