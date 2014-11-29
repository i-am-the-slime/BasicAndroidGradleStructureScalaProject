package co.aryaapp.journal

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import co.aryaapp.helpers.{AryaBaseActivity, AndroidConversions}
import co.aryaapp._
import language.postfixOps
import TypedResource._
import AndroidConversions._

class JournalActivity extends AryaBaseActivity {
  lazy val pager = this.findView(TR.journalViewPager)
  lazy val adapter = new JournalPagerAdapter(getSupportFragmentManager)
  lazy val closeButton = this.findView(TR.closeButton)
  lazy val nextButton = this.findView(TR.nextButton)

  override def onBackPressed() = {
    if(isTherePreviousPagerItem(pager, adapter)){
      goToPreviousPagerItem(pager, adapter)
    } else {
      finish()
    }
  }

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    //Setup View Pager for the fragments and its adapter
    setContentView(R.layout.activity_journal_frame)
    closeButton.setOnClickListener((v:View) => finish())
    pager.setAdapter(adapter)
    nextButton.setOnClickListener((v:View) => goToNextPagerItem(pager, adapter))
  }

  def goToNextPagerItem(pager:ViewPager, adapter:JournalPagerAdapter) = {
    if(isThereNextPagerItem(pager, adapter))
      pager.setCurrentItem(pager.getCurrentItem + 1, true)
  }

  def goToPreviousPagerItem(pager:ViewPager, adapter:JournalPagerAdapter) = {
    if(isTherePreviousPagerItem(pager, adapter))
      pager.setCurrentItem(pager.getCurrentItem - 1, true)
  }

  def isThereNextPagerItem(pager:ViewPager, adapter:JournalPagerAdapter):Boolean = {
    pager.getCurrentItem < (adapter.getCount - 1)
  }

  def isTherePreviousPagerItem(pager:ViewPager, adapter:JournalPagerAdapter):Boolean = {
    pager.getCurrentItem > 0
  }

  override def finish() = {
    super.finish()
  }
}
