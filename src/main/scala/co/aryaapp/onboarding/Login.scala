package co.aryaapp.onboarding

import java.net.UnknownHostException

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.{ViewGroup, LayoutInflater, View}
import android.widget.EditText
import co.aryaapp.TypedResource._
import co.aryaapp._
import co.aryaapp.communication.TokenGetter
import co.aryaapp.communication.TokenGetter.InvalidEmailOrPassword
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp.helpers.Animations

import scala.util.{Failure, Success}

class Login extends OnboardingFragment{
  lazy val email = getView.findView(TR.email)
  lazy val password = getView.findView(TR.password)
  lazy val errorLabel = getView.findView(TR.error_message)
  lazy val loginButton = getView.findView(TR.login_button)
  lazy val progressBar = getView.findView(TR.progress_bar)
  lazy val forgotLabel = getView.findView(TR.forgot_label)


  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    inflater.inflate(R.layout.frag_onboarding_login, container, false)
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    loginButton.onClick(loginPressed())
    makeLabelClickable()
    email.setOnFocusChangeListener( (v: View, focused: Boolean) =>
      if(focused)
        errorLabel.setVisibility(View.GONE)
      else
      if(!focused) changeInvalidEditTextToRed(v.asInstanceOf[EditText], isEmailValid)
    )
  }

  def makeLabelClickable() = {
    forgotLabel.setMovementMethod(LinkMovementMethod.getInstance())
  }

  def changeInvalidEditTextToRed(v:EditText, validate:String => Boolean) = {
    val text = v.getText.toString
    if (validate(text))
      v.setBackgroundResource(R.color.grey)
    else
      v.setBackgroundResource(R.color.red)
  }

  def loginPressed() = {
    progressBar.setVisibility(View.VISIBLE)
    if(isEmailValid(email.getText.toString)){
      loginButton.startAnimation(
        Animations.cartoonDashAway(0, (_) => {
          loginButton.setVisibility(View.INVISIBLE)
          hideKeyboard(loginButton)
          TokenGetter.getToken(email.getText.toString, password.getText.toString).onComplete {
            case Success(someToken) =>
              loginSuccessful(someToken)
            case Failure(f) =>
              loginFailed(f)
          }
        }
      ))
    } else {
      email.startAnimation(Animations.wiggle(0))
    }
  }

  def loginSuccessful(token:String) = {
    activity.loginSuccessful(token)
  }

  def loginFailed(message:Throwable) = {
    val errorMessage = message match {
      case invalid:InvalidEmailOrPassword =>
        "E-Mail or password was wrong. Please correct them and try again."
      case uhe:UnknownHostException =>
        "Please check your internet connection and try again."
//      case _ => "Unknown Error"
    }

    activity.drawUi(() => {
      errorLabel.setText(errorMessage)
      errorLabel.setVisibility(View.VISIBLE)
      loginButton.startAnimation(Animations.moveInFromRight(3000, 600, 0).andThen(_ =>
        activity.drawUi(() => progressBar.setVisibility(View.INVISIBLE))))
      loginButton.setVisibility(View.VISIBLE)
    })
  }

  def isEmailValid(email:String):Boolean = {
    email.matches("\\S+@\\S+\\.\\S+")
  }
}
