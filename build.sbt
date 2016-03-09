name := "akka-microservice-template"

version := "1.0"

scalaVersion := "2.11.8"


/* scala library dependencies */

val ScalaLoggingVersion = "+"
val AkkaVersion = "2.4.2"
val ScaldiVersion = "+"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging"    %% "scala-logging"                              % ScalaLoggingVersion,

  "com.typesafe.akka"             %% "akka-actor"                                 % AkkaVersion,
  "com.typesafe.akka"             %% "akka-http-experimental"                     % AkkaVersion,
  "com.typesafe.akka"             %% "akka-http-spray-json-experimental"          % AkkaVersion,
  "com.typesafe.akka"             %% "akka-cluster"                               % AkkaVersion,
  "com.typesafe.akka"             %% "akka-cluster-tools"                         % AkkaVersion,

  "org.scaldi"                    %% "scaldi"                                     % ScaldiVersion,
  "org.scaldi"                    %% "scaldi-akka"                                % ScaldiVersion)


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
