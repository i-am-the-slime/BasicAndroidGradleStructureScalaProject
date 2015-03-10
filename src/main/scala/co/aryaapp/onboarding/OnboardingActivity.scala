package co.aryaapp.onboarding

import java.net.UnknownHostException

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.TextView
import co.aryaapp.TypedResource._
import co.aryaapp._
import co.aryaapp.helpers.{Animations, AryaBaseActivity}

import scala.concurrent.Future


object OnboardingActivity {
  val LOGIN_REQUEST_CODE = 1
  val REGISTER_REQUEST_CODE = 0
}
class OnboardingActivity extends AryaBaseActivity {

  def replaceFragment(f: => Fragment, name:String):Unit = {
    val frame = this.findView(TR.frame)
    frame.startAnimation(Animations.moveDownAndOut(frame, 600, 0).andThen(_ => reallyReplaceFragment(f, name)))
  }

  def popFragment():Unit = {
    val frame = this.findView(TR.frame)
    frame.startAnimation(Animations.moveDownAndOut(frame, 600, 0).andThen(_ => reallyPopFragment()))
  }

  def reallyPopFragment(): Unit = {
    val frame = findViewById(R.id.frame)
    drawUi( () => {
      frame.setVisibility(View.INVISIBLE)
    })
    val backStackCountBefore = getSupportFragmentManager.getBackStackEntryCount
    getSupportFragmentManager.popBackStack()
    Future {
      while(getSupportFragmentManager.getBackStackEntryCount == backStackCountBefore)
        Thread.sleep(50)
    }.onComplete{
      _ =>
        drawUi( () => frame.startAnimation(Animations.moveUpAndIn(frame, 600, 0)))
    }
  }

  private def reallyReplaceFragment(f: => Fragment, name:String) = {
    val frame = findViewById(R.id.frame)
    drawUi( () => {
      frame.setVisibility(View.INVISIBLE)
    })
    val backStackCountBefore = getSupportFragmentManager.getBackStackEntryCount
    getSupportFragmentManager
      .beginTransaction()
      .replace(R.id.frame, f)
      .setCustomAnimations(R.anim.nothing, R.anim.nothing)
      .addToBackStack(name)
      .commit()
    Future {
      while(getSupportFragmentManager.getBackStackEntryCount == backStackCountBefore)
        Thread.sleep(50)
    }.onComplete(
      _ => drawUi( () => frame.startAnimation(Animations.moveUpAndIn(frame, 600, 0)))
    )
  }

  override def onBackPressed(): Unit = {
    val entries = getSupportFragmentManager.getBackStackEntryCount
    if(entries > 0)
      popFragment()
  }

  override def onCreate(b: Bundle): Unit = {
    super.onCreate(b)
    setContentView(R.layout.activity_onboarding)
    getSupportFragmentManager
      .beginTransaction()
      .setCustomAnimations(R.anim.move_up, R.anim.move_down)
      .add(R.id.frame, new StartFragment)
      .commit()
    ()
  }

  def loginFailed(f:Throwable, textField:TextView) = {
    f match {
      case e:UnknownHostException =>
        textField.setText("Make sure you have a working internet connection and try again please.")
    }
    Log.e("", f.getClass + f.getLocalizedMessage)
  }

  def loginSuccessful(token:String) = {
    setResult(Activity.RESULT_OK, TokenIntent.make(token))
    finish()
  }

}
