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
    implementation("com.google.guava:guava:32.1.3-jre")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
}

spotless {
  java {
      prettier(
          mapOf(
              "prettier" to "3.1.0",
              "prettier-plugin-java" to "2.5.0"
          )
      )
          .config(
              mapOf(
                  "parser" to "java",
                  "tabWidth" to 2,
                  "printWidth" to 90,
                  "plugins" to listOf("prettier-plugin-java")
              )
          )
  }
}
