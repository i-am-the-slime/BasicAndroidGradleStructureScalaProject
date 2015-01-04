package co.aryaapp.main

import android.support.v4.app.{FragmentManager, Fragment, FragmentPagerAdapter}
import android.view.ViewGroup
import co.aryaapp.main.fragments.{CustomiseFragment, HomeFragment}

class MainImagePagerAdapter(fm:FragmentManager) extends FragmentPagerAdapter(fm){

  val fragments = List(
    () => new HomeFragment()
    ,() => new CustomiseFragment()
  )
  override def getItem(i: Int): Fragment = fragments(i)()
  override def getCount: Int = fragments.length

}
