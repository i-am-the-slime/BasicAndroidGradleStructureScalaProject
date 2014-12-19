package co.aryaapp.journal

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.{View, Gravity}
import co.aryaapp.communication.{Answer, Question}
import co.aryaapp.{TR, TypedResource}
import co.aryaapp.helpers.AndroidConversions
import language.postfixOps
import AndroidConversions._
import TypedResource._

abstract case class JournalBaseFragment(icon:Int, title:Int, subtitle:Int) extends Fragment{

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    view.findView(TR.titlebar_icon).setBackgroundResource(icon)
    view.findView(TR.titlebar_title).setText(title)
    view.findView(TR.titlebar_subtitle).setText(subtitle)
  }

  def populateViewFromAnswer(answer:Answer)
  def getAnswerFromView:Option[Answer]
}
