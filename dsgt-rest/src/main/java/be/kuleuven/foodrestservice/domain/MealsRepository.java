package be.kuleuven.foodrestservice.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class MealsRepository {
    // map: id -> meal
    private static final Map<String, Meal> meals = new HashMap<>();
    private ArrayList<Order> orders = new ArrayList<>();


    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setId("5268203c-de76-4921-a3e3-439db69c462a");
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealType(MealType.MEAT);
        a.setKcal(1100);
        a.setPrice((10.00));

        meals.put(a.getId(), a);

        Meal b = new Meal();
        b.setId("4237681a-441f-47fc-a747-8e0169bacea1");
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealType(MealType.VEGAN);
        b.setKcal(637);
        b.setPrice((7.00));

        meals.put(b.getId(), b);

        Meal c = new Meal();
        c.setId("cfd1601f-29a0-485d-8d21-7607ec0340c8");
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealType(MealType.FISH);
        c.setKcal(950);
        c.setPrice(5.00);

        meals.put(c.getId(), c);
    }

    public Optional<Meal> findMeal(String id) {
        Assert.notNull(id, "The meal id must not be null");
        Meal meal = meals.get(id);
//        System.out.println("meal: " + meal.getName() + " / " + meal.getId());
        System.out.println("searching for meal...");
        return Optional.ofNullable(meal);
    }

    public Collection<Meal> getAllMeal() {
        return meals.values();
    }

    public void addMeal(Meal meal){
        System.out.println("inserting meal with id " + meal.getId());
        meals.put(meal.getId(), meal);
        System.out.println("Successfully added meal in MealsRepository!");
    }

    public void updateMeal(Meal meal){
        System.out.println("updating meal with id " + meal.getId());
        Meal oldMeal = findMeal(meal.getId()).get();
        oldMeal.setName(meal.getName());
        oldMeal.setKcal(meal.getKcal());
        oldMeal.setDescription(meal.getDescription());
        oldMeal.setMealType(meal.getMealType());
        oldMeal.setPrice(meal.getPrice());

        System.out.println("Successfully updated meal in MealsRepository!");
    }

    public void deleteMeal(String id){
        meals.remove(id);
        System.out.println("Successfully deleted meal in MealsRepository!");

    }

    public void addOrder(Order newOrder){
        orders.add(newOrder);
        System.out.println("Successfully added an order to the queue in MealsRepository!\nsize of queue is now: " + orders.size());

    }
}
