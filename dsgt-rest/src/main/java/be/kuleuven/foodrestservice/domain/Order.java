package be.kuleuven.foodrestservice.domain;

import java.util.ArrayList;

public class Order {

    private String address;
    private ArrayList<String> mealIds = new ArrayList<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getMealIds() {
        return mealIds;
    }

    public void setMealIds(ArrayList<String> mealIds) {
        this.mealIds = mealIds;
    }
    public void addMealToOrder(String id){
        mealIds.add(id);
    }
}
