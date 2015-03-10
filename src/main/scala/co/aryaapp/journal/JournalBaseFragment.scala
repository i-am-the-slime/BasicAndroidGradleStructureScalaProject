package co.aryaapp.journal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import argonaut.Argonaut._
import co.aryaapp.TR
import co.aryaapp.TypedResource._

import scala.language.postfixOps
import scala.util.Try

abstract case class JournalBaseFragment(icon:Int, title:Int, subtitle:Int) extends Fragment{
  val answerKey = "JournalAnswer"

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    view.findView(TR.titlebar_icon).setBackgroundResource(icon)
    view.findView(TR.titlebar_title).setText(title)
    view.findView(TR.titlebar_subtitle).setText(subtitle)
    for(
      answerJson ← Try(savedInstanceState.getString(answerKey))
    ) populateViewFromAnswer(answerJson)
  }

  override def onSaveInstanceState(outState: Bundle): Unit = {
    super.onSaveInstanceState(outState)
    for( answer ← getAnswerFromView ) outState.putString(answerKey, answer.asJson.nospaces )
  }

  def populateViewFromAnswer(answer:String):Unit
  def getAnswerFromView:Option[String]
}
