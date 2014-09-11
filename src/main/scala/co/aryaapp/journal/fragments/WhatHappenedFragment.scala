package co.aryaapp.journal.fragments

import android.os.Bundle
import android.util.Log
import android.view.View.OnLongClickListener
import co.aryaapp.database.AryaDB
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.BaseAdapter
import co.aryaapp.helpers.AndroidConversions
import co.aryaapp.{TR, TypedResource, Animations, R}
import co.aryaapp.journal.JournalBaseFragment
import AndroidConversions._
import TypedResource._
import org.scaloid.common._

class WhatHappenedFragment extends JournalBaseFragment{

  lazy val listAdapter = new WhatHappenedListAdapter(getWhatHappenedItemsFromDB)
  lazy val database = new AryaDB

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val view = inflater.inflate(R.layout.frag_what_happened, container, false)
    setTitle(view, "\uE073", R.string.frag_what_happened_title, R.string.frag_what_happened_subtitle)
    view.findView(TR.plus_button).onClick(initiateAddNewItem())
    view.findView(TR.list_view).setAdapter(listAdapter)
    view
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

  def initiateAddNewItem() = {
    val dialog = new AlertDialogBuilder("Add new option", "Write a short description of what happened"){
      val result = new SEditText().singleLine(p = true).textColor(0xFF000000)

      def addEditText():Unit = {
        val item = result.getText.toString
        addNewItem(item)
      }
      positiveButton("Add", addEditText())
      negativeButton("Cancel")
      setView(result)
    }
    dialog.show()
  }

  def addNewItem(item:String) = {
    //Add it to the database
    if (listAdapter.isNotYetInList(item)) {
      listAdapter.add(item)
      writeWhatHappenedItemsToDB(listAdapter.items)
    }
  }

  class WhatHappenedListAdapter(var items:List[String]) extends BaseAdapter(){
    val theId = 41
    var checkedMap:Map[String, Boolean] = Map()

    override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
      val text = items(position)

      val trashCan = new STextView("\ue609")
        .typeface(strokeTypeface)
        .textSize(27 sp)
        .textColor(R.color.black)
        .visibility(View.INVISIBLE)
        .onClick(remove(text))

      val relLayout = new SRelativeLayout{
        val checkbox = SCheckBox()
          .<<
          .alignParentLeft
          .alignParentTop
          .wrap
          .>>

        checkbox.onClick(flipChecked(text, checkbox))

        setToCheckedIfWasChecked(text, checkbox)

        val textView = STextView(text)
          .typeface(latoTypeface)
          .textSize(20 sp)
          .textColor(R.color.black)
          .<<.rightOf(checkbox).alignParentTop.>>

        this += trashCan.<<.wrap.alignParentRight.>>

      }
      relLayout.padding(12 dip, 12 dip, 12 dip, 12 dip)
      relLayout.id(theId)
      relLayout.setOnLongClickListener(new OnLongClickListener {
        override def onLongClick(v: View): Boolean = {
          trashCan.visibility(View.VISIBLE)
          trashCan.startAnimation(Animations.appearByRotation(200))
          true
        }
      })
      relLayout
    }


    def setToCheckedIfWasChecked(key:String, cb:SCheckBox) = {
      checkedMap.get(key).fold()(cb.checked(_))
    }

    def flipChecked(key:String, cb:SCheckBox) = {
      checkedMap += key -> cb.isChecked
    }

    override def getCount: Int = items.length

    override def getItemId(position: Int): Long = theId

    override def getItem(position: Int): String = items(position)

    def isNotYetInList(string:String):Boolean = !items.contains(string)

    def add(newItem:String) = {
      items = newItem :: items
      checkedMap += newItem -> true
      notifyDataSetChanged()
    }

    def remove(item:String) = {
      items = items.filter(i => i != item)
      writeWhatHappenedItemsToDB(items)
      notifyDataSetChanged()
    }
  }
}
