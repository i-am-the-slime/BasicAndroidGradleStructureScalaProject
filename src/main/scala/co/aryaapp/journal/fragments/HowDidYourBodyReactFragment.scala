package co.aryaapp.journal.fragments

import android.app.{Dialog, AlertDialog}
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View.OnTouchListener
import android.view.{MotionEvent, View, ViewGroup, LayoutInflater}
import android.widget.ImageView
import co.aryaapp.{TR, TypedResource, R}
import co.aryaapp.helpers.AndroidConversions
import co.aryaapp.journal.JournalBaseFragment
import org.scaloid.common._
import AndroidConversions._
import TypedResource._

import scala.util.Try

class HowDidYourBodyReactFragment extends JournalBaseFragment{

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val view = inflater.inflate(R.layout.frag_body, container, false)
    setTitle(view, "\uE13e", R.string.frag_how_did_your_body_react_title, R.string.frag_how_did_your_body_react_subtitle)
    //Set it up such that when clicking on body picture we check the
    val touchImage = view.findView(TR.touch_image)
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
        if (pixel != 0xFFFFFFFF) {
          showDialogue(pixel)
          true
        } else false
      }
    }
  }

  object BodyPartColours {
   val Head = -7367566
   val Neck = -417537
   val Chest = -59576
   val Belly = -14542849
   val LeftArm = -16580695
   val RightArm = -35584
   val LeftLeg = -13540045
   val RightLeg = -15209217
   val LeftFoot = -3457664
   val RightFoot = -7184064
   val Crotch = -1245417
  }

  import BodyPartColours._


  def showDialogue(colour:Int) = {
    val choices:Array[String] = colour match {
      case Head => Array("My head hurts")
      case Neck => Array("My neck hurts")
      case Chest => Array("My chest hurts")
      case Belly => Array("Butterflies in my stomach", "My stomach hurts")
      case LeftArm => Array("My left arm hurts")
      case RightArm => Array("My right arm hurts")
      case LeftLeg => Array("My left leg hurts")
      case RightLeg => Array("My right leg hurts")
      case LeftFoot => Array("My left foot hurts")
      case RightFoot => Array("My right foot hurts")
      case Crotch => Array("AAAAAARGH!")
      case _ => Array()
    }

    if(choices.length > 0) {
      val dialogueBuilder = new AlertDialogBuilder("How does it feel?", null)
      lazy val dialogue:Dialog = dialogueBuilder.show()
      val view = new SVerticalLayout{
        choices.foreach(
          choice => SButton(choice)
            .onClick(processChoice(choice, dialogue))
            .textColor(0xFF000000)
            .textSize(20 sp)
            .typeface(latoTypeface)
            .backgroundResource(R.drawable.button_dark)
            .<<.margin(12 dip).>>
        )
      }
      dialogueBuilder.setView(view)
      dialogueBuilder.negativeButton("Cancel")
      dialogue
    }
  }

  def processChoice(choice:String, dialog:Dialog) = {
    //TODO:
    dialog.cancel()
    Log.e("MOTHER", choice)
  }
}
