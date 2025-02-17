package org.example.service;


import org.example.model.Order;
import org.example.repisotory.OrderRepository;
import org.example.model.Product;
import org.example.repisotory.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Optional<Order> getOrder(Long id) {
        logger.info("Fetching order with id: {}", id);
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return orderRepository.findAll();
    }

    @Transactional
    public Order addOrder(Order order) {
        logger.info("Adding a new order");
        saveProducts(order);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(Long id, Order order) {
        logger.info("Updating order with id: {}", id);
        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        saveProducts(order);
        existingOrder.setProducts(order.getProducts());
        existingOrder.calculateTotalCost();
        return orderRepository.save(existingOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        logger.info("Deleting order with id: {}", id);
        orderRepository.deleteById(id);
    }

    private void saveProducts(Order order) {
        if (order.getProducts() != null) {
            for (Product product : order.getProducts()) {
                if (product.getId() == null || !productRepository.existsById(product.getId())) {
                    productRepository.saveAndFlush(product);
                }
            }
        }
    }
}