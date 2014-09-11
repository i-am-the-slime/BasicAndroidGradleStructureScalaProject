package co.aryaapp.journal.audio

import android.media.MediaRecorder

class Recorder(val path:String, val isRecording:Boolean, stateChangeListeners:List[Boolean =>Unit], val mr:MediaRecorder) {

  def this(path:String) = this(path, false, List(), new MediaRecorder)

  def startRecording() : Recorder = {
    val stopped = if (!isRecording) this else stopRecording()
    stopped.mr.prepare()
    stopped.mr.start()
    stopped.changeRecordingState(isRecording = true)
  }

  def stopRecording() : Recorder = {
    mr.stop()
    mr.release()
    changeRecordingState(isRecording = false)
  }

  def registerChangeListener(f:Boolean => Unit) : Recorder = {
    new Recorder(path, isRecording, f ::stateChangeListeners, mr)
  }

  def unregisterChangeListener(f:Boolean => Unit) : Recorder = {
    val newListeners = stateChangeListeners filterNot (_ == f)
    new Recorder(path, isRecording, newListeners, mr)
  }

  private def changeRecordingState(isRecording:Boolean) : Recorder = {
    notifyListeners(isRecording)
    new Recorder(path, isRecording, stateChangeListeners, mr)
  }

  private def notifyListeners(isRecording:Boolean) = {
    stateChangeListeners.foreach{
      (f:Boolean => Unit) => f(isRecording)
    }
  }
}

class AryaMediaRecorder(val path:String) extends MediaRecorder {
  setAudioSource(1)
  setOutputFormat(1)
  setOutputFile(path)
  setAudioEncoder(1)

  def startRecording():AryaMediaRecorder = {
    val amr = AryaMediaRecorder(this)
    amr.prepare()
    amr.start()
    amr
  }

  def stopRecording():AryaMediaRecorder = {
    this.release()
    val amr = AryaMediaRecorder(this)
    amr.stop()
    amr
  }
}

object AryaMediaRecorder{
  def apply(amr:AryaMediaRecorder):AryaMediaRecorder = {
    amr.release()
    new AryaMediaRecorder(amr.path)
  }
}
