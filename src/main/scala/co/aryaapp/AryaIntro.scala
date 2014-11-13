package co.aryaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import co.aryaapp.communication.AryaService
import co.aryaapp.communication.DataTypes.{PostJournal, TextAnswer, Question, Journal}
import co.aryaapp.helpers.AryaBaseActivity
import co.aryaapp.main.AryaMain
import co.aryaapp.onboarding.{OnboardingActivity, TokenIntent}
import co.aryaapp.user.AryaUserCredentials

import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.{Failure, Success}
import scala.collection.JavaConversions._

class AryaIntro extends AryaBaseActivity{
  import co.aryaapp.AryaIntro._

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    requestCode match {
      case GET_TOKEN =>
        val token = TokenIntent.get(data)
        AryaUserCredentials.writeAccessTokenToPrefs(token)
        launchActivity[AryaMain]
    }
  }

  override def onCreate(b: Bundle): Unit = {
    super.onCreate(b)
    if(AryaUserCredentials.isAccessTokenInPrefs){
      val token = AryaUserCredentials.getAccessTokenFromPrefs.get
      val journals = Future {
        val adapter = AryaService.getPrivateRestAdapter(token)
        val questions = List(
          Question("What is your problem?", "I really care.", "text_answer", List(TextAnswer("", "No-Thing.")))
        )
        adapter.createJournal(PostJournal(Journal("123", "now", "not at all", questions)))
//          adapter.journals()
      }
      journals.onComplete{
        case Success(j) => Log.e("Mother", j.toString)
        case Failure(f) => Log.e("Mother", f.getMessage)
      }
      launchActivity[AryaMain]
      finish()
    }
    else
      launchActivityForResult[OnboardingActivity](GET_TOKEN)
  }
}

object AryaIntro {
  val GET_TOKEN = 0
}
