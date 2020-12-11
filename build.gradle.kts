plugins {
    java
    `maven-publish`
}

group = "de.md5lukas"
version = "1.0.0-SNAPSHOT"
description = "db"
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()

    maven(url = "https://repo.sytm.de/repository/maven-hosted/")
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

publishing {
    repositories {
        maven {
            val releasesRepoUrl = "https://repo.sytm.de/repository/maven-releases/"
            val snapshotsRepoUrl = "https://repo.sytm.de/repository/maven-snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                if (project.hasProperty("mavenUsername")) {
                    username = project.properties["mavenUsername"] as String
                }
                if (project.hasProperty("mavenPassword")) {
                    password = project.properties["mavenPassword"] as String
                }
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
