package jp.nukokusabot

import java.sql.Timestamp
import java.util.Calendar

trait Utils {

  def timestamp = {
    val now = new Timestamp(Calendar.getInstance.getTimeInMillis)
    "[" + now.toString.split('.')(0) + "]"
  }

}