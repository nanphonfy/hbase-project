package cn.nanphonfy.traffic.api

import java.io.{FileNotFoundException, PrintWriter}
import java.lang.management.ManagementFactory

import cn.nanphonfy.traffic.api.util.HBaseReader
import org.http4s.server.blaze.BlazeBuilder
import org.slf4j.LoggerFactory


object APIServer {
  private val logger = LoggerFactory.getLogger("")

  /**
    * 输出程序PID
    *
    * @param path PID文件路径
    */
  private def OutputPid(path: String) {
    try {
      val out: PrintWriter = new PrintWriter(path)
      val name: String = ManagementFactory.getRuntimeMXBean.getName
      val pid: String = name.split("@")(0)
      out.print(pid)
      out.close
    }
    catch {
      case e: FileNotFoundException => {
        logger.error(e.getMessage)
      }
    }
  }

  def main(args: Array[String]): Unit = {

    if (args.length < 3) {
      println("Usage: host port tableName")
    } else {
      try {

        logger.info("Application starting....")

        val host = args(0)
        val port = args(1).toInt
        val tableName = args(2)

        if (HBaseReader.Check()) {
          logger.info("connecting hbase success.")
        } else {
          logger.info("Connecting hbase failed.")
          logger.info("Application ended.")
          System.exit(1)
        }

        OutputPid("pid")

        val bl = new BusinessLogic(tableName)
        val service = bl.Process

        logger.info(s"Application serves forever on $host:$port")
        BlazeBuilder.bindHttp(port, host)
          .mountService(service, "/")
          .run
          .awaitShutdown()
      } catch {
        case ex: Throwable =>
          logger.error(ex.toString)
      }
    }
  }
}
