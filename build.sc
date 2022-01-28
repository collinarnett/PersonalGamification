import mill._, scalalib._

object PersonalGamification extends ScalaModule {
  def scalaVersion = "3.1.0"
  def ivyDeps = Agg(
      ivy"org.scalatest::scalatest::3.2.9",
      ivy"com.fasterxml.jackson.module:jackson-module-scala_2.13:2.12.5",
      ivy"com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.5",
      ivy"com.github.scopt::scopt:4.0.1"
  )
}

