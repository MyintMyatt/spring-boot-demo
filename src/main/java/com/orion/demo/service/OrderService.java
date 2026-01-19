package com.orion.demo.service;

import com.orion.demo.constant.OrderStatus;
import com.orion.demo.dto.request.OrderItemRequest;
import com.orion.demo.dto.request.OrderRequest;
import com.orion.demo.entity.Customer;
import com.orion.demo.entity.Order;
import com.orion.demo.entity.OrderItem;
import com.orion.demo.entity.Product;
import com.orion.demo.repo.CustomerRepository;
import com.orion.demo.repo.OrderRepository;
import com.orion.demo.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order placeOrder(OrderRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.CREATED)
                .build();

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal itemTotal =
                    product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            total = total.add(itemTotal);

            items.add(OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .price(product.getPrice())
                    .build());
        }

        order.setItems(items);
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }
}

