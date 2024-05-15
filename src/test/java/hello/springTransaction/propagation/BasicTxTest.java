package hello.springTransaction.propagation;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

@SpringBootTest
@Slf4j
public class BasicTxTest {

    @Autowired
    PlatformTransactionManager transactionManager;

    @TestConfiguration
    static class Config {

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

    }


    @Test
    void commit(){

        log.info("Transaction 시작");
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

        log.info("Transaction Commit 코드 실행");
        transactionManager.commit(transaction);
        log.info("Transaction Commit 완료");

    }


    @Test
    void rollback(){

        log.info("Transaction 시작");
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

        log.info("Transaction rollback 코드 실행");
        transactionManager.rollback(transaction);
        log.info("Transaction rollback 완료");

    }
}
