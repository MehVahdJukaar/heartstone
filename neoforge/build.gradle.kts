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

    modCompileOnly("curse.maven:farmers-delight-398521:5566383")
    modCompileOnly("curse.maven:configured-457570:5441232")
}

sourceSets.named("main") {
    resources.srcDir("src/generated/resources")
}
