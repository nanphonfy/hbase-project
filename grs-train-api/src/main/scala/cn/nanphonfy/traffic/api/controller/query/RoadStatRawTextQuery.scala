package cn.nanphonfy.traffic.api.controller.query

class RoadStatRawTextQuery(tableName: String) extends Queryable {
  override def Query(roads: Iterable[String], start: String, end: String): String = {
    val qm = new QueryManager(tableName)
    val data = qm.GetRoadsData(roads, start, end)

    data.map { case (road, recs) => recs.map(r => road + "," + r.time + "," + r.total_volume + "," + r.total_speed).mkString("\n") }.mkString("\n")
  }
}
