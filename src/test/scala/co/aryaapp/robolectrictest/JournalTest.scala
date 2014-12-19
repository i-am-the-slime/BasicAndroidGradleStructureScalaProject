package co.aryaapp.robolectrictest

import co.aryaapp.TR
import co.aryaapp.communication.TestJournal
import co.aryaapp.helpers.AndroidConversions
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp.journal.JournalActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation._
import org.robolectric.shadows.ShadowSupportMenuInflater
import org.scalatest.Matchers
import org.scalatest.junit.AssertionsForJUnit

@RunWith(classOf[RobolectricSbtTestRunner])
@Config(reportSdk = 18, emulateSdk = 18, shadows = Array(classOf[ShadowSupportMenuInflater]))
class JournalTest extends AssertionsForJUnit with Matchers {

  @Test def test_pressing_next_should_create_an_empty_journal() = {
    val a:JournalActivity = Electrician.makeActivity[JournalActivity]
    while(a.isThereNextPagerItem(a.pager, a.adapter))
      a.nextButton.performClick()
    val confirmButton = a.findView(TR.btn_confirm)
    val journal = a.adapter.saveJournals()
    journal should not be TestJournal(List())
  }
  
}
