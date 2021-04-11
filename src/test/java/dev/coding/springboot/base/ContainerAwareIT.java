package dev.coding.springboot.base;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.containers.wait.strategy.Wait.forListeningPort;

@Testcontainers
public class ContainerAwareIT {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:13";
    private static final String RABBITMQ_DOCKER_IMAGE_NAME = "rabbitmq:3-management";
    private static final String REDIS_DOCKER_IMAGE_NAME = "redis:6";

    private static final int RABBITMQ_PORT = 5672;
    private static final int REDIS_PORT = 6379;

    @Container
    public static PostgreSQLContainer postgresContainer = new PostgreSQLContainer(DockerImageName.parse(POSTGRES_DOCKER_IMAGE_NAME));

    @Container
    public static GenericContainer rabbitMQContainer = new GenericContainer(DockerImageName.parse(RABBITMQ_DOCKER_IMAGE_NAME))
            .withExposedPorts(RABBITMQ_PORT)
            .waitingFor(forListeningPort());

    @Container
    public static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse(REDIS_DOCKER_IMAGE_NAME))
            .withExposedPorts(REDIS_PORT)
            .waitingFor(forListeningPort());

    @DynamicPropertySource
    public static void registerProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

        registry.add("spring.rabbitmq.host", rabbitMQContainer::getContainerIpAddress);
        registry.add("spring.rabbitmq.port", () -> rabbitMQContainer.getMappedPort(RABBITMQ_PORT));

        registry.add("spring.redis.host", redisContainer::getContainerIpAddress);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(REDIS_PORT));
    }

    @Test
    public void isPostgresContainerRunning() {
        assertTrue(postgresContainer.isRunning());
    }

    @Test
    public void isRedisContainerRunning() {
        assertTrue(redisContainer.isRunning());
    }

    @Test
    public void isRabbitMQContainerRunning() {
        assertTrue(rabbitMQContainer.isRunning());
    }
}