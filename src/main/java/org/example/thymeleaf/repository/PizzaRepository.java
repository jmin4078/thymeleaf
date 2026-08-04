package org.example.thymeleaf.repository;

import org.example.thymeleaf.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
}