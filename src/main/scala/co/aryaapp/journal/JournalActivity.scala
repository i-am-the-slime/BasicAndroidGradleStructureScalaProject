package co.aryaapp.journal

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import co.aryaapp.{R, TR, TypedResource}
import co.aryaapp.helpers.{Animations, AryaBaseActivity, AndroidConversions}
import language.postfixOps
import TypedResource._
import AndroidConversions._

import scala.concurrent.Future

class JournalActivity extends AryaBaseActivity {
  lazy val pager = this.findView(TR.journalViewPager)
  lazy val adapter = new JournalPagerAdapter(getSupportFragmentManager, pager.getId)
  lazy val closeButton = this.findView(TR.closeButton)
  lazy val nextButton = this.findView(TR.nextButton)
  lazy val pixelsToMove = getScreenWidth - nextButtonDefaultX + nextButton.getWidth
  var nextButtonDefaultX = 0.0f

  override def onBackPressed() = {
    if(isTherePreviousPagerItem(pager, adapter)){
      goToPreviousPagerItem(pager, adapter)
    } else {
        finish()
    }
  }
  
  def animateNextButtonIn() = {
    animateNextButton( (deltaX: Float, duration:Int) =>
      Animations.moveInFromRight(deltaX, duration, 0)
    )
  }

  private def animateNextButton(animation:(Float, Int) => Animation) = {
    val anim = animation(pixelsToMove, 500)
    anim.setFillAfter(true)
    nextButton.startAnimation(anim)

  }
  
  def animateNextButtonOut() = {
    animateNextButton( (deltaX:Float, duration:Int) =>
      Animations.moveOutToRight(deltaX, duration, 0)
    )
  }

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    //Setup View Pager for the fragments and its adapter
    setContentView(R.layout.activity_journal_frame)
    pager.setAdapter(adapter)
    nextButton.setOnClickListener((v:View) => {Log.e("MOTHER", "Btn clicked") ;goToNextPagerItem(pager, adapter)})
    nextButtonDefaultX = nextButton.getX
    closeButton.setOnClickListener((v: View) => finish())
  }

  def goToNextPagerItem(pager:ViewPager, adapter:JournalPagerAdapter) = {

    if(pager.getCurrentItem == (adapter.getCount - 2)){
      animateNextButtonOut()
    }

    if(isThereNextPagerItem(pager, adapter))
      pager.setCurrentItem(pager.getCurrentItem + 1, true)
  }

  def goToPreviousPagerItem(pager:ViewPager, adapter:JournalPagerAdapter) = {

    if(pager.getCurrentItem == (adapter.getCount - 1)) {
      animateNextButtonIn()
    }

    if(isTherePreviousPagerItem(pager, adapter)) {
      pager.setCurrentItem(pager.getCurrentItem - 1, true)
    }
  }

  def isThereNextPagerItem(pager:ViewPager, adapter:JournalPagerAdapter):Boolean = {
    pager.getCurrentItem < (adapter.getCount - 1)
  }

  def isTherePreviousPagerItem(pager:ViewPager, adapter:JournalPagerAdapter):Boolean = {
    pager.getCurrentItem > 0
  }
}
