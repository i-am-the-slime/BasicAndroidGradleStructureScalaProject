import android.Keys._
import collection.JavaConversions._

import android.Dependencies.aar

android.Plugin.androidBuild

name := "arya"

platformTarget in Android := "android-21"

scalaVersion := "2.11.6"

mergeManifests in Android := false

proguardCache in Android ++= Seq(
    ProguardCache("argonaut") % "io.argonaut" %% "argonaut"
  , ProguardCache("scalaz") % "scalaz" %% "scalaz"
  , ProguardCache("scalstm") % "org.scala-stm"
  , ProguardCache("slick") % "com.typesafe.slick"
  , ProguardCache("android-support") % "support-v4"
  , ProguardCache("material-dialogs") % "com.allofestad" % "material-design"
)

resolvers ++= Seq(
  "jcenter" at "http://jcenter.bintray.com",
  "clinker" at "http://clinker.47deg.com/nexus/content/groups/public",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

unmanagedClasspath in Test ++= (builder in Android).value.getBootClasspath map
  Attributed.blank

// Testing with Robolectric
libraryDependencies ++= Seq(
  "org.apache.maven" % "maven-ant-tasks" % "2.1.3" % "test"
  , "org.robolectric" % "robolectric" % "2.4" % "test->default"
  , "com.novocode" % "junit-interface" % "0.11" % "test"
  , "junit" % "junit" % "4.11" % "test"
)

// Testing without Robolectric
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.0"
  , "org.scalamock" %% "scalamock-scalatest-support" % "3.1.2"
)

//Crazy stuff
libraryDependencies += "com.chuusai" %% "shapeless" % "2.0.0"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.0"

libraryDependencies += "org.scala-stm" %% "scala-stm" % "0.7"

// AppCompat
libraryDependencies ++= Seq(
  "com.android.support" % "support-v4" % "21.0.3"
  , aar("com.android.support" % "appcompat-v7" % "21.0.3")
//  , aar("com.android.support" % "gridlayout-v7" % "21.0.3")
//  , aar("com.android.support" % "palette-v7" % "21.0.3")
//  , aar("com.android.support" % "cardview-v7" % "21.0.3")
  , aar("com.android.support" % "recyclerview-v7" % "21.0.3")
)

//Basics
libraryDependencies ++= Seq(
  "uk.co.chrisjenx" % "calligraphy" % "1.2.1-SNAPSHOT"
//  , "com.nineoldandroids" % "library" % "2.4.0"
  , "joda-time" % "joda-time" % "2.4"
  , "org.joda" % "joda-convert" % "1.6"
  , "com.squareup.okhttp" % "okhttp-urlconnection" % "2.0.0"
  , "com.squareup.okhttp" % "okhttp" % "2.0.0"
)

//JSON
libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut" % "6.1-M4"
  , "co.aryaapp" %% "macros" % "1.2"
)

//Database (Slick)
libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-nop" % "1.6.4"
  , "com.typesafe.slick" %% "slick" % "2.1.0"
  , "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0"
  , "org.sqldroid" % "sqldroid" % "1.0.3"
)

//Material backports
libraryDependencies ++= Seq(
  aar("com.afollestad" % "material-dialogs" % "0.3.0") // Material Dialogs
  , aar("com.balysv" % "material-ripple" % "1.0.5-SNAPSHOT") //Material effect
)

scalacOptions in (Compile, compile) ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",       // yes, this is 2 args
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
  //, "-Ywarn-unused-import"
)

apkbuildExcludes in Android ++= Seq(
  "META-INF/LICENSE.txt",
  "META-INF/NOTICE.txt",
  "META-INF/LICENSE",
  "META-INF/NOTICE",
  "META-INF/license.txt",
  "META-INF/notice.txt",
  "LICENSE.txt"
)

run <<= run in Android

install <<= install in Android

val java7Home = Some(file("/Library/Java/JavaVirtualMachines/jdk1.7.0_71.jdk/Contents/Home/"))

javaHome in (Compile, compile) := java7Home

javaHome in (Compile, products) := java7Home

val failedSound = Sounds.Basso

sound.play(compile in Compile, Sounds.None, failedSound)

sound.play(install in Android, Sounds.Purr, failedSound)

sound.play(rGenerator in Android, Sounds.None, failedSound)
