package biggiesparks

import spark.{SparkContext,RDD}
import snap._
import scala.io.Source

object SimpleCaller {
  var ref = null;

  def main(args: Array[String]) {
    //dead simple first version
    val sc = new SparkContext("local", "SimpleCaller", "/data/spark-0.7.0", List("target/scala-2.9.2/sparkifiedbiggie_2.9.2-0.0.1.jar"))

    val lines = Source.fromFile(args(0)).getLines.toIndexedSeq

    val readRDD = sc.parallelize(lines)

    val count = readRDD.count()
    println(count)

    //args(0) reference
    //args(1) read file
    //args(2) ground truth
  }
}