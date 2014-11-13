package co.aryaapp.journal

import android.support.v4.app.{Fragment, FragmentManager, FragmentPagerAdapter}
import co.aryaapp.journal.fragments._

class JournalPagerAdapter(fm: FragmentManager) extends FragmentPagerAdapter(fm) {

  val fragments = Seq(
    () => new HowAreYouFeelingFragment
    ,() => new WhatHappenedFragment
    ,() => new HowDidYouReactFragment
    ,() => new HowDidYourBodyReactFragment
    ,() => new WhatAreYouThinkingFragment
    ,() => new DoneFragment
  )

  override def getCount: Int = fragments.length

  override def getItem(position: Int): Fragment = fragments(position)()
}
