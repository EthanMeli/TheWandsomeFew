plugins {
    application
}

val javafxVersion = "27-ea+13"
val platform = osdetect()

dependencies {
    implementation(project(":protocol"))
    implementation("org.openjfx:javafx-base:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-controls:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:$platform")
}

application {
    mainClass.set("com.ethan.thewandsomefew.client.GameClient")
    applicationDefaultJvmArgs = listOf("--enable-preview")
}

fun osdetect(): String {
    val os = System.getProperty("os.name").lowercase()
    val arch = System.getProperty("os.arch").lowercase()
    return when {
        os.contains("win") -> "win"
        os.contains("mac") && arch.contains("aarch64") -> "mac-aarch64"
        os.contains("mac") -> "mac"
        else -> "linux"
    }
}