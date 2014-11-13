import android.Keys._

import android.Dependencies.aar

android.Plugin.androidBuild


name := "arya"

platformTarget in Android := "android-21"

scalaVersion := "2.11.2"

mergeManifests in Android := false

proguardCache in Android += ProguardCache("slick") % "com.typesafe.slick"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "jcenter" at "http://jcenter.bintray.com",
  "clinker" at "http://clinker.47deg.com/nexus/content/groups/public"
)

libraryDependencies ++= Seq(
  "com.android.support" % "support-v4" % "21.0.0"
  , aar("com.balysv" % "material-ripple" % "1.0.5-SNAPSHOT")
  , aar("me.drakeet.materialdialog" % "library" % "1.0.7")
  , aar("com.balysv.materialmenu" % "material-menu-toolbar" % "1.4.0")
  , "com.nineoldandroids" % "library" % "2.4.0"
  , aar("com.android.support" % "appcompat-v7" % "21.0.0")
  , aar("com.android.support" % "gridlayout-v7" % "21.0.0")
  , aar("com.github.dmytrodanylyk.circular-progress-button" % "library" % "1.1.3")
  , aar("com.android.support" % "palette-v7" % "21.0.0")
  , aar("com.android.support" % "cardview-v7" % "21.0.0")
  , aar("com.android.support" % "recyclerview-v7" % "21.0.0")
  , "com.squareup.picasso" % "picasso" % "2.3.4"
  , aar("com.google.android.gms" % "play-services" % "6.1.11")
  , "uk.co.chrisjenx" % "calligraphy" % "1.2.0"
  , "com.squareup.retrofit" % "retrofit" % "1.6.1"
  , "com.squareup.okhttp" % "okhttp-urlconnection" % "2.0.0"
  , "com.squareup.okhttp" % "okhttp" % "2.0.0"
  , "io.argonaut" %% "argonaut" % "6.1-M4"
  , "com.scalarx" %% "scalarx" % "0.2.6"
  , "org.slf4j" % "slf4j-nop" % "1.6.4"
  , "com.typesafe.slick" %% "slick" % "2.1.0"
  , "joda-time" % "joda-time" % "2.4"
  , "org.joda" % "joda-convert" % "1.6"
  , "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0"
  , "org.scalatest" %% "scalatest" % "2.2.0"
  , "org.scalamock" %% "scalamock-scalatest-support" % "3.1.2"
  , "org.scala-lang" %% "scala-pickling" % "0.9.0"
)

proguardOptions in Android ++= Seq(
  "-dontobfuscate", "-dontoptimize", "-dontwarn scala.collection.mutable.**",
  "-keep public class * extends junit.framework.TestCase",
  "-keepclassmembers class * extends junit.framework.TestCase { *; }",
  "-dontwarn org.scalatest.**",
  "-dontwarn org.scalactic.**",
  "-dontwarn org.scalamock.**",
  """
    |-keepattributes Signature
    |-keepattributes *Annotation*
    |-keep class com.squareup.okhttp.** { *; }
    |-keep interface com.squareup.okhttp.** { *; }
    |-dontwarn com.squareup.okhttp.**
    |
    |-dontwarn rx.**
    |-dontwarn retrofit.**
    |-keep class retrofit.** { *; }
    |-keepclasseswithmembers class * {
    |    @retrofit.http.* <methods>;
    |}
    |
    |-keep class sun.misc.Unsafe { *; }
    |#your package path where your gson models are stored
    |-keep class com.example.models.** { *; }
    |
  """.stripMargin,
  "-dontwarn okio.**",
  "-dontwarn javax.naming.InitialContext", //Slick
  "-dontnote org.slf4j.**",
  "-keep class scala.collection.Seq.**",
  "-keep public class org.sqldroid.**",
  "-keep class scala.concurrent.Future$.**",
  "-keep class scala.slick.driver.JdbcProfile$Implicits",
  "-dontwarn org.joda.convert.**",
  "-dontwarn  scala.pickling.**"
)

scalacOptions in (Compile, compile) ++= Seq(
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps"
)

apkbuildExcludes in Android ++= Seq(
  "META-INF/LICENSE.txt",
  "LICENSE.txt",
  "META-INF/NOTICE.txt",
  "META-INF/notice.txt",
  "META-INF/license.txt"
)

run <<= run in Android

install <<= install in Android

javaHome in (Compile, compile) := Some(file("/Library/Java/JavaVirtualMachines/jdk1.7.0_71.jdk/Contents/Home"))

val failedSound = Sounds.Basso

sound.play(compile in Compile, Sounds.None, failedSound)

sound.play(install in Android, Sounds.Purr, failedSound)

sound.play(rGenerator in Android, Sounds.None, failedSound)
