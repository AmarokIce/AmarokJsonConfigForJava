plugins {
    id("java")
    id("idea")
    id("maven-publish")
}

group = "club.someoneice.config"
version = "0.1-SNAPSHOT"

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

publishing {
    repositories {
        maven {
            name = "GithubPackage"
            url = uri("https://maven.pkg.github.com/amarokice/amarokjsonconfigforjava")
            credentials {
                username = "AmarokIce"
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        publications.create<MavenPublication>("amarok-json-config-for-java") {
            groupId = this.groupId
            artifactId = this.artifactId
            version = this.version
            pom.packaging = "jar"
            artifact("$buildDir/libs/${this.artifactId}-${this.version}.jar")
        }
    }
}