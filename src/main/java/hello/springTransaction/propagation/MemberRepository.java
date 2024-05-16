package hello.springTransaction.propagation;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberRepository {

    private final EntityManager em;

    @Transactional
    public void save(Member member) {
        log.info("member save");
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        log.info("member findById");
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        log.info("member findAll");
        String query = "select m from Member m";
        return em.createQuery(query).getResultList();
    }

    @Query("select m from Member m where m.userName = :userName")
    public Optional<Member> findByUsername(@Param("userName") String username) {
        log.info("member findByUsername");
        String query = "select m from Member m where m.userName = :userName";
        return em.createQuery(query, Member.class)
                .setParameter("userName", username)
                .getResultStream()
                .findFirst();
    }

}
