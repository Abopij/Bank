package bank.com;

import bank.com.entities.BankAccount;
import bank.com.entities.BankAccountRedis;
import bank.com.entities.MoneyType;
import bank.com.entities.Role;
import bank.com.security.auth.AuthenticationResponse;
import bank.com.security.auth.RegisterRequest;
import bank.com.repo.AccountDao;
import bank.com.services.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Log
public class ScheduledTasks {

    private final BankAccountService bankAccountService;

    @Scheduled(fixedDelay = 1000 * 60) // раз в минуту
    public void increaseTask() {
        long currentTime = System.currentTimeMillis();
        bankAccountService.increaseAllDeposits(5);
        log.info("Time increase by 5% is " + (System.currentTimeMillis() - currentTime));
    }

    @Scheduled(fixedDelay = 1000 * 1000)
    public void createClientsTest() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    HttpClient client = HttpClient.newHttpClient();

                    RegisterRequest registerRequest = RegisterRequest.builder()
                            .username(UUID.randomUUID().toString())
                            .email(UUID.randomUUID() + "@" + "abopij.com")
                            .password("securepassword")
                            .money(Math.random() * 10000 + 1)
                            .date(new Date(System.currentTimeMillis())) // нам это не важно
                            .type(MoneyType.RUB)
                            .build();

                    ObjectMapper objectMapper = new ObjectMapper();
                    String requestBody = objectMapper.writeValueAsString(registerRequest);

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:8765/api/v1/auth/register"))
                            .header("Content-Type", "application/json")
                            .POST(java.net.http.HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        AuthenticationResponse authResponse = objectMapper.readValue(response.body(), AuthenticationResponse.class);
                    } else {
                        log.severe("Problem (ScheduledTask1)");
                    }
                } catch (Exception e) {
                    log.severe("Problem (ScheduledTask2)");
                }
            }).start();
        }
    }
}
