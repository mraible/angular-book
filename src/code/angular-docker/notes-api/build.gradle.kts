import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.gradle.node.npm.task.NpxTask

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
    id("com.github.ben-manes.versions") version "0.46.0"
    id("com.github.node-gradle.node") version "3.5.1"
    id("com.google.cloud.tools.jib") version "3.3.1"
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.spring") version "1.8.20"
    kotlin("plugin.jpa") version "1.8.20"
}

group = "com.okta.developer"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.okta.spring:okta-spring-boot-starter:3.0.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    if (project.hasProperty("prod")) {
        runtimeOnly("org.postgresql:postgresql")
    } else {
        runtimeOnly("com.h2database:h2")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val profile = if (project.hasProperty("prod")) "prod" else "dev"

tasks.bootRun {
    args("--spring.profiles.active=${profile}")
}

tasks.processResources {
    rename("application-${profile}.properties", "application.properties")
    if (profile == "prod") {
        dependsOn(buildWeb)
        from("${spa}/dist/notes") {
            into("static")
        }
    }
}

val spa = "${projectDir}/../notes"

node {
    version.set("18")
    nodeProjectDir.set(file(spa))
}

val buildWeb = tasks.register<NpxTask>("buildNpm") {
    dependsOn(tasks.npmInstall)
    command.set("ng")
    args.set(listOf("build"))
    inputs.dir("${spa}/src")
    inputs.dir(fileTree("${spa}/node_modules").exclude("${spa}/.cache"))
    outputs.dir("${spa}/dist")
}

jib {
    to {
        image = "mraible/bootiful-angular"
    }
    container {
        environment = mapOf("SPRING_PROFILES_ACTIVE" to profile)
    }
}
