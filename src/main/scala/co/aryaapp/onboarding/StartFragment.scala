package co.aryaapp.onboarding

import android.os.Bundle
import android.view.{LayoutInflater, View, ViewGroup}
import co.aryaapp.TypedResource._
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp.{R, TR}

class StartFragment extends OnboardingFragment{
  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreateView(inflater, container, savedInstanceState)
    val v = inflater.inflate(R.layout.frag_onboarding_base, container, false)
    v.findView(TR.login_button).onClick(activity.replaceFragment(new Login, "Login"))
    v.findView(TR.register_button).onClick(activity.replaceFragment(new Register, "Register"))
    v
  }

}
