package co.aryaapp.journal.fragments

import java.io.File

import android.media.{MediaPlayer, MediaRecorder}
import android.os.{Environment, Bundle}
import android.util.Log
import android.view.{View, ViewGroup, LayoutInflater}
import co.aryaapp.journal.audio.RecorderPlayerController
import co.aryaapp.{TypedResource, TR, R}
import co.aryaapp.journal.JournalBaseFragment
import co.aryaapp.helpers.AndroidConversions
import AndroidConversions._
import TypedResource._

import scala.util.Try

class WhatAreYouThinkingFragment extends
      JournalBaseFragment(
        R.drawable.ic_thoughts,
        R.string.frag_what_are_you_thinking_title,
        R.string.frag_what_are_you_thinking_subtitle
      ) {

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val view = getActivity.getLayoutInflater.inflate(R.layout.frag_journal_thoughts, container, false)
    val audioRecorderAndPlayerView = view.findView(TR.audio_recorder_and_player)
    val recorderPlayerController = new RecorderPlayerController(audioRecorderAndPlayerView)(getActivity)
    view
  }


}

