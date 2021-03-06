package co.aryaapp.helpers

import android.view.View
import android.view.animation.Animation.AnimationListener
import android.view.animation._
import android.widget.TextView

import scala.util.Random

object Animations {
  def combineAnimations(animations: List[MarkAnimation]):MarkAnimation = {
    val as = new AnimationSet(false) with MarkAnimation
    animations.foreach(a => as.addAnimation(a))
    as
  }

  def flyEmIn(views:List[View], delay:Long=0):Unit = {
    views.foreach(v => {
      v.startAnimation(scaleIntoPlace(Random.nextInt(500)+delay))
    })
  }

  def standardFadeIn(delay:Long) = fadeIn(0.2f, 250, delay)
  def standardFadeOut(delay:Long) = fadeOut(250, delay)
  def standardMoveInFromRight(delay:Long) = moveInFromRight(300, 500, delay)
  def standardMoveOutToLeft(delay:Long) = moveOutToLeft(300, 500, delay)
  def standardMoveInFromLeft(delay:Long) = moveInFromRight(-300, 500, delay)
  def standardMoveUp(delay:Long)=moveUp(300, 500, delay)

  def bumpIn(delay:Long=0):MarkAnimation = {
    val a =combineAnimations(List(
      scaleDown(0.4f, 250, delay),
      fadeIn(0.0f, 100, delay)
    ))
    a.setInterpolator(new OvershootInterpolator(1.7f))
    a
  }

  def rotate(degrees:Float, duration:Long, delay:Long=0):MarkAnimation = {
    val a = new RotateAnimation(0, degrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) with MarkAnimation
    a.setFillAfter(true)
    applyBasicAttributes(a, duration, delay)
  }

  def moveDownABitAndFadeOut(delay:Long=0):MarkAnimation = {
    val a = combineAnimations(List(
      fadeOut(350, delay),
      moveDown(200, 400, delay)
    ))
    a.setFillAfter(true)
    a
  }

  def moveDownABitAndFadeIn(delay:Long=0):MarkAnimation = {
    val a = combineAnimations(List(
      fadeIn(0, 350, delay),
      moveDown(200, 400, delay)
    ))
    a.setFillAfter(true)
    a
  }

  def scaleIntoPlace(delay:Long=0):MarkAnimation = {
    combineAnimations(List(
      scaleDown(1.5f, 400, delay),
      fadeIn(0.0f, 250, delay)
    ))
  }

  def moveInFromRightAndFade(delay:Long=0):MarkAnimation = {
    combineAnimations(List(
      standardFadeIn(delay),
      standardMoveInFromRight(delay)
    ))
  }

  def moveOutToLeftAndFade(delay:Long=0):MarkAnimation = {
    combineAnimations(List(
      standardFadeOut(delay),
      standardMoveOutToLeft(delay)
    ))
  }

  def moveInFromLeftAndFade(delay:Long=0):MarkAnimation = {
    combineAnimations(List(
      standardFadeIn(delay),
      standardMoveInFromLeft(delay)
    ))
  }

  def moveUpAndFadeIn(delay:Long=0):MarkAnimation = {
    combineAnimations(List(
      standardFadeIn(delay),
      standardMoveUp(delay)
    ))
  }

