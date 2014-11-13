package co.aryaapp.user

import android.content.{Context, SharedPreferences}
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.util.Log
import co.aryaapp.TypedResource
import co.aryaapp.communication.{TokenGetter, AryaService}
import com.squareup.okhttp.{MediaType, RequestBody, Request, OkHttpClient}

import scala.concurrent.{ExecutionContext, Future, Promise}

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
