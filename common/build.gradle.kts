plugins {
    id("com.possible-triangle.common")
}

common {
    accessWidener()
}

val moonlight_version: String by extra
val cca_version: String by extra
val trinkets_version: String by extra

dependencies {
    modCompileOnly("net.mehvahdjukaar:moonlight-common:${moonlight_version}")
    accessTransformers("net.mehvahdjukaar:moonlight-common:${moonlight_version}")

    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${cca_version}")
    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${cca_version}")
    modCompileOnly("dev.emi:trinkets:${trinkets_version}")
    modCompileOnly("curse.maven:curios-309927:4581099")
}
