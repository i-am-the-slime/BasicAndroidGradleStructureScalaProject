package co.aryaapp.communication

import java.lang.reflect.Type
import java.util.{List => JavaList}

import argonaut.{Argonaut, CodecJson}
import Argonaut._
import com.google.gson._

object DataTypes {
  case class ThemeContainer(themes:JavaList[Theme])
  object ThemeContainer {
    implicit def ThemeContainerCodecJson: CodecJson[ThemeContainer] =
      casecodec1(ThemeContainer.apply, ThemeContainer.unapply)("themes")
  }
  case class Theme(uuid:String, color:String, wallpaper:String, created_at:String, updated_at:String)

  case class GetNotes(notes:JavaList[Note]) //Has an s and is a list
  case class PostNote(note:Note) //Doesn't have a fucking s
  case class PostNoteResult(notes:Note) //Has a fucking s
  case class Note(content:String)

  case class GetJournals(journals:JavaList[Journal])
  case class PostJournal(journal:Journal)
  case class PostJournalResult(journals:Journal)

  case class PostUser(user:User)
  case class PostUserResult(users:User)
  case class User(email:String, passwordHash:String)

  class RichJsonObject(implicit jsonObject:JsonObject) {
    def getString(key:String) = jsonObject.get(key).getAsString
    def getFloat(key:String) = jsonObject.get(key).getAsFloat
  }
  // Journal
  abstract class Answer(val answer_type:String)
  case class SliderAnswers(sliderAnswers:java.util.List[SliderAnswer]) extends Answer("slider_answers")
  case class SliderAnswer(name:String, value:Float, min:Float, max:Float)
  case class TextAnswer(name:String, value:String) extends Answer("text_answer")

  case class Question(title:String, subtitle:String, answerType:String, answer:Answer)

  case class Journal(uuid:String, created_at:String, updated_at:String, questions:JavaList[Question]) {
    def addQuestion(q:Question) = this.copy(questions = this.questions.)
  }

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

  class AnswerDeSerialiser extends AbstractSuperTypeDeSerialiser[Answer] {
    override val typeFieldName: String = "answer_type"
    override val classOfMappings: Map[String, Type] = Map(
      "slider_answers" -> classOf[SliderAnswer],
      "text_answer" -> classOf[TextAnswer]
    )
    override def typeFieldGetter: (Answer) => String = (a:Answer) => a.answer_type
  }
}
