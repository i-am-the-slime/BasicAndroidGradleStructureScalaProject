package co.aryaapp.robolectrictest

import android.app.Activity
import org.robolectric.Robolectric
import org.robolectric.util.ActivityController

import scala.reflect.ClassTag

object Electrician extends Robolectric {

  def makeActivity[A <: Activity](implicit ct:ClassTag[A]):ActivityController[A] =
    Robolectric.buildActivity(ct.runtimeClass.asInstanceOf[Class[A]])
}
