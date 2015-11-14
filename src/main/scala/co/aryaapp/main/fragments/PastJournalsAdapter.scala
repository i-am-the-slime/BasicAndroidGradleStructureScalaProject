package co.aryaapp.main.fragments

import android.support.v7.widget.RecyclerView
import android.view.{ViewGroup, View}
import co.aryaapp.communication.DataTypes._

/**
 * Created by mark on 25.04.15.
 */
case class PastJournalViewHolder(view:View) extends RecyclerView.ViewHolder(view) {

}
case class PastJournalsAdapter(answers:List[Question]) extends RecyclerView.Adapter[PastJournalViewHolder] {
  override def getItemCount: Int = answers.length

  override def onBindViewHolder(vh: PastJournalViewHolder, i: Int): Unit = {
    answers(i) match {
      case Question(_, title, _, _, _, UserDataSlider(options)) =>
      case Question(_, title, _, _, _, UserDataList(_, _)) =>
      case Question(_, title, _, _, _, userData:UserDataImages) =>
      case Question(_, title, _, _, _, userData:UserDataText) =>
      case Question(_, title, _, _, _, userData:UserDataAudioText) =>
    }
  }

  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): PastJournalViewHolder =
    PastJournalViewHolder(viewGroup)
}
