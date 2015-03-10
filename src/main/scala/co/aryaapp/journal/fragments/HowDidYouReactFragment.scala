package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{LayoutInflater, View, ViewGroup}
import co.aryaapp.TypedResource._
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.{R, TR}

class HowDidYouReactFragment extends
      JournalBaseFragment(
        R.drawable.ic_right_arrow,
        R.string.frag_how_did_you_react_title,
        R.string.frag_how_did_you_react_subtitle
      ) with TextAnswer {

  override lazy val answerEditText = getView.findView(TR.reaction)

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    inflater.inflate(R.layout.frag_journal_your_reaction, container, false)
  }

}
