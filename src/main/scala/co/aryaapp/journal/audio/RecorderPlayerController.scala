package co.aryaapp.journal.audio

import java.io.File
import java.util.{TimerTask, Timer}

import android.app.{AlertDialog, Activity}
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.media.{MediaPlayer, MediaRecorder}
import android.os.Environment
import android.util.Log
import android.view.{View, ViewGroup}
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import co.aryaapp.{R, TR, TypedResource}
import co.aryaapp.helpers.{Animations, AndroidConversions}
import AndroidConversions._
import TypedResource._
import com.afollestad.materialdialogs.MaterialDialog.Callback
import com.afollestad.materialdialogs.{Theme, MaterialDialogCompat, MaterialDialog}
import com.google.android.gms.internal.di

import scala.util.Try

class RecorderPlayerController(container:ViewGroup)(implicit val ctx:Activity) {
  val PLAYING = "\ue6aa"
  val PAUSED = "\ue624"

  val recordButton = container.findView(TR.record_button)
  val playerContainer = container.findView(TR.player_container)
  recordButton.onClick( record() )

  val timeElapsed = playerContainer.findView(TR.time_elapsed)
  val timeRemaining = playerContainer.findView(TR.time_remaining)
  val seekBar = playerContainer.findView(TR.seek_bar)

  val timer = new Timer

  seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener {

    override def onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean): Unit = Unit

    override def onStopTrackingTouch(seekBar: SeekBar): Unit = {
      updatePlayerFromView()
      updateViewFromPlayer(player)
      if(playPauseButton.getText == PLAYING){
        timerTask = makeTimerTask()
        runTimerTask(timerTask)
      }

    }

    override def onStartTrackingTouch(seekBar: SeekBar): Unit = {
      timerTask.cancel()
    }
  })

  val playPauseButton = playerContainer.findView(TR.play)
  playPauseButton.onClick(
    playPauseButton.getText match {
      case PLAYING =>
        playPauseButton.setText(PAUSED)
        pausePlaying()
      case PAUSED =>
        playPauseButton.setText(PLAYING)
        startPlaying()
    }
  )

  // Record again button
  val startOverButton = playerContainer.findView(TR.start_over)
  startOverButton.onClick{
    deleteAndResetToBeginning()
    record()
  }

  //Delete button
  val deleteButton = playerContainer.findView(TR.delete)
  deleteButton.onClick(deleteAndResetToBeginning())

  val basePath = Environment.getExternalStorageDirectory.getAbsolutePath
  val path = basePath + File.separatorChar + "recording.3gp"
  var recorder:MediaRecorder = null
  var recording:Boolean = false

  var player:MediaPlayer = null

  var timerTask:TimerTask = makeTimerTask()

  def makeTimerTask():TimerTask = () => ctx.runOnUiThread( new Runnable {
    override def run(): Unit = { updateViewFromPlayer(player) }
  })

  def deleteAndResetToBeginning() = {
    deleteRecording()
    player.pause()
    recordButton.setVisibility(View.VISIBLE)
    playerContainer.setVisibility(View.GONE)
  }

  def cleanup() = {
    player.release()
    recorder.release()
  }

  def record() = {
    startRecording()
    def recordingCancelled() {
      stopRecording()
      deleteRecording()
    }
    def recordingAccepted() {
      Log.e("MOTHER", "recording accepted")
      stopRecording()
      recordButton.setVisibility(View.GONE)
      playerContainer.setVisibility(View.VISIBLE)
      preparePlayer()
      prepareViews(player)
      updateViewFromPlayer(player)
    }

    makeRecordingDialogue(recordingAccepted, recordingCancelled)
  }

  def preparePlayer() = {
    if(player == null) {
      player = new MediaPlayer
    } else {
      player.reset()
    }
    if (Try(player.setDataSource(path)).isSuccess){
      player.prepare()
    }
  }
  
  def prepareViews(player:MediaPlayer) = {
    seekBar.setMax(player.getDuration)
    timeElapsed.setText("00:00")
    timeRemaining.setText("-"+millisToMMSS(player.getDuration))
  }

  def makeRecordingDialogue(positive: () => Unit, negative: () => Unit) = {
    val inflater = ctx.getLayoutInflater
    val view = inflater.inflate(R.layout.dialogue_record, null)
    val recordingIndicator = view.findView(TR.recording_indicator)
    val anim = Animations.spinHorizontally(1400).andThen(a => recordingIndicator.startAnimation(a))
    recordingIndicator.startAnimation(anim)

    new MaterialDialog.Builder(ctx)
      .title("Recording your voice...")
      .customView(view)
      .positiveText("Done")
      .negativeText("Cancel")
      .negativeColor(ctx.getResources.getColor(R.color.black))
      .theme(Theme.LIGHT)
      .callback(new Callback {
        override def onNegative(materialDialog: MaterialDialog): Unit = {
          negative(); materialDialog.dismiss()
        }
        override def onPositive(materialDialog: MaterialDialog): Unit = {
          positive(); materialDialog.dismiss()
        }
      })
      .show()
  }

  def doesFileExist(path:String):Boolean = { new File(path).exists() }

  def startPlaying() = {
    player.seekTo(seekBar.getProgress)
    player.start()
    player.setOnCompletionListener{mp:MediaPlayer => {
        mp.seekTo(0)
        updateViewFromPlayer(mp)
        playPauseButton.setText(PAUSED)
        timerTask.cancel()
    }}
    timerTask = makeTimerTask()
    runTimerTask(timerTask)
  }


  def runTimerTask(task:TimerTask) = {
    timer.schedule(task, 0, 100)
  }

  def updatePlayerFromView() = {
    player.seekTo(seekBar.getProgress)
  }

  def updateViewFromPlayer(player:MediaPlayer) = {
    seekBar.setProgress(player.getCurrentPosition)
    timeElapsed.setText(millisToMMSS(player.getCurrentPosition))
    timeRemaining.setText("-"+millisToMMSS(player.getDuration-player.getCurrentPosition))
  }

  def millisToMMSS(millis:Int):String = {
    val seconds = millis/1000
    "%02d:%02d".format(seconds/60, seconds%60)
  }

  def pausePlaying() = {
    updateViewFromPlayer(player)
    player.pause()
  }

  def stopPlaying() = {
    if(player != null){
      player.release()
      player = null
    }
  }

  def startRecording() = {
    if(recorder != null)
      stopRecording()
    recorder = new MediaRecorder
    recorder.setAudioSource(1)
    recorder.setOutputFormat(1)
    recorder.setOutputFile(path)
    recorder.setAudioEncoder(1)
    recorder.prepare()
    recorder.start()
    recording = true
  }

  def stopRecording() = {
    if(recorder != null) {
      recorder.stop()
      recorder.release()
      recorder = null
    }
  }

  def deleteRecording() = {
    val file = new File(path)
    if (file.exists())
      file.delete()
  }
}
