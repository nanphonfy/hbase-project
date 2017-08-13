package cn.nanphonfy.traffic.api.controller.query

import argonaut.Argonaut._
import argonaut.Json
import cn.nanphonfy.traffic.api.model.RoadStat

/**
  * 对道路公交流量的查询，返回连续路段上公交的总流量与平均速度
  * 其中对公交流量按30分钟为间隔进行去重处理
  */
class BusRoadsStatInfoQuery(tableName: String) extends Queryable {
  override def Query(roads: Iterable[String], start: String, end: String): String = {
    val qm = new QueryManager(tableName)
    val data = qm.GetRoadsData(roads, start, end)

    //计算公交车的平均速度
    val average_speed = data.map { case (road, records) => new RoadStat(road, records) }.map(_.bus_speed).sum / data.size

    val isBus = (vehicle: String) => vehicle.startsWith("B") && vehicle.length == 6

    //计算总的公交车流量
    val duration = data.head._2.size * 5
    val road_time_vehicles = data.map(_._2.map(_.vehicles).toList).toList
    val time_road_vehicles = road_time_vehicles.transpose
    val time_vehicles = time_road_vehicles.map(roads => roads.reduce(_ ++ _).distinct.filter(isBus))

    val total_volume = if (duration <= 30) {
      //如果时间少于30分钟，直接计算不同车牌号的总和
      time_vehicles.flatten.distinct.size
    } else {
      //如果时间大于30分钟，需要以30分钟为时间窗口计算
      val group = time_vehicles.sliding(6).toList
      //前30分钟的车辆数
      val initNums = time_vehicles.take(6).flatten.distinct.size
      //后面时间需要利用时间窗口去掉重复的车辆
      val totalNums = group.tail.foldLeft(initNums)((sum, list) => sum + list.flatten.distinct.size - list.take(5).flatten.distinct.size)

      totalNums
    }

    Json(
      "speed" -> jNumber(average_speed).getOrElse(jNumber(0.0).get),
      "volume" -> jNumber(total_volume).getOrElse(jNumber(0.0).get)
    ).toString
  }
}
