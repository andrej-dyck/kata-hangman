import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.70"
    java
    application
}

group = "ad.kata.hangman"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("org.assertj:assertj-core:3.14.0")
    // needed for IntelliJ
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_10
}

sourceSets.getByName("main") {
    java.srcDirs("src/")
    withConvention(KotlinSourceSet::class) {
        kotlin.srcDirs("src/")
    }
}

sourceSets.getByName("test") {
    java.srcDirs("test/")
    withConvention(KotlinSourceSet::class) {
        kotlin.srcDirs("test/")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xinline-classes")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        includeEngines("junit-jupiter")
        excludeEngines("junit-vintage")
    }
    testLogging {
        events("passed", "skipped", "failed")
    }
}

application {
    applicationName = "ElegantHangman"
    mainClassName = "ad.kata.hangman.Main"
}

tasks.withType<Jar> {
    archiveClassifier.set("")
    archiveAppendix.set("")
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClassName
            )
        )
    }
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}

tasks.withType<Wrapper> {
    gradleVersion = "6.1"
    distributionType = Wrapper.DistributionType.BIN
}