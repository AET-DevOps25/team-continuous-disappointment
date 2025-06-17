dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
	}
}

dependencies {
	implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc")
}