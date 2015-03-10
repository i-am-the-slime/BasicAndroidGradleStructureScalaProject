package co.aryaapp.database

import android.content.{ContentValues, Context}
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}

object AryaDB {
  object ColumnType extends Enumeration{
    type ColumnType = Value
    val INTEGER, REAL, TEXT, TEXT_NOT_NULL, BLOB = Value
  }
  import co.aryaapp.database.AryaDB.ColumnType._

  /** GENERAL */
  val DB_NAME = "aryadb"
  val DB_VERSION = 1

  /** COLUMNS */
  val COLUMN_ID = "_id"
  val COLUMN_ITEM = "item"

  /** TABLES */
  val TABLE_WHAT_HAPPENED_ITEMS = "what_happened_items"

  /** CREATE COMMANDS */
  val CREATE_TABLE_WHAT_HAPPENED_ITEMS = createTable(TABLE_WHAT_HAPPENED_ITEMS, (COLUMN_ITEM, TEXT_NOT_NULL))

  private def createTable(tableName:String, columns:(String, ColumnType)*) = {
    "CREATE TABLE " + tableName + "(" +
      COLUMN_ID + " integer primary key autoincrement" +
      columns.map{
        case (name, colType) => ", " + name + " " + colType.toString.replace("_", " ")
      }.foldLeft("")((prev, str) => prev + str)
  } + ")"
}

import co.aryaapp.database.AryaDB._

class AryaDB(implicit val ctx:Context)
  extends SQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION) {

  override def onCreate(db: SQLiteDatabase): Unit = {
    db.execSQL(CREATE_TABLE_WHAT_HAPPENED_ITEMS)
  }

  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int): Unit = {
    //Don't forget to adjust this on DB update
  }

  def readWhatHappenedItems:List[String] = {
    //val query = "SELECT " + COLUMN_ITEM + " FROM " + TABLE_WHAT_HAPPENED_ITEMS
    //val cursor = getReadableDatabase.rawQuery(query , null)
    //TODO: Fix this
    List("No", "No")
  }

  def writeWhatHappenedItems(items:List[String]) = {
    val db = getWritableDatabase
    db.beginTransaction()
    db.delete(TABLE_WHAT_HAPPENED_ITEMS, null, null)
    items.foreach( item =>{
      val cv = new ContentValues
      cv.put(COLUMN_ITEM, item)
      db.insert(TABLE_WHAT_HAPPENED_ITEMS, null, cv)
    })
    db.setTransactionSuccessful()
    db.endTransaction()
    db.close()
  }
}

