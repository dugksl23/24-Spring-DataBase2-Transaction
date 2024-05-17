package hello.springTransaction.propagation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private LogService logService;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LogRepository logRepository;


    /**
     * @MemberService : @Transaction off
     * @MemberRepository : @Transaction on
     * @LogRepository : @Transaction off
     */
    @Test
    void outerTransactionOffTest_joinMemberV1() {
        // given...
        String userName = "user 1";

        // when... 정상 저장 된다.
        memberService.joinV1(userName);

        // then...
        assertTrue(memberRepository.findByUserName(userName).isPresent());
        Assertions.assertTrue(logRepository.findByMessage(userName).isPresent());
    }

    /**
     * @MemberService : @Transaction off
     * @MemberRepository : @Transaction on
     * @LogRepository : @Transaction on Exception
     */
    @Test
    void outerTransactionOffTest_joinMemberV2_fail() {
        // given...
        String userName = "로그 예외";

        // when... 정상 저장 된다.
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                memberService.joinV1(userName)).isInstanceOf(RuntimeException.class);

        Assertions.assertTrue(memberRepository.findByUserName(userName).isPresent());
        Assertions.assertTrue(logRepository.findByMessage(userName).isEmpty());

    }


    /**
     * @MemberService : @Transaction on
     * @LogRepository : @Transaction on
     * @MemberRepository : @Transaction off
     * @LogRepository : @Transaction off
     */
    @Test
    void SingleTransactionOffTestAtService() {
        // given...
        String userName = "user 1";

        // when... 정상 저장 된다.
        memberService.joinV1(userName);

        // then...
        assertTrue(memberService.findByUserName(userName).isPresent());
        Assertions.assertTrue(logService.findByMessage(userName).isPresent());
    }


    @Test
    void joinMemberTestV2() {
        String userName = "롤백 예외";
        memberService.joinV2(userName);
    }

}