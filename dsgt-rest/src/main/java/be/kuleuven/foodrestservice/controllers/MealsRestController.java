package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/rest/meals/{id}")
    EntityModel<Meal> getMealById(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id).orElseThrow(() -> new MealNotFoundException(id));

        return mealToEntityModel(id, meal);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }

    @GetMapping("/rest/meals/getCheapestMeal")
    EntityModel<Meal> getCheapestMeal() {
        Collection<Meal> meals = mealsRepository.getAllMeal();
        Meal cheapest = meals.stream().min(Comparator.comparing(Meal::getPrice)).orElseThrow(NoSuchElementException::new);

        return mealToEntityModel(cheapest.getId(), cheapest);
    }

    @GetMapping("/rest/meals/getLargestMeal")
    EntityModel<Meal> getLargestMeal() {
        Collection<Meal> meals = mealsRepository.getAllMeal();
        Meal largest = meals.stream().max(Comparator.comparing(Meal::getKcal)).orElseThrow(NoSuchElementException::new);

        return mealToEntityModel(largest.getId(), largest);
    }

    @RequestMapping(value = "/rest/meals/addMeal", method = RequestMethod.POST)
    EntityModel<Meal>  addMeal(@RequestBody Meal meal) {
        mealsRepository.addMeal(meal);
        System.out.println("Meal successfully added!");
        return mealToEntityModel(meal.getId(), meal);
    }

    @RequestMapping(value = "/rest/meals/updateMeal", method = RequestMethod.PUT)
    void updateMeal(@RequestBody Meal meal) {
        Meal originalMeal = mealsRepository.findMeal(meal.getId()).get();
        originalMeal.setName(meal.getName());
        originalMeal.setMealType(meal.getMealType());
        originalMeal.setPrice(meal.getPrice());
        originalMeal.setKcal(meal.getKcal());
        originalMeal.setDescription(meal.getDescription());
        System.out.println("Meal successfully updated!");
    }

    @DeleteMapping("/rest/meals/{id}")
    ResponseEntity<Object> deleteMeal(@PathVariable String id) {
        mealsRepository.deleteMeal(id);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/rest/meals/addOrder")
    ResponseEntity<Object> addOrder(@RequestBody JSONObject orderInfo) {
        Order newOrder = new Order();
        newOrder.setAddress(orderInfo.get("address").toString());
        ArrayList<String> ids = (ArrayList<String>) orderInfo.get("ids");

        for(String id: ids){
            newOrder.addMealToOrder(id);
        }
        mealsRepository.addOrder(newOrder);
        return ResponseEntity.noContent().build();

    }





    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"));
    }

}
