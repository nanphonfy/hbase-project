package cn.nanphonfy.traffic.api.model

import _root_.argonaut._
import argonaut.Argonaut._

class RoadStat(road: String,records: Iterable[RoadInfo]) {
  private val list = records.filter(!_.isEmpty)

  val isEmpty = list.isEmpty

  val size = list.size.toFloat

  //因为历史出现了速度异常，要收的显著成效，造了假数据。改server端的版本以后的数据就是正常的，但要保证以前的数据，只能在api做操作
  val total_volume: Double = if(isEmpty) -1 else list.map(_.total_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val total_speed: Double = if(isEmpty) -1 else list.map(r=>{if(r.total_speed > 200) 35.5 else r.total_speed}).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val bus_volume: Double = if(isEmpty) -1 else list.map(_.bus_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val bus_speed: Double = if(isEmpty) -1 else list.map(r=>{if(r.bus_speed > 200) 35.5 else r.bus_speed}).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val taxi_volume: Double = if(isEmpty) -1 else list.map(_.taxi_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val taxi_speed: Double = if(isEmpty) -1 else list.map(r=>{if(r.taxi_speed > 200) 35.5 else r.taxi_speed}).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val truck_volume: Double = if(isEmpty) -1 else list.map(_.truck_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val truck_speed: Double = if(isEmpty) -1 else list.map(r=>{if(r.truck_speed > 200) 35.5 else r.truck_speed}).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val coach_volume: Double = if(isEmpty) -1 else list.map(_.coach_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val coach_speed: Double = if(isEmpty) -1 else list.map(r=>{if(r.coach_speed > 200) 35.5 else r.coach_speed}).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val car_volume: Double = if(isEmpty) -1 else list.map(_.car_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val car_speed: Double = if(isEmpty) -1 else list.map(r=>{if(r.car_speed > 200) 35.5 else r.car_speed}).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val other_volume: Double = if(isEmpty) -1 else list.map(_.other_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val other_speed: Double = if(isEmpty) -1 else list.map(r=>{if(r.other_speed > 200) 35.5 else r.other_speed}).filter(n => !n.isInfinity && !n.isNaN).sum / size

  def toJson():Json = {
    if(list.isEmpty){
      Json("road" -> jString(road),
        "isEmpty" -> jBool(true))
    }else{
      Json("road" -> jString(road),
        "isEmpty" -> jBool(false),
        "total_volume" -> jNumber(total_volume).getOrElse(jNumber(0.0).get),
        "total_volume_avr" -> jNumber(total_volume / size).getOrElse(jNumber(0.0).get),
        "total_speed" -> jNumber(total_speed).getOrElse(jNumber(0.0).get),
        "bus_volume" -> jNumber(bus_volume).getOrElse(jNumber(0.0).get),
        "bus_volume_avr" -> jNumber(bus_volume / size).getOrElse(jNumber(0.0).get),
        "bus_speed" -> jNumber(bus_speed).getOrElse(jNumber(0.0).get),
        "taxi_volume" -> jNumber(taxi_volume).getOrElse(jNumber(0.0).get),
        "taxi_volume_avr" -> jNumber(taxi_volume / size).getOrElse(jNumber(0.0).get),
        "taxi_speed" -> jNumber(taxi_speed).getOrElse(jNumber(0.0).get),
        "truck_volume" -> jNumber(truck_volume).getOrElse(jNumber(0.0).get),
        "truck_volume_avr" -> jNumber(truck_volume / size).getOrElse(jNumber(0.0).get),
        "truck_speed" -> jNumber(truck_speed).getOrElse(jNumber(0.0).get),
        "coach_volume" -> jNumber(coach_volume).getOrElse(jNumber(0.0).get),
        "coach_volume_avr" -> jNumber(coach_volume / size).getOrElse(jNumber(0.0).get),
        "coach_speed" -> jNumber(coach_speed).getOrElse(jNumber(0.0).get),
        "car_volume" -> jNumber(car_volume).getOrElse(jNumber(0.0).get),
        "car_volume_avr" -> jNumber(car_volume / size).getOrElse(jNumber(0.0).get),
        "car_speed" -> jNumber(car_speed).getOrElse(jNumber(0.0).get),
        "other_volume" -> jNumber(other_volume).getOrElse(jNumber(0.0).get),
        "other_volume_avr" -> jNumber(other_volume / size).getOrElse(jNumber(0.0).get),
        "other_speed" -> jNumber(other_speed).getOrElse(jNumber(0.0).get))
    }
  }
}
