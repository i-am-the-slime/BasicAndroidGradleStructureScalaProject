package co.aryaapp.robolectrictest

import android.app.Activity
import org.robolectric.Robolectric

import scala.reflect.ClassTag

object Electrician extends Robolectric {

  def makeActivity[A <: Activity](implicit ct:ClassTag[A]):A =
    Robolectric.buildActivity(ct.runtimeClass.asInstanceOf[Class[A]]).create().get
}
