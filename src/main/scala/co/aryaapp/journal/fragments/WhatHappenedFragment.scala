package co.aryaapp.journal.fragments

import android.app.{AlertDialog, Dialog}
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.View.OnLongClickListener
import co.aryaapp.database.AryaDB
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.{EditText, CheckedTextView, BaseAdapter}
import co.aryaapp.helpers.AndroidConversions
import co.aryaapp.{TR, TypedResource, R}
import co.aryaapp.journal.JournalBaseFragment
import AndroidConversions._
import TypedResource._

import scala.collection
import scala.collection.parallel.mutable

class WhatHappenedViewHolder(itemView:View) extends ViewHolder(itemView) {
  lazy val checkbox = itemView.findView(TR.what_happened_item_checkbox)
  lazy val ripple = itemView.findView(TR.what_happened_item_ripple)
}

class WhatHappenedAdapter(items:collection.mutable.Stack[String]) extends RecyclerView.Adapter[WhatHappenedViewHolder] {

  override def onCreateViewHolder(parent: ViewGroup, viewType: Int): WhatHappenedViewHolder = {
    val view = LayoutInflater.from(parent.getContext).inflate(R.layout.recycler_item_frag_what_happened, parent, false)
    new WhatHappenedViewHolder(view)
  }

  override def getItemCount: Int = items.length

  override def onBindViewHolder(holder: WhatHappenedViewHolder, position: Int): Unit = {
    val item = items(position)
    val cb = holder.checkbox
    cb.setText(item)
    holder.ripple.setOnClickListener((v:View) => cb.toggle())
  }
}

class WhatHappenedFragment extends
      JournalBaseFragment(
        R.drawable.ic_bubbles,
        R.string.frag_what_happened_title,
        R.string.frag_what_happened_subtitle) {

  implicit val ctx = getActivity
  lazy val database = new AryaDB
  def recyclerView = getActivity.findView(TR.what_happened_recycler_view)
  lazy val plusButton = getActivity.findView(TR.plus_button)
  val listItems = collection.mutable.Stack("Some", "Thing")
  lazy val listItemAdapter = new WhatHappenedAdapter(listItems)

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val view = getActivity.getLayoutInflater.inflate(R.layout.frag_journal_what_happened, container, false)
//    view.findView(TR.plus_button).onClick(initiateAddNewItem())
//    view.findView(TR.list_view).setAdapter(listAdapter)
    view
  }


  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity))
    recyclerView.setAdapter(listItemAdapter)
    plusButton.setOnClickListener((v:View) => addNewListItem())
  }

  def addNewListItem() : Unit = {
    val activity = getActivity
    val input = new EditText(activity)
    input.setTextColor(getActivity.getResources.getColor(R.color.black))
    input.requestFocus()
    new AlertDialog.Builder(activity)
     .setTitle("Type in what happened!")
     .setView(input)
     .setPositiveButton("OK", (di:DialogInterface, _:Int) => {
        val text = input.getText.toString
        if(text!="") {
          listItems.push(text)
          listItemAdapter.notifyItemInserted(0)
        }
      di.dismiss()
      })
     .setNegativeButton("Cancel", (di:DialogInterface, _:Int) => di.dismiss())
     .show()
  }

  def getWhatHappenedItemsFromDB:List[String] = {
    val dbResult = database.readWhatHappenedItems
    if (dbResult.nonEmpty)
      dbResult
    else {
      val defaults = getActivity.getResources.getStringArray(R.array.what_happened_defaults)
      defaults.toList
    }
  }

  def writeWhatHappenedItemsToDB(items:List[String]) = {
    database.writeWhatHappenedItems(items)
  }

}
