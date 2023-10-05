plugins {
    java
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.hibernate.orm") version "6.2.7.Final"
    id("org.graalvm.buildtools.native") version "0.9.24"
    id("ru.vyarus.quality") version "4.9.0"
}

group = "com.debit"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-logging:3.1.4")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.postgresql:postgresql")
    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.springframework.security:spring-security-test")
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.3")
    }
}

tasks.withType<Test> {

    val file = File(".env")
    if(file.canRead()) {
        file.forEachLine {
            val envVar = it.split("=")
            environment(envVar[0], envVar[1])
        }
    } else {
        println("No .env file\nLoading default test vars")
        environment("H2URL", "jdbc:h2:mem:db")
        environment("H2PASSWORD", "test")
        environment("H2USER", "user")
        environment("JWT_SECRET", "secret")
        environment("JWT_LIFETIME", "111111")
    }

    useJUnitPlatform()
}

hibernate {
    enhancement {
        enableAssociationManagement.set(true)
    }
}

