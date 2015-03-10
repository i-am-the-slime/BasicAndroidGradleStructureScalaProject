package co.aryaapp.communication
import argonaut._, Argonaut._
import co.aryaapp.communication.DataTypes.Question
import co.aryaapp.macros.Macros.argonaut

object DataTypes {
  @argonaut case class ThemeContainer(themes:List[Theme])
  @argonaut case class Theme(uuid:String, color:String, wallpaper:String, createdAt:String, updatedAt:String)

  @argonaut case class GetNotes(notes:List[Note]) //Has an s and is a list
  @argonaut case class PostNoteResult(notes:Note) //Has a fucking s
  @argonaut case class Note(content:String)

  @argonaut case class GetJournals(journals:List[Journal])
  @argonaut case class PostJournal(journal:Journal)
  @argonaut case class PostJournalResult(journals:Journal)

  @argonaut case class PostUser(user:User)
  @argonaut case class PostUserResult(users:User)
  @argonaut case class GetUser(user:User)

  @argonaut case class PostNewUser(user:NewUser)
  @argonaut case class NewUser(email:String, passwordHash:String)

  @argonaut case class User(
    uuid:String,
    email:String,
    createdAt:String,
    updatedAt:String,
    fullname:Option[String],
    gender:String,
    public:Boolean,
    professional:Boolean,
    achievementsCount:Int,
    activitiesCount:Int,
    activitiesUnreadCount:Int,
    invitesCount:Int,
    invitesPendingCount:Int,
    journalsCount:Int,
    notesCount:Int,
    links:Map[String, String],
    questions:List[Question],
    theme:Theme
    )


  sealed trait UserData

  case class Question(
    uuid:String,
    title:String,
    description:String,
    view:String,
  //  autocompletes:Boolean,
    keywords:List[String],
    userData:UserData
  )


  object Question {
//    def apply(uuid:String, title:String, description:String, view:String, keywords:List[String], userDataString:String): Option[Question] = {
//      val ud = view match {
//        case "sliders" ⇒ userDataString.decodeOption[UserDataSlider]
//        case "list" ⇒ userDataString.decodeOption[UserDataList]
//        case "images" ⇒ userDataString.decodeOption[UserDataImages]
//        case "audio_text" ⇒ userDataString.decodeOption[UserDataAudioText]
//        case "text" ⇒ userDataString.decodeOption[UserDataText]
//        case _ ⇒ None
//      }
//      for (userData ← ud) yield Question(uuid, title, description, view, keywords, userData)
//    }

    def jsonFromUserData(v:String, ud:UserData) = {
      v match {
        case "sliders" ⇒ ud.asInstanceOf[UserDataSlider].asJson
        case "list" ⇒ ud.asInstanceOf[UserDataList].asJson
        case "images" ⇒ ud.asInstanceOf[UserDataImages].asJson
        case "audio-text" ⇒ ud.asInstanceOf[UserDataAudioText].asJson
        case "text" ⇒ ud.asInstanceOf[UserDataText].asJson
      }

    }

    implicit def QuestionEncodeCodecJson:EncodeJson[Question] =
      EncodeJson( (q:Question) ⇒ {
        val userDataJson = jsonFromUserData(q.view, q.userData)
        ("uuid" := q.uuid) ->: ("title" := q.title) ->: ("description" := q.description) ->: ("view" := q.view) ->:
          ("keywords" := q.keywords) ->: ("user_data" := userDataJson) ->: jEmptyObject
//
//        (q.uuid, q.title, q.description, q.view, q.keywords, ud)
//        ("uuid", "title", "description", "view", "keywords", "user_data")
    })

    implicit def QuestionDecodeCodecJson:DecodeJson[Question] = DecodeJson(c ⇒ for {
      uuid ← (c --\ "uuid").as[String]
      title ← (c --\ "title").as[String]
      description ← (c --\ "description").as[String]
      view ← (c --\ "view").as[String]
      keywords ← (c --\ "keywords").as[List[String]]
      userData ← view match {
        case "sliders" ⇒ (c --\ "user_data").as[UserDataSlider]
        case "list" ⇒ (c --\ "user_data").as[UserDataList]
        case "images" ⇒ (c --\ "user_data").as[UserDataImages]
        case "text" ⇒ (c --\ "user_data").as[UserDataText]
        case "audio-text" ⇒ (c --\ "user_data").as[UserDataAudioText]
        case _ ⇒ DecodeResult.fail[UserData]("View field did not match any valid userData type.", c.history)
      }
    } yield Question(uuid, title, description, view, keywords, userData)
    )
  }

  @argonaut case class SliderOption(title:String, identifier:String, minValue:Int, maxValue:Int, defaultValue:Int/*, legend:List[String], steps:Int*/)

  case class UserDataSlider(options:List[SliderOption]) extends UserData
  object UserDataSlider{
    val view = "sliders"
    implicit def UserDataSliderCJ: CodecJson[UserDataSlider] =
      casecodec1(UserDataSlider.apply, UserDataSlider.unapply)("options")
  }

  case object UserDataList extends UserData {
    val view = "list"
    implicit def UserDataListCJ: CodecJson[UserDataList] =
      casecodec2(UserDataList.apply, UserDataList.unapply)("autocompletes", "defaults")
  }
  case class UserDataList(autocompletes:Boolean, defaults:List[String]) extends UserData

  @argonaut case class Frame(x:Int, y:Int, width:Int, height:Int)
  @argonaut case class ImagesImage(frame:Frame, identifier:String, options:List[String], url:String)

  case class UserDataImages(images:List[ImagesImage]) extends UserData
  object UserDataImages {
    val view = "images"
    implicit def UserDataImagesCJ: CodecJson[UserDataImages] =
      casecodec1(UserDataImages.apply, UserDataImages.unapply)("images")
  }

  object UserDataAudioText {
    val view = "audio-text"
    implicit def UserDataAudioTextCJ: CodecJson[UserDataAudioText] =
      casecodec1(UserDataAudioText.apply, UserDataAudioText.unapply)("placeholder")
  }

  case class UserDataAudioText(placeholder:String) extends UserData

  case class UserDataText(placeholder:String) extends UserData

  object UserDataText {
    val view = "text"
    implicit def UserDataTextCJ: CodecJson[UserDataText] =
      casecodec1(UserDataText.apply, UserDataText.unapply)("placeholder")
  }

  @argonaut case class JournalPage(uuid:String, title:String, subtitle:String, questions:List[Question])
  @argonaut case class Journal(uuid:String, createdAt:String, updatedAt:String, pages:List[JournalPage])
  @argonaut case class JournalEntry(journalUuid:String, createdAt:String, updatedAt:String, answers:Map[String, Map[String, String]])

}