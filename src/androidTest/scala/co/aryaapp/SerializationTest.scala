package co.aryaapp

import android.test.AndroidTestCase
import android.util.Log
import co.aryaapp.communication._
import co.aryaapp.database.BasicJournals
import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.time.Span._
import argonaut._, Argonaut._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

import argonaut._, Argonaut._
import scalaz._, Scalaz._

class SerializationTest extends AndroidTestCase with AssertionsForJUnit with Matchers with ScalaFutures  {

  def test_journals_should_serialize_and_deserialize() = {
    val journals = BasicJournals.createBasicJournal()
    val json = journals.asJson.nospaces
    val decoded = json.decodeOption[Journal].get
    assert(journals == decoded)
  }
}

