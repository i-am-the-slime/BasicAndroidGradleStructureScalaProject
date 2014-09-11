package co.aryaapp.journal

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import co.aryaapp.journal.fragments.JournalViewPager
import co.aryaapp.{Animations, R}
import org.scaloid.common._
import org.scaloid.support.v4.{SViewPager, SFragmentActivity}
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import language.postfixOps

class JournalActivity extends SFragmentActivity {
  implicit lazy val ctx:Activity = this
  lazy val iconTypeface = Typeface.createFromAsset(getAssets, "fonts/ligature.ttf")
  lazy val latoTypeface = Typeface.createFromAsset(getAssets, "fonts/lato-light.ttf")
  lazy val pager = new JournalViewPager().id(0xfc880)
  lazy val adapter = new JournalPagerAdapter(getSupportFragmentManager)
  var close:STextView = null

  override def onBackPressed() = {
    if(isTherePreviousPagerItem(pager, adapter)){
      goToPreviousPagerItem(pager, adapter)
    } else {
      close.startAnimation(Animations.wiggle(0))
    }
  }

  override def attachBaseContext(newBase:Context) = {
    super.attachBaseContext(new CalligraphyContextWrapper(newBase))
  }

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    //Setup View Pager for the fragments and its adapter
    val pagerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    pager.setLayoutParams(pagerParams)
    pager.setAdapter(adapter)

    val mainLayout = new SRelativeLayout{
      this += pager

      val nextButton = SButton("Next")
        .onClick(goToNextPagerItem(pager, adapter))
        .typeface(latoTypeface)
        .<<.alignParentBottom.margin(5 dip, 12 dip, 10 dip, 12 dip).>>
      nextButton.setBackground(R.drawable.button_green)

      pager.<<.alignParentBottom

      close = STextView("\uE10f")
        .typeface(iconTypeface)
        .textSize(48 sp)
        .onClick(finish())
        .<<.marginLeft(8 dip).alignParentLeft.alignParentTop.wrap.>>
    }
    mainLayout.setBackgroundResource(R.drawable.default_background)
    setContentView(mainLayout)
    getActionBar.hide()
  }

  def goToNextPagerItem(pager:SViewPager, adapter:JournalPagerAdapter) = {
    if(isThereNextPagerItem(pager, adapter))
      pager.setCurrentItem(pager.getCurrentItem + 1, true)
  }

  def goToPreviousPagerItem(pager:SViewPager, adapter:JournalPagerAdapter) = {
    if(isTherePreviousPagerItem(pager, adapter))
      pager.setCurrentItem(pager.getCurrentItem - 1, true)
  }

  def isThereNextPagerItem(pager:SViewPager, adapter:JournalPagerAdapter):Boolean = {
    pager.getCurrentItem < (adapter.getCount - 1)
  }

  def isTherePreviousPagerItem(pager:SViewPager, adapter:JournalPagerAdapter):Boolean = {
    pager.getCurrentItem > 0
  }

  override def finish() = {
    super.finish()
    overridePendingTransition(R.anim.nothing, R.anim.move_down)
  }
}
