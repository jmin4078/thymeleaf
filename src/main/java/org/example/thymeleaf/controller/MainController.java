package org.example.thymeleaf.controller;

import lombok.RequiredArgsConstructor;
import org.example.thymeleaf.config.AppProperties;
import org.example.thymeleaf.entity.Pizza;
import org.example.thymeleaf.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MainController {
    private final AppProperties appProperties;
//    private final String msg;
//
//    public MainController(
//            AppProperties appProperties,
//            @Value("${app.message}") String msg) {
//        this.appProperties = appProperties;
//        this.msg = msg;
//    }

    @Value("${app.message}")
    private String msg; // 일종의 필드 주입

    // NoBeanRepository(JpaRepository)
    private final PizzaRepository pizzaRepository;

    @GetMapping
    public String index(Model model) {
//        model.addAttribute("data", "text-data");
        String data = """
                <script>alert('XSS!')</script>
                """;
        model.addAttribute("data", data);
        model.addAttribute("msg", appProperties.message());
        model.addAttribute("msg2", msg);
//        model.addAttribute("pizzas", pizzaRepository.findAll());
        model.addAttribute("pizzas",
                pizzaRepository.findAll().stream().map(PizzaDTO::fromEntity).toList());
        return "index";
    }

    public record PizzaDTO(String name, int price) {
        Pizza toEntity() {
            return Pizza.builder().name(name).price(price).build();
        }
        static PizzaDTO fromEntity(Pizza pizza) {
            return new PizzaDTO(pizza.getName(), pizza.getPrice());
        }
    }

    @PostMapping
    public String create(@ModelAttribute PizzaDTO pizzaDTO) {
        pizzaRepository.save(pizzaDTO.toEntity());
        return "redirect:/";
    }
}