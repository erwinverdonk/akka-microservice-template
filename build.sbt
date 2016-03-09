name := "akka-microservice-template"

version := "1.0"

scalaVersion := "2.11.8"


/* scala library dependencies */

val AkkaVersion = "2.4.+"

libraryDependencies ++= Seq(
  "com.typesafe.akka"             %% "akka-actor"            % AkkaVersion
)


/* scala test library dependencies */

val ScalaTestVersion = "+"

libraryDependencies ++= Seq(
  "com.typesafe.akka"             %% "akka-testkit"           % AkkaVersion             % Test,
  "org.scalatest"                 %% "scalatest"              % ScalaTestVersion        % Test,
  "org.scalatest"                 %% "scalatest-matchers"     % ScalaTestVersion        % Test
)


/* java library dependencies */

libraryDependencies ++= Seq(

)


/* java test library dependencies */

libraryDependencies ++= Seq(

)
