plugins {
  application
}

dependencies {
  implementation(project(":protocol"))
}

application {
  mainClass.set("com.ethan.thewandsomefew.server.GameServer")
}