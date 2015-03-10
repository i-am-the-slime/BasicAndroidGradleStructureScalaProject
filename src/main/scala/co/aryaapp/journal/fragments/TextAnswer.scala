package co.aryaapp.journal.fragments

import android.widget.TextView
import argonaut.Argonaut._
import argonaut._
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.macros.Macros.argonaut

import scalaz.Scalaz._

/**
 * Created by mark on 25.02.15.
 */
trait TextAnswer extends JournalBaseFragment {
  def answerEditText:TextView

  override def populateViewFromAnswer(answer: String): Unit = answer.decodeOption[TextAnswer] match {
    case Some(TextAnswer(_, answerString)) => answerEditText.setText(answerString)
    case _ ⇒ ()
  }

  @argonaut case class TextAnswer(questionId:String, answer:String)

  override def getAnswerFromView: Option[String] = {
    val answerString = answerEditText.getText.toString
    TextAnswer("stupid_question_uuid", answerString).asJson.nospaces.some
  }
}
