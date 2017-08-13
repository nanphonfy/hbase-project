package cn.nanphonfy.traffic.api

import cn.nanphonfy.traffic.api.controller.query._
import org.apache.hadoop.hbase.util.Bytes
import org.http4s.dsl._
import org.http4s.server.HttpService
import org.http4s.{Header, Query, Request}


class BusinessLogic(tableName: String) {
  private def GetQuery(req: Request): Query = {
    val url = req.uri.toString()
    if (url.length == req.pathInfo.length) {
      Query.empty
    } else {
      Query.fromString(url.toLowerCase.substring(req.pathInfo.length + 1))
    }
  }

  private val QueryControllers: Map[String, Queryable] = Map(
    //道路统计信息查询
    "roadstat" -> new RoadStatInfoQuery(tableName),
    //公交道路统计排序查询
    "busroads" -> new BusRoadsSortInfoQuery(tableName),
    //公交流量总量查询
    "bustotal" -> new BusRoadsStatInfoQuery(tableName),
    //道路原始数据查询
    "roaddata" -> new RoadStatRawTextQuery(tableName)
  )

  def Process(): HttpService = HttpService {
    case req@GET -> Root / queryType => {
      val query = GetQuery(req)
      var roads = query.params.getOrElse("roadids", "").split(",").toList
      var start = query.params.getOrElse("start", "")
      val end = query.params.getOrElse("end", start)

      if (roads.head == "") {
        roads = query.params.getOrElse("roadname", "").split(",").toList
        start = "xx"
      }
      if (roads.isEmpty || start.isEmpty || !QueryControllers.contains(queryType)) {
        Forbidden("access forbidden")
      } else {
        Ok(QueryControllers(queryType).Query(roads, start, end)).
          putHeaders(Header("Access-Control-Allow-Origin", "*"))
      }
    }
    case _ =>
      NotFound("NotFound")
  }
}
