package hello.springTransaction.apply;


import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class InitTest {

    @Autowired
    private InitService initService;

    @TestConfiguration
    static class InitTestConfiguration {

        @Bean
        public InitService getInitService() {
            log.info("InitService 초기화");
            return new InitService();
        }
    }


    @Test
    void initTest() {
        log.info("aop class={}", this.getClass().getName());
    }

}

@Slf4j
class InitService {

    @PostConstruct
    @Transactional
    public void initV1() {
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        if (isActive) {
            log.info("InitService.initV1() active : {}", isActive);
        } else {
            log.info("InitService.initV1() not active : {}", isActive);
        }
    }
}



