package biggiesparks

import spark.{SparkContext,RDD}
import snap._
import scala.io.Source

object SimpleCaller {
  var ref = null;


  def main(args: Array[String]) {
    //dead simple first version
    val sc = new SparkContext("local", "SimpleCaller")

    //val accum = sc.accumulable(0)

    val reads = sc.textFile(args(0)).filter(!_.startsWith("@")).flatMap(SamParse.parse(_)).groupBy(_._1).map( { case (k,v) => (k,v.map(_._2).reduceLeft(_ + _))})
    println(reads.first)
    //val ref = FASTA.read(args(1))
    //val broadcastRef = sc.broadcast(ref.pieces(0))

    //val count = reads.flatMap(runSimpleCaller(broadcastRef.value, _)).count()
    //val count = snps.count()
    // val count = reads.filter(!_.startsWith("@")).map(SamParse.parse).distinct().count()
    // println(count)
    //println(count)

    //args(0) reference
    //args(1) read file
    //args(2) ground truth
  }
}