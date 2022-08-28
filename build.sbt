lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := """play-java-fileupload-example""",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.8",
      libraryDependencies ++= Seq(
          guice,
        "edu.stanford.nlp" % "stanford-corenlp" % "4.5.0",
        "edu.stanford.nlp" % "stanford-corenlp" % "4.5.0" classifier "models",
         "com.opencsv" % "opencsv" % "5.6",
        "org.apache.commons" % "commons-lang3" % "3.12.0"
      ),
    libraryDependencies += guice,
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v"),
    javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation", "-Werror"),
    javafmtOnCompile := true
  )
