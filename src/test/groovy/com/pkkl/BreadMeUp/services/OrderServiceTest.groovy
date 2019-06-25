import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.exceptions.NotFoundException
import com.pkkl.BreadMeUp.model.Bakery
import com.pkkl.BreadMeUp.model.Order
import com.pkkl.BreadMeUp.model.User
import com.pkkl.BreadMeUp.repositories.BakeryRepository
import com.pkkl.BreadMeUp.repositories.ClosedDaysRepository
import com.pkkl.BreadMeUp.repositories.OrderRepository
import com.pkkl.BreadMeUp.repositories.ProductRepository
import com.pkkl.BreadMeUp.repositories.UserRepository
import com.pkkl.BreadMeUp.services.OrderServiceImpl
import com.pkkl.BreadMeUp.services.OrderService
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
class OrderServiceTest extends Specification {

    private OrderRepository orderRepository = Mock(OrderRepository.class)
    private ProductRepository productRepository = Mock(ProductRepository.class)
    private BakeryRepository bakeryRepository = Mock(BakeryRepository.class)
    private UserRepository userRepository = Mock(UserRepository.class)
    private ClosedDaysRepository closedDaysRepository = Mock(ClosedDaysRepository.class)

    private OrderService orderService;

    def setup() {
        this.orderService = new OrderServiceImpl(
                this.orderRepository,
                this.productRepository,
                this.bakeryRepository,
                this.userRepository,
                this.closedDaysRepository
        );
    }

    def "Should return object when order exists"() {
        given:
        Order order = Mock(Order.class)
        and:
        orderRepository.findById(1) >> Optional.of(order)
        when:
        Order returnedOrder = orderService.getById(1)
        then:
        returnedOrder == order
    }

    def "Should throw DatabaseException when order does not exist"() {
        given:
        orderRepository.findById(1) >> Optional.ofNullable(null)
        when:
        orderService.getById(1)
        then:
        thrown(NotFoundException.class)
    }

    def "Should return object's collection when orders exist"() {
        given:
        Order order1 = Mock(Order.class)
        Order order2 = Mock(Order.class)
        and:
        orderRepository.findAll() >> List.of(order1, order2)
        when:
        List<Order> returnedOrders = orderService.getAll()
        then:
        returnedOrders.size() == 2
        returnedOrders.contains(order1)
        returnedOrders.contains(order2)
    }

    def "Should update throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        Order order = Mock(Order.class)
        and:
        orderRepository.save(_ as Order) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.orderService.update(order)
        then:
        thrown(ConstraintException.class)
    }

    def "Should update throw DatabaseException when repository save throws exception"() {
        given:
        Order order = Mock(Order.class)
        and:
        orderRepository.save(_ as Order) >> { p -> throw new RuntimeException("message") }
        when:
        this.orderService.update(order)
        then:
        thrown(DatabaseException.class)
    }

    def "Should getByBakeryId return order's collection with given bakery"() {
        given:
        Bakery bakery1 = Mock(Bakery.class) {
            getId() >> 21
        }
        Order order1 = Order.builder().bakery(bakery1).build()
        Bakery bakery2 = Mock(Bakery.class) {
            getId() >> 37
        }
        Order order2 = Order.builder().bakery(bakery2).build()
        Order order3 = Order.builder().bakery(bakery1).build()
        and:
        orderRepository.findAll() >> List.of(order1, order2, order3)
        when:
        List<Order> returnedOrders = this.orderService.getByBakeryId(bakery1.getId())
        then:
        returnedOrders.size() == 2
        returnedOrders.first() == order1
        returnedOrders.last() == order3
    }

    def "Should getByUserId return order's collection with given user"() {
        given:
        User user1 = Mock(User.class) {
            getId() >> 37
        }
        Order order1 = Order.builder().user(user1).build()
        User user2 = Mock(User.class) {
            getId() >> 21
        }
        Order order2 = Order.builder().user(user2).build()
        Order order3 = Order.builder().user(user1).build()
        and:
        orderRepository.findAll() >> List.of(order1, order2, order3)
        when:
        List<Order> returnedOrders = this.orderService.getByUserId(user1.getId())
        then:
        returnedOrders.size() == 2
        returnedOrders.first() == order1
        returnedOrders.first() == order1
        returnedOrders.last() == order3
    }

    def "Should getByUserId return empty collection when user has no orders"() {
        given:
        User user1 = Mock(User.class) {
            getId() >> 37
        }
        Order order1 = Order.builder().user(user1).build()
        User user2 = Mock(User.class) {
            getId() >> 21
        }
        Order order2 = Order.builder().user(user1).build()
        orderRepository.findAll() >> List.of(order1, order2)
        when:
        List<Order> returnedOrders = this.orderService.getByUserId(user2.getId())
        then:
        returnedOrders.size() == 0
    }

}