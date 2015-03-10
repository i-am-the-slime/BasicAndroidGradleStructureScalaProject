package co.aryaapp.journal.fragments

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
    "body_belly" → Belly,
    "body_chest" → Chest,
    "body_foot_left" → LeftFoot,
    "body_foot_right" → RightFoot,
    "body_forearm_left" → LeftForearm,
    "body_forearm_right" → RightForearm,
    "body_groin" → Groin,
    "body_hand_left" → LeftHand,
    "body_hand_right" → RightHand,
    "body_head" → Head,
    "body_neck" → Neck,
    "body_shin_left" → LeftShin,
    "body_shin_right" → RightShin,
    "body_thigh_left" → LeftThigh,
    "body_thigh_right" → RightThigh,
    "body_upper_arm_left" → LeftUpperArm,
    "body_upper_arm_right" → RightUpperArm
  )
  val bodyPartToName = nameToBodyPart.map(_.swap)

  val colourToBodyPart = Map(
    -11355969 -> Belly,
    -4238716  -> Chest,
    -9064587  -> LeftFoot,
    -12699034 -> RightFoot,
    -4237998 -> LeftForearm,
    -11354239 -> RightForearm,
    -11786971 -> Groin,
    -13416155 -> LeftHand,
    -4219566 -> RightHand,
    -11369279 -> Head,
    -7580993 -> Neck,
    -2397544 -> LeftShin,
    -8754115 -> RightShin,
    -4142717 -> LeftThigh,
    -15054731 -> RightThigh,
    -6439086 -> LeftUpperArm,
    -9758112 -> RightUpperArm
  )
  val bodyPartToColour = colourToBodyPart.map(_.swap)
}
