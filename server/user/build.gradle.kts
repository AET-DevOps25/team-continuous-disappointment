jib {
    from {
        image = "eclipse-temurin:21-jre-alpine"
    }
    to {
        image = "ghcr.io/aet-devops25/team-continuous-disappointment/user"
        tags =  setOf("latest")
    }
    container {
        ports = listOf("8081")

        environment = mapOf(
			"SPRING_PROFILES_ACTIVE" to "production"
		)
        user = "1000"

		creationTime = "USE_CURRENT_TIMESTAMP"
    }

	dockerClient {
        executable = "/usr/local/bin/docker"
    }
}