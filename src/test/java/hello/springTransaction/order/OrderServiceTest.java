package hello.springTransaction.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void orderNotEnoughExceptionTest() {
        log.info("orderService Test");

        // given...
        Order order = new Order();
        log.info("status : {}", PayStatus.NotEnough.toString());
        order.setUserName(String.valueOf(PayStatus.NotEnough));

        // when...
        try {
            orderService.save(order);
        } catch (NotEnoughMoneyException e) {
            log.info(e.getMessage());
        }

        // then...
        Optional<Order> byId = orderRepository.findById(order.getId());
        assertEquals(order.getId(), byId.get().getId());
        log.info("status : {}", byId.get().getPayStatus());
    }

    @Test
    void orderCompleteTest() throws NotEnoughMoneyException {
        log.info("orderService Test");

        // given...
        Order order = new Order();
        log.info("status : {}", PayStatus.Comp.toString());
        order.setUserName(String.valueOf(PayStatus.Comp));

        // when...
        orderService.save(order);

        // then...
        Optional<Order> byId = orderRepository.findById(order.getId());
        // id 채번해온 뒤 1차 캐싱 후에 db 동기화 후 반환.
        assertEquals(order.getId(), byId.get().getId());

    }

    @Test
    void orderRuntimeExceptionTest() throws NotEnoughMoneyException {
        log.info("orderService Test");

        // given...
        Order order = new Order();
        log.info("status : {}", PayStatus.RunException.toString());
        order.setUserName(String.valueOf(PayStatus.RunException));

        // when...
        orderService.save(order);

        // then...
        Optional<Order> byId = orderRepository.findById(order.getId());
        // id 채번해온 뒤 1차 캐싱 후에 db 동기화 후 반환.
        assertEquals(order.getId(), byId.get().getId());

    }

}