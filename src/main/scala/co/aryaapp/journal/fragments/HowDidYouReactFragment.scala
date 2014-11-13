package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{ViewGroup, LayoutInflater, View}
import co.aryaapp.R
import co.aryaapp.journal.JournalBaseFragment

class HowDidYouReactFragment extends
      JournalBaseFragment(
        R.drawable.ic_right_arrow,
        R.string.frag_how_did_you_react_title,
        R.string.frag_how_did_you_react_subtitle
      ){

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    getActivity.getLayoutInflater.inflate(R.layout.frag_journal_your_reaction, container, false)
  }
}
