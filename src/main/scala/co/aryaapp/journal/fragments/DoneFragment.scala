package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.Button
import co.aryaapp.{TypedResource, TR, R}
import co.aryaapp.communication.Answer
import co.aryaapp.journal.{JournalPagerAdapter, JournalBaseFragment}
import TypedResource._
import co.aryaapp.helpers.AndroidConversions._

class DoneFragment(jpa:JournalPagerAdapter) extends JournalBaseFragment(
  R.drawable.ic_checkmark, R.string.frag_done_title, R.string.frag_done_subtitle ){

  lazy val confirmButton = getView.findView(TR.btn_confirm)

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    inflater.inflate(R.layout.frag_journal_done, container, false)
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    confirmButton.setOnClickListener(() => jpa.saveJournals())
  }

  override def populateViewFromAnswer(answer: Answer): Unit = ()

  override def getAnswerFromView(): Option[Answer] = None
}

