plugins {
    id("java")
}

group = "tinesone"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("https://mvn.lib.co.nz/public") {
        content {
            includeGroup("me.libraryaddict.disguises")
        }
    }
}

dependencies {
    compileOnly("me.libraryaddict.disguises:libsdisguises:11.0.0")
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
}


