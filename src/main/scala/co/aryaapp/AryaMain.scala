package co.aryaapp

import android.content.{Intent, Context}
import language.postfixOps
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.util.TypedValue
import android.view.Gravity
import co.aryaapp.journal.JournalActivity
import org.scaloid.common._
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class AryaMain extends SActivity{
  lazy val typeface = Typeface.createFromAsset(getAssets, "fonts/lato-light.ttf")
  lazy val self = this

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setActionBarToTheProperFont()

    val layout = new SRelativeLayout {
      style {
        case tv: STextView => tv.typeface(typeface)
        case b: SButton => b.typeface(typeface)
      }
      val welcome = STextView("Willkommen zurück!").textSize(44 sp).gravity(Gravity.CENTER_HORIZONTAL)
        <<.alignParentTop.centerInParent.wrap.>>
      val start = SButton("Wie fühlst du dich heute?").textSize(24 sp).
        <<.marginTop(10 dip).below(welcome).centerInParent.wrap.>>
      start.onClick(startEntry())
      val s = new SpannableString("\ue639 Schick dein Feedback")
      s.setSpan(new TypefaceSpan(self, "stroke.otf"), 0, 2, 0)
      val feedback = SButton(s).textSize(14 sp).
        <<.alignParentBottom.marginBottom(24 dip).centerInParent.wrap.>>
      feedback.onClick(sendFeedback())
    }

    layout.setBackgroundResource(R.drawable.default_background)
    layout.setPadding(0, 55 dip, 0, 10 dip)
    setContentView(layout)
    startEntry()
  }

  def startEntry() = {
    startActivity[JournalActivity]
    overridePendingTransition(R.anim.move_up, R.anim.nothing)
  }

  def sendFeedback() = {
    val intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","info@aryapp.co", null))
    intent.putExtra(Intent.EXTRA_SUBJECT, "Arya app Feedback")
    startActivity(Intent.createChooser(intent, "Email senden"))
  }

  def setActionBarToTheProperFont() = {
    val s = new SpannableString("ARYA")
    s.setSpan(new TypefaceSpan(this, "Lato-Medium.ttf"), 0, s.length(), 0)
    getActionBar.setTitle(s)
  }

  def getActionBarHeight:Int = {
    val tv = new TypedValue
    getTheme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
    tv.getFloat.toInt
  }

  override def attachBaseContext(newBase:Context) = {
    super.attachBaseContext(new CalligraphyContextWrapper(newBase))
  }

  override def onResume() = {
    super.onResume()
  }
}
