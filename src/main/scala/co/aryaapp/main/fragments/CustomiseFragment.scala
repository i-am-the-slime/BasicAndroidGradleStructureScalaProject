package co.aryaapp.main.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.{View, ViewGroup, LayoutInflater}
import co.aryaapp.R

class CustomiseFragment extends Fragment{
  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreateView(inflater, container, savedInstanceState)
    inflater.inflate(R.layout.frag_customise, container, false)
  }
}
