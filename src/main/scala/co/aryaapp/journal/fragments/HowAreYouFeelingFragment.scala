package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.TextView
import co.aryaapp.{TypedResource, R, TR}
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.helpers._
import AndroidConversions._
import TypedResource._

class HowAreYouFeelingFragment extends JournalBaseFragment {

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val view = inflater.inflate(R.layout.frag_feeling, container, false)
    setTitle(view, "\ue06f", R.string.frag_how_are_you_feeling_title, R.string.frag_how_are_you_feeling_subtitle)
    view
  }

}
