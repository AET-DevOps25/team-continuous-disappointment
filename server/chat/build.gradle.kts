dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}

jib {
    from {
        image = "eclipse-temurin:21-jre-alpine"
    }
    to {
        image = "ghcr.io/aet-devops25/team-continuous-disappointment/chat"
        tags =  setOf("latest")
    }
    container {
        ports = listOf("8082")

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