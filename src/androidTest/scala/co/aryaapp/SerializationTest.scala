package co.aryaapp

import android.test.AndroidTestCase
import android.util.Log
import argonaut.Argonaut._
import co.aryaapp.communication.DataTypes._
import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.AssertionsForJUnit

import scalaz._

class SerializationTest extends AndroidTestCase with AssertionsForJUnit with Matchers with ScalaFutures  {

  val exampleUser = """{"user":{"uuid":"45d9e41e-8df6-4e0f-b922-2e6c1e05b115","email":"mark.eibes@googlemail.com","created_at":"2014-09-26T22:36:58.283Z","updated_at":"2014-09-26T22:36:58.410Z","fullname":null,"gender":"male","public":false,"professional":false,"achievements_count":0,"activities_count":0,"activities_unread_count":0,"invites_count":0,"invites_pending_count":0,"journals_count":71,"notes_count":471,"links":{"achievements":"http://aryaapp-staging.herokuapp.com/user/achievements","activities":"http://aryaapp-staging.herokuapp.com/user/activities","activities/unread":"http://aryaapp-staging.herokuapp.com/user/activities/unread","devices":"http://aryaapp-staging.herokuapp.com/user/devices","invites":"http://aryaapp-staging.herokuapp.com/user/invites","invites/pending":"http://aryaapp-staging.herokuapp.com/user/invites/pending","journals":"http://aryaapp-staging.herokuapp.com/user/journals","notes":"http://aryaapp-staging.herokuapp.com/user/notes","themes":"http://aryaapp-staging.herokuapp.com/user/themes"},"questions":[{"uuid":"f6f7eaf1-a9df-460b-a6a2-8f3ba8a1c91c","title":"How are you feeling?","description":"Move the sliders to indicate your feeling.","view":"sliders","keywords":[],"user_data":{"options":[{"title":"General feeling","identifier":"general","min_value":-100,"max_value":100,"default_value":0},{"title":"Joy","identifier":"joy","min_value":-100,"max_value":100,"default_value":0},{"title":"Trust","identifier":"trust","min_value":-100,"max_value":100,"default_value":0},{"title":"Fear","identifier":"fear","min_value":-100,"max_value":100,"default_value":0},{"title":"Surprise","identifier":"surprise","min_value":-100,"max_value":100,"default_value":0},{"title":"Sadness","identifier":"sadness","min_value":-100,"max_value":100,"default_value":0},{"title":"Lonely","identifier":"lonely","min_value":-100,"max_value":100,"default_value":0},{"title":"Disgust","identifier":"disgust","min_value":-100,"max_value":100,"default_value":0},{"title":"Anger","identifier":"anger","min_value":-100,"max_value":100,"default_value":0},{"title":"Anticipation","identifier":"anticipation","min_value":-100,"max_value":100,"default_value":0}]}},{"uuid":"6b399586-023f-440a-a49e-1e866a9dc63c","title":"Describe the circumstances","description":"Type in one or multiple items.","view":"list","keywords":[],"user_data":{"autocompletes":true,"defaults":["Got teased","Bad sleep","Had arguments with someone","Good grade at school","Had a fun converstation"]}},{"uuid":"44475546-9eda-4784-bcc9-42428fd3ddb3","title":"How does your body feel?","description":"Describe what you feel and where you feel it.","view":"images","keywords":[],"user_data":{"images":[{"frame":{"x":10,"y":10,"width":100,"height":120},"identifier":"left_arm","options":["My arm hurts","My arm tingles"],"url":"yolo1.png"},{"frame":{"x":100,"y":80,"width":50,"height":150},"identifier":"left_leg","options":["My leg hurts","My leg tingles"],"url":"yolo2.png"}]}},{"uuid":"594155d6-012e-4fff-959a-c830c3ad4ece","title":"What are your thoughts?","description":"You can type in what your thoughts are.","view":"audio-text","keywords":[],"user_data":{"placeholder":"You can type in your thoughts here..."}},{"uuid":"c96f00b1-37c5-42ba-8e32-794992c13db1","title":"How did you react?","description":"Type in how you reacted to this situation.","view":"text","keywords":[],"user_data":{"placeholder":"You can type in your reaction here..."}}],"theme":{"uuid":"96655b12-114c-4b2f-9d37-0287b7adaf2e","color":"038c0d","wallpaper":"TestBackground3.jpg","created_at":"2014-09-26T22:36:57.950Z","updated_at":"2014-09-26T22:36:57.950Z"}}}"""
  def test_users_should_serialize_and_deserialize() = {
    val userData = List(
       Question(
         "f6f7eaf1-a9df-460b-a6a2-8f3ba8a1c91c",
         "How are you feeling?",
         "Move the sliders to indicate your feeling.",
         "sliders",
         List(),
         UserDataSlider( List(
           SliderOption("General feeling", "general", -100, 100, 0),
           SliderOption("Joy", "joy", -100, 100, 0),
           SliderOption("Trust", "trust", -100, 100, 0),
           SliderOption("Fear", "fear", -100, 100, 0),
           SliderOption("Surprise", "surprise", -100, 100, 0),
           SliderOption("Sadness", "sadness", -100, 100, 0),
           SliderOption("Lonely", "lonely", -100, 100, 0),
           SliderOption("Disgust", "disgust", -100, 100, 0),
           SliderOption("Anger", "anger", -100, 100, 0),
           SliderOption("Anticipation", "anticipation", -100, 100, 0)
           )
         )
       ),
       Question(
         "6b399586-023f-440a-a49e-1e866a9dc63c",
         "Describe the circumstances",
         "Type in one or multiple items.",
         "list",
         List(),
         UserDataList(
           autocompletes = true,
           List(
             "Got teased",
             "Bad sleep",
             "Had arguments with someone",
             "Good grade at school",
             "Had a fun converstation"
           )
         )
       ),
       Question(
         "44475546-9eda-4784-bcc9-42428fd3ddb3",
         "How does your body feel?",
         "Describe what you feel and where you feel it.",
         "images",
         List(),
         UserDataImages(
           List(
             ImagesImage(Frame(10, 10, 100, 120), "left_arm", List("My arm hurts", "My arm tingles"), "yolo1.png"),
             ImagesImage(Frame(100, 80, 50, 150), "left_leg", List("My leg hurts", "My leg tingles"), "yolo2.png")
           )
         )
       ),
       Question(
         "594155d6-012e-4fff-959a-c830c3ad4ece",
         "What are your thoughts?",
         "You can type in what your thoughts are.",
         "audio-text",
         List(),
         UserDataAudioText("You can type in your thoughts here...")
       ),
       Question(
         "c96f00b1-37c5-42ba-8e32-794992c13db1",
         "How did you react?",
         "Type in how you reacted to this situation.",
         "text",
         List(),
         UserDataText("You can type in your reaction here...")
       )
     )
    val user = User(
    "45d9e41e-8df6-4e0f-b922-2e6c1e05b115",
     "mark.eibes@googlemail.com",
     "2014-09-26T22:36:58.283Z",
    "2014-09-26T22:36:58.410Z",
     None, //TODO fix this shit
     "male",
     public = false,
     professional = false,
     0,
     0,
     0,
     0,
     0,
     71,
     471,
     Map("achievements" → "http://aryaapp-staging.herokuapp.com/user/achievements",
         "activities" → "http://aryaapp-staging.herokuapp.com/user/activities",
         "activities/unread" → "http://aryaapp-staging.herokuapp.com/user/activities/unread",
         "devices" → "http://aryaapp-staging.herokuapp.com/user/devices",
         "invites" → "http://aryaapp-staging.herokuapp.com/user/invites",
         "invites/pending" → "http://aryaapp-staging.herokuapp.com/user/invites/pending",
         "journals" → "http://aryaapp-staging.herokuapp.com/user/journals",
         "notes" → "http://aryaapp-staging.herokuapp.com/user/notes",
         "themes" → "http://aryaapp-staging.herokuapp.com/user/themes"),
     userData,
    Theme("96655b12-114c-4b2f-9d37-0287b7adaf2e", "038c0d", "TestBackground3.jpg", "2014-09-26T22:36:57.950Z", "2014-09-26T22:36:57.950Z")
    )
    val gu = GetUser(user)

    val eu: Validation[String, GetUser] = exampleUser.decodeValidation[GetUser]
    eu match {
      case Failure(msg) ⇒ Log.e("MOTHER", "Fucked it up " + msg)
      case _ ⇒ Log.e("MOTHER", "Decoded fine.")
    }

    exampleUser.decodeOption[GetUser].get shouldBe gu
//    gu should be (exampleUser.decodeOption[GetUser].get)

//    val decoded = exampleUser.decodeOption[GetUser].get
//    val json = decoded.asJson.nospaces
//    assert(exampleUser.decodeOption[GetUser] == json.decodeOption[GetUser])
  }
}

