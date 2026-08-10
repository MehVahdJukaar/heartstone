plugins {
    id("com.possible-triangle.neoforge")
}

neoforge {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

val moonlight_version: String by extra

dependencies {
    modImplementation("net.mehvahdjukaar:moonlight-neoforge:${moonlight_version}")
    accessTransformers("net.mehvahdjukaar:moonlight-neoforge:${moonlight_version}")

    // Mirror of common deps
    modCompileOnly("curse.maven:curios-309927:4581099")
}

sourceSets.named("main") {
    resources.srcDir("src/generated/resources")
}
