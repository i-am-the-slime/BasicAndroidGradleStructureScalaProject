package co.aryaapp.communication
import argonaut._, Argonaut._

import java.lang.reflect.Type
import java.util.{List => JavaList}

import argonaut.CodecJson
import com.google.gson._
import com.google.gson.{JsonObject => GsonObject}

//def funky(name:String, args:String*) = {
//val cc = "casecodec"+args.length
//val nameJson = name+"CodecJson"
//val argstring = args.mkString("\", \"")
//s"""object $name {
//  implicit def $nameJson: CodecJson[$name] =
//    $cc($name.apply, $name.unapply)("$argstring")
//}"""
//}

case class ThemeContainer(themes:JavaList[Theme])
case class Theme(uuid:String, color:String, wallpaper:String, created_at:String, updated_at:String)

case class GetNotes(notes:List[Note]) //Has an s and is a list
object GetNotes { implicit def NotesCodecJson: CodecJson[GetNotes] = casecodec1(GetNotes.apply, GetNotes.unapply)("notes") }
case class PostNote(note:Note) //Doesn't have a fucking s
object PostNote { implicit def PostNoteCodecJson: CodecJson[PostNote] = casecodec1(PostNote.apply, PostNote.unapply)("note")}
case class PostNoteResult(notes:Note) //Has a fucking s
object PostNoteResult{ implicit def PostNoteResultCodecJson: CodecJson[PostNoteResult] = casecodec1(PostNoteResult.apply, PostNoteResult.unapply)("notes")}
case class Note(content:String)
object Note{ implicit def NoteCodecJson: CodecJson[Note] = casecodec1(Note.apply, Note.unapply)("content") }

case class GetJournals(journals:List[Journal])
case class PostJournal(journal:Journal)
case class PostJournalResult(journals:Journal)

case class PostUser(user:User)
case class PostUserResult(users:User)
case class User(email:String, passwordHash:String)

class RichJsonObject(implicit jsonObject:GsonObject) {
  def getString(key:String) = jsonObject.get(key).getAsString
  def getFloat(key:String) = jsonObject.get(key).getAsFloat
}


abstract class Question(val question_type:String)
case class SliderQuestion(uuid:String, name:String, min:Float, max:Float) extends Question("slider_question")
object SliderQuestion{
  implicit def SliderQuestionCodecJson: CodecJson[SliderQuestion] =
    casecodec4(SliderQuestion.apply, SliderQuestion.unapply)("uuid", "name", "min", "max")
}

case class TextQuestion(uuid:String, name:String) extends Question("text_question")
object TextQuestion{
  implicit def TextQuestionCodecJson: CodecJson[TextQuestion] =
    casecodec2(TextQuestion.apply, TextQuestion.unapply)("uuid", "name")
}

case class CheckboxesQuestion(uuid:String, name:String) extends Question("checkboxes_question")
object CheckboxesQuestion{
  implicit def CheckboxesQuestionCodecJson: CodecJson[CheckboxesQuestion] =
    casecodec2(CheckboxesQuestion.apply, CheckboxesQuestion.unapply)("uuid", "name")
}
case class BodyQuestion(uuid:String, name:String) extends Question("body_question")
object BodyQuestion {
  implicit def BodyQuestionCodecJson: CodecJson[BodyQuestion] =
    casecodec2(BodyQuestion.apply, BodyQuestion.unapply)("uuid", "name")
}
case class SoundQuestion(uuid:String, name:String) extends Question("sound_question")
object SoundQuestion {
  implicit def SoundQuestionCodecJson: CodecJson[SoundQuestion] =
    casecodec2(SoundQuestion.apply, SoundQuestion.unapply)("uuid", "name")
}

case class JournalPage(uuid:String, title:String, subtitle:String, questions:List[Question])
object JournalPage {
  implicit def JournalPageCodecJson: CodecJson[JournalPage] =
    casecodec4(JournalPage.apply, JournalPage.unapply)("uuid", "title", "subtitle", "questions")
}

case class Journal(uuid:String, created_at:String, updated_at:String, pages:List[JournalPage])
object Journal {
  implicit def JournalCodecJson: CodecJson[Journal] =
    casecodec4(Journal.apply, Journal.unapply)("uuid", "created_at", "updated_at", "pages")
}

                                         // journalPageUuid to (questionUuid to Answer)
case class JournalEntry(journalUuid:String, createdAt:String, updatedAt:String, answers:Map[String, Map[String, String]])


trait AbstractSuperTypeDeSerialiser[A] extends JsonSerializer[A] with JsonDeserializer[A] {
  val typeFieldName:String
  val classOfMappings:Map[String, Type]
  def typeFieldGetter:A => String

  override def deserialize(jsonElement: JsonElement, `type`: Type, jsonDeserializationContext: JsonDeserializationContext): A = {
    val json = jsonElement.getAsJsonObject
    val key = json.get(typeFieldName).getAsString
    val clazzOf = classOfMappings(key)
    jsonDeserializationContext.deserialize(json, clazzOf)
  }

  override def serialize(t: A, `type`: Type, jsonSerializationContext: JsonSerializationContext): JsonElement = {
    val clazzOf = classOfMappings(typeFieldGetter(t))
    jsonSerializationContext.serialize(t, clazzOf)
  }
}

class QuestionDeSerialiser extends AbstractSuperTypeDeSerialiser[Question] {
  override val typeFieldName: String = "question_type"
  override val classOfMappings: Map[String, Type] = Map(
    "slider_question" -> classOf[SliderQuestion],
    "text_question" -> classOf[TextQuestion],
    "checkboxes_question" -> classOf[CheckboxesQuestion],
    "body_question" -> classOf[CheckboxesQuestion],
    "sound_question" -> classOf[SoundQuestion]
  )
  override def typeFieldGetter: (Question) => String = (a:Question) => a.question_type
}
