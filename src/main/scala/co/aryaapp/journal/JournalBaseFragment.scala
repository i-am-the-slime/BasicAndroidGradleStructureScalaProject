package co.aryaapp.journal

import android.graphics.Typeface
import android.view.{View, Gravity}
import co.aryaapp.{TR, TypedResource}
import co.aryaapp.helpers.AndroidConversions
import org.scaloid.common._
import org.scaloid.support.v4.SFragment
import language.postfixOps
import AndroidConversions._
import TypedResource._

abstract class JournalBaseFragment extends SFragment{
  lazy val iconTypeface = Typeface.createFromAsset(getActivity.getAssets, "fonts/ligature.ttf")
  lazy val latoTypeface = Typeface.createFromAsset(getActivity.getAssets, "fonts/lato-light.ttf")
  lazy val strokeTypeface = Typeface.createFromAsset(getActivity.getAssets, "fonts/stroke.otf")

  def setTitle(view:View, icon:String, title:Int, subtitle:Int) = {
    view.findView(TR.titlebar_icon).setText(icon)
    view.findView(TR.titlebar_title).setText(title)
    view.findView(TR.titlebar_subtitle).setText(subtitle)
  }
}
