import AssemblyKeys._

assemblySettings

net.virtualvoid.sbt.graph.Plugin.graphSettings

atmosSettings

jarName in assembly := "omnibus.jar"

test in assembly := {}

organization := "com.agourlay"

name := "omnibus"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions := Seq(
  "-unchecked",
  "-Xlint",
  "-deprecation",
  "-encoding","utf8",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-feature")

resolvers ++= Seq(
   "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases/"
  ,"sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
  ,"typesafe release"   at "http://repo.typesafe.com/typesafe/releases/"
  ,"typesafe repo"      at "http://repo.typesafe.com/typesafe/repo/"
  ,"typesafe snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
  ,"maven central"      at "http://repo1.maven.org/maven2/"
  ,"akka repo"          at "http://repo.akka.io/"
  ,"akka snapshots"     at "http://repo.akka.io/snapshots"
  ,"spray repo"         at "http://repo.spray.io/"
  ,"spray nightly"      at "http://nightlies.spray.io/"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

libraryDependencies ++= {
  val akkaVersion   = "2.2.3"
  val sprayVersion  = "1.2-RC4"
  Seq(
       "io.spray"               %   "spray-can"         % sprayVersion               withSources() 
      ,"io.spray"               %   "spray-routing"     % sprayVersion               withSources()
      ,"io.spray"               %   "spray-caching"     % sprayVersion               withSources()
      ,"io.spray"               %   "spray-testkit"     % sprayVersion    % "test"   withSources()
      ,"io.spray"               %%  "spray-json"        % "1.2.5"                    withSources()
      ,"com.typesafe.akka"      %%  "akka-actor"        % akkaVersion                withSources()
      ,"com.typesafe.akka"      %%  "akka-slf4j"        % akkaVersion                withSources()
      ,"com.typesafe.akka"      %%  "akka-testkit"      % akkaVersion     % "test"   withSources()
      ,"joda-time"              %   "joda-time"         % "2.3"                      withSources()
      ,"ch.qos.logback"         %   "logback-classic"   % "1.0.13"                   withSources()
      ,"junit"                  %   "junit"             % "4.11"          % "test"   withSources()
      ,"org.specs2"             %%  "specs2"            % "2.2.3"         % "test"   withSources()  
  )
}

seq(Revolver.settings: _*)