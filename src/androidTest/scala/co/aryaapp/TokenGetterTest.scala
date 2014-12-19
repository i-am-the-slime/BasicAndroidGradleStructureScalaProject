package co.aryaapp

import android.os.AsyncTask
import android.test.AndroidTestCase
import co.aryaapp.communication.TokenGetter
import co.aryaapp.communication.TokenGetter.InvalidEmailOrPassword
import co.aryaapp.devicetest.helper.BaseAryaActivityTest
import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.time.Span._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

class TokenGetterTest extends BaseAryaActivityTest {
  implicit val exec = ExecutionContext.fromExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

  def test_a_valid_request_to_the_server_should_return_a_token() = {
    val result = TokenGetter.getToken("mark.eibes@googlemail.com", "helloworld")
    assert(result.isReadyWithin(20 seconds))
    assert(result.futureValue.startsWith("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdW"))
  }

  def test_an_invalid_email_or_password_exception_should_produce_an_exception() = {
    val errorBody = """{"errors":{"code":400,"message":"Invalid email or password."}}"""
    Try(TokenGetter.handleTokenRequestResultBody(400, errorBody)) match {
      case Success(s) => fail("Did not produce an exception")
      case Failure(f) => assert(f.isInstanceOf[InvalidEmailOrPassword])
    }
  }

  def test_a_token_response_should_be_unwrapped_correctly() = {
    val token = "theTokenItself"
    val tokenBody = s"""{"token":{"access_token":"$token","token_type":"bearer"}}"""
    val parsedToken = TokenGetter.handleTokenRequestResultBody(200, tokenBody)
    parsedToken shouldBe token
  }
}
