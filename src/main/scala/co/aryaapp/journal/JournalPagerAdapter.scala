package co.aryaapp.journal

import android.os.Parcelable
import android.support.v4.app.{Fragment, FragmentManager, FragmentPagerAdapter}
import android.view.ViewGroup
import co.aryaapp.communication.{TestJournal, Answer}
import co.aryaapp.journal.fragments._

import scala.util.Try
import argonaut._, Argonaut._

class JournalPagerAdapter(fm: FragmentManager, viewPagerId:Int) extends FragmentPagerAdapter(fm) {

  override def destroyItem(container: ViewGroup, position: Int, frag: scala.Any): Unit = {
    val f = frag.asInstanceOf[JournalBaseFragment]
    updateAnswersFromFragment(f, position)
    super.destroyItem(container, position, frag)
  }

  def updateAnswersFromFragment(f:JournalBaseFragment, position:Int) = {
   val answer = f.getAnswerFromView
   answer.foreach( a => answers = answers + (position -> a))
  }

  var answers = Map[Int, Answer]()

  def saveJournals():TestJournal = {
    for{
      position <- 0 until getCount-1
      frag <- findFragmentByPosition(position) if frag != null
    } updateAnswersFromFragment(frag, position)
    val theAnswers = answers.values
    val answer = TestJournal(theAnswers.toList)
    answer
  }

  def findFragmentByPosition(position:Int):Try[JournalBaseFragment] = {
    val itemId = getItemId(position)
    Try(fm.findFragmentByTag("android:switcher:"+"viewPagerId"+":"+itemId).asInstanceOf[JournalBaseFragment])
  }

  override def getCount: Int = 6

  override def getItem(position: Int): Fragment = {
    val frag = position match {
      case 0 => new HowAreYouFeelingFragment
      case 1 => new WhatHappenedFragment
      case 2 => new HowDidYouReactFragment
      case 3 => new HowDidYourBodyReactFragment
      case 4 => new WhatAreYouThinkingFragment
      case 5 => new DoneFragment(this)
    }
    answers.get(position).foreach(frag.populateViewFromAnswer)
    frag
  }

}
