name := "akka-microservice-template"

version := "1.0"

scalaVersion := "2.11.8"


Keys.useJCenter := true

/* auto-header configuration */
import de.heikoseeberger.sbtheader.license.Apache2_0
headers := Map(
  "scala" → Apache2_0("2016", "fester.io"),
  "conf" → Apache2_0("2016", "fester.io"))


/* scala library dependencies */

val ScalaLoggingVersion = "+"
val AkkaVersion = "2.4.2"
val ScaldiVersion = "+"
val ConfigsVersion = "0.3.+"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging"    %% "scala-logging"                              % ScalaLoggingVersion,

  "com.typesafe.akka"             %% "akka-actor"                                 % AkkaVersion,
  "com.typesafe.akka"             %% "akka-http-experimental"                     % AkkaVersion,
  "com.typesafe.akka"             %% "akka-http-spray-json-experimental"          % AkkaVersion,
  "com.typesafe.akka"             %% "akka-cluster"                               % AkkaVersion,
  "com.typesafe.akka"             %% "akka-cluster-tools"                         % AkkaVersion,
  "com.typesafe.akka"             %% "akka-cluster-metrics"                       % AkkaVersion,
  "com.typesafe.akka"             %% "akka-agent"                                 % AkkaVersion,
  "com.typesafe.akka"             %% "akka-persistence"                           % AkkaVersion,
  "com.typesafe.akka"             %% "akka-slf4j"                                 % AkkaVersion,

  "org.scaldi"                    %% "scaldi"                                     % ScaldiVersion,
  "org.scaldi"                    %% "scaldi-akka"                                % ScaldiVersion,

  "com.github.kxbmap"             %% "configs"                                    % ConfigsVersion)


/* scala test library dependencies */

val ScalaTestVersion = "+"

libraryDependencies ++= Seq(
  "com.typesafe.akka"             %% "akka-testkit"                               % AkkaVersion             % Test,
  "com.typesafe.akka"             %% "akka-http-testkit"                          % AkkaVersion             % Test,

  "org.scalatest"                 %% "scalatest"                                  % ScalaTestVersion        % Test,
  "org.scalatest"                 %% "scalatest-matchers"                         % ScalaTestVersion        % Test)


/* java library dependencies */

val LogbackVersion = "+"

libraryDependencies ++= Seq(
  "ch.qos.logback"                % "logback-classic"                             % LogbackVersion)


/* java test library dependencies */

libraryDependencies ++= Seq(

)
