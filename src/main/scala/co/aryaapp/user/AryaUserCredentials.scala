package co.aryaapp.user

import android.content.Context
import android.os.AsyncTask
import android.preference.PreferenceManager

import scala.concurrent.ExecutionContext

object AryaUserCredentials {
  val OAUTH_ACCESS_TOKEN = "oauth_access_token"
  implicit val exec = ExecutionContext.fromExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

  def getAccessTokenFromPrefs(implicit ctx:Context):Option[String] = {
    val sp = PreferenceManager.getDefaultSharedPreferences(ctx)
    val accessToken = sp.getString(OAUTH_ACCESS_TOKEN, "")
    if(accessToken=="")
      None
    else
      Some(accessToken)
  }

  def isAccessTokenInPrefs(implicit ctx:Context):Boolean = getAccessTokenFromPrefs.isDefined

  def writeAccessTokenToPrefs(accessToken:String)(implicit ctx:Context) = {
    val sp = PreferenceManager.getDefaultSharedPreferences(ctx)
    val edit = sp.edit().putString(OAUTH_ACCESS_TOKEN, accessToken)
    edit.commit()
  }

}
