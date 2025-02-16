package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/{id}")
    public Optional<Order> getOrder(@PathVariable Long id) {
        return orderRepository.findById(id);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Optional<Order> existingOrderOptional = orderRepository.findById(id);
        if (!existingOrderOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Order existingOrder = existingOrderOptional.get();

        existingOrder.setTotalCost(order.getTotalCost());
        existingOrder.setProducts(order.getProducts());

        Order updatedOrder = orderRepository.save(existingOrder);

        return ResponseEntity.ok(updatedOrder);
    }

}
