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
    val clientId = RestClient.ClientId
    val clientSecret = RestClient.ClientSecret
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
    val body = RequestBody.create(RestClient.Json, requestString)
    val request = new Request.Builder()
      .url(new URL(RestClient.ServerBase + "/oauth/token"))
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
    val BadRequest = 400
    val Ok = 200
    responseCode match {
      case BadRequest =>
        val container = gson.fromJson(responseBody, classOf[ServerErrorsContainer])
        container.errors.message match {
          case "Invalid email or password." =>
            throw new InvalidEmailOrPassword()
        }
      case Ok =>
        val container = gson.fromJson(responseBody, classOf[TokenContainer])
        container.token.access_token
    }
  }

}
