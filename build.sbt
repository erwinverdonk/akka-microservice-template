import de.heikoseeberger.sbtheader.license.Apache2_0

//
// Library dependencies
//

//@formatter:off
val ScalaLoggingVersion = "+"
val AkkaVersion = "2.4.3"
val ScaldiVersion = "+"
val ConfigsVersion = "0.3.+"
val SigarLoaderVersion = "1.6.6-rev002"
val KamonVersion = "0.6.+"
val ScalaDependencies = Seq(
  "com.typesafe.scala-logging"    %% "scala-logging"                              % ScalaLoggingVersion,

  "com.typesafe.akka"             %% "akka-actor"                                 % AkkaVersion,
  "com.typesafe.akka"             %% "akka-stream"                                % AkkaVersion,
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

  "com.github.kxbmap"             %% "configs"                                    % ConfigsVersion,

  "io.kamon"                      %  "sigar-loader"                               % SigarLoaderVersion,
  "io.kamon"                      %% "kamon-core"                                 % KamonVersion,
  "io.kamon"                      %% "kamon-scala"                                % KamonVersion,
  "io.kamon"                      %% "kamon-akka"                                 % KamonVersion,
  "io.kamon"                      %% "kamon-autoweave"                            % KamonVersion,
  "io.kamon"                      %% "kamon-annotation"                           % KamonVersion,
  "io.kamon"                      %% "kamon-statsd"                               % KamonVersion,
  "io.kamon"                      %% "kamon-system-metrics"                       % KamonVersion)

val ScalaTestVersion = "+"
val ScalaTestDependencies = Seq(
  "com.typesafe.akka"             %% "akka-testkit"                               % AkkaVersion,
  "com.typesafe.akka"             %% "akka-stream-testkit"                        % AkkaVersion,
  "com.typesafe.akka"             %% "akka-http-testkit"                          % AkkaVersion,
  "com.typesafe.akka"             %% "akka-multi-node-testkit"                    % AkkaVersion,

  "org.scalatest"                 %% "scalatest"                                  % ScalaTestVersion,
  "org.scalatest"                 %% "scalatest-matchers"                         % ScalaTestVersion)

val LogbackVersion = "+"
val JavaDependencies = Seq(
  "ch.qos.logback"                % "logback-classic"                             % LogbackVersion)

val JavaTestDependencies = Seq()

val DependencyOverrides = Set(
  "com.typesafe.akka"             %% "akka-actor"                                 % AkkaVersion)
//@formatter:on

val project = Project(
  id = "akka-micro-service-template",
  base = file(".")).
  settings(SbtMultiJvm.multiJvmSettings: _*).
  settings(
    organization := "fester-io",
    name := "akka-micro-service-template",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.8",
    useJCenter := true,
    headers := Map(
      "scala" → Apache2_0("2016", "fester.io"),
      "conf" → Apache2_0("2016", "fester.io")),
    libraryDependencies ++= ScalaDependencies ++ ScalaTestDependencies ++ JavaDependencies ++ JavaTestDependencies,
    // make sure that MultiJvm tests are compiled by the default test compile task
    compile in MultiJvm <<= (compile in MultiJvm) triggeredBy (compile in Test),
    parallelExecution in Test := false,
    // make sure that MultiJvm tests are executed by the default test target,
    // and combine the results from Test and MultiJvm targets
    executeTests in Test <<= (executeTests in Test, executeTests in MultiJvm) map {
      case (testResults, multiJvmResults) ⇒
        Tests.Output(
          overall = if (testResults.overall.id < multiJvmResults.overall.id) multiJvmResults.overall else testResults.overall,
          events = testResults.events ++ multiJvmResults.events,
          summaries = testResults.summaries ++ multiJvmResults.summaries)
    },
    jvmOptions in MultiJvm += s"-Djava.library.path=${baseDirectory.toString}/native",
    licenses := Seq(("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")))).
  configs(MultiJvm)

fork in run := true
