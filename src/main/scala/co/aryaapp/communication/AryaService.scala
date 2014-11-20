package co.aryaapp.communication

import java.lang.reflect.Type

import android.os.AsyncTask
import co.aryaapp.persistence.AryaGson
import com.google.gson.{FieldNamingPolicy, GsonBuilder}
import com.squareup.okhttp._
import retrofit.RequestInterceptor.RequestFacade
import retrofit.client.OkClient
import retrofit.converter.{GsonConverter, Converter}
import retrofit.http.{Body, GET, POST}
import retrofit.mime.{TypedInput, TypedOutput}
import retrofit.{RequestInterceptor, RestAdapter}

import scala.concurrent.ExecutionContext

trait AryaPrivateService {
  //You need a token for these.
  @GET("/user/notes")
  def notes(): GetNotes
  @POST("/user/notes")
  def createNote(@Body noteContainer: PostNote):PostNoteResult
  //Journals
  @GET("/user/journals")
  def journals(): GetJournals
  @POST("/user/journals")
  def createJournal(@Body noteContainer: PostJournal):PostJournalResult
}

trait AryaPublicService {
  @GET("/themes")
  def themes(): ThemeContainer
  @POST("/users")
  def createUser(@Body userContainer: PostUser):PostUserResult
}

class AryaPrivateInterceptor(accessToken:String) extends RequestInterceptor{
  override def intercept(rf: RequestFacade): Unit = {
    rf.addHeader("Authorization", "Bearer " + accessToken)
    rf.addHeader("Content-Type", "application/json")
  }
}

class AryaPublicInterceptor extends RequestInterceptor{
  override def intercept(rf: RequestFacade): Unit =
      rf.addHeader("Authorization", "Client " + AryaService.CLIENT_ID)
}


object AryaService {
  implicit val exec = ExecutionContext.fromExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
  val CLIENT_ID = "e782c0c0-cf2c-4720-929f-bdcd314028f7"
  val CLIENT_SECRET = "b4c081b1561238cf9b5e22e9c4deb67b10d39e8ebe748e2f334c7afeba6875bb"
  val SERVER_BASE = "http://aryaapp-staging.herokuapp.com"
  val JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")

  val gson = AryaGson()

  def basicRestAdapter = new RestAdapter.Builder()
    .setEndpoint(SERVER_BASE)
    .setLogLevel(RestAdapter.LogLevel.FULL)
//    .setConverter(new GsonConverter(gson))
      .setConverter(new AryaConverter())
    .setClient(new OkClient(new OkHttpClient))
  
  private def privateRestAdapter(token:String) = basicRestAdapter
    .setRequestInterceptor(new AryaPrivateInterceptor(token))
    .build()
  
  private def publicRestAdapter = basicRestAdapter
    .setRequestInterceptor(new AryaPublicInterceptor())
    .build()

  def getPrivateRestAdapter(token:String):AryaPrivateService = {
    privateRestAdapter(token).create(classOf[AryaPrivateService])
  }
  
  def getPublicRestAdapter:AryaPublicService = {
    publicRestAdapter.create(classOf[AryaPublicService])
  }

}
