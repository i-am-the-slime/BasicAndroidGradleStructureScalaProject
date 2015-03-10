package co.aryaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import co.aryaapp.communication.DataTypes._
import co.aryaapp.communication.RestClient
import co.aryaapp.helpers.AryaBaseActivity
import co.aryaapp.main.AryaMain
import co.aryaapp.onboarding.{OnboardingActivity, TokenIntent}
import co.aryaapp.user.AryaUserCredentials

import scala.collection.mutable.{Map => MMap}
import scala.language.postfixOps
import scala.util.{Failure, Success}

class AryaIntro extends AryaBaseActivity {
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
//      val journals = Future {
//        val adapter = AryaService.getPrivateRestAdapter(token)
//        val questions = List(
//          Question("What is your problem?", "I really care.", "text_answer", TextAnswer("", "No-Thing."))
//        )
//        adapter.createJournal(PostJournal(Journal("123", "now", "not at all", questions)))
//          adapter.journals()
//      }
//      journals.onComplete{
//        case Success(j) => Log.e("Mother", j.toString)
//        case Failure(f) => Log.e("Mother", f.getMessage)
//      }
      val journalEntries = List(
        JournalEntry("abc", "jetz", "jetz", Map(
          "key1" -> Map("innerKey1" -> "value1"),
          "key2" -> Map("innerKey2" -> "value2")
        ))
      )

      val client = new RestClient(Some(token))
//      val postResult = client.postToServer[User, PostUserResult]("/user", Note("My best note, yeah?"))
//      postResult.onComplete{
//        case Success(s) =>
//          Log.e("MOTHER", s"Post result is $s")
          val result = client.getFromServer[GetJournals]("/user/journals")
          result.onComplete{
            case Success(su) => Log.e("MOTHER", s"Get result is $su")
            case Failure(f) => Log.e("MOTHER", "Error is " + f.getMessage)
//          }
//        case Failure(f) => Log.e("MOTHER", "Error is " + f.getMessage)
      }
//      JournalIO.saveJournalEntries(journalEntries)
//      Log.e("MOTHER", "Journal Entries:" + JournalIO.getJournalEntries.toString())
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
