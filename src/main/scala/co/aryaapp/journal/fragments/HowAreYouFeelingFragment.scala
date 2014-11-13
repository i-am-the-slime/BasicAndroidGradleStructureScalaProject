package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.TextView
import co.aryaapp.{TypedResource, R, TR}
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.helpers._
import AndroidConversions._
import TypedResource._

class HowAreYouFeelingFragment extends
  JournalBaseFragment(
    R.drawable.ic_heart,
    R.string.frag_how_are_you_feeling_title,
    R.string.frag_how_are_you_feeling_subtitle) {

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    getActivity.getLayoutInflater.inflate(R.layout.frag_journal_feeling, container, false)
  }

}
