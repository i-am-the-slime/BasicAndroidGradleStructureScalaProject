package co.aryaapp.onboarding

import android.content.Intent

object TokenIntent {
  val key = "token"
  def make(token:String):Intent = new Intent().putExtra(key, token)
  def get(intent:Intent):String = intent.getStringExtra(key)
}
