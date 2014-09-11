package co.aryaapp.journal.fragments

import android.content.Context
import android.view.MotionEvent
import org.scaloid.support.v4.SViewPager

class JournalViewPager(implicit val ctx:Context) extends SViewPager{
  override def onInterceptTouchEvent(ev: MotionEvent): Boolean = false
  override def onTouchEvent(ev: MotionEvent): Boolean = false
}
