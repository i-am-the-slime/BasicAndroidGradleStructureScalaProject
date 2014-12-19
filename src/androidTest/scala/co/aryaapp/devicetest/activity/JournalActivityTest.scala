package co.aryaapp.devicetest.activity

import android.graphics.drawable.ColorDrawable
import android.test.suitebuilder.annotation.MediumTest
import android.test.{TouchUtils, ActivityInstrumentationTestCase2, AndroidTestCase}
import co.aryaapp.communication.TestJournal
import co.aryaapp.{TypedResource, TR, R}
import TypedResource._
import co.aryaapp.journal.JournalActivity
import co.aryaapp.devicetest.helper.KeyboardUtils
import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.AssertionsForJUnit

class JournalActivityTest extends ActivityInstrumentationTestCase2[JournalActivity](classOf[JournalActivity])
                with AssertionsForJUnit
                with Matchers {

  implicit val testCase = this

  override def tearDown(): Unit = {
    getActivity.finish()
  }

  def test_all():Unit = {
    val a = getActivity
    while(a.isThereNextPagerItem(a.pager, a.adapter))
      a.nextButton.performClick()
    val confirmButton = a.findView(TR.btn_confirm)
    val journal = a.adapter.saveJournals()
    journal should not be TestJournal(List())
  }

//  def test_isEmailValid_is_false_on_email_addresses_without_an_AT_sign():Unit = {
//    getActivity.isEmailValid("no.man.com") shouldBe false
//  }
//
//  def test_isEmailValid_is_false_on_email_addresses_without_a_full_stop(): Unit = {
//    getActivity.isEmailValid("hey@ye") shouldBe false
//  }
//
//  def test_isEmailValid_is_true_on_valid_email_addresses(): Unit = {
//    getActivity.isEmailValid("no@shit.sherlock") shouldBe true
//  }
//
//  @MediumTest
//  def test_entering_an_invalid_email_address_and_then_losing_focus_should_change_the_background_colour_to_red(): Unit = {
//    val emailTextField = getActivity.findView(TR.email)
//    val passwordTextField = getActivity.findView(TR.password)
//    val red = getActivity.getResources.getColor(R.color.red)
//
//    TouchUtils.clickView(this, emailTextField)
//    sendKeys("not an email address")
//    TouchUtils.clickView(this, passwordTextField)
//
//    val colour = emailTextField.getBackground.asInstanceOf[ColorDrawable].getColor
//    colour shouldBe red
//  }
//
//  @MediumTest
//  def test_entering_a_valid_email_address_when_the_field_is_already_red_should_turn_it_grey_again(): Unit = {
//    val emailTextField = getActivity.findView(TR.email)
//    val passwordTextField = getActivity.findView(TR.password)
//    val red = getActivity.getResources.getColor(R.color.red)
//    val grey = getActivity.getResources.getColor(R.color.grey)
//    KeyboardUtils.writeKeys("fuck")
//    TouchUtils.clickView(this, passwordTextField)
//    val colourBefore = emailTextField.getBackground.asInstanceOf[ColorDrawable].getColor
//    colourBefore shouldBe red
//    emailTextField.getText.toString shouldBe "fuck"
//    TouchUtils.clickView(this, emailTextField)
//    KeyboardUtils.pressBackspace(4)
//    emailTextField.getText.toString shouldBe ""
//    KeyboardUtils.writeKeys("mother@fucker.com")
//    TouchUtils.clickView(this, passwordTextField)
//    val colour = emailTextField.getBackground.asInstanceOf[ColorDrawable].getColor
//    colour shouldBe grey
//  }
//
//  def test_when_given_a_valid_token_it_shall_finish_with_an_OK_code(): Unit = {
//    val token = "hey"
//    getActivity.loginSuccessful(token)
//  }

}
