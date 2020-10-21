plugins {
  kotlin("jvm") version "1.4.10"
}

group = "cl.ravenhill"
version = "1.0.1-B.1"

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.30")
  implementation("khttp:khttp:1.0.0")
  implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.0.1")
  implementation(group = "commons-io", name = "commons-io", version = "2.6")
  implementation(
    group = "io.github.microutils",
    name = "kotlin-logging",
    version = "2.0.3",
    ext = "pom"
  )
  testImplementation(kotlin("test-junit5"))
}