package co.aryaapp.communication

import java.net.URL

import android.os.AsyncTask
import android.util.Log
import argonaut.Argonaut._
import argonaut._
import com.squareup.okhttp.{OkHttpClient, Request, RequestBody}

import scala.concurrent.{ExecutionContext, Future}

case class TokenContainer(token:Token)
object TokenContainer {
  implicit def TokenContainerCodecJson: CodecJson[TokenContainer] =
    casecodec1(TokenContainer.apply, TokenContainer.unapply)("token")
}
case class Token(accessToken:String, tokenType:String)
object Token {
implicit def TokenCodecJson: CodecJson[Token] =
casecodec2(Token.apply, Token.unapply)("access_token", "token_type")
}


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
    val BadRequest = 400
    val Ok = 200
    responseCode match {
      case BadRequest =>
        val container = responseBody.decodeOption[Resp].get
        container.errors.message match {
          case "Invalid email or password." =>
            throw new InvalidEmailOrPassword()
        }
      case Ok =>
        val container = responseBody.decodeOption[TokenContainer].get
        container.token.accessToken
    }
  }

}
