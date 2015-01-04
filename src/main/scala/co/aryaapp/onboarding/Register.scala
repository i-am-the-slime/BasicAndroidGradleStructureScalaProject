package co.aryaapp.onboarding

import java.net.UnknownHostException

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.{TextView, EditText}
import co.aryaapp.communication._
import co.aryaapp.communication.TokenGetter.InvalidEmailOrPassword
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp._
import TypedResource._
import co.aryaapp.helpers.Animations

import scala.util.{Try, Failure, Success}

class Register extends OnboardingFragment{
  lazy val email = getView.findView(TR.registerEmail)
  lazy val password = getView.findView(TR.registerPassword)
  lazy val repeatPassword = getView.findView(TR.repeatPassword)
  lazy val errorLabel = getView.findView(TR.registerError)
  lazy val createAccountButton = getView.findView(TR.createAccountButton)
  lazy val termsCheckBox = getView.findView(TR.termsCheckBox)
  lazy val termsCheckBoxLabel = getView.findView(TR.termsCheckBoxLabel)
  lazy val progressBar = getView.findView(TR.registerProgress)


  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    inflater.inflate(R.layout.frag_onboarding_register, container, false)
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    createAccountButton.onClick(createPressed())
    makeLabelClickable(termsCheckBoxLabel)
    email.setOnFocusChangeListener( (v: View, focused: Boolean) =>
      if(focused)
        errorLabel.setVisibility(View.GONE)
      else
        if(!focused) changeInvalidEditTextToRed(v.asInstanceOf[EditText], isEmailValid)
    )
    val parent = view.getParent.asInstanceOf[View]
  }

  def makeLabelClickable(label:TextView) = {
    label.setMovementMethod(LinkMovementMethod.getInstance())
  }

  def changeInvalidEditTextToRed(v:EditText, validate:String => Boolean) = {
    val text = v.getText.toString
    if (validate(text))
      v.setBackgroundResource(R.color.grey)
    else
      v.setBackgroundResource(R.color.red)
  }

  def createPressed() = {
    val errors = fieldErrors()
    if(errors.isEmpty) {
      progressBar.setVisibility(View.VISIBLE)
      createAccountButton.startAnimation(
        Animations.cartoonDashAway(0, (_) => {
          createAccountButton.setVisibility(View.INVISIBLE)
          hideKeyboard(createAccountButton)
          val userToCreate = PostUser( User(email.getText.toString, hashPassword(password.getText.toString)) )
          val createdUser = Try( new RestClient(None).postToServer[PostUser, PostUserResult]("/users", userToCreate) )
          createdUser match {
            case Success(user) =>
              TokenGetter.getToken(email.getText.toString, password.getText.toString).onComplete{
                case Success(token) => activity.loginSuccessful(token)
                case Failure(t) => loginFailed(t)
              }
            case Failure(t) =>  registrationFailed(t)
          }
        }
        ))
    } else {
      errorLabel.setVisibility(View.VISIBLE)
      errorLabel.setText(errors.mkString("\n"))
      Log.e("Mother", "Something wrong")
    }
  }

  def fieldErrors():List[String] = {
    List(
      (termsCheckBox.isChecked, "You must agree to the terms if you want to use Arya"),
      (isEmailValid(email.getText.toString), "Email address is invalid"),
      (isPasswordValid(password.getText.toString), "Password needs to contain an upper case, a lower case and a number. It must be at least 8 characters long"),
      (password.getText.toString == repeatPassword.getText.toString, "Passwords do not match.")
    ).flatMap{
      case (check:Boolean, error:String) => checkAndError(check, error)
    }
  }

  def checkAndError(check:Boolean, error:String):List[String] = {
    if(check) Nil else error :: Nil
  }

  def isPasswordValid(password:String) = {
    password.length > 7 &&
      password.intersect("ABCDEFGHIJKLMNOPQRSTUVWXYZ").toSet != Set() &&
      password.intersect("abcdefghijklmnopqrstuvwxyz").toSet != Set() &&
      password.intersect("0123456789").toSet != Set()
  }

  def hashPassword(password:String):String = password

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
      createAccountButton.startAnimation(Animations.moveInFromRight(3000, 600, 0).andThen(_ =>
        activity.drawUi(() => progressBar.setVisibility(View.INVISIBLE))))
      createAccountButton.setVisibility(View.VISIBLE)
    })
  }

  def registrationFailed(message:Throwable) = {
    //TODO simplify and shove some stuff into something more reusable (it's used in frag_onboarding_login failed in here and Login)
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
      createAccountButton.startAnimation(Animations.moveInFromRight(3000, 600, 0).andThen(_ =>
        activity.drawUi(() => progressBar.setVisibility(View.INVISIBLE))))
      createAccountButton.setVisibility(View.VISIBLE)
    })
  }

  def isEmailValid(email:String):Boolean = {
    email.matches("\\S+@\\S+\\.\\S+")
  }
}
