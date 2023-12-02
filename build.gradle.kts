plugins {
    id("java")
    id("com.diffplug.spotless") version "6.23.2"
}

group = "com.tylerkindy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

spotless {
  java {
      googleJavaFormat()
  }
}
