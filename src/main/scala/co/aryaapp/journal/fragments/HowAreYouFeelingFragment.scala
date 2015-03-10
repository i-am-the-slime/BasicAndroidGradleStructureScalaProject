package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.SeekBar
import co.aryaapp.TypedResource._
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.{R, TR}

import scala.collection.immutable.IndexedSeq

class HowAreYouFeelingFragment extends
  JournalBaseFragment(
    R.drawable.ic_heart,
    R.string.frag_how_are_you_feeling_title,
    R.string.frag_how_are_you_feeling_subtitle) with SliderAnswer {

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    inflater.inflate(R.layout.frag_journal_feeling, container, false)
  }

  override lazy val sliders: IndexedSeq[SeekBar] = {
    val view = getView.findView(TR.slider_container)
    for {
      index <- 0 until view.getChildCount
      child = view.getChildAt(index) if child.getTag != null && child.getTag == "slider"
    } yield child.asInstanceOf[SeekBar]
  }

}
