package co.aryaapp.database

import java.io.FileWriter

import android.content.Context
import co.aryaapp.communication.JournalEntry
import co.aryaapp.persistence.AryaGson

import scala.io.BufferedSource

object JournalIO {
  val Filename = "journals"

  def getFilename(implicit ctx:Context) = ctx.getFilesDir.getAbsolutePath + Filename

  def getJournalEntryFile(implicit ctx:Context):BufferedSource = {
    scala.io.Source.fromFile(getFilename)
  }
  
  def overwriteJournalEntryFile(json:String)(implicit ctx:Context)  = {
    val fw = new FileWriter(getFilename)
    fw.write(json) ;
    fw.close()
  }

  def asJournalEntries(source:BufferedSource): List[JournalEntry] =
    AryaGson().fromJson(source.bufferedReader(), classOf[List[JournalEntry]])

  def getJournalEntries(implicit ctx:Context):List[JournalEntry] =
    asJournalEntries(getJournalEntryFile)

  def asJson(entries:List[JournalEntry]):String =
    AryaGson().toJson(entries)

  def saveJournalEntries(entries:List[JournalEntry])(implicit ctx:Context) = {
    val json = asJson(entries)
    overwriteJournalEntryFile(json)
  }
}
