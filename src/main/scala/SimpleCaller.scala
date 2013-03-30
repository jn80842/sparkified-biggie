package biggiesparks

import spark.{SparkContext,RDD}
import snap._

object SimpleCaller {
  var ref = null;

  def main(args: Array[String]) {
    //dead simple first version
    val sc = new SparkContext("local", "SimpleCaller", "/data/spark-0.7.0")
    val refFile = new File(args(0))
    ref = sc.broadcast(refFile)

    val reads = SAM.read(args(1))
    val readSeq = reads.toIndexSeq()
    val readRDD = sc.parallelize(readSeq)

    val count = readRDD.count()
    println(count)

    //args(0) reference
    //args(1) read file
    //args(2) ground truth
  }
}