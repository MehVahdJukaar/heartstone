plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

val moonlight_version: String by extra
val codecui_version: String by extra
val cca_version: String by extra
val trinkets_version: String by extra

dependencies {
    modImplementation("net.mehvahdjukaar:moonlight-fabric:${moonlight_version}")
    // JiJ'd into Moonlight, so not on the dev runtime classpath — add explicitly to avoid missing schema codec class.
    modRuntimeOnly("net.mehvahdjukaar:codecui-fabric:${codecui_version}")

    // Mirror of common deps
    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${cca_version}")
    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${cca_version}")
    modCompileOnly("dev.emi:trinkets:${trinkets_version}")
    modCompileOnly("curse.maven:curios-309927:4581099")

    modCompileOnly("curse.maven:modmenu-308702:7808443")
}
