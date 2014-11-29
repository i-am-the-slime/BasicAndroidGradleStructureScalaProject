package co.aryaapp


import android.test.AndroidTestCase
import android.util.Log
import co.aryaapp.communication._
import co.aryaapp.database.{SlickDatabase, BasicJournals}
import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.time.Span._
import argonaut._, Argonaut._


class DatabaseTest extends AndroidTestCase with AssertionsForJUnit with Matchers with ScalaFutures {

  def test_the_database_should_insert_journals() = {
    implicit val ctx = getContext
    val db = new SlickDatabase()
    val journal = BasicJournals.createBasicJournal()
    db.deleteJournal(journal)
    db.addJournal(journal)
    val savedJournal = db.getJournal(journal.uuid)
    assert(journal == savedJournal.get)
  }

  def test_deleting_from_the_database_should_not_crash_if_the_journal_to_delete_cant_be_found() = {
    implicit val ctx = getContext
    val db = new SlickDatabase()
    val journal = BasicJournals.createBasicJournal()
    db.deleteJournal(journal)
    db.deleteJournal(journal)
  }

  def test_the_database_should_survive_when_trying_to_insert_the_same_journal_twice() = {
    implicit val ctx = getContext
    val db = new SlickDatabase()
    val journal = BasicJournals.createBasicJournal()
    db.addJournal(journal)
    db.addJournal(journal)
  }

  def test_the_database_should_read_and_write_journal_entries() = {
    implicit val ctx = getContext
    val db = new SlickDatabase()
    val answers = Map("question1" -> Map("answer1" -> "some answer", "answer2" -> "some other answer"),
                      "question2" -> Map("answer3" -> "3rd  answer", "answer3" -> "some final answer"))
    val entry = JournalEntry("uuid", "createdAt", "updatedAt", answers)
    db.saveJournalEntry(entry)
    assert(db.fetchJournalEntries().head == entry)
  }
}
