package co.aryaapp.main

import android.content.Context
import android.os.Bundle
import co.aryaapp.TypedResource._
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp.helpers.{AryaBaseActivity, SlideIn}
import co.aryaapp.journal.JournalActivity
import co.aryaapp.view.ImageSlidingTabLayout
import co.aryaapp.{R, TR}
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import scala.language.postfixOps


class AryaMain extends AryaBaseActivity with SlideIn{

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

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupViewPager()
    setupTabs()
    startNewJournalActivity() //TODO Remove this in production
    ()
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
