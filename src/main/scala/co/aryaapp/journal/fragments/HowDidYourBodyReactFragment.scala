package co.aryaapp.journal.fragments

import android.graphics._
import android.graphics.drawable.{TransitionDrawable, BitmapDrawable}
import android.os.Bundle
import android.support.v7.widget.LinearLayoutCompat
import android.view.View.OnTouchListener
import android.view.{MotionEvent, View, ViewGroup, LayoutInflater}
import android.widget.{CheckBox, ImageView}
import co.aryaapp.{TR, TypedResource, R}
import co.aryaapp.helpers.AndroidConversions
import co.aryaapp.journal.JournalBaseFragment
import AndroidConversions._
import TypedResource._
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.{SimpleCallback, Callback}

import scala.collection.concurrent.TrieMap
import scala.util.Try

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

  val selectedBodyParts:TrieMap[Int, Seq[CharSequence]] = TrieMap()

  import BodyPartColours._


  def showDialogue(colour:Int) = {
    val choices: Array[String] = colour match {
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

    if (choices.length > 0) {
      val checkboxes = for {
        choice <- choices
        cb = new CheckBox(getActivity)
        _ = cb.setChecked(selectedBodyParts.getOrElse(colour, Nil).contains(choice))
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
            processChoice(colour, texts)
          }
        })
        .customView(view)
        .show()
    }

    def getCurrentImageBitmap:Bitmap = {
      getTouchImage(getView).getDrawable.asInstanceOf[BitmapDrawable].getBitmap
    }

    def getImageFromBodyPart(colour:Int):Bitmap = {
      val resource = colour match {
        case Head => R.drawable.guy_head

      }
      val options = new BitmapFactory.Options()
      options.inPreferredConfig = Bitmap.Config.ARGB_8888
      BitmapFactory.decodeResource(getResources, resource, options)
    }

    def processChoice(colour: Int, texts: Seq[CharSequence]) = {
      selectedBodyParts.get(colour) match {
        case Some(issues) =>
          selectedBodyParts.update(colour, texts)
        case None =>
          selectedBodyParts.put(colour, texts)
      }
      drawAllParts()
    }

    def drawAllParts() =  {
      val touchImageView = getTouchImage(getView)
      val options = new BitmapFactory.Options()
      options.inPreferredConfig = Bitmap.Config.ARGB_8888
      val base = BitmapFactory.decodeResource(getResources, R.drawable.guy, options)
      touchImageView.setImageBitmap(base)
      for {
        (bodyPart, text) <- selectedBodyParts if text.nonEmpty
      } replaceCurrentBitmap(bodyPart)
      touchImageView.invalidate()
    }


    def replaceCurrentBitmap(colour:Int) = {
      val touchImageView = getTouchImage(getView)
      val bmp = drawOverBitmap(getCurrentImageBitmap, getImageFromBodyPart(colour))
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
  }

}
