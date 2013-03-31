package biggiesparks

import snap._

object ParseRead {
  def parse(line: String): Int = {
    val entry = snap.SAM.parseEntry(line)
    return entry.position
  }
}