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

@argonaut case class TestJournal(answers:List[Answer])

abstract class Answer { def typ:String }
case class SliderAnswer(question:String, values:Map[String, Int], typ:String=SliderAnswer.typ) extends Answer
object SliderAnswer{
  val typ = "slider_answer"
  implicit def codec: CodecJson[SliderAnswer] =
    casecodec3(SliderAnswer.apply, SliderAnswer.unapply)("question", "values", "type")
}
case class ListAnswer (question:String, values:List[String], typ:String=ListAnswer.typ) extends Answer
object ListAnswer{
  val typ = "list_answer"
  implicit def codec: CodecJson[ListAnswer] =
    casecodec3(ListAnswer.apply, ListAnswer.unapply)("question", "values", "type")
}
case class TextAnswer(question:String, values:String, typ:String=ListAnswer.typ) extends Answer
object TextAnswer{
  val typ = "text_answer"
  implicit def codec: CodecJson[TextAnswer] =
    casecodec3(TextAnswer.apply, TextAnswer.unapply)("question", "values", "type")
}
case class BodyAnswer(question:String, values:Map[String, List[String]], typ:String=BodyAnswer.typ) extends Answer
object BodyAnswer{
  val typ = "body_answer"
  implicit def codec: CodecJson[BodyAnswer] =
    casecodec3(BodyAnswer.apply, BodyAnswer.unapply)("question", "values", "type")
}

object Answer{
  implicit def AnswerCodecJson:CodecJson[Answer] =
  CodecJson({
    case a:SliderAnswer => a.asJson
    case a:ListAnswer  => a.asJson
    case a:TextAnswer  => a.asJson
    case a:BodyAnswer => a.asJson
  },
  j => {
    val typ = (j --\ "type").as[String].getOr("motherfucker_you_spell_wrong")
    typ match {
      case SliderAnswer.typ => SliderAnswer.codec(j).map[Answer](identity)
      case ListAnswer.typ => ListAnswer.codec(j).map[Answer](identity)
      case TextAnswer.typ => TextAnswer.codec(j).map[Answer](identity)
      case BodyAnswer.typ => BodyAnswer.codec(j).map[Answer](identity)
    }
  }
  )
}

@argonaut case class PostUser(user:User)
@argonaut case class PostUserResult(users:User)

@argonaut case class User(email:String, passwordHash:String)

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

@argonaut case class JournalPage(uuid:String, title:String, subtitle:String, questions:List[Question])

@argonaut case class Journal(uuid:String, createdAt:String, updatedAt:String, pages:List[JournalPage])

@argonaut case class JournalEntry(journalUuid:String, createdAt:String, updatedAt:String, answers:Map[String, Map[String, String]])

