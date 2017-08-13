package cn.nanphonfy.traffic.api.model

import java.io.Serializable

import _root_.argonaut._
import argonaut.Argonaut._

case class RoadInfo(road: String, time: String, isEmpty: Boolean,
                    total_volume: Float, total_speed: Float,
                    bus_volume: Float, bus_speed: Float,
                    taxi_volume: Float, taxi_speed: Float,
                    truck_volume: Float, truck_speed: Float,
                    coach_volume: Float, coach_speed: Float,
                    car_volume: Float, car_speed: Float,
                    other_volume: Float, other_speed: Float,
                    val vehicles: List[String] = Nil
                   ) {

  def getVehicle(): List[String] = {
    vehicles
  }

  def toJson = {
    if (isEmpty) {
      Json("road" -> jString(road),
        "time" -> jString(time),
        "isEmpty" -> jBool(true))
    } else {
      Json("road" -> jString(road),
        "time" -> jString(time),
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
        "car_speed" -> jNumber(car_speed).getOrElse(jNumber(0.0).get),
        "other_volume" -> jNumber(other_volume).getOrElse(jNumber(0.0).get),
        "other_speed" -> jNumber(other_speed).getOrElse(jNumber(0.0).get))
    }
  }
}

object RoadInfo {
  def EmptyJson(road: String, time: String) = Json("road" -> jString(road), "time" -> jString(time), "isEmpty" -> jBool(true))

  def apply(road: String, time: String, record: String, vehicles: String) = {
    val f = record.split(",").toList.map(_.toFloat)
    new RoadInfo(road, time, false, f(0), f(1), f(2), f(3), f(4), f(5), f(6), f(7), f(8), f(9), f(10), f(11), f(12), f(13), vehicles.split(",").toList)
  }

  def Empty(road: String, time: String) = {
    new RoadInfo(road, time, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
  }
}