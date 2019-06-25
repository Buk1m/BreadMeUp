package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.dtos.*;
import com.pkkl.BreadMeUp.model.Order;
import com.pkkl.BreadMeUp.model.OrderProduct;
import com.pkkl.BreadMeUp.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/orders")
@RestController
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    /**
     * Get Order Details
     *
     * @param orderId order's unique identifier
     * @return OrderDetailsDto
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public OrderDetailsDto getOrderDetails(@PathVariable(name = "id") int orderId) {
        Order order = orderService.getById(orderId);
        OrderDetailsDto orderDto = mapOrderToOrderDetailsDto(order);
        UserDetailsDto userDto = modelMapper.map(order.getUser(), UserDetailsDto.class);
        orderDto.setUser(userDto);
        return orderDto;
    }

    /**
     * Get orders list
     *
     * @return OrderBasicDto
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<OrderBasicDto> getOrders() {
        List<Order> orders = orderService.getAll();
        return mapOrderListToOrderBasicDtoList(orders);
    }

    /**
     * Get own orders list
     *
     * @return OrderBasicDto
     */
    @GetMapping(
            value = "/my",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<OrderBasicDto> getOwnOrders(Principal principal) {
        List<Order> orders = orderService.getOrdersByPrincipal(principal);
        return mapOrderListToOrderBasicDtoList(orders);
    }

    /**
     * Cancel order
     *
     * @param orderId order id to cancel
     */
    @PatchMapping(
            value = "/cancel/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable(name = "id") int orderId, Principal principal) {
        orderService.cancelOrder(orderId);
    }

    /**
     * Create order
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@Valid @RequestBody final OrderCreateDto orderDto, Principal principal) {
        List<OrderProduct> orderProducts = orderDto.getOrderProducts().stream()
                .map(o -> this.modelMapper.map(o, OrderProduct.class))
                .collect(Collectors.toList());

        Order order = mapOrderCreateDtoToOrder(orderDto);
        orderService.add(order, orderProducts, principal);

    }

    /**
     * Complete order
     *
     * @param orderId order's identifier
     */
    @PatchMapping(
            value = "/complete/{id}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public OrderPriceDto completeOrder(@PathVariable(name = "id") int orderId, Principal principal, @Valid @RequestBody DiscountDto discountDto) {
        return orderService.completeOrder(orderId, principal, discountDto.getDiscountTypes());
    }

    //Mappers
    private Order mapOrderCreateDtoToOrder(OrderCreateDto orderCreateDtoDto) {
        return modelMapper.map(orderCreateDtoDto, Order.class);
    }

    private OrderDetailsDto mapOrderToOrderDetailsDto(Order order) {
        return modelMapper.map(order, OrderDetailsDto.class);
    }

    private OrderBasicDto mapOrderToOrderBasicDto(final Order order) {
        return modelMapper.map(order, OrderBasicDto.class);
    }

    private List<OrderBasicDto> mapOrderListToOrderBasicDtoList(final List<Order> orders) {
        return orders.stream()
                .map(this::mapOrderToOrderBasicDto)
                .collect(Collectors.toList());
    }

}
