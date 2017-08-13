package cn.nanphonfy.traffic.api.util

import java.text.{ParseException, SimpleDateFormat}
import java.util.{Calendar, Date}

/**
  * Created by nanphonfy on 2017/8/13.
  */
object StringUtil {
  private val df: SimpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss")
  private val GIVE_UP_ZERO: Int = 100000
  val PATTERN_yyMMdd: String = "yyMMdd"
  val PATTERN_yy_MM_dd: String = "yy-MM-dd"
  val ROW_KEY_SIZE: Int = "0000047489:16-12-24 18:40:00,".length

  def getDay(i: Int): String = {
    var day: String = ""
    if (i < 10) {
      day = "0" + i
    }
    else {
      day = "" + i
    }
    return day
  }

  def getInt(str: String, defaultValue: Int): Int = {
    if (str == null) return defaultValue
    if (isInt(str)) {
      return str.toInt
    }
    else {
      return defaultValue
    }
  }

  def isInt(str: String): Boolean = {
    return str.matches("\\d+")
  }

  @throws[ParseException]
  def getTimestamp(str: String): Long = {
    return df.parse(str).getTime
  }

  @throws[ParseException]
  def getTimestamp(str: String, pattern: String): Long = {
    if (str == null) {
      return 0
    }
    val _df: SimpleDateFormat = new SimpleDateFormat(pattern)
    return _df.parse(str).getTime
  }

  def getLongMaxSubTimestamp(timestamp: Long): Long = {
    return (Long.MaxValue - timestamp) / GIVE_UP_ZERO
  }

  @throws[ParseException]
  def getTimesLice(str: String): Int = {
    val arr: Array[String] = str.split(":")
    val h: Int = getInt(arr(0), 0)
    val m: Int = getInt(arr(1), 0)
    return (h * 60 + m) / 5
  }

  @throws[ParseException]
  def getQualifier(timeslie: Int): String = {
    var qualifier: String = ""
    if (timeslie < 10) {
      qualifier = "00" + timeslie
    }
    else if (timeslie < 100) {
      qualifier = "0" + timeslie
    }
    else {
      qualifier = "" + timeslie
    }
    return qualifier
  }

  def getDuration(durationOld: Long): String = {
    val duration = durationOld / 1000
    if (duration <= 0) {
      return "0"
    }
    try {
      val hour: Long = duration / 3600
      val min: Long = (duration % 3600) / 60
      val s: Long = (duration % 3600) % 60
      if (hour == 0) {
        if (min == 0) {
          return s + "秒"
        }
        else {
          return min + "分钟" + s + "秒"
        }
      }
      else {
        return hour + "小时" + min + "分钟" + s + "秒"
      }
    }
    catch {
      case e: Exception => {
        e.printStackTrace
      }
    }
    return ""
  }

  def subOndDay(dateTime: String): String = {
    val beginDate = parseStringToDate(dateTime)
    val date = Calendar.getInstance()
    date.setTime(beginDate)
    date.set(Calendar.DATE, date.get(Calendar.DATE) - 1)
    df.format(date.getTime)
  }

  @throws[ParseException]
  def parseStringToDate(date: String): Date = {
    if (date == null) {
      return null
    }
    return df.parse(date)
  }
}
