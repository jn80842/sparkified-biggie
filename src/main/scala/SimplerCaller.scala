package biggiesparks

import spark.{SparkContext,RDD}
import snap._
import scala.io.Source

object SimplerCaller {
  var ref = null;

  def runSimpleCaller(ref: GenomePiece, reads: Array[String]): IndexedSeq[SNP] = {
    val vc = new SparkSimpleVC(ref, reads)
    vc.run()
    return vc.diffsnps
  }

  def main(args: Array[String]) {
    //dead simple first version
    val sc = new SparkContext("local", "SimpleCaller")

    //val accum = sc.accumulable(0)
    //read in sam file as array of strings split by \n


    val ref = FASTA.read(args(1))
    val broadcastRef = sc.broadcast(ref.pieces(0))

    val diffsnps = sc.textFile(args(0), 10).filter(!_.startsWith("@")).glom().flatMap(runSimpleCaller(broadcastRef.value, _))
    //println(reads.count())
    val true_snps = TVSim.readSNPsToSeq(args(2))
    Utils.scoreDiffSnpsOnly(diffsnps.toArray, true_snps)

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