package co.aryaapp.persistence

import android.content.Context
import co.aryaapp.communication.Journal
import com.google.gson.Gson

object SharedPreferencesHelper {
  val Name = "co.aryaapp.arya"
  val UnfinishedJournal = "unfinished_journal"
}

import SharedPreferencesHelper._

trait SharedPreferencesHelper {
//  private val DefaultString = "bollox"
//  val ctx:Context
//
//  def sharedPrefs = ctx.getSharedPreferences(Name, Context.MODE_PRIVATE)
//
//  def writeJournal(journal:Journal) = {
//    sharedPrefs
//      .edit()
//      .putString("last_journal", journal.pickle.toString)
//      .commit()
//  }
//
//  def getJournal = {
//    val result = sharedPrefs.getString("last_journal", DefaultString)
//    if (result == DefaultString)
//      None
//    else
//      Some(result)
//  }

//  def getUnfinishedJournal:Option[Journal] = {
//    val string = sharedPrefs.getString(UnfinishedJournal, DefaultString)
//    if(string == DefaultString)
//      None
//    else {
//      val gson = new Gson()
//      gson.fromJson(string, classOf[Journal])
//    }
//  }
}
