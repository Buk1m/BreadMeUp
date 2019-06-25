package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.discounts.*;
import com.pkkl.BreadMeUp.dtos.OrderPriceDto;
import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.exceptions.InvalidTokenPayloadException;
import com.pkkl.BreadMeUp.exceptions.NotFoundException;
import com.pkkl.BreadMeUp.model.*;
import com.pkkl.BreadMeUp.repositories.*;
import com.pkkl.BreadMeUp.security.AuthUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j

@Service
public class OrderServiceImpl extends BaseService<Order, OrderRepository> implements OrderService {

    private final ProductRepository productRepository;
    private final BakeryRepository bakeryRepository;
    private final UserRepository userRepository;
    private final ClosedDaysRepository closedDaysRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            BakeryRepository bakeryRepository,
                            UserRepository userRepository,
                            ClosedDaysRepository closedDaysRepository) {
        this.repository = orderRepository;
        this.bakeryRepository = bakeryRepository;
        this.closedDaysRepository = closedDaysRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Order getById(int id) {
        try{
            return repository.findById(id).orElseThrow(() -> new NotFoundException("Order doesn't exist \\U+1F635"));
        }
        catch(NotFoundException e){
            throw e;
        }
        catch (Exception ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<Order> getAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public void cancelOrder(int orderId) {

        Order order = this.getById(orderId);
        if (order.isCancelled())
            throw new ConstraintException("Order was already cancelled.");

        Stream<Product> orderRelatedProducts = order.getOrderProducts().stream().map(OrderProduct::getProduct);
        Stream<ProductAvailability> orderRelatedProductAvailabilities =
                orderRelatedProducts
                        .map(Product::getProductAvailability)
                        .flatMap(Collection::stream)
                        .filter(pa -> pa.getDate().equals(order.getDate()));

        orderRelatedProductAvailabilities
                .forEach(pa -> pa.setOrderedNumber(pa.getOrderedNumber() - order.getOrderProducts().stream()
                        .filter(i -> i.getProduct().getId() == pa.getProduct().getId()).mapToInt(OrderProduct::getAmount).sum()));
        order.setCancelled(true);
        this.repository.save(order);
    }

    @Override
    public Order update(final Order order) {
        return saveOrUpdate(order);
    }

    @Transactional
    @Override
    public Order add(final Order order, List<OrderProduct> orderProducts, Principal principal) {
        User user = getUserByPrincipal(principal);
        Bakery bakery = getBakeryByOrder(order);

        if (isBakeryClosed(order, bakery))
            throw new ConstraintException("Bakery is closed that day. Orders cannot be made");

        Set<OrderProduct> orderedProducts = orderProducts.stream()
                .map(op -> {
                            Product product = getProductFromOrderProduct(op);
                            ProductAvailability availability = product.getProductAvailability().stream()
                                    .filter(isSameOrderDateAndProduct(order, product)).findAny().orElse(
                                            createDefaultProductAvailability(product, order.getDate())
                                    );
                            availability.appendToOrderedNumber(op.getAmount());
                            product.getProductAvailability().add(availability);
                            return createOrderProduct(op, product, order);
                        }
                ).collect(Collectors.toSet());

        order.setOrderProducts(orderedProducts);
        order.setBakery(bakery);
        order.setUser(user);

        return repository.save(order);
    }

    @Override
    public List<Order> getByBakeryId(int id) {
        try {
            return repository.findAll().stream()
                    .filter(o -> o.getBakery().getId() == id)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Order> getByUserId(int id) {
        try {
            return repository.findAll().stream()
                    .filter(o -> Objects.equals(o.getUser().getId(), id))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public OrderPriceDto completeOrder(int orderId, Principal principal, Set<DiscountType> discounts) {
        Order order = this.repository.findById(orderId).orElseThrow(
                () -> new NotFoundException("Order with given id doesn't exist.")
        );

        if(order.isCancelled() || order.isCompleted()){
            throw new ConstraintException("Order has invalid state. It's either completed or cancelled.");
        }

        Set<DiscountStrategy> discountStrategies = new HashSet<>();
        double priceBeforeDiscount = order.calculateTotalPrice(discountStrategies);
        discountStrategies = getDiscountStrategies(discounts);
        order.setCompleted(true);
        double discountedPrice = order.calculateTotalPrice(discountStrategies);
        repository.save(order);
        return new OrderPriceDto(priceBeforeDiscount, discountedPrice);
    }

    private Set<DiscountStrategy> getDiscountStrategies(Set<DiscountType> discounts) {
        Set<DiscountStrategy> discountStrategies = new HashSet<>();

        // ugly
        if (discounts.contains(DiscountType.NORMAL)) {
            discountStrategies.add(new NormalStrategy());
        }
        if (discounts.contains(DiscountType.ORDER_ITEMS_COUNT)) {
            discountStrategies.add(new OrderItemsCountStrategy());
        }
        if (discounts.contains(DiscountType.ORDER_TOTAL_COST)) {
            discountStrategies.add(new OrderTotalCostStrategy());
        }
        if (discounts.contains(DiscountType.SINGLE_PRODUCT_LOVER)) {
            discountStrategies.add(new SingleProductLoverStrategy());
        }
        if (discounts.contains(DiscountType.HAPPY_HOURS)) {
            discountStrategies.add(new HappyHoursStrategy());
        }
        return discountStrategies;
    }


    private boolean isBakeryClosed(Order order, Bakery bakery) {
        var closedDays = closedDaysRepository.findAll();
        return closedDays.stream()
                .anyMatch(c -> c.getBakeries().contains(bakery) && c.getDate().equals(order.getDate()));
    }

    private ProductAvailability createDefaultProductAvailability(Product product, LocalDate date) {
        return ProductAvailability.builder()
                .date(date)
                .limit(product.getLimit())
                .orderedNumber(0)
                .product(product)
                .build();
    }

    private Predicate<ProductAvailability> isSameOrderDateAndProduct(Order order, Product product) {
        return pa -> {
            boolean isSameProduct = pa.getProduct().getId() == product.getId();
            boolean isSameDay = pa.getDate().equals(order.getDate());
            return isSameProduct && isSameDay;
        };
    }

    private OrderProduct createOrderProduct(OrderProduct op, Product product, Order order) {
        return OrderProduct.builder()
                .price(product.getPrice())
                .amount(op.getAmount())
                .productName(product.getName())
                .product(product)
                .order(order)
                .build();
    }

    private Product getProductFromOrderProduct(OrderProduct op) {
        return productRepository.findById(op.getProduct().getId()).orElseThrow(() ->
                new RuntimeException("Ordered product doesn't exists"));
    }

    private Bakery getBakeryByOrder(Order order) {
        return bakeryRepository.findById(order.getBakery().getId()).orElseThrow(() -> new RuntimeException("Wyjebałem się"));
    }

    @Override
    public List<Order> getOrdersByPrincipal(Principal principal) {
        return getByUserId(getUserIdFromPrincipal(principal));
    }

    private User getUserByPrincipal(Principal principal) {
        return userRepository.findById(getUserIdFromPrincipal(principal)).orElseThrow(() -> new RuntimeException("User not found."));
    }

    private int getUserIdFromPrincipal(Principal principal) {
        Integer userId = getAuthUserDetails(principal).getUser().getId();
        if (userId == null) {
            throw new InvalidTokenPayloadException("Payload exception must contain user data");
        }
        return userId;
    }

    private AuthUserDetails getAuthUserDetails(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        return (AuthUserDetails) token.getPrincipal();
    }
}
