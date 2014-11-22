package co.aryaapp.database

import android.content.Context
import co.aryaapp.communication.{Question, JournalPage, Journal, JournalEntry}

import scala.slick.driver.SQLiteDriver
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.jdbc.meta.MTable
import argonaut._, Argonaut._

class SlickDatabase(implicit ctx:Context) {
  val Driver = "org.sqldroid.SQLDroidDriver"
  val keepThis = Class.forName(Driver).newInstance()
  lazy val db = Database.forURL("jdbc:sqlite:" + ctx.getApplicationContext.getFilesDir + "db.txt", driver = Driver)

  def query[T](block: Session => T): T = db withSession {
    implicit s:Session=> block(s)
  }

  /* Create any tables that don't exist yet */
  db withSession { implicit session =>
    if (MTable.getTables(QuestionsTableName).list.isEmpty)
      questions.ddl.create
    if (MTable.getTables(JournalPagesTableName).list.isEmpty)
      journalPages.ddl.create
    if (MTable.getTables(JournalsTableName).list.isEmpty)
      journals.ddl.create
    if (MTable.getTables(JournalEntriesTableName).list.isEmpty)
      journalEntries.ddl.create
  }

  /**
   * Questions
   */
  val QuestionsTableName = "questions"
  case class QuestionUUID(value: String) extends MappedTo[String]
  class QuestionsTable(tag:Tag) extends Table[(QuestionUUID, JournalPageUUID, String, String)](tag, QuestionsTableName){
    def uuid = column[QuestionUUID]("_id", O.PrimaryKey)
    def journalPageUuid = column[JournalPageUUID]("journal_page_uuid")
    def typ = column[String]("type", O.NotNull)
    def json = column[String]("json", O.NotNull)
    def * = (uuid, journalPageUuid, typ, json)
    def journalPage = foreignKey("journal_page_fk", journalPageUuid, journalPages)
  }
  val questions = TableQuery[QuestionsTable]

  def questionIsAlreadyInDB(uuid:QuestionUUID):Boolean = db withSession { implicit session =>
    questions.filter(_.uuid == uuid).list.isEmpty
  }

  /**
   * JournalPages
   */
  val JournalPagesTableName = "journal_pages"
  case class JournalPageUUID(value: String) extends MappedTo[String]
  class JournalPagesTable(tag:Tag) extends Table[(JournalPageUUID, JournalUUID, String, String)](tag, JournalPagesTableName){
    def uuid = column[JournalPageUUID]("_id", O.PrimaryKey)
    def journalUuid = column[JournalUUID]("journal_uuid", O.NotNull)
    def title = column[String]("title", O.NotNull)
    def subtitle = column[String]("subtitle", O.NotNull)
    def * = (uuid, journalUuid, title, subtitle)
    def journal = foreignKey("journal_fk", journalUuid, journals)
  }
  val journalPages = TableQuery[JournalPagesTable]

  def journalPageIsAlreadyInDB(uuid:JournalPageUUID):Boolean = db withSession { implicit session =>
    journalPages.filter(_.uuid == uuid).list.isEmpty
  }

  /**
   * Journals
   */
  val JournalsTableName = "journals"
  case class JournalUUID(value: String) extends MappedTo[String]
  class JournalsTable(tag:Tag) extends Table[(JournalUUID, String, String)](tag, JournalsTableName){
    def uuid = column[JournalUUID]("uuid", O.PrimaryKey)
    def createdAt = column[String]("created_at", O.NotNull)
    def updatedAt = column[String]("updated_at", O.NotNull)
    def * = (uuid, createdAt, updatedAt)
  }
  val journals = TableQuery[JournalsTable]

  def journalIsAlreadyInDB(uuid:JournalUUID):Boolean = db withSession { implicit session =>
    journals.filter(_.uuid == uuid).list.isEmpty
  }

  def fetchJournalsRows() = db withSession { implicit session => journals.list }

  def journalUuids():List[JournalUUID] =
    query(implicit session => ( for { journal <- journals } yield journal.uuid).list )

  def saveJournal(journal:Journal) = {
    val journalUuid = JournalUUID(journal.uuid)
    if (!journalIsAlreadyInDB(journalUuid)){
      journals += (journalUuid, journal.created_at, journal.updated_at)
      journal.pages.foreach( page =>
        val journalPageUuid = JournalPa
        if(!journalPageIsAlreadyInDB(JournalPageUUID(page.uuid))){
          page.questions.foreach( question =>
            if(!questionIsAlreadyInDB(QuestionUUID(question.uuid))){
              questions += (question.uuid, question.typ, question.asJson)
            }
          )
        }
      )
    }
  }

  /**
   * Journal Entries
   */
  val JournalEntriesTableName = "journal_entries"
  class JournalEntriesTable(tag: Tag) extends Table[(Int, String, String, Boolean)](tag, JournalEntriesTableName) {
    def id = column[Int]("_id", O.PrimaryKey, O.AutoInc)
    def journalUuid = column[String]("journal_uuid")
    def responsesJson = column[String]("responses_json")
    def sent = column[Boolean]("sent", O.Default(false))
    def * = (id, journalUuid, responsesJson, sent)
  }
  val journalEntries = TableQuery[JournalEntriesTable]

  def fetchJournalEntriesRows() = db withSession { implicit session => journalEntries.list }

  def fetchJournalEntries():List[JournalEntry] = {
    for {
      (_, journalUuid, jsonAnswers, _) <- fetchJournalEntriesRows()
      answers <- jsonAnswers.decodeOption[Map[String, Map[String, String]]]
    } yield JournalEntry(journalUuid, "don't know", "and don't care", answers)
  }

  def saveJournalEntry(journalEntry:JournalEntry):Unit = {
    db withSession { implicit session =>
        journalEntries += (0, journalEntry.journalUuid, journalEntry.answers.asJson.nospaces, false)
    }
  }
}
