import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import userservice.client.StocksClient;
import userservice.client.StocksClientImpl;
import userservice.model.User;
import userservice.service.UserService;

@SpringBootTest
@ContextConfiguration(initializers = IntegrationTests.Initializer.class)
@Testcontainers
public class IntegrationTests {
    @ClassRule
    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:12.4").withDatabaseName("psql-db")
                    .withUsername("postgres").withPassword("admin");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext ac) {
            TestPropertyValues.of(
                    String.format("spring.datasource.url=%s", postgreSQLContainer.getJdbcUrl()),
                    String.format("spring.datasource.username=%s", postgreSQLContainer.getUsername()),
                    String.format("spring.datasource.password=%s", postgreSQLContainer.getPassword()),
                    String.format("spring.datasource.driver-class-name=%s", postgreSQLContainer.getDriverClassName())
            ).applyTo(ac.getEnvironment());
        }
    }

    @ClassRule
    public static GenericContainer stocksServer
            = new FixedHostPortGenericContainer("stockexchange:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080);

    private static final StocksClient stocksClient = new StocksClientImpl("localhost", 8080);

    @Autowired
    public UserService userService;

    @Test
    public void testBuyStocks() {
        final var companyId= stocksClient.addCompany("company");
        stocksClient.addStocks(companyId, 5);
        stocksClient.setPrice(companyId, 100500);

        final var userId = userService.addUser(new User());
        userService.addBalance(userId, 100);
        Assert.assertFalse(userService.buyStocks(userId, companyId, 1));
        userService.addBalance(userId, 100500);
        Assert.assertTrue(userService.buyStocks(userId, companyId, 1));
        Assert.assertEquals(1, userService.getStocksAmount(userId, companyId));
    }

    @Test
    public void testResellStocks() {
        final var companyId= stocksClient.addCompany("company");
        stocksClient.addStocks(companyId, 5);
        stocksClient.setPrice(companyId, 5);

        final var userId = userService.addUser(new User());
        userService.addBalance(userId, 20);
        Assert.assertTrue(userService.buyStocks(userId, companyId, 4));
        Assert.assertTrue(userService.sellStocks(userId, companyId, 3));
        Assert.assertEquals(1, userService.getStocksAmount(userId, companyId));
        Assert.assertEquals(15, userService.getBalance(userId), 0);
    }

    @Test
    public void testResellChangedStocks() {
        final var companyId= stocksClient.addCompany("company");
        stocksClient.addStocks(companyId, 5);
        stocksClient.setPrice(companyId, 5);

        final var userId = userService.addUser(new User());
        userService.addBalance(userId, 20);
        Assert.assertTrue(userService.buyStocks(userId, companyId, 4));

        stocksClient.setPrice(companyId, 10);

        Assert.assertTrue(userService.sellStocks(userId, companyId, 3));
        Assert.assertEquals(1, userService.getStocksAmount(userId, companyId));
        Assert.assertEquals(30, userService.getBalance(userId), 0);
    }
}
