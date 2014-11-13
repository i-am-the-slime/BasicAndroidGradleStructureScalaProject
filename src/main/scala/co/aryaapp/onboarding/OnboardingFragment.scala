package co.aryaapp.onboarding

import android.support.v4.app.Fragment
import co.aryaapp.helpers.AryaBaseActivity

trait OnboardingFragment extends Fragment{
  lazy implicit val activity = getActivity.asInstanceOf[OnboardingActivity]
  implicit val ec = AryaBaseActivity.exec
}
