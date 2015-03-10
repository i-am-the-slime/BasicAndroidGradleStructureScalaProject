package co.aryaapp.main.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.{LayoutInflater, View, ViewGroup}
import co.aryaapp.R

class HomeFragment extends Fragment{
  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreateView(inflater, container, savedInstanceState)
    inflater.inflate(R.layout.frag_main_home, container, false)
  }
}
