package co.aryaapp.database

import android.content.Context
import android.os.Bundle
import co.aryaapp.communication.JournalEntry

import scala.concurrent.Future
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.jdbc.meta.MTable
import argonaut._, Argonaut._


class SlickDatabase(implicit ctx:Context) {

  val JournalTableName = "Journals"
  class Journals(tag: Tag) extends Table[(Int, String, Boolean)](tag, JournalTableName) {
    def id = column[Int]("_id", O.PrimaryKey, O.AutoInc)
    def json = column[String]("json")
    def sent = column[Boolean]("sent", O.Default(false))
    def * = (id, json, sent)
  }

  val journalsData = TableQuery[Journals]

  lazy val db = Database.forURL(
    "jdbc:sqlite" + ctx.getApplicationContext.getFilesDir + "journals.txt",
    driver = "org.sqldroid.SQLDroidDriver")

  db withSession { implicit session =>
    if (MTable.getTables(JournalTableName).list.isEmpty)
      journalsData.ddl.create
  }

  def fetchRows() = {
    db withSession { implicit session =>
      journalsData.list
    }
  }

  def fetchJournals():List[JournalEntry] = {
    for {
      (_, json, _) <- fetchRows()
      journalEntry <- json.decodeOption[JournalEntry]
    } yield journalEntry
  }

  def saveJournalEntry(journalEntry:JournalEntry):Unit = {
    db withSession {
      implicit session =>
        journalsData += (0, journalEntry.asJson.spaces2, false)
    }
  }
}
