package co.aryaapp.helpers

import android.app.Activity
import android.content.{Context, Intent}
import android.os.{AsyncTask, Bundle}
import android.support.v7.app.ActionBarActivity
import co.aryaapp.R
import co.aryaapp.helpers.AndroidConversions._
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

object AryaBaseActivity {
  val exec = ExecutionContext.fromExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
}

abstract class AryaBaseActivity extends ActionBarActivity{
  implicit val exec = AryaBaseActivity.exec
  implicit val ctx = this


  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    getWindow.getAttributes.windowAnimations = R.style.SlideWindowAnimation
  }


  override def attachBaseContext(newBase:Context) = {
    super.attachBaseContext(new CalligraphyContextWrapper(newBase))
  }

  def launchActivity[A <: Activity](implicit ctx:Context, tag:ClassTag[A]) = {
    startActivity(new Intent(ctx, tag.runtimeClass))
  }

  def launchActivityForResult[A <: Activity](requestCode:Int)(implicit ctx:Context, tag:ClassTag[A]) = {
    startActivityForResult(new Intent(ctx, tag.runtimeClass), requestCode)
  }

  def launchActivityForResultWithOptions[A <: Activity](requestCode:Int, options:Bundle)(implicit ctx:Context, tag:ClassTag[A]) = {
    startActivityForResult(new Intent(ctx, tag.runtimeClass), requestCode, options)
  }

  def drawUi(f:() => Unit): Unit ={
    this.runOnUiThread(toRunnable(f))
  }

  override def startActivity(intent: Intent): Unit = {
    super.startActivity(intent)
//    overridePendingTransition(R.anim.move_up, R.anim.nothing)
  }

  override def startActivity(intent: Intent, options: Bundle): Unit = {
    super.startActivity(intent, options)
//    overridePendingTransition(R.anim.move_up, R.anim.nothing)
  }

  override def finish() = {
    super.finish()
//    overridePendingTransition(R.anim.nothing, R.anim.move_down)
  }
}
