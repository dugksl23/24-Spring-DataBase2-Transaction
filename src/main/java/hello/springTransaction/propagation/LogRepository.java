package hello.springTransaction.propagation;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LogRepository {

    private final EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Log logger) {
        log.info("log message : {}", logger.getMessage());
        entityManager.persist(logger);

        if(logger.getMessage().contains("로그 예외")){
            log.info("checked Exception : runtime Exception");
            throw new RuntimeException();
        }

    }

    public Optional<Log> findByMessage(String message) {
        return entityManager.createQuery("select l from Log l where l.message = :message", Log.class)
                .setParameter("message", message)
                .getResultStream().findFirst();
    }

}
