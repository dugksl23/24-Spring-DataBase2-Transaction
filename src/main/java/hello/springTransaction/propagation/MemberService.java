package hello.springTransaction.propagation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    @Transactional
    public void joinV1(String userName) {
        log.info("joinV1 userName={}", userName);
        Member member = new Member(userName);
        log.info("memberRepository 호출 시작");
        memberRepository.save(member);
        log.info("memberRepository 호출 종료");

        Log logMsg = new Log(userName);
        log.info("logRepository 호출 시작");
        logRepository.save(logMsg);
        log.info("logRepository 호출 종료");
    }

    public void joinV2(String userName) {
        log.info("joinV2 userName={}", userName);
        Member member = new Member(userName);
        log.info("memberRepository 호출 시작");
        memberRepository.save(member);
        log.info("memberRepository 호출 종료");

        Log logMsg = new Log(userName);
        log.info("logRepository 호출 시작");
        try {
            logRepository.save(logMsg);
        } catch (RuntimeException e) {
            log.info("로그 저장 실패 : {}", e.getMessage());
            log.info("정상 흐름 반환"); // 예외 흐름 복구 및 정상 흐름으로 반환
        }
        log.info("logRepository 호출 종료");
    }

    public Optional<Member> findByUserName(String userName) {
        return memberRepository.findByUserName(userName);
    }

}
