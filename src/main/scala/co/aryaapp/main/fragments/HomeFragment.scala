package co.aryaapp.main.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.{View, ViewGroup, LayoutInflater}
import co.aryaapp.R

class HomeFragment extends Fragment{
  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreateView(inflater, container, savedInstanceState)
    getActivity.getLayoutInflater.inflate(R.layout.frag_main_home, container, false)
  }
}
