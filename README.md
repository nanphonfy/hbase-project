# hbase-tool
直接使用maven的package，然后写shell脚本运行。
## XiongWenHBaseWork类  
 设计了新的rowkey和列名  
 shell脚本示例:  
 `nohup java -cp hbase-take-new-1.0-SNAPSHOT-jar-with-dependencies.jar cn.sibat.hbase.take.XiongWenHBaseWork GdRoadNew /home/public/zhengshaorong/backup/ 2017-08-01.txt >test.log &`
 ## WhileHBaseWork类(旧表)  
 用其中几天的数据模拟多天的数据  
  shell脚本示例:  
 `nohup java -cp hbase-tool-MN-SNAPSHOT-jar-with-dependencies.jar cn.sibat.hbase.tool.WhileHBaseWork 12 31 2017-07- 1 31 2017-03- >test.log &`
