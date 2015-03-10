package co.aryaapp.journal.fragments

import android.os.Bundle
import android.view.{LayoutInflater, View, ViewGroup}
import co.aryaapp.TypedResource._
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.journal.audio.RecorderPlayerController
import co.aryaapp.{R, TR}

class WhatAreYouThinkingFragment extends
      JournalBaseFragment(
        R.drawable.ic_thoughts,
        R.string.frag_what_are_you_thinking_title,
        R.string.frag_what_are_you_thinking_subtitle
      ) with TextAnswer {

  override lazy val answerEditText = getView.findView(TR.thoughtsEditText)

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val view = inflater.inflate(R.layout.frag_journal_thoughts, container, false)
    val audioRecorderAndPlayerView = view.findView(TR.audio_recorder_and_player)
    val recorderPlayerController = new RecorderPlayerController(audioRecorderAndPlayerView)(getActivity)
    view
  }

}

