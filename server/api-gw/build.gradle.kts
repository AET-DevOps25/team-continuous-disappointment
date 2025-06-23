dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
	}
}

dependencies {
	implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
}

jib {
    from {
        image = "eclipse-temurin:21-jre-alpine"
    }
    to {
        image = "ghcr.io/aet-devops25/team-continuous-disappointment/api-gw"
        tags =  setOf("latest")
    }
    container {
        ports = listOf("8080")

        environment = mapOf(
			"SPRING_PROFILES_ACTIVE" to "production",
			"SERVER_ADDRESS" to "0.0.0.0"
		)
        user = "1000"

		creationTime = "USE_CURRENT_TIMESTAMP"

		jvmFlags = listOf(
            "-XX:+UseContainerSupport",
        )
    }


	dockerClient {
        executable = "/usr/local/bin/docker"
    }
}