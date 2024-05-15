package hello.springTransaction.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void save(Order order) throws NotEnoughMoneyException {
        log.info("order 호출: {}", order);
        orderRepository.save(order);

        log.info("order Process 시작");
        if (order.getUserName().equals(PayStatus.RunException.toString()  )) {
            log.info("시스템 예외 발생");
            throw new RuntimeException("runtime Exception");
        }
        if (order.getUserName().equals(PayStatus.NotEnough.toString())) {
            log.info("Not enough Exception, 비지니스 예외");
            order.setPayStatus(PayStatus.Stand.toString());
            throw new NotEnoughMoneyException("잔고 부족");
        }

        log.info("정상 승인");
        order.setPayStatus(PayStatus.Comp.toString());
        log.info("결제 프로세스 완  ");
    }

}
