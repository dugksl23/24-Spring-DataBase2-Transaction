package hello.springTransaction.apply;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.helpers.BasicMDCAdapter;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class BasicTest {

    @Autowired
    private BasicService basicService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public BasicService basicService() {
            return new BasicService();
        }
    }

    @Test
    @DisplayName("Transaction AOP Proxy Test")
    void AopTest() {

        // when...
        log.info("aop class={}", basicService.getClass().getName());

        // then...
        Assertions.assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    @DisplayName("Transaction AOP Proxy Active Test")
    void AopActiveTest() {

        //when...
        log.info("aop class={}", basicService.getClass().getName());
        basicService.tx();
        basicService.nonTx();


    }


    static class BasicService {

        @Transactional
        public void tx() {
            log.info("call tx");
            boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            if (transactionActive) {
                log.info("transaction active : {}", transactionActive);
            }
        }


        public void nonTx() {
            log.info("Non call tx");
            boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            if (!transactionActive) {
                log.info("transaction active : {}", transactionActive);
            }
        }
    }


}


