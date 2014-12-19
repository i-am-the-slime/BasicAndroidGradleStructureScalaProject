package co.aryaapp.journal.fragments

import android.graphics._
import android.graphics.drawable.{TransitionDrawable, BitmapDrawable}
import android.os.Bundle
import android.support.v7.widget.LinearLayoutCompat
import android.util.Log
import android.view.View.OnTouchListener
import android.view.{MotionEvent, View, ViewGroup, LayoutInflater}
import android.widget.{CheckBox, ImageView}
import co.aryaapp.communication.{BodyAnswer, Answer}
import co.aryaapp.{TR, TypedResource, R}
import co.aryaapp.helpers.AndroidConversions
import co.aryaapp.journal.JournalBaseFragment
import AndroidConversions._
import TypedResource._
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.{SimpleCallback, Callback}

import scala.collection.concurrent.TrieMap
import scala.util.Try
import scalaz.{Isomorphism, IsomorphismsLow0}
import BodyModel._

class HowDidYourBodyReactFragment extends
      JournalBaseFragment(
        R.drawable.ic_body,
        R.string.frag_how_did_your_body_react_title,
        R.string.frag_how_did_your_body_react_subtitle
      ){

  def getTouchImage(v:View) = v.findView(TR.touch_image)

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val view = getActivity.getLayoutInflater.inflate(R.layout.frag_journal_body, container, false)
    //Set it up such that when clicking on body picture we check the
    val touchImage = getTouchImage(view)
    val referenceImage = view.findView(TR.colour_image)
    val otl = getOnTouchListenerForImageView(touchImage, referenceImage)
    touchImage.setOnTouchListener(otl)
    view
  }

  def getOnTouchListenerForImageView(view:ImageView, reference:ImageView):OnTouchListener = new OnTouchListener {
    override def onTouch(v: View, e: MotionEvent): Boolean = {
      if (e.getAction != MotionEvent.ACTION_DOWN) false
      else {
        val matrixed = Array(e.getX, e.getY)
        val matrix = new Matrix
        reference.getImageMatrix.invert(matrix)
        matrix.mapPoints(matrixed)
        val pixel = Try(reference.getDrawable.asInstanceOf[BitmapDrawable].getBitmap.getPixel(matrixed(0).toInt, matrixed(1).toInt)).getOrElse(0xFFFFFFFF)
        Log.e("MOTHER", pixel.toString)
        val bodyPart = colourToBodyPart.get(pixel)
        if (bodyPart.isDefined) {
          showDialogue(bodyPart.get)
          true
        } else false
      }
    }
  }

  val selectedBodyParts:TrieMap[BodyPart, Seq[CharSequence]] = TrieMap()

  def showDialogue(bodyPart:BodyPart) = {
    val choices: Array[String] = bodyPart match {
      case Belly => Array("Butterflies in my stomach", "My stomach hurts")
      case Chest => Array("My chest hurts")
      case LeftFoot => Array("My left foot hurts")
      case RightFoot => Array("My right foot hurts")
      case LeftForearm => Array("My left forearm hurts")
      case RightForearm => Array("My right forearm hurts")
      case Groin => Array("My balls hurt")
      case LeftHand => Array("My left hand hurts")
      case RightHand => Array("My right hand hurts")
      case Head => Array("My head hurts")
      case Neck => Array("My neck hurts")
      case LeftShin => Array("My left shin hurts")
      case RightShin => Array("My right shin hurts")
      case LeftThigh => Array("My left thigh hurts")
      case RightThigh => Array("My right thigh hurts")
      case LeftUpperArm => Array("My left upper arm hurts")
      case RightUpperArm =>  Array("My right upper arm hurts")
      case _ => Array()
    }

    if (choices.length > 0) {
      val checkboxes = for {
        choice <- choices
        cb = new CheckBox(getActivity)
        _ = cb.setChecked(selectedBodyParts.getOrElse(bodyPart, Nil).contains(choice))
        _ = cb.setTextColor(getResources.getColor(R.color.black))
        _ = cb.setText(choice)
      } yield cb

      val view = new LinearLayoutCompat(getActivity)
      view.setOrientation(LinearLayoutCompat.VERTICAL)
      for (cb <- checkboxes) view.addView(cb)

      new MaterialDialog.Builder(getActivity)
        .title("Select the correct things.")
        .negativeColor(getResources.getColor(R.color.black))
        .positiveText("OK")
        .negativeText("Cancel")
        .callback(new SimpleCallback {
          override def onPositive(materialDialog: MaterialDialog): Unit = {
            val texts = for {
              cb <- checkboxes if cb.isChecked
            } yield cb.getText
            processChoice(bodyPart, texts)
          }
        })
        .customView(view)
        .show()
    }

    def processChoice(bodyPart: BodyPart, texts: Seq[CharSequence]) = {
      selectedBodyParts.get(bodyPart) match {
        case Some(issues) =>
          selectedBodyParts.update(bodyPart, texts)
        case None =>
          selectedBodyParts.put(bodyPart, texts)
      }
      drawAllParts()
    }
  }

  def getCurrentImageBitmap:Bitmap = {
    getTouchImage(getView).getDrawable.asInstanceOf[BitmapDrawable].getBitmap
  }

  def getImageFromBodyPart(bodyPart:BodyPart):Bitmap = {
    val resource = bodyPart match {
      case Belly => R.drawable.body_belly
      case Chest => R.drawable.body_chest
      case LeftFoot => R.drawable.body_foot_left
      case RightFoot => R.drawable.body_foot_right
      case LeftForearm => R.drawable.body_forearm_left
      case RightForearm => R.drawable.body_forearm_right
      case Groin => R.drawable.body_groin
      case LeftHand => R.drawable.body_hand_left
      case RightHand => R.drawable.body_hand_right
      case Head => R.drawable.body_head
      case Neck => R.drawable.body_neck
      case LeftShin => R.drawable.body_shin_left
      case RightShin => R.drawable.body_shin_right
      case LeftThigh => R.drawable.body_thigh_left
      case RightThigh => R.drawable.body_thigh_right
      case LeftUpperArm => R.drawable.body_upper_arm_left
      case RightUpperArm => R.drawable.body_upper_arm_right
    }
    val options = new BitmapFactory.Options()
    options.inPreferredConfig = Bitmap.Config.ARGB_8888
    BitmapFactory.decodeResource(getResources, resource, options)
  }

  def drawAllParts() =  {
    val touchImageView = getTouchImage(getView)
    val options = new BitmapFactory.Options()
    options.inPreferredConfig = Bitmap.Config.ARGB_8888
    val base = BitmapFactory.decodeResource(getResources, R.drawable.body, options)
    touchImageView.setImageBitmap(base)
    for {
      (bodyPart, text) <- selectedBodyParts if text.nonEmpty
    } replaceCurrentBitmap(bodyPart)
    touchImageView.invalidate()
  }

  def replaceCurrentBitmap(bodyPart:BodyPart) = {
    val touchImageView = getTouchImage(getView)
    val bmp = drawOverBitmap(getCurrentImageBitmap, getImageFromBodyPart(bodyPart))
    touchImageView.setImageBitmap(bmp)
  }

  def drawOverBitmap(source:Bitmap, destination:Bitmap):Bitmap = {
    val bmp = source.copy(Bitmap.Config.ARGB_8888, true)
    source.recycle()
    val canvas = new Canvas(bmp)
    canvas.drawBitmap(destination, 0f, 0f, null)
    destination.recycle()
    bmp
  }

  override def populateViewFromAnswer(answer: Answer): Unit = answer match {
    case BodyAnswer(_, bodyPartMap, _) =>
      val bodyPartAndFeelings = for {
        (bodyPartString, feelings) <- bodyPartMap
        bodyPart = nameToBodyPart(bodyPartString)
      } yield (bodyPart, feelings)
      selectedBodyParts.clear()
      selectedBodyParts ++ bodyPartAndFeelings
      drawAllParts()
      getView.invalidate()
  }

  override def getAnswerFromView: Option[Answer] = {
    val values = selectedBodyParts.map{
      case (bodyPart, answerList) =>
        (bodyPartToName(bodyPart), answerList.map(_.toString))
    }.mapValues(_.toList).toMap
    Some(BodyAnswer("body_uuid", values))
  }
}
