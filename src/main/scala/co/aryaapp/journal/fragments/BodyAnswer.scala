package co.aryaapp.journal.fragments

import argonaut.Argonaut._
import argonaut._
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.macros.Macros.argonaut

import scala.collection.concurrent.TrieMap

/**
 * Created by mark on 25.02.15.
 */
trait BodyAnswer extends JournalBaseFragment {

  @argonaut case class BodyAnswer(questionId:String, answer:Map[String, List[String]])

  def answers:TrieMap[String, List[String]]

  override def populateViewFromAnswer(answer: String): Unit = {
    answer.decodeOption[BodyAnswer] match {
      case Some(BodyAnswer(_, as)) =>
        answers.empty
        for(answer ← as) answers += answer
      case _ ⇒ ()
    }
  }

  override def getAnswerFromView: Option[String] = {
    Some(BodyAnswer("ABCD", answers.toMap[String, List[String]]).asJson.nospaces)
  }
}
