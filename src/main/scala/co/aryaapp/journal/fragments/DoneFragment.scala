package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{LayoutInflater, View, ViewGroup}
import co.aryaapp.R
import co.aryaapp.journal.JournalBaseFragment

class DoneFragment extends JournalBaseFragment(
  R.drawable.ic_checkmark, R.string.frag_done_title, R.string.frag_done_subtitle ){

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    inflater.inflate(R.layout.frag_journal_done, container, false)
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
  }

  override def populateViewFromAnswer(answer: String): Unit = ()

  override def getAnswerFromView: Option[String] = None
}

