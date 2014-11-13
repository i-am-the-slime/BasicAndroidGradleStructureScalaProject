package co.aryaapp

import android.app.Instrumentation
import android.test.InstrumentationTestCase
import android.view.KeyEvent._

object KeyboardUtils {

  def writeKeys(keys:String)(implicit tc:InstrumentationTestCase) = {
    tc.getInstrumentation.sendStringSync(keys)
  }

  def pressBackspace(n:Int=1)(implicit tc:InstrumentationTestCase) = {
    (1 to n).foreach(_ => tc.sendKeys(KEYCODE_DEL))
  }

  def charsToKeys(string:String):Seq[Int] = {
    string.toCharArray.map{
      case 'a' => KEYCODE_A
      case 'b' => KEYCODE_B
      case 'c' => KEYCODE_C
      case 'd' => KEYCODE_D
      case 'e' => KEYCODE_E
      case 'f' => KEYCODE_F
      case 'g' => KEYCODE_G
      case 'h' => KEYCODE_H
      case 'i' => KEYCODE_I
      case 'j' => KEYCODE_J
      case 'k' => KEYCODE_K
      case 'l' => KEYCODE_L
      case 'm' => KEYCODE_M
      case 'n' => KEYCODE_N
      case 'o' => KEYCODE_O
      case 'p' => KEYCODE_P
      case 'q' => KEYCODE_Q
      case 'r' => KEYCODE_R
      case 's' => KEYCODE_S
      case 't' => KEYCODE_T
      case 'u' => KEYCODE_U
      case 'v' => KEYCODE_V
      case 'w' => KEYCODE_W
      case 'x' => KEYCODE_X
      case 'y' => KEYCODE_Y
      case 'z' => KEYCODE_Z
      case 'A' => META_SHIFT_ON | KEYCODE_A
      case 'B' => META_SHIFT_ON | KEYCODE_B
      case 'C' => META_SHIFT_ON | KEYCODE_C
      case 'D' => META_SHIFT_ON | KEYCODE_D
      case 'E' => META_SHIFT_ON | KEYCODE_E
      case 'F' => META_SHIFT_ON | KEYCODE_F
      case 'G' => META_SHIFT_ON | KEYCODE_G
      case 'H' => META_SHIFT_ON | KEYCODE_H
      case 'I' => META_SHIFT_ON | KEYCODE_I
      case 'J' => META_SHIFT_ON | KEYCODE_J
      case 'K' => META_SHIFT_ON | KEYCODE_K
      case 'L' => META_SHIFT_ON | KEYCODE_L
      case 'M' => META_SHIFT_ON | KEYCODE_M
      case 'N' => META_SHIFT_ON | KEYCODE_N
      case 'O' => META_SHIFT_ON | KEYCODE_O
      case 'P' => META_SHIFT_ON | KEYCODE_P
      case 'Q' => META_SHIFT_ON | KEYCODE_Q
      case 'R' => META_SHIFT_ON | KEYCODE_R
      case 'S' => META_SHIFT_ON | KEYCODE_S
      case 'T' => META_SHIFT_ON | KEYCODE_T
      case 'U' => META_SHIFT_ON | KEYCODE_U
      case 'V' => META_SHIFT_ON | KEYCODE_V
      case 'W' => META_SHIFT_ON | KEYCODE_W
      case 'X' => META_SHIFT_ON | KEYCODE_X
      case 'Y' => META_SHIFT_ON | KEYCODE_Y
      case 'Z' => META_SHIFT_ON | KEYCODE_Z
      case '@' => KEYCODE_AT
      case '.' => KEYCODE_PERIOD
      case ',' => KEYCODE_COMMA
    }
  }
}
