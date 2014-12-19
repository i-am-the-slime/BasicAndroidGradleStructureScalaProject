package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.{SeekBar, TextView}
import co.aryaapp.communication.{SliderAnswer, Answer}
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

  lazy val sliders = {
    val view = getView.findView(TR.slider_container)
    for {
      index <- 0 until view.getChildCount
      child = view.getChildAt(index) if child.getTag != null && child.getTag == "slider"
    } yield child.asInstanceOf[SeekBar]
  }

  override def populateViewFromAnswer(answer: Answer): Unit = {
    answer match {
      case a:SliderAnswer =>
        val valueAndView = a.values.values.toList.zip(sliders)
        for(
          (value, view ) <- valueAndView
        )  view.setProgress(value)
    }
  }

  override def getAnswerFromView(): Option[Answer] = {
    //TODO convince Menno that questions are not necessary or get them from the view
    val values = sliders.map(slider => ("question" ,slider.getProgress)).toMap[String, Int]
    Some(SliderAnswer("ABCD", values))
  }
}
