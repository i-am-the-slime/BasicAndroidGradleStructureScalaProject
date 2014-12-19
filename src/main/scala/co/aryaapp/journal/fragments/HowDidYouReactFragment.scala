package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{ViewGroup, LayoutInflater, View}
import co.aryaapp.{TypedResource, TR, R}
import co.aryaapp.communication.{TextAnswer, Answer}
import co.aryaapp.journal.JournalBaseFragment
import TypedResource._
import scalaz._, Scalaz._

class HowDidYouReactFragment extends
      JournalBaseFragment(
        R.drawable.ic_right_arrow,
        R.string.frag_how_did_you_react_title,
        R.string.frag_how_did_you_react_subtitle
      ){

  lazy val answerEditText = getView.findView(TR.reaction)

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    getActivity.getLayoutInflater.inflate(R.layout.frag_journal_your_reaction, container, false)
  }

  override def populateViewFromAnswer(answer: Answer): Unit = answer match {
    case TextAnswer(_, answerString, _) => answerEditText.setText(answerString)
  }

  override def getAnswerFromView(): Option[Answer] = {
    val answerString = answerEditText.getText.toString
    TextAnswer("stupid_question_uuid", answerString).some
  }
}
