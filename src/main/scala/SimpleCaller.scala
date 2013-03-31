package biggiesparks

import spark.{SparkContext,RDD}
import snap._
import scala.io.Source

object SimpleCaller {
  var ref = null;

  def main(args: Array[String]) {
    //dead simple first version
    val sc = new SparkContext("local", "SimpleCaller", "/home/eecs/jnewcomb/spark-0.7.0", 
      List("target/scala-2.9.2/sparkifiedbiggie_2.9.2-0.0.1.jar"))

    //val accum = sc.accumulable(0)

    val reads = sc.textFile(args(0))

    val count = reads.filter(!_.startsWith("@")).map(SamParse.parse).distinct().count()
    println(count)

    //args(0) reference
    //args(1) read file
    //args(2) ground truth
  }
}