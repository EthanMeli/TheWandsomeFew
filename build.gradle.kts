import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion

allprojects {
  group = "com.ethan.thewandsomefew"
  version = "0.1.0"

  repositories {
    mavenCentral()
  }
}

subprojects {
  // Apply Java plugin to every subproject (protocol/server/client/tools)
  apply(plugin = "java")

  // Now 'java' extension exists and toolchain is valid
  extensions.configure<org.gradle.api.plugins.JavaPluginExtension>("java") {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(25))
    }
  }

  tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("--enable-preview")
  }

  tasks.withType<Test>().configureEach {
    jvmArgs("--enable-preview")
  }
}