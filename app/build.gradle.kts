plugins {
    kotlin("jvm") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories {
    mavenCentral()
    maven(url = "https://m2.dv8tion.net/releases")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.2.2")

    implementation("net.dv8tion:JDA:4.3.0_324")

    implementation("io.github.microutils:kotlin-logging:2.0.4")
    runtimeOnly("org.apache.logging.log4j:log4j-to-slf4j:2.14.0")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    implementation("org.jetbrains.exposed:exposed-core:0.34.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.34.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.34.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.34.1")
    implementation("org.xerial:sqlite-jdbc:3.34.0")
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "wisp.AppKt"
        }
    }

    shadowJar {
        archiveBaseName.set("wisp")
        archiveClassifier.set("fat")
        archiveVersion.set("")
    }
}
