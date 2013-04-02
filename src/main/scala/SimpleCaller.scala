package biggiesparks

import spark.{SparkContext,RDD}
import snap._
import scala.io.Source

object SimpleCaller {
  var ref = null;

  def runSimpleCaller(ref: GenomePiece, reads: String): IndexedSeq[SNP] = {
    val vc = new SparkSimpleVC(ref, reads)
    vc.run()
    return vc.snpsset.toIndexedSeq
  }

  def main(args: Array[String]) {
    //dead simple first version
    val sc = new SparkContext("local", "SimpleCaller")

    //val accum = sc.accumulable(0)

    val reads = sc.textFile(args(0))
    val ref = FASTA.read(args(1))
    //val broadcastRef = sc.broadcast(ref.pieces(0))
    //val curriedSimpleCaller = Function.curried(runSimpleCaller _)
    //val snps = reads.flatMap(curriedSimpleCaller(broadcastRef.value))
    //val count = snps.count()
     val count = reads.filter(!_.startsWith("@")).map(SamParse.parse).distinct().count()
    // println(count)
    println(count)

    //args(0) reference
    //args(1) read file
    //args(2) ground truth
  }
}