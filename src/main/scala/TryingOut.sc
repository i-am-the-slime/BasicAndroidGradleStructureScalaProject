import java.lang.reflect.Type

import co.aryaapp.communication.DataTypes._
import com.google.gson._
import scala.collection.JavaConversions._


val gson = new GsonBuilder()
  .registerTypeAdapter(classOf[Answer], new AnswerDes)
  .create()

val textAnswer = TextAnswer("what_you_felt", "I felt bad")
val question = Question("questionTitle", "questionSubtitle", "slider_answer", List(textAnswer))
val journal = Journal("abc", "now", "later", List(question))

val journalString = gson.toJson(journal)

val backToJournal = gson.fromJson(journalString, classOf[Journal])
val answer = backToJournal.questions(0).answers(0).asInstanceOf[TextAnswer]
answer.name
answer.value

