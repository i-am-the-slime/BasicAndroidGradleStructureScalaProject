package co.aryaapp.journal.fragments

import scalaz.Isomorphism

object BodyModel {

  sealed trait BodyPart extends Serializable with Product
  case object Belly extends BodyPart
  case object Chest extends BodyPart
  case object LeftFoot extends BodyPart
  case object RightFoot extends BodyPart
  case object LeftForearm extends BodyPart
  case object RightForearm extends BodyPart
  case object Groin extends BodyPart
  case object LeftHand extends BodyPart
  case object RightHand extends BodyPart
  case object Head extends BodyPart
  case object Neck extends BodyPart
  case object LeftShin extends BodyPart
  case object RightShin extends BodyPart
  case object LeftThigh extends BodyPart
  case object RightThigh extends BodyPart
  case object LeftUpperArm extends BodyPart
  case object RightUpperArm extends BodyPart

  val nameToBodyPart = Map(
    "body_belly" -> Belly,
    "body_chest" -> Chest,
    "body_foot_left" -> LeftFoot,
    "body_foot_right" -> RightFoot,
    "body_forearm_left" -> LeftForearm,
    "body_forearm_right" -> RightForearm,
    "body_groin" -> Groin,
    "body_hand_left" -> LeftHand,
    "body_hand_right" -> RightHand,
    "body_head" -> Head,
    "body_neck" -> Neck,
    "body_shin_left" -> LeftShin,
    "body_shin_right" -> RightShin,
    "body_thigh_left" -> LeftThigh,
    "body_thigh_right" -> RightThigh,
    "body_upper_arm_left" -> LeftUpperArm,
    "body_upper_arm_right" -> RightUpperArm
  )
  val bodyPartToName = nameToBodyPart.map(_.swap)

  val colourToBodyPart = Map(
    -58075 -> Belly,
    -2893824  -> Chest,
    -14970481  -> LeftFoot,
    -12604939 -> RightFoot,
    -4711843 -> LeftForearm,
    -10066330 -> RightForearm,
    -3761043 -> Groin,
    -10080879 -> LeftHand,
    -65281 -> RightHand,
    -256 -> Head,
    -33876 -> Neck,
    -6710887 -> LeftShin,
    -280064 -> RightShin,
    -8730301 -> LeftThigh,
    -27874 -> RightThigh,
    -9215145 -> LeftUpperArm,
    -8796480 -> RightUpperArm
  )
  val bodyPartToColour = colourToBodyPart.map(_.swap)
}
