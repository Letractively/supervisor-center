import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "supervisor-center"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "com.loicdescotte.coffeebean" % "html5tags_2.9.1" % "1.0-SNAPSHOT",
      "org.apache.xmlrpc" % "xmlrpc-client" % "3.1.3"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}
