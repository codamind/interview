package com.resurs.interview.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class MockedThirdPartyApiService implements ThirdPartyAPIService {
    private final Random random = new Random();

    public AccountBalanceResponse getCustomerAccountBalance(String accountId) {
        return new AccountBalanceResponse(accountId, random.nextInt(100000), "SEK", LocalDateTime.now().minusMinutes(10));
    }
}
