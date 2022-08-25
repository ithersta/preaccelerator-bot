import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("app.cash.sqldelight") version "2.0.0-alpha03"
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
    application
}

group = "ru.spbstu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.repsy.io/mvn/ithersta/tgbotapi") }
}

dependencies {
    implementation("com.ithersta.tgbotapi:tgbotapi-fsm:0.8.0")
    implementation("dev.inmo:tgbotapi:3.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.4.0")
    implementation("org.postgresql:postgresql:42.4.2")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("app.cash.sqldelight:jdbc-driver:2.0.0-alpha03")
    implementation("io.insert-koin:koin-core:3.2.0")
    implementation("io.insert-koin:koin-annotations:1.0.1")
    ksp("io.insert-koin:koin-ksp-compiler:1.0.1")
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
        dialect("app.cash.sqldelight:postgresql-dialect:2.0.0-alpha03")
        //deriveSchemaFromMigrations = true
    }
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}
