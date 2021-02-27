package dev.coding.springboot.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static dev.coding.springboot.common.TestConstants.PROFILE_INTEGRATION_TEST;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testcontainers.containers.wait.strategy.Wait.forListeningPort;

@ActiveProfiles(PROFILE_INTEGRATION_TEST)
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:13";
    private static final String RABBITMQ_DOCKER_IMAGE_NAME = "rabbitmq:3-management";
    private static final String REDIS_DOCKER_IMAGE_NAME = "redis:6";

    @Container
    public static PostgreSQLContainer postgresContainer = new PostgreSQLContainer(DockerImageName.parse(POSTGRES_DOCKER_IMAGE_NAME))
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @Container
    public static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse(REDIS_DOCKER_IMAGE_NAME))
            .withExposedPorts(6379)
            .waitingFor(forListeningPort());

    @Container
    public static GenericContainer rabbitMQContainer = new GenericContainer(DockerImageName.parse(RABBITMQ_DOCKER_IMAGE_NAME))
            .withExposedPorts(5672)
            .waitingFor(forListeningPort());

    @DynamicPropertySource
    static void registerProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

        registry.add("spring.redis.host", redisContainer::getContainerIpAddress);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));

        registry.add("spring.rabbitmq.host", rabbitMQContainer::getContainerIpAddress);
        registry.add("spring.rabbitmq.port", () -> rabbitMQContainer.getMappedPort(5672));
    }

    @Test
    void isRunning() {
        assertTrue(postgresContainer.isRunning());
        assertTrue(redisContainer.isRunning());
        assertTrue(rabbitMQContainer.isRunning());
    }

}


