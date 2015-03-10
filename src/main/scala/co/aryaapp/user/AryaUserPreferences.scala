package co.aryaapp.user

import android.content.Context
import android.os.AsyncTask
import android.preference.PreferenceManager

import scala.concurrent.ExecutionContext

object AryaUserPreferences {
  val EMAIL = "email"
  implicit val exec = ExecutionContext.fromExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

  def getFromPrefs[A](key:A)(implicit ctx:Context):Option[A]  = {
    val sp = PreferenceManager.getDefaultSharedPreferences(ctx)
    key match {
      case key: String =>
        val result = sp.getString(key, "")
        if (result == "") None else Some(result.asInstanceOf[A])
    }
  }

  def getEmail(implicit ctx:Context):Option[String] = getFromPrefs(EMAIL)
}
