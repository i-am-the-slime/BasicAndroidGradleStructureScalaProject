package co.aryaapp.helpers

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class SlidingFrameLayout(ctx:Context, attrs:AttributeSet) extends FrameLayout(ctx, attrs) {
  def getXFraction:Float = {
    val width = getWidth
    if(width==0f) 0f else getX/width
  }

  def setXFraction(xFraction:Float) = {
    val width = getWidth
    setX(if (width > 0) xFraction * width else -9999)
  }
}

