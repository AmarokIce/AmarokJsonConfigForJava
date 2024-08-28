plugins {
    id("java")
}

group = "club.someoneice.config"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("http://maven.snowlyicewolf.club")
        isAllowInsecureProtocol = true
    }
}

dependencies {
    implementation("club.someoneice.json:amarok-json-for-java:1.6")

    testImplementation("com.google.guava:guava:33.0.0-jre")
}

tasks.test {
    systemProperty("file.encoding", "UTF-8")
}