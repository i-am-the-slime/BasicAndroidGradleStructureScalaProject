package co.aryaapp.journal.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.{CheckedTextView, EditText}
import co.aryaapp.TypedResource._
import co.aryaapp.database.AryaDB
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.{R, TR}

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
        R.string.frag_what_happened_subtitle) with ListAnswer {

  implicit val ctx = getActivity
  lazy val database = new AryaDB
  override def recyclerView = getActivity.findView(TR.what_happened_recycler_view)
  lazy val plusButton = getActivity.findView(TR.plus_button)
  val listItems = collection.mutable.Stack("Some", "Thing")
  lazy val listItemAdapter = new WhatHappenedAdapter(listItems)

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    inflater.inflate(R.layout.frag_journal_what_happened, container, false)
  }

  override def addListItem(item:String) = {
    if(!listItems.contains(item)){
      listItems.push(item)
      listItemAdapter.notifyItemInserted(0)
    }
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity))
    recyclerView.setAdapter(listItemAdapter)
    plusButton.setOnClickListener((v:View) => addNewListItem())
  }

  def addNewListItem():AlertDialog = {
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
          addListItem(text)
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
