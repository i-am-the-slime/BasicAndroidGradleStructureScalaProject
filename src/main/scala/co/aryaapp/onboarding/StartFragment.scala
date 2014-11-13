package co.aryaapp.onboarding

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.{View, ViewGroup, LayoutInflater}
import co.aryaapp.{TypedResource, TR, R}
import co.aryaapp.helpers.AndroidConversions._
import TypedResource._

class StartFragment extends OnboardingFragment{
  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreateView(inflater, container, savedInstanceState)
    val v = getActivity.getLayoutInflater.inflate(R.layout.frag_onboarding_base, container, false)
    v.findView(TR.login_button).onClick(activity.replaceFragment(new Login, "Login"))
    v.findView(TR.register_button).onClick(activity.replaceFragment(new Register, "Register"))
    v
  }

}
