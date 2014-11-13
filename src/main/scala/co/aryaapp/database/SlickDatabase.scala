package co.aryaapp.database

import android.content.Context
import android.os.Bundle

import scala.concurrent.Future
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.jdbc.meta.MTable

class SlickDatabase {

  // Table name in the SQL database
  final val JournalsTableName = "JOURNALS"

  // Table definition
  class JournalTable(tag: Tag) extends Table[(Int, String)](tag, JournalsTableName ) {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc) // This is the primary key column.
    def name = column[String]("SOME_TEXT")
    // Every table needs a * projection with the same type as the table's type parameter.
    def * = (id, name)
  }

  // Table representation instance
  val myData = TableQuery[JournalTable]

  // Database connection
  private var db:Database = _
  def getDatabase(ctx:Context) = {
    if(db == null)
      db = Database.forURL("jdbc:sqlite:" + ctx.getFilesDir + "slick-sandbox.txt", driver = "org.sqldroid.SQLDroidDriver")
    db
  }
  /*
   * Create the table if needed
   */
  def createTable() = {
    db withSession { implicit session =>
      if (MTable.getTables(JournalsTableName).list.isEmpty) {
        myData.ddl.create
      }
    }
  }

  def fetchRows():List[(Int, String)] = {
    db withSession {
      implicit session =>
        // Get existing rows
        myData.list
    }
  }

  /**
   * Add one row to table
   */
  def saveData() : Unit = {
    // This is an example usage of an implicit database session.
    db withSession {
      implicit session =>
        // Add a row to the existing data set
//        myData += (0, mEdit.getText().toString)
    }
  }

  /**
   * Remove data from table
   */
  def clearData() : Unit = {
    // In opposition to saveData(), this is an example of using
    // an explicit session. It could have been implicit as well.
    val session = db.createSession()
    // Delete all rows
    myData.delete(session)
  }
  
}
