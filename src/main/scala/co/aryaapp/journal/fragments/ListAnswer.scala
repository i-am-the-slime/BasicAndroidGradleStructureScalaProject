package co.aryaapp.journal.fragments

import android.support.v7.widget.RecyclerView
import android.widget.{CheckedTextView, SeekBar}
import co.aryaapp.R
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.macros.Macros.argonaut
import argonaut._, Argonaut._

import scala.collection.immutable.IndexedSeq

/**
 * Created by mark on 26.02.15.
 */
trait ListAnswer extends JournalBaseFragment {

  @argonaut case class ListAnswer(questionId:String, answer:List[String])

  val listItems:collection.mutable.Stack[String]
  def addListItem(item:String):Unit
  def recyclerView:RecyclerView

  override def populateViewFromAnswer(answer: String): Unit =
    answer.decodeOption[ListAnswer] match {
    case Some(ListAnswer(_, answers)) => for (item <- answers) addListItem(item)
    case _ ⇒ ()
  }

  override def getAnswerFromView: Option[String] = {
    val values = for{
      index <- 0 until recyclerView.getChildCount
      child = recyclerView.getChildAt(index) if child.getId == R.id.what_happened_item_checkbox
      checkedTV:CheckedTextView = child.asInstanceOf[CheckedTextView] if child.isActivated
    } yield checkedTV.getText.toString
    Some(ListAnswer("dont know the uuid", values.toList).asJson.nospaces)
  }

  }
