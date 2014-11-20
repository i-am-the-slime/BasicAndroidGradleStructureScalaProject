package co.aryaapp.persistence

import co.aryaapp.communication.{QuestionDeSerialiser, Question}
import com.google.gson.{GsonBuilder, FieldNamingPolicy}

/**
 * Created by mark on 14.11.14.
 */
object AryaGson {
  def apply() = new GsonBuilder()
                 .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                 .registerTypeAdapter(classOf[Question], new QuestionDeSerialiser)
                 .create()
}
