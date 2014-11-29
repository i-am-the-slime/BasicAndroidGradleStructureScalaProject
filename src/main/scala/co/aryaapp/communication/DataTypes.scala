package co.aryaapp.communication
import argonaut._, Argonaut._
import co.aryaapp.macros.Macros._

import java.util.{List => JavaList}

import argonaut.CodecJson

case class ThemeContainer(themes:JavaList[Theme])
case class Theme(uuid:String, color:String, wallpaper:String, created_at:String, updated_at:String)

@argonaut case class GetNotes(notes:List[Note]) //Has an s and is a list
@argonaut case class PostNoteResult(notes:Note) //Has a fucking s
@argonaut case class Note(content:String)

case class GetJournals(journals:List[Journal])
case class PostJournal(journal:Journal)
case class PostJournalResult(journals:Journal)

case class PostUser(user:User)
object PostUser {
  implicit def PostUserCodecJson: CodecJson[PostUser] =
    casecodec1(PostUser.apply, PostUser.unapply)("user")
}
case class PostUserResult(users:User)
object PostUserResult {
  implicit def PostUserResultCodecJson: CodecJson[PostUserResult] =
    casecodec1(PostUserResult.apply, PostUserResult.unapply)("users")
}

case class User(email:String, passwordHash:String)
object User {
  implicit def UserCodecJson: CodecJson[User] =
    casecodec2(User.apply, User.unapply)("email", "password_hash")
}

sealed trait Question {
  def typ:String
  def uuid:String
}

object Question{
  implicit def QuestionCodecJson:CodecJson[Question] =
    CodecJson({
      case q: SliderQuestion => q.asJson
      case q: CheckboxesQuestion => q.asJson
      case q: BodyQuestion => q.asJson
      case q: SoundQuestion => q.asJson
      case q: TextQuestion => q.asJson
    } ,
      j => {
        val typ = (j --\ "type").as[String].getOr("motherfucker_you_spell_wrong")
        typ match {
          case SliderQuestion.typ => SliderQuestion.SliderQuestionCodecJson(j).map[Question](identity)
          case TextQuestion.typ => TextQuestion.TextQuestionCodecJson(j).map[Question](identity)
          case CheckboxesQuestion.typ => CheckboxesQuestion.CheckboxesQuestionCodecJson(j).map[Question](identity)
          case BodyQuestion.typ => BodyQuestion.BodyQuestionCodecJson(j).map[Question](identity)
          case SoundQuestion.typ => SoundQuestion.SoundQuestionCodecJson(j).map[Question](identity)
        }
      }
    )
}



case class SliderQuestion(uuid:String, name:String, min:Float, max:Float, typ:String=SliderQuestion.typ) extends Question
object SliderQuestion{
  val typ = "slider_question"
  implicit def SliderQuestionCodecJson: CodecJson[SliderQuestion] =
    casecodec5(SliderQuestion.apply, SliderQuestion.unapply)("uuid", "name", "min", "max", "type")
}

case class TextQuestion(uuid:String, name:String, typ:String=TextQuestion.typ) extends Question
object TextQuestion{
  val typ = "text_question"
  implicit def TextQuestionCodecJson: CodecJson[TextQuestion] =
    casecodec3(TextQuestion.apply, TextQuestion.unapply)("uuid", "name", "type")
}

case class CheckboxesQuestion(uuid:String, name:String, typ:String=CheckboxesQuestion.typ) extends Question
object CheckboxesQuestion{
  val typ = "checkboxes_question"
  implicit def CheckboxesQuestionCodecJson: CodecJson[CheckboxesQuestion] =
    casecodec3(CheckboxesQuestion.apply, CheckboxesQuestion.unapply)("uuid", "name", "type")
}

case class BodyQuestion(uuid:String, name:String, typ:String=BodyQuestion.typ) extends Question
object BodyQuestion {
  val typ = "body_question"
  implicit def BodyQuestionCodecJson: CodecJson[BodyQuestion] =
    casecodec3(BodyQuestion.apply, BodyQuestion.unapply)("uuid", "name", "type")
}
case class SoundQuestion(uuid:String, name:String, typ:String=SoundQuestion.typ) extends Question
object SoundQuestion {
  val typ = "sound_question"
  implicit def SoundQuestionCodecJson: CodecJson[SoundQuestion] =
    casecodec3(SoundQuestion.apply, SoundQuestion.unapply)("uuid", "name", "type")
}

case class JournalPage(uuid:String, title:String, subtitle:String, questions:List[Question])
object JournalPage {
  implicit def JournalPageCodecJson: CodecJson[JournalPage] =
    casecodec4(JournalPage.apply, JournalPage.unapply)("uuid", "title", "subtitle", "questions")
}

case class Journal(uuid:String, createdAt:String, updatedAt:String, pages:List[JournalPage])
object Journal {
  implicit def JournalCodecJson: CodecJson[Journal] =
    casecodec4(Journal.apply, Journal.unapply)("uuid", "created_at", "updated_at", "pages")
}

case class JournalEntry(journalUuid:String, createdAt:String, updatedAt:String, answers:Map[String, Map[String, String]])
object JournalEntry {
  implicit def JournalEntryCodecJson: CodecJson[JournalEntry] =
    casecodec4(JournalEntry.apply, JournalEntry.unapply)("journal_uuid", "created_at", "updated_at", "answers")
}

