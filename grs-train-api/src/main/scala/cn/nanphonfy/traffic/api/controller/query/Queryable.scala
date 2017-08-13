package cn.nanphonfy.traffic.api.controller.query

trait Queryable {
  def Query(roads: Iterable[String], start: String, end: String): String
}
