plugins {
    id("java")
}

group = "de.lukas"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.fazecast:jSerialComm:2.10.4")
}

tasks.test {
    useJUnitPlatform()
}