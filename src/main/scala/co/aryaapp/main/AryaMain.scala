package co.aryaapp.main

import android.content.{Intent, Context}
import android.graphics.Color
import android.os.{AsyncTask, Bundle}
import android.support.v4.app.{Fragment, FragmentPagerAdapter}
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener
import android.support.v7.app.ActionBarActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view
import android.view.{ViewGroup, Menu, Gravity, View}
import android.widget.TextView
import co.aryaapp.TypedResource._
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp.helpers.{AndroidConversions, AryaBaseActivity}
import co.aryaapp.journal.JournalActivity
import co.aryaapp.main.fragments.{CustomiseFragment, HomeFragment}
import co.aryaapp.view.ImageSlidingTabLayout
import co.aryaapp.{TypedResource, R, TR}
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import scala.language.postfixOps


class AryaMain extends AryaBaseActivity {
  val activity = this

  lazy val viewPager = this.findView(TR.mainPager)
  lazy val slidingTabs:ImageSlidingTabLayout = this.findView(TR.slidingTabs)

  def setupViewPager() = {
    viewPager.setAdapter(new MainImagePagerAdapter(getSupportFragmentManager))
  }

  def setupTabs() = {
    slidingTabs.setCustomTabView(R.layout.partial_main_tab)
    slidingTabs.setViewFiller((position, view) => {
      val icon = view.findView(TR.tab_icon)
      position match {
        case 0 =>
          icon.setText("\uE648")
        case 1 =>
          icon.setText("\uE62C")
          icon.setBackgroundColor(getResources.getColor(R.color.blue))
          icon.setOnClickListener(() => startNewJournalActivity())
        case 2 =>
          icon.setText("\uE675")
      }
      view
    })
    slidingTabs.setViewPager(viewPager)
  }

  def startNewJournalActivity():Boolean = {
    launchActivity[JournalActivity]
    true
  }

//  def setupNavigation() = {
//    navigationItems(0)
//  }

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupViewPager()
    setupTabs()
//    setupNavigation()
//    setSupportActionBar(toolBar)
//    startNewJournalActivity()
  }


  override def onSaveInstanceState(outState: Bundle): Unit = {
    super.onSaveInstanceState(outState)
  }


  override def onPostCreate(savedInstanceState: Bundle): Unit = {
    super.onPostCreate(savedInstanceState)
  }


  override def attachBaseContext(newBase: Context) = {
    super.attachBaseContext(new CalligraphyContextWrapper(newBase))
  }
}
