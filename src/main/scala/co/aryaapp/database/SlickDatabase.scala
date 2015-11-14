package co.aryaapp.database

import android.content.Context
import argonaut.Argonaut._
import co.aryaapp.communication.DataTypes.{Journal, JournalEntry}
import org.joda.time.{LocalDateTime, DateTime}
import com.github.tototoshi.slick.SQLiteJodaSupport._
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.jdbc.meta.MTable

class SlickDatabase(implicit ctx:Context) {
  val Driver = "org.sqldroid.SQLDroidDriver"
  val keepThis = Class.forName(Driver).newInstance()
  lazy val db = Database.forURL("jdbc:sqlite:" + ctx.getApplicationContext.getFilesDir + "/db.txt", driver = Driver)

  /**
   * Answers
   */
  val AnswersTableName = "ANSWERS"
  class AnswersTable(tag:Tag) extends Table[(String, DateTime)](tag, AnswersTableName){
    def json = column[String]("json", O.NotNull)
    def date = column[DateTime]("date", O.NotNull)
    def * = (json, date)
  }
  val answers = TableQuery[AnswersTable]

  def addAnswers(as:List[String]):Unit = {
    db withTransaction {
      implicit session =>
        val date = DateTime.now()
        answers += ((as.asJson.nospaces, date))
    }
    ()
  }


  /**
   * Journals
   */
  val JournalsTableName = "JOURNALS"
  case class JournalUUID(value: String) extends MappedTo[String]
  class JournalsTable(tag:Tag) extends Table[(JournalUUID, String)](tag, JournalsTableName){
    def uuid = column[JournalUUID]("uuid", O.PrimaryKey)
    def json = column[String]("json", O.NotNull)
    def * = (uuid, json)
  }
  val journals = TableQuery[JournalsTable]

  def addJournal(journal:Journal) : Unit = {
    db withTransaction { implicit session =>
      val journalUuid = JournalUUID(journal.uuid)
      if( !journals.filter(_.uuid === journalUuid).exists.run ) {
        journals += ((journalUuid, journal.asJson.nospaces))
      }
    }
    ()
  }

  def deleteJournal(journal:Journal): Unit = {
    db withTransaction { implicit session =>
      val journalUuid = JournalUUID(journal.uuid)
      journals.filter(_.uuid === journalUuid).delete
    }
    ()
  }

  def getJournal(uuid:String):Option[Journal] = {
    db withTransaction { implicit session ⇒
      journals.filter(_.uuid === JournalUUID(uuid)).firstOption.flatMap{
        case (_, json) => json.decodeOption[Journal]
      }
    }
  }

  /**
   * Journal Entries
   */
  val JournalEntriesTableName = "JOURNAL_ENTRIES"
  class JournalEntriesTable(tag: Tag) extends Table[(Int, String, String, String, String, Boolean)](tag, JournalEntriesTableName) {
    def id = column[Int]("_id", O.PrimaryKey, O.AutoInc)
    def journalUuid = column[String]("journal_uuid")
    def createdAt = column[String]("created_at")
    def updatedAt = column[String]("updated_at")
    def responsesJson = column[String]("responses_json")
    def sent = column[Boolean]("sent", O.Default(false))
    def * = (id, journalUuid, createdAt, updatedAt, responsesJson, sent)
  }

  val journalEntries = TableQuery[JournalEntriesTable]

  def fetchJournalEntries():List[JournalEntry] = {
    for {
      (_, uuid, createdAt, updatedAt, jsonAnswers, _) ← db withSession { implicit session ⇒ journalEntries.list }
      answers ← jsonAnswers.decodeOption[Map[String, Map[String, String]]]
    } yield JournalEntry(uuid, createdAt, updatedAt, answers)
  }

  def saveJournalEntry(je:JournalEntry):Unit = {
    db withSession { implicit session ⇒
        journalEntries += ((0, je.journalUuid, je.createdAt, je.updatedAt, je.answers.asJson.nospaces, false))
    }
    () //TODO: Check if there is a way to return an option or something instead
  }

  /* Create any tables that don't exist yet */
  db withSession {
    val ddl = answers.ddl ++ journals.ddl ++ journalEntries.ddl
    implicit session ⇒ {
      if (MTable.getTables(JournalsTableName).list.isEmpty){
        ddl.create
      }
    }
  }

}
