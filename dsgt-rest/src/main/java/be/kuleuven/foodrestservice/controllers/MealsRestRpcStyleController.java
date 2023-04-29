package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealType;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }
    @GetMapping("/restrpc/meals/getCheapestMeal")
    Meal getCheapestMeal() {
        Collection<Meal> meals = mealsRepository.getAllMeal();
        Optional<Meal> meal = Optional.of(meals.stream().min(Comparator.comparing(Meal::getPrice)).orElseThrow(NoSuchElementException::new));

        return meal.orElseThrow(() -> new MealNotFoundException(meal.get().getId()));
    }

    @GetMapping("/restrpc/meals/getLargestMeal")
    Meal getLargestMeal() {
        Collection<Meal> meals = mealsRepository.getAllMeal();
        Optional<Meal> meal = Optional.of(meals.stream().max(Comparator.comparing(Meal::getKcal)).orElseThrow(NoSuchElementException::new));

        return meal.orElseThrow(() -> new MealNotFoundException(meal.get().getId()));
    }



    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @PostMapping(value = "/restrpc/meals/addMeal")
    @ResponseStatus(HttpStatus.CREATED)
    void addMeal(@RequestBody JSONObject jsonMeal) {
        System.out.println("AddMeal: Incoming JSON data: " + jsonMeal);
        //create Meal object
        Meal newMeal = new Meal();
        //construct meal
        newMeal.setId("12345");
        newMeal.setName((String) jsonMeal.get("name"));
        newMeal.setPrice((double) jsonMeal.get("price"));
        newMeal.setDescription((String) jsonMeal.get("description"));
        newMeal.setMealType(MealType.fromValue(jsonMeal.get("mealType").toString().toLowerCase()));
        newMeal.setKcal((int) jsonMeal.get("kcal"));
        //add meal to array
        mealsRepository.addMeal(newMeal);
        System.out.println("Meal successfully added!");
    }

    @PutMapping(value = "/restrpc/meals/updateMeal")
    void updateMeal(@RequestBody JSONObject jsonMeal) {
        System.out.println("updateMeal: Incoming JSON data: " + jsonMeal);

        //create Meal object
        Meal newMeal = new Meal();
        //construct meal
        newMeal.setId((String) jsonMeal.get("id"));
        newMeal.setName((String) jsonMeal.get("name"));
        newMeal.setPrice((double) jsonMeal.get("price"));
        newMeal.setDescription((String) jsonMeal.get("description"));
        newMeal.setMealType(MealType.fromValue(jsonMeal.get("mealType").toString().toLowerCase()));
        newMeal.setKcal((int) jsonMeal.get("kcal"));
        //add meal to array
        mealsRepository.updateMeal(newMeal);

        System.out.println("Meal successfully updated!");

    }

    @DeleteMapping("/restrpc/meals/{id}")
    void deleteMeal(@PathVariable String id) {
        mealsRepository.deleteMeal(id);
    }

    @PostMapping("/restrpc/meals/addOrder")
    void addOrder(@RequestBody JSONObject orderInfo) {
        Order newOrder = new Order();
        newOrder.setAddress(orderInfo.get("address").toString());
        ArrayList<String> ids = (ArrayList<String>) orderInfo.get("ids");

        for(String id: ids){
            newOrder.addMealToOrder(id);
        }
        mealsRepository.addOrder(newOrder);

    }

}
