package biggiesparks

import scala.collection.mutable.HashSet
import snap._

//this is a per-read rewrite of snap.SimpleVC
object SamParse {
  // At what quality do we consider reads confident
  val READ_QUALITY_THRESHOLD = 20
  val MIN_PHRED = 30              // Ignore bases mapped with score lower than this.

  def parse(line: String): HashSet[(Int, Int)] = {
    val read = snap.SAM.parseEntry(line)
    val coverage = new HashSet[(Int, Int)]
    val readPos = read.position
    if (shouldIgnoreRead(read)) {
      //do stuff later
    } else {
      var posInRead = 0
      var posInRef = readPos
      for ((count, op) <- parseCigar(read.cigar)) {
        op match {
          case 'S' =>
           posInRead += count
          case '=' =>
            var i = 0
            while (i < count) {
              val base = DNA.BASE_TO_CODE(read.sequence.charAt(posInRead + i))
              if (base != 'N') {
                coverage.add((posInRef + i, 1))
              }
              i += 1
            }
          case 'X' =>
            var i = 0
            while (i < count) {
              if (Utils.parsePhred(read.quality.charAt(posInRead + i)) >= MIN_PHRED) {
                val base = DNA.BASE_TO_CODE(read.sequence.charAt(posInRead + i))
                if (base != 'N') {
                  coverage.add((posInRef + i, 1))
                }
                i += 1
              }
            }
          case 'M' =>
            var i = 0
            while (i < count) {
              if (Utils.parsePhred(read.quality.charAt(posInRead + i)) >= MIN_PHRED) {
                val base = DNA.BASE_TO_CODE(read.sequence.charAt(posInRead + i))
                if (base != 'N') {
                  coverage.add((posInRef + i, 1))
                }
                i += 1
              }
            }
          case other =>
            //do nothing for coverage
        }
      }
    }
    /*
    * first stat: coverage
    * just return sum of directions for now
    */
    return coverage
  }

    def shouldIgnoreRead(read: SAMEntry): Boolean = {
    if (read.mapQuality < READ_QUALITY_THRESHOLD) { //should also check ref name when we have it
      return true
    }
    var amountClipped = 0
    for ((count, op) <- parseCigar(read.cigar) if op == 'S') {
      amountClipped += count
    }
    if (read.sequence.length - amountClipped < 70) {
      return true
    }
    return false
  }

  def parseCigar(cigar: String): Iterator[(Int, Char)] = new Iterator[(Int, Char)] {
    var pos = 0

    def hasNext: Boolean = pos < cigar.length

    override def next(): (Int, Char) = {
      if (!hasNext) {
        throw new java.util.NoSuchElementException("next on empty iterator")
      }
      var count = 0
      while (cigar.charAt(pos) >= '0' && cigar.charAt(pos) <= '9') {
        count = 10 * count + (cigar.charAt(pos) - '0')
        pos += 1
      }
      val op = cigar.charAt(pos)
      pos += 1
      return (count, op)
    }
  }

  // Get the length of the reference genome that the given read spans
  def getCoveredLength(read: SAMEntry): Int = {
    parseCigar(read.cigar).filter(p => "=XMD".contains(p._2)).map(_._1).sum
  }
}