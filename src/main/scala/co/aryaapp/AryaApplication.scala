package co.aryaapp

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

object AryaApplication {
}

class AryaApplication extends Application{
  override def onCreate():Unit = {
    super.onCreate()
    CalligraphyConfig.initDefault("fonts/osreg.ttf", R.attr.fontPath)
  }
}