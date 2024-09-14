plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.http.server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
    testImplementation("io.kotest:kotest-runner-junit5:5.3.0")
    testImplementation("io.mockk:mockk:1.12.4")
}

sourceSets {
    main {
        output.setResourcesDir("build/classes/main")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
