package co.aryaapp

//import android.graphics.drawable.ColorDrawable
//import android.test._
//import android.test.suitebuilder.annotation.MediumTest
//import android.view.KeyEvent
//import co.aryaapp.onboarding.Login
//import org.scalatest.{FlatSpec, Matchers}
//import org.scalatest.junit.{JUnitRunner, ShouldMatchersForJUnit, AssertionsForJUnit}
//import co.aryaapp.helpers.AndroidConversions._
//import TypedResource._

//class LoginInstrumentationTest extends ActivityInstrumentationTestCase2[Login](classOf[Login])
//                with AssertionsForJUnit
//                with Matchers
//{

//  implicit val testCase = this
//
//  override def tearDown(): Unit = {
//    getActivity.finish()
//  }
//
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

//}

