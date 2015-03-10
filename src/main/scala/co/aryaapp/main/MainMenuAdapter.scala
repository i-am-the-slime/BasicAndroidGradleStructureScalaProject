package co.aryaapp.main

import android.support.v7.widget.RecyclerView
import android.view.{LayoutInflater, View, ViewGroup}
import co.aryaapp.TypedResource._
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp.{R, TR}

case class MenuItem(title:String, icon:String, callback:(View) => Unit)


class MainMenuHolder(view: View) extends RecyclerView.ViewHolder(view) {
  lazy val iconView = view.findView(TR.menu_entry_icon)
  lazy val titleView = view.findView(TR.menu_entry_title)

  def init(icon:String, title:String, callback:(View) => Unit) = {
    iconView.setText(icon)
    titleView.setText(title)
    view.setOnClickListener(callback)
  }
}


class MainMenuAdapter(menuItems: Seq[MenuItem]) extends RecyclerView.Adapter[MainMenuHolder] {

  override def onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuHolder = {
    val view = LayoutInflater.from(parent.getContext).inflate(R.layout.recycler_item_main_side_menu, parent, false)
    new MainMenuHolder(view)
  }


  override def getItemCount = menuItems.length


  override def onBindViewHolder(holder: MainMenuHolder, position: Int): Unit = {
    val item = menuItems(position)
    holder.init(item.icon, item.title, item.callback)
  }
}
