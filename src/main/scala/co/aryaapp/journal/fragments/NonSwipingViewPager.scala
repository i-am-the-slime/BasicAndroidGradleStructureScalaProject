package co.aryaapp.journal.fragments

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NonSwipingViewPager(implicit val ctx:Context, attrs:AttributeSet) extends ViewPager(ctx, attrs){
  override def onInterceptTouchEvent(ev: MotionEvent): Boolean = false
  override def onTouchEvent(ev: MotionEvent): Boolean = false
}
