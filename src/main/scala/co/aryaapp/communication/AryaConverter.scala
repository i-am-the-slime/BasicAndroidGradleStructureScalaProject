package co.aryaapp.communication

import java.lang.reflect.Type
import co.aryaapp.Shit
import retrofit.converter.{ConversionException, GsonConverter, Converter}
import retrofit.mime.{TypedString, TypedInput, TypedOutput}
import argonaut._, Argonaut._

class AryaConverter extends Converter {

  override def fromBody(typedInput: TypedInput, typ: Type): AnyRef = {
    val inputString = scala.io.Source.fromInputStream(typedInput.in()).mkString
    val shitClass = classOf[Shit]
    val jsonOpt = typ match {
      case `shitClass` =>  inputString.decodeOption[Shit]
    }
    jsonOpt.fold(throw new ConversionException(s"Could not convert: $inputString"))(identity)
  }

  override def toBody(o: scala.Any): TypedOutput = {
    new TypedString("NEIN!")
  }
}

