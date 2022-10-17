import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("app.cash.sqldelight") version "2.0.0-alpha04"
    id("com.google.devtools.ksp") version "1.7.20-1.0.7"
    application
}

group = "ru.spbstu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.repsy.io/mvn/ithersta/tgbotapi") }
}

dependencies {
    implementation("com.ithersta.tgbotapi:tgbotapi-fsm:0.17.0")
    implementation("com.ithersta.tgbotapi:tgbotapi-menu:0.17.0")
    implementation("com.ithersta.tgbotapi:tgbotapi-pagination:0.17.0")
    implementation("dev.inmo:tgbotapi:3.2.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.4.1")
    implementation("org.postgresql:postgresql:42.5.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("app.cash.sqldelight:jdbc-driver:2.0.0-alpha04")
    implementation("io.insert-koin:koin-core:3.2.2")
    implementation("io.insert-koin:koin-annotations:1.0.3")
    implementation("org.slf4j:slf4j-simple:2.0.3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.2")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("commons-validator:commons-validator:1.7")
    implementation("io.ktor:ktor-client-okhttp:2.1.2")
    ksp("io.insert-koin:koin-ksp-compiler:1.0.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ru.spbstu.preaccelerator.MainKt")
}

sqldelight {
    database("AppDatabase") {
        packageName = "ru.spbstu.preaccelerator.data"
        dialect("app.cash.sqldelight:postgresql-dialect:2.0.0-alpha04")
    }
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}