  def fadeOut(duration:Long, delay:Long):MarkAnimation ={
    val a=new AlphaAnimation(1.0f, 0.0f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def fadeOutInWithText(startOpacity:Float, duration:Long, delay:Long, text:String, tv:TextView, view:View, viewToFadeOut:View):MarkAnimation = {
    val out = fadeOut(duration, delay)
    out.andThen(_ => {
        tv.setText(text)
        viewToFadeOut.setVisibility(View.VISIBLE)
        view.startAnimation(fadeIn(0, duration, duration))
      }
    )
    out
  }

  def scaleDown(startScale:Float, duration:Long, delay:Long):MarkAnimation = {
    val a = new ScaleAnimation(startScale, 1.0f, startScale, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def fadeIn(startOpacity:Float, duration:Long, delay:Long):MarkAnimation = {
    val a = new AlphaAnimation(startOpacity, 1.0f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def moveInFromLeft(howfar:Float, duration:Long, delay:Long):MarkAnimation = {
    val a = new TranslateAnimation(-howfar, 0.0f, 0.0f, 0.0f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def moveInFromRight(howfar:Float, duration:Long, delay:Long):MarkAnimation = {
    val a = new TranslateAnimation(howfar, 0.0f, 0.0f, 0.0f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def moveOutToLeft(howfar:Float, duration:Long, delay:Long):MarkAnimation = {
    val a = new TranslateAnimation(0.0f, -howfar, 0.0f, 0.0f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def moveOutToRight(howfar:Float, duration:Long, delay:Long):MarkAnimation = {
    val a = new TranslateAnimation(0.0f, howfar, 0.0f, 0.0f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def moveDown(howfar:Float, duration:Long, delay:Long):MarkAnimation = {
    val a = new TranslateAnimation(0.0f, 0.0f, 0.0f, howfar) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def moveUp(howfar:Float, duration:Long, delay:Long):MarkAnimation = {
    val a = new TranslateAnimation(0.0f, 0.0f, howfar, 0.0f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def moveDownAndOut(v: View, duration:Long, delay:Long):MarkAnimation = {
    val a = new TranslateAnimation(0f, 0f, 0f, v.getHeight+20f)  with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def moveUpAndIn(v: => View, duration:Long, delay:Long):MarkAnimation = {
    lazy val view = v
    view.setVisibility(View.VISIBLE)
    val a = new TranslateAnimation(0f, 0f, view.getHeight+20f, 0) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
  }

  def appearByRotation(duration:Long, delay:Long=0):MarkAnimation = {
    val a = new ScaleAnimation(0.0f, -1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
    val b = new ScaleAnimation(1.0f, -1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) with MarkAnimation
    applyBasicAttributes(b, duration, delay+duration)
    combineAnimations(List(a,b))
  }

  def spinHorizontally(duration:Long, delay:Long=0):MarkAnimation = {
    val a = new ScaleAnimation(1.0f, -1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) with MarkAnimation
    applyBasicAttributes(a, duration, delay)
    val b = new ScaleAnimation(1.0f, -1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) with MarkAnimation
    applyBasicAttributes(b, duration, delay+duration)
    combineAnimations(List(a,b))
  }


  def applyBasicAttributes(a:MarkAnimation, duration:Long, delay:Long):MarkAnimation ={
    a.setDuration(duration)
    a.setStartOffset(delay)
    a.setInterpolator(new AccelerateDecelerateInterpolator)
    a.setRepeatCount(0)
    a
  }

  def wiggle(delay:Long=0):MarkAnimation = {
    val a = new TranslateAnimation(-3.5f, 3.5f, 0.0f, 0.0f) with MarkAnimation
    a.setInterpolator(new MarkWiggleInterpolator)
    a.setDuration(400)
    a.setStartOffset(delay)
    a
  }

  def cartoonDashAway(delay:Long=0, clbk:MarkAnimation => Any):MarkAnimation = {
    val a = new TranslateAnimation(0f, 900f, 0f, 0f) with MarkAnimation
    a.setInterpolator(new CartoonRunAwayPositionInterpolator)
    a.setDuration(500)
    a.andThen(clbk)
    a.setStartOffset(delay)
    a
  }

  class MarkWiggleInterpolator() extends Interpolator {
    override def getInterpolation(t: Float): Float = {
      1.4f*Math.sin(t*6*Math.PI).asInstanceOf[Float]
    }
  }

  class CartoonRunAwayPositionInterpolator() extends Interpolator {
    override def getInterpolation(t: Float): Float = {
      t match {
        case left if left < 0.5f => -0.5f*left*left
        case stopping if stopping < 0.6f => -0.125f
        case dashingRight => 47/272f - 675/272f*t + 225/68f*t
      }
    }
  }

  trait MarkAnimation extends Animation{
    val self = this
    def andThen(callback: MarkAnimation => Any):MarkAnimation = {
      this.setAnimationListener(new AnimationListener() {
        def onAnimationEnd(animation: Animation) = {
          callback(self)
          ()
        }
        def onAnimationStart(animation: Animation): Unit = {}
        def onAnimationRepeat(animation: Animation): Unit = {}
      })
      this
    }
  }
}

