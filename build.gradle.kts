plugins {
    kotlin("jvm") version "2.2.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.github.classgraph:classgraph:4.8.184")
    implementation(kotlin("reflect"))
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


