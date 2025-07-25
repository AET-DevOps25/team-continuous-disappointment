plugins {
    id("org.springframework.boot") version "3.3.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("com.google.cloud.tools.jib") version "3.4.1" apply false 
    id("java")
    id("checkstyle")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.google.cloud.tools.jib")
    apply(plugin = "checkstyle")

    group = "com.continuousdisappointment"
    version = "0.0.1-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()
    }

    checkstyle {
        toolVersion = "10.26.1"
        configFile = rootProject.file("config/checkstyle.xml")
    }

    dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.security:spring-security-core:6.5.1")
        implementation ("org.springframework.security:spring-security-oauth2-core:5.8.5")

        implementation("org.apache.logging.log4j:log4j-api:2.23.0")
        implementation("org.apache.logging.log4j:log4j-core:2.23.0")

        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.5.0")
        implementation("org.springdoc:springdoc-openapi-starter-common:2.5.0")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        runtimeOnly("io.micrometer:micrometer-registry-prometheus")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = true
    }

    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        enabled = true
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
