package org.example.Homework41.service;

import org.example.Homework41.model.Order;
import org.example.Homework41.model.Product;
import org.example.Homework41.repository.OrderRepository;
import org.example.Homework41.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        // Process the products in the order
        Set<Product> processedProducts = order.getProducts().stream()
                .map(product -> {
                    if (product.getId() != null) {
                        // If product ID is provided, check if it exists in the database
                        return productRepository.findById(product.getId())
                                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + product.getId()));
                    } else {
                        // If product ID is null, save it as a new product
                        return productRepository.save(product);
                    }
                })
                .collect(Collectors.toSet());

        // Set the processed products back to the order
        order.setProducts(List.copyOf(processedProducts));

        // Calculate the total cost of the order
        BigDecimal totalCost = processedProducts.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalCost(totalCost);

        // Save and return the order
        return orderRepository.save(order);
    }




    public Order updateOrder(Long id, List<Product> updatedProducts) {
        return orderRepository.findById(id).map(order -> {
            order.setProducts(new ArrayList<>(updatedProducts)); // Ensure modifiable list
            return orderRepository.save(order);
        }).orElseThrow(() -> new Exception("Order not found"));
    }


    public void deleteOrder(Long id) {
        Assert.notNull(id, "Order ID must not be null");
        orderRepository.deleteById(id);
    }

}
