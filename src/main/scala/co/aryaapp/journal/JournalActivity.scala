package co.aryaapp.journal

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.View
import android.view.animation.Animation
import co.aryaapp.TypedResource._
import co.aryaapp.communication.DataTypes.{Journal, JournalPage}
import co.aryaapp.database.{SlickDatabase, AryaDB}
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp.helpers._
import co.aryaapp.java.MaterialMenuDrawable
import co.aryaapp.java.MaterialMenuDrawable.Stroke
import co.aryaapp.journal.fragments._
import co.aryaapp.{R, TR}

import scala.language.postfixOps
import scalaz.syntax.std.option._

class JournalActivity extends AryaBaseActivity with SlideIn with SlideOut{
  lazy val containerId = R.id.fragment_container
  lazy val fragmentContainer = this.findView(TR.fragment_container)
  lazy val closeButton = this.findView(TR.closeButton)
  lazy val nextButton = this.findView(TR.nextButton)
  lazy val nextButtonDrawable = new MaterialMenuDrawable(this, getResources.getColor(R.color.white), Stroke.THIN)
  lazy val pixelsToMove = getScreenWidth - nextButtonDefaultX + nextButton.getWidth

  var nextButtonDefaultX = 0f

  var answers = Map[Int, String]()

  lazy val pages = Array(
    () ⇒ new HowAreYouFeelingFragment,
    () ⇒ new WhatHappenedFragment,
    () ⇒ new HowDidYouReactFragment,
    () ⇒ new HowDidYourBodyReactFragment,
    () ⇒ new WhatAreYouThinkingFragment,
    () ⇒ new DoneFragment )

  def currentFragment:JournalBaseFragment = getSupportFragmentManager.getFragments.get(0).asInstanceOf[JournalBaseFragment]
  def currentPosition = currentFragment.getTag.toInt

