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
    private MemberRepository memberRepository;
    @Autowired
    private LogRepository logRepository;


    /**
     * @MemberService    : @Transaction off
     * @MemberRepository : @Transaction on
     * @LogRepository    : @Transaction off
     */
    @Test
    void outerTransactionOffTest_joinMemberV1(){
        // given...
        String userName = "user 1";
        
        // when... 정상 저장 된다.
        memberService.joinV1(userName);

        // then...
        assertTrue(memberRepository.findByUsername(userName).isPresent());
        Assertions.assertTrue(logRepository.findByUserName(userName).isPresent());
    }

    @Test
    void joinMemberTestV2(){
        String userName = "롤백 예외";
        memberService.joinV2(userName);
    }

}