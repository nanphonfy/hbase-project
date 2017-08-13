package cn.nanphonfy.traffic.api.model

import java.util

import argonaut.Argonaut._
import argonaut.Json

import scala.collection.mutable

/**
  * Created by fredzx on 2017/1/4.
  */
class BusRoadStat(roadnum: Int, records: Iterable[RoadInfo]) {
  private val list = records.filter(!_.isEmpty)
  val cars = new util.ArrayList[String]()

  //统计流量初始化
  val vtype_speed_cnt = new mutable.HashMap[String, Int]
  val vtype_list = List("bus", "taxi", "truck", "coach", "car", "other")
  vtype_list.foreach(vtype => {
    vtype_speed_cnt += ((vtype, 0))
  })

  var count = 0
  val fitList = list.groupBy(_.time)

  fitList.toList.sortWith(comstr) foreach {
    case (key, value) => {
      count += 1
      if (count % 15 == 0) {
        cars.clear()
      }
      value.foreach(s => {
        val vehicle = s.vehicles
        vehicle.foreach(r => {
          if (r != null && r.length > 4) {
            val rs = r.split(":")
            if (rs.size > 1) {
              val vtype = rs(0)
              val plate = rs(1)
              if (vtype_list.contains(vtype)) {
                if (!cars.contains(plate)) {
                  cars.add(plate)
                  vtype_speed_cnt(vtype) += 1
                }
              }
            }
          }
        })
      })
    }
  }


  def comstr(e1: (String, Iterable[RoadInfo]), e2: (String, Iterable[RoadInfo])) = e1._1.compareTo(e2._1) > 0


  //
  //  fitList.keySet.foreach( ri => {
  //    count += 1
  //    if(count%15==0){
  //      cars.clear()
  //    }
  //
  //    fitList(ri).foreach(s => {
  //      val vehicle = s.vehicles
  //      vehicle.foreach(r =>{
  //        if(r!=null&&r.length>4){
  //          val rs = r.split(":")
  //          if(rs.size>1) {
  //            val vtype = rs(0)
  //            val plate = rs(1)
  //            if (vtype_list.contains(vtype)) {
  //              if (!cars.contains(plate)) {
  //                cars.add(plate)
  //                vtype_speed_cnt(vtype) += 1
  //              }
  //
  //            }
  //          }
  //
  //        }
  //      })
  //    })
  //  })


  val bus_volume = vtype_speed_cnt("bus")
  val taxi_volume = vtype_speed_cnt("taxi")
  val truck_volume = vtype_speed_cnt("truck")
  val coach_volume = vtype_speed_cnt("coach")
  val car_volume = vtype_speed_cnt("car")
  val total_volume = bus_volume + taxi_volume + truck_volume + coach_volume + car_volume

  val bus_volume_size = list.map(_.bus_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).size
  val bus_speed: Double = list.map(_.bus_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).sum / bus_volume_size

  val taxi_volume_size = list.map(_.taxi_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).size
  val taxi_speed: Double = list.map(_.taxi_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).sum / taxi_volume_size


  val truck_volume_size = list.map(_.truck_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).size
  val truck_speed: Double = list.map(_.truck_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).sum / truck_volume_size


  val coach_volume_size = list.map(_.coach_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).size
  val coach_speed: Double = list.map(_.coach_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).sum / coach_volume_size


  val car_volume_size = list.map(_.car_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).size
  val car_speed: Double = list.map(_.car_speed).filter(n => !n.isInfinity && !n.isNaN && n != 0 && n < 100 && n > 10).sum / car_volume_size


  var total_speed = bus_speed * bus_volume
  val taxi_total = taxi_volume * taxi_speed
  if (!taxi_total.isNaN) total_speed += taxi_total
  val truck_total = truck_volume * truck_speed
  if (!truck_total.isNaN) total_speed += truck_total
  val coach_total = coach_speed * coach_volume
  if (!coach_total.isNaN) total_speed += coach_total
  total_speed = total_speed / total_volume


  def toJson(): Json = {
    if (list.isEmpty) {
      Json(
        "isEmpty" -> jBool(true))
    } else {

      Json(
        "isEmpty" -> jBool(false),
        "total_volume" -> jNumber(total_volume).getOrElse(jNumber(0.0).get),
        "total_speed" -> jNumber(total_speed).getOrElse(jNumber(0.0).get),
        "bus_volume" -> jNumber(bus_volume).getOrElse(jNumber(0.0).get),
        "bus_speed" -> jNumber(bus_speed).getOrElse(jNumber(0.0).get),
        "taxi_volume" -> jNumber(taxi_volume).getOrElse(jNumber(0.0).get),
        "taxi_speed" -> jNumber(taxi_speed).getOrElse(jNumber(0.0).get),
        "truck_volume" -> jNumber(truck_volume).getOrElse(jNumber(0.0).get),
        "truck_speed" -> jNumber(truck_speed).getOrElse(jNumber(0.0).get),
        "coach_volume" -> jNumber(coach_volume).getOrElse(jNumber(0.0).get),
        "coach_speed" -> jNumber(coach_speed).getOrElse(jNumber(0.0).get),
        "car_volume" -> jNumber(car_volume).getOrElse(jNumber(0.0).get),
        "car_speed" -> jNumber(car_speed).getOrElse(jNumber(0.0).get)
      )
    }
  }

}
