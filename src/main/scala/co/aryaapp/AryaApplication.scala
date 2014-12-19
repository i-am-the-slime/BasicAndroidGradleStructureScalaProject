package co.aryaapp

import android.app.Application
import android.content.Context
import android.view.Display
import uk.co.chrisjenx.calligraphy.{CalligraphyContextWrapper, CalligraphyConfig}

object AryaApplication {
}

class AryaApplication extends Application{
  override def onCreate() {
    super.onCreate()
    CalligraphyConfig.initDefault("fonts/osreg.ttf", R.attr.fontPath)
  }
}