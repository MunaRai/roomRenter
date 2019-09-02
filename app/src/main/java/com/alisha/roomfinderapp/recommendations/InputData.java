package com.alisha.roomfinderapp.recommendations;

import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.models.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class InputData {

    private List<Room> items;

    public InputData(List<Room> items) {
        this.items = items;
    }

    public List<Room> getItems() {
        return items;
    }

    public Map<User, HashMap<Room, Double>> initializeData(int numberOfUsers) {
        Map<User, HashMap<Room, Double>> data = new HashMap<>();
        HashMap<Room, Double> newUser;
        Set<Room> newRecommendationSet;
        for (int i = 0; i < numberOfUsers; i++) {
            newUser = new HashMap<Room, Double>();
            newRecommendationSet = new HashSet<>();
            for (int j = 0; j < 3; j++) {
                newRecommendationSet.add(items.get((int) (Math.random() * 5)));
            }
            for (Room item : newRecommendationSet) {
                newUser.put(item, Math.random());
            }
//            data.put(new User("User " + i), newUser);
        }
        return data;
    }

}