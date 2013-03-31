package biggiesparks

import spark.{SparkContext,RDD}
import snap._

object SimpleCaller {
  var ref = null;

  def main(args: Array[String]) {
    //dead simple first version
    val sc = new SparkContext("local", "SimpleCaller", "/data/spark-0.7.0", List("target/scala-2.9.2/sparkifiedbiggie_2.9.2-0.0.1.jar"))

    val reads = SAM.read(args(1))
    val readSeq = reads.toIndexedSeq
    val readRDD = sc.parallelize(readSeq)

    val count = readRDD.count()
    println(count)

    //args(0) reference
    //args(1) read file
    //args(2) ground truth
  }
}