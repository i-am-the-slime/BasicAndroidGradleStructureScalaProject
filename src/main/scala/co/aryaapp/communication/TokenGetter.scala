package co.aryaapp.communication

import java.net.URL

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.squareup.okhttp.{Request, RequestBody, OkHttpClient}

import scala.concurrent.{ExecutionContext, Future}

case class TokenContainer(token:Token)
case class Token(access_token:String, token_type:String)

case class ServerErrorsContainer(errors:ServerError)
case class ServerError(code:Int, message:String)

object TokenGetter {
  implicit val exec = ExecutionContext.fromExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
  class InvalidEmailOrPassword extends Throwable

  def getToken(email:String, password:String):Future[String] = Future {
    val client = new OkHttpClient()
    val clientId = AryaService.CLIENT_ID
    val clientSecret = AryaService.CLIENT_SECRET
    val requestString = s"""
          |{
          | "oauth": {
          |    "grant_type":"password",
          |    "client_id":"$clientId",
          |    "client_secret":"$clientSecret",
          |    "email":"$email",
          |    "password":"$password"
          | }
          |}
        """.stripMargin
    val body = RequestBody.create(AryaService.JSON_MEDIA_TYPE, requestString)
    val request = new Request.Builder()
      .url(new URL(AryaService.SERVER_BASE + "/oauth/token"))
      .addHeader("Accept", "application/json")
      .post(body)
      .build()
    val result = client.newCall(request).execute()
    val responseString = result.body().string()
    Log.e("Arya", responseString)
    handleTokenRequestResultBody(result.code(), responseString)
  }

  def handleTokenRequestResultBody(responseCode:Int, responseBody:String):String = {
    val gson = new Gson()
    responseCode match {
      case badRequest if badRequest == 400 =>
        val container = gson.fromJson(responseBody, classOf[ServerErrorsContainer])
        container.errors.message match {
          case "Invalid email or password." =>
            throw new InvalidEmailOrPassword()
        }
      case ok if ok == 200 =>
        val container = gson.fromJson(responseBody, classOf[TokenContainer])
        container.token.access_token
    }
  }

}
