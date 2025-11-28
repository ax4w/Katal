plugins {
    kotlin("jvm") version "2.2.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.ax4w.katal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.github.classgraph:classgraph:4.8.184")
    implementation(kotlin("reflect"))
    implementation("com.github.ajalt.clikt:clikt:5.0.1")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to "me.ax4w.katal.MainKt")
    }
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}


