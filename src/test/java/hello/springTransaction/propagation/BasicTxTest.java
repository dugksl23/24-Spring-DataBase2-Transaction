package hello.springTransaction.propagation;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

@SpringBootTest
@Slf4j
@Execution(ExecutionMode.CONCURRENT)
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
    void commitV1() {

        log.info("TransactionV1 시작");
        TransactionStatus transaction1 = transactionManager.getTransaction(new DefaultTransactionDefinition());

        log.info("TransactionV1-conn00 Commit 코드 실행");
        transactionManager.commit(transaction1);
        log.info("TransactionV1-conn00 Commit 완료");

    }

    @Test
    void double_commit() {

        log.info("Transaction V2 시작");
        log.info("Transaction V2 conn 1 시작");
        TransactionStatus transaction1 = transactionManager.getTransaction(new DefaultTransactionDefinition());

        log.info("conn 1 commit 시작");
        transactionManager.commit(transaction1);
        log.info("conn 1 Commit 완료");

        log.info("TransactionV2 conn02 시작");
        TransactionStatus transaction2 = transactionManager.getTransaction(new DefaultTransactionDefinition());

        log.info("conn02 rollback 시작");
        transactionManager.rollback(transaction2);
        log.info("conn02 rollback 완료");

    }


    //    @Test
    void rollback() {

        log.info("Transaction 시작");
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

        log.info("Transaction rollback 코드 실행");
        transactionManager.rollback(transaction);
        log.info("Transaction rollback 완료");

    }

    @Test
    void inner_commit(){

        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction());
        // isNewTransaction() 처음 수행된 트랜잭션인지 확인하는 메서드

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction());

        log.info("inner commit 수행 중");
        transactionManager.commit(inner);
        log.info("inner commit 완료");

        log.info("outer commit 실행 중");
        transactionManager.commit(outer);
        log.info("outer commit   완료");
    }

    @Test
    void outer_rollback(){

        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction());
        // isNewTransaction() 처음 수행된 트랜잭션인지 확인하는 메서드

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction());

        log.info("inner commit 수행 중");
        transactionManager.commit(inner);
        log.info("inner commit 완료");

        log.info("outer rollback 실행 중");
        transactionManager.rollback(outer);
        log.info("outer rollback 완료");
    }

    @Test
    void inner_rollback(){

        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction());
        // isNewTransaction() 처음 수행된 트랜잭션인지 확인하는 메서드

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction());

        log.info("inner rollback 수행 중");
        transactionManager.rollback(inner); // 기존 외부 트랜잭션의 DB Connection 에 rollback-only 마크
        log.info("inner rollback 완료");

        log.info("outer commit 실행 중");
        transactionManager.commit(outer);
        log.info("outer commit 완료");
    }

}
