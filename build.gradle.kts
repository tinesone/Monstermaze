group = "tinesone"
version = "1.0.0"

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
}



dependencies {
    paperweight.paperDevBundle("26.1.2.build.+")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}


