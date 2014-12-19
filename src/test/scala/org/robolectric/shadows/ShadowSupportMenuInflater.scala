package org.robolectric.shadows

import android.support.v7.internal.view.SupportMenuInflater
import android.view.Menu
import org.robolectric.annotation.{Implementation, Implements}

@Implements(classOf[SupportMenuInflater])
class ShadowSupportMenuInflater extends ShadowMenuInflater{
  @Implementation
  override def inflate(menuRes:Int, menu:Menu): Unit = {
    super.inflate(menuRes, menu)
  }
}