  override def onSaveInstanceState(outState: Bundle): Unit = {
    //TODO: Store JSON of questions/pages
    //TODO: Store their respective answers
    super.onSaveInstanceState(outState)
  }

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    //TODO: Get the questions from the saved instance state
    //TODO: Get their answers
    //Setup View Pager for the fragments and its adapter
    setContentView(R.layout.activity_journal)
    closeButton.setOnClickListener((_: View) ⇒ finish())
    setupNextButton()
    val baseFrag = pages(0)()
    getSupportFragmentManager.beginTransaction().add(containerId, baseFrag, "0").commit()
    ()
  }

  def setupNextButton() = {
    nextButton setOnClickListener nextButtonListener
    nextButton setBackground nextButtonDrawable
    nextButtonDefaultX = nextButton getX
  }
  
  val nextButtonListener = (nb:View) ⇒ {
    saveCurrentAnswersToMap()
    goToNextPagerItem()
  }

  def saveCurrentAnswersToMap() = {
    val pos = currentPosition
    val frag = currentFragment
    for ((k, v) ← answers) Log.e("Mother", s"key: $k  value: $v")
    pos match {
      case last if last == (pages.length - 1) ⇒
        val db = new SlickDatabase()
        db.addAnswers(answers.values.toList)
//        TODO: Maybe trigger sending it to the Server or handle that in the DB
        finish()

      case p if p < (pages.length - 1) ⇒
        for (answer ← frag.getAnswerFromView) {
          answers = answers.updated(pos, answer)
        }
    }
  }

  override def onBackPressed() = {
    val hasGoneBack = goToPreviousPagerItem()
    if( !hasGoneBack ) finish()
  }

  sealed trait PositionTransition{ def before:Int; def after:Int }
  case class BackwardsTransition(before:Int, after:Int) extends PositionTransition
  case class ForwardsTransition(before:Int, after:Int) extends PositionTransition
  case class NoTransition(position:Int) extends PositionTransition {
    override def before = position
    override def after = position
  }

  def titleForFragment(frag:JournalBaseFragment):String = {
    getResources getString frag.title
  }

  def setCurrentItemFromPosition(positionTransition:PositionTransition):Boolean = {
    def transaction(pos:Int) = {
      val frag = pages(pos)()
      getSupportFragmentManager.beginTransaction().replace(containerId, frag, String.valueOf(pos))
    }
    val transactionOpt:Option[FragmentTransaction] = positionTransition match {
      case ForwardsTransition(_, after) ⇒
        transaction(after).some
      case BackwardsTransition(_, after) ⇒
        transaction(after).some
      case _ ⇒ None
    }
    for(ta ← transactionOpt) {
      ta.commit()
      if(positionTransition.after == pages.length-1)
        nextButtonDrawable.animateIconState(MaterialMenuDrawable.IconState.CHECK, false)
      else if(positionTransition.before == pages.length-1 && positionTransition.after == pages.length-2)
        nextButtonDrawable.animateIconState(MaterialMenuDrawable.IconState.ARROW, false)
    }
    transactionOpt isDefined
  }

  def goToNextPagerItem():Boolean = {
    val positionTransition:PositionTransition =
      currentPosition match {
        case pos if (pos >= 0) && (pos < pages.length-1) ⇒
          ForwardsTransition(pos, pos + 1)
        case pos ⇒ NoTransition(pos)
      }
    setCurrentItemFromPosition(positionTransition)
  }

  def goToPreviousPagerItem():Boolean = {
    val positionTransition:PositionTransition =
      currentPosition match {
        case pos if (pos > 0) && (pos < pages.length) ⇒
          BackwardsTransition(pos, pos - 1)
        case pos ⇒ NoTransition(pos)
      }
    setCurrentItemFromPosition(positionTransition)
  }

  def animateNextButtonIn() = {
    animateNextButton( (deltaX: Float, duration:Long) ⇒
      Animations.moveInFromRight(deltaX, duration, 0)
    )
  }

  private def animateNextButton(animation:(Float, Long) ⇒  Animation) = {
    val anim = animation(pixelsToMove, 500)
    anim.setFillAfter(true)
    nextButton.startAnimation(anim)
  }
  
  def animateNextButtonOut() = {
    animateNextButton( (deltaX:Float, duration:Long) ⇒
      Animations.moveOutToRight(deltaX, duration, 0)
    )
  }


  /*
    override def destroyItem(container: ViewGroup, position: Int, frag: scala.Any): Unit = {
    val f = frag.asInstanceOf[JournalBaseFragment]
    updateAnswersFromFragment(f, position)
    super.destroyItem(container, position, frag)
  }

  def updateAnswersFromFragment(f:JournalBaseFragment, position:Int) = {
   val answer = f.getAnswerFromView
   answer.foreach( a => answers = answers + (position -> a))
  }

  var answers = Map[Int, Answer]()

  def saveJournals():TestJournal = {
    for{
      position <- 0 until getCount-1
      frag <- findFragmentByPosition(position) if frag != null
    } updateAnswersFromFragment(frag, position)
    val theAnswers = answers.values
    val answer = TestJournal(theAnswers.toList)
    answer
  }

  def findFragmentByPosition(position:Int):Try[JournalBaseFragment] = {
    val itemId = getItemId(position)
    Try(fm.findFragmentByTag("android:switcher:"+"viewPagerId"+":"+itemId).asInstanceOf[JournalBaseFragment])
  }

  override def getCount: Int = 6

  override def getItem(position: Int): Fragment = {
    val frag = position match {
      case 0 => new HowAreYouFeelingFragment
      case 1 => new WhatHappenedFragment
      case 2 => new HowDidYouReactFragment
      case 3 => new HowDidYourBodyReactFragment
      case 4 => new WhatAreYouThinkingFragment
      case 5 => new DoneFragment(this)
    }
    answers.get(position).foreach(frag.populateViewFromAnswer)
    frag
  }
   */
}
