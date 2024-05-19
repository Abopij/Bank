package bank.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankServerApplication.class, args);
    }

}
