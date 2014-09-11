package co.aryaapp.journal

import android.support.v4.app.{Fragment, FragmentManager, FragmentPagerAdapter}
import co.aryaapp.journal.fragments.{WhatAreYouThinkingFragment, HowDidYourBodyReactFragment, WhatHappenedFragment, HowAreYouFeelingFragment}

class JournalPagerAdapter(fm: FragmentManager) extends FragmentPagerAdapter(fm) {

  override def getCount: Int = 4

  lazy val one = new HowAreYouFeelingFragment
  lazy val two = new WhatHappenedFragment
  lazy val three = new HowDidYourBodyReactFragment
  lazy val four = new WhatAreYouThinkingFragment

  override def getItem(position: Int): Fragment = {
    position match {
      case 0 => one
      case 1 => two
      case 2 => three
      case 3 => four
    }
  }
}
