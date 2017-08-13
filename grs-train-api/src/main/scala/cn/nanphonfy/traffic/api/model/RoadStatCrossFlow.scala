package cn.nanphonfy.traffic.api.model

import _root_.argonaut._
import argonaut.Argonaut._

class RoadStatCrossFlow(road: String, records: Iterable[RoadInfo]) {
  private val list = records.filter(!_.isEmpty)
  val isEmpty = list.isEmpty

  //得到公交车速度为0的集合
  val zeroList=list.filter((_.bus_speed==0))
  //参与平均速度计算，应该摒弃掉速度为0的
  val busSize = list.size.toFloat-zeroList.size.toFloat//当某道路id的公交车速度都为0时，size为0，会出现NaN的值，导致json解析错误
  val size = list.size.toFloat

  val total_volume: Double = if(isEmpty) -1 else list.map(_.total_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val total_speed: Double = if(isEmpty) -1 else list.map(_.total_speed).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val bus_volume: Double = if(isEmpty) -1 else list.map(_.bus_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val bus_speed: Double = if (isEmpty) -1 else list.map(_.bus_speed).filter(n => !n.isInfinity && !n.isNaN).sum / (if (busSize == 0) 1 else busSize)
  val taxi_volume: Double = if(isEmpty) -1 else list.map(_.taxi_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val taxi_speed: Double = if(isEmpty) -1 else list.map(_.taxi_speed).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val truck_volume: Double = if(isEmpty) -1 else list.map(_.truck_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val truck_speed: Double = if(isEmpty) -1 else list.map(_.truck_speed).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val coach_volume: Double = if(isEmpty) -1 else list.map(_.coach_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val coach_speed: Double = if(isEmpty) -1 else list.map(_.coach_speed).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val car_volume: Double = if(isEmpty) -1 else list.map(_.car_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val car_speed: Double = if(isEmpty) -1 else list.map(_.car_speed).filter(n => !n.isInfinity && !n.isNaN).sum / size
  val other_volume: Double = if(isEmpty) -1 else list.map(_.other_volume).filter(n => !n.isInfinity && !n.isNaN).sum
  val other_speed: Double = if(isEmpty) -1 else list.map(_.other_speed).filter(n => !n.isInfinity && !n.isNaN).sum / size

  if(list.size == zeroList.size){// test by zsr
    println("list",list.size,"zeroList",zeroList.size)
    println("bus_volume",bus_volume,"bus_speed",bus_speed)
  }

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
