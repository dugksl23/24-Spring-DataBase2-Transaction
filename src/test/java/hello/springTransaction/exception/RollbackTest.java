package hello.springTransaction.exception;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class RollbackTest {

    @Autowired
    RollbackService rollbackService;

    @TestConfiguration
    static class RollbackTestConfig {

        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }


    @Test
    void rollbackTest() {
        Assertions.assertThatThrownBy(() -> rollbackService.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException() {
        Assertions.assertThatThrownBy(() -> rollbackService.checkedException())
                .isInstanceOf(MyException.class);
    }

    @Test
    void unCheckedException() {
        Assertions.assertThatThrownBy(()-> rollbackService.rollbackFor())
                .isInstanceOf(MyException.class);
    }


}

@Slf4j
class RollbackService {

    // 런타임 예외 발생 : 롤백
    @Transactional
    public void runtimeException() {
        log.info("call runtime Exception");
        throw new RuntimeException();
    }


    // 체크 예외 발생 : 커밋
    @Transactional
    public void checkedException() throws MyException {
        log.info("call checkedException");
        throw new MyException();
    }

    // 체크 예외 rollbackFor 지정 : 롤백
    @Transactional(rollbackFor = MyException.class)
    public void rollbackFor() throws MyException {
        throw new MyException();
    }


}

class MyException extends Exception {
}
