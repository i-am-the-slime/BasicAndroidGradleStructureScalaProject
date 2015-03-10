package co.aryaapp.journal.fragments

import android.widget.SeekBar
import argonaut.Argonaut._
import argonaut._
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.macros.Macros.argonaut

import scala.collection.immutable.IndexedSeq

trait SliderAnswer extends JournalBaseFragment {
  def sliders: IndexedSeq[SeekBar]

  override def populateViewFromAnswer(answer: String): Unit = {
    answer.decodeOption[SliderAnswer] match {
      case Some(a:SliderAnswer) =>
        val valueAndView = a.answer.values.toList.zip(sliders)
        for(
          (value, view ) <- valueAndView
        )  view.setProgress(value)
      case _ ⇒ ()
    }
  }

  @argonaut case class SliderAnswer(questionId:String, answer:Map[String, Int])

  override def getAnswerFromView: Option[String] = {
    val values = sliders.map(slider => ("question" , slider.getProgress)).toMap[String, Int]
    Some(SliderAnswer("ABCD", values).asJson.nospaces)
  }
}
