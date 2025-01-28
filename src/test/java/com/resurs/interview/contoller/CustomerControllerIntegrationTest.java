package com.resurs.interview.contoller;

import com.resurs.interview.db.CustomerEntity;
import com.resurs.interview.db.CustomerRepository;
import com.resurs.interview.model.api.CustomerCreateRequest;
import com.resurs.interview.model.api.CustomerResponse;
import com.resurs.interview.model.api.TransactionCreateRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.ZonedDateTime;
import java.util.Optional;

import static com.resurs.interview.model.db.TransactionType.CREDIT;
import static com.resurs.interview.model.db.TransactionType.DEBIT;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomerControllerIntegrationTest.class);

    public static PostgreSQLContainer postgresContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withEnv("DEFAULT_REGION", "eu-west-1").withDatabaseName("testdb")
            .withUsername("sa")
            .withPassword("sa");


    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository customerRepository;

    private RestClient restClient;

    @BeforeAll
    static void beforeAll() {
        if (!postgresContainer.isRunning()) {
            postgresContainer.start();
        }
    }

    @BeforeEach
    void setUp() {
        if (restClient == null) {
            restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
        }
        customerRepository.deleteAll();
    }

    @Test
    void createNewCustomer() {
        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("Thor", "Lambert", "test@test.com", "Address 23", "110 11", "TestCity", "640902-4624");

        CustomerResponse result = createCustomer(customerCreateRequest);

        //API response correct
        assertNotNull(result);
        assertEquals(customerCreateRequest.getFirstName(), result.firstName());
        assertEquals(customerCreateRequest.getLastName(), result.lastName());
        assertEquals(customerCreateRequest.getEmail(), result.email());
        assertEquals(customerCreateRequest.getPostcode(), result.postcode());
        assertEquals(customerCreateRequest.getCity(), result.city());
        assertEquals(customerCreateRequest.getSocialSecurityNumber(), result.socialSecurityNumber());

        //Database output correct
        Optional<CustomerEntity> customerEntityDB = customerRepository.findById(result.id());
        assertTrue(customerEntityDB.isPresent());
        CustomerEntity customerEntity = customerEntityDB.get();
        assertEquals(customerCreateRequest.getFirstName(), customerEntity.getFirstName());
        assertEquals(customerCreateRequest.getLastName(), customerEntity.getLastName());
        assertEquals(customerCreateRequest.getEmail(), customerEntity.getEmail());
        assertEquals(customerCreateRequest.getPostcode(), customerEntity.getPostcode());
        assertEquals(customerCreateRequest.getCity(), customerEntity.getCity());
        assertEquals(customerCreateRequest.getSocialSecurityNumber(), customerEntity.getSsn());
    }

    @Test
    void getCustomerById_exists() {
        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("Thor", "Lambert", "test@test.com", "Address 23", "110 11", "TestCity", "640902-4624");
        CustomerResponse customer = createCustomer(customerCreateRequest);
        CustomerResponse customerFromGet = getCustomer(customer.id());
        assertEquals(customerCreateRequest.getFirstName(), customerFromGet.firstName());
        assertEquals(customerCreateRequest.getLastName(), customerFromGet.lastName());
        assertEquals(customerCreateRequest.getEmail(), customerFromGet.email());
        assertEquals(customerCreateRequest.getPostcode(), customerFromGet.postcode());
        assertEquals(customerCreateRequest.getCity(), customerFromGet.city());
        assertEquals(customerCreateRequest.getSocialSecurityNumber(), customerFromGet.socialSecurityNumber());

    }

    @Test
    void getCustomerById_NotFound() {
        RestClient.ResponseSpec response = getCustomerFailed(1L);
        response.onStatus(t -> {
            assertTrue(t.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            return false;
        });


    }

    @Test
    void getCreditScore() {
        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("Thor", "Lambert", "test@test.com", "Address 23", "110 11", "TestCity", "640902-4624");
        CustomerResponse customer = createCustomer(customerCreateRequest);
        TransactionCreateRequest transactionDEBIT = new TransactionCreateRequest(500.00, ZonedDateTime.now(), DEBIT);
        TransactionCreateRequest transactionCREDIT = new TransactionCreateRequest(1200.50, ZonedDateTime.now(), CREDIT);
        createTransaction(customer.id(), transactionDEBIT);
        createTransaction(customer.id(), transactionCREDIT);
        CustomerResponse customerWithNewScore = getCustomer(customer.id());
        assertTrue(customerWithNewScore.creditScore() > 0);

    }


    @Test
    void requestLoan() {
    }

    private CustomerResponse createCustomer(CustomerCreateRequest customerCreateRequest) {
        return restClient.post()
                .uri("/api/v1/customer")
                .body(customerCreateRequest).retrieve()
                .onStatus(t -> {
                    if (t.getStatusCode().is4xxClientError()) {
                        logger.warn(t.getStatusText());
                    }
                    return false;
                })
                .body(CustomerResponse.class);
    }

    private void createTransaction(Long customerId, TransactionCreateRequest transactionCreateRequest) {
        ResponseEntity<String> response = restClient.post()
                .uri(String.format("/api/v1/transactions/customer/%s/create", customerId))
                .body(transactionCreateRequest).retrieve().toEntity(String.class);


    }

    private CustomerResponse getCustomer(Long id) {
        return restClient.get()
                .uri("/api/v1/customer/" + id)
                .retrieve()
                .body(CustomerResponse.class);
    }

    private RestClient.ResponseSpec getCustomerFailed(Long id) {
        return restClient.get()
                .uri("/api/v1/customer/" + id)
                .retrieve();
    }
}