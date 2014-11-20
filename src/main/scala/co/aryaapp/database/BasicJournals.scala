package co.aryaapp.database

/**
 * Created by mark on 13.11.14.
 */

import co.aryaapp.communication._
import org.joda.time.DateTime

object BasicJournals {
  var uuid = 1
  def getUuid:String = { uuid += 1; uuid.toString }

  def createBasicJournal() = {
    val now = DateTime.now().toString
    Journal(getUuid, now, now, List(
      JournalPage(getUuid, "Wie fühlst du dich?", "Bewege den Regler, um dein Gefühl einzustellen", List(
        SliderQuestion(getUuid, "General feeling", 0.0f, 100.0f),
        SliderQuestion(getUuid, "Freude", 0.0f, 100.0f),
        SliderQuestion(getUuid, "Vertrauen", 0.0f, 100.0f),
        SliderQuestion(getUuid, "Angst", 0.0f, 100.0f),
        SliderQuestion(getUuid, "Überraschung", 0.0f, 100.0f),
        SliderQuestion(getUuid, "Traurigkeit", 0.0f, 100.0f),
        SliderQuestion(getUuid, "Einsamkeit", 0.0f, 100.0f),
        SliderQuestion(getUuid, "Ekel", 0.0f, 100.0f),
        SliderQuestion(getUuid, "Zorn", 0.0f, 100.0f),
        SliderQuestion(getUuid, "Vorfreude", 0.0f, 100.0f))
      ),
      JournalPage(getUuid, "Was ist passiert?", "Wähle eine oder mehrere Optionen", List(
        CheckboxesQuestion(getUuid, "Gib ein, was passiert ist!")
      )),
      JournalPage(getUuid, "Wie hast du reagiert?", "Trage hier ein, wie du auf die Situation reagiert hast", List(
        TextQuestion(getUuid, "Hier kannst du deine Reaktion eingeben...")
      )),
      JournalPage(getUuid, "Wie reagierte dein Körper?", "Berühre den entsprechenden Bereich", List(
        BodyQuestion(getUuid, "Drück drauf!")
      )),
      JournalPage(getUuid, "Was denkst du?", "benutze den Button um deine Gedanken einzugeben", List(
        TextQuestion(getUuid, "Hier kannst du deinen Gedanken eingeben"),
        SoundQuestion(getUuid, "Einen Text aufnehmen")
      ))
    )
    )
  }
}
