package co.aryaapp.robolectrictest

import org.robolectric.annotation.Config
import org.robolectric.res.Fs
import org.robolectric.{AndroidManifest, RobolectricTestRunner}

/**
 * Created by mark on 17.12.14.
 */
class RobolectricSbtTestRunner(testClass:Class[_]) extends RobolectricTestRunner(testClass) {
  val maxSdk = 18

  override def getAppManifest(config: Config): AndroidManifest = {
    val manifestPath = "src/main/AndroidManifest.xml"
    val resPath = "src/main/res/"
    val assetsPath = "src/main/assets"
    def file(s:String) = Fs.fileFromPath(s)
    new AndroidManifest(file(manifestPath), file(resPath), file(assetsPath)){
      override def getTargetSdkVersion = maxSdk
    }
  }
}
