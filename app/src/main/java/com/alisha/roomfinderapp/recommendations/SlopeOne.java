package com.alisha.roomfinderapp.recommendations;

import android.util.Log;

import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.models.User;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Slope One algorithm implementation
 */
public class SlopeOne {
    private static final String TAG = "SlopeOne";

    private static Map<Room, Map<Room, Double>> diff = new HashMap<>();
    private static Map<Room, Map<Room, Integer>> freq = new HashMap<>();
    private Map<User, HashMap<Room, Double>> inputData;
    private List<Room> roomList;
    private static Map<User, HashMap<Room, Double>> outputData = new HashMap<>();

    public SlopeOne(Map<User, HashMap<Room, Double>> inputData, List<Room> roomList) {
        this.inputData = inputData;
        this.roomList = roomList;
    }

    public Map<User, HashMap<Room, Double>> slopeOne() {
//        inputData = InputData.initializeData(numberOfUsers);
        System.out.println("Slope One - Before the Prediction\n");
        buildDifferencesMatrix(inputData);
        System.out.println("\nSlope One - With Predictions\n");
        return predict(inputData);
    }

    /**
     * Based on the available data, calculate the relationships between the
     * items and number of occurences
     *
     * @param data
     *            existing user data and their items' ratings
     */
    private void buildDifferencesMatrix(Map<User, HashMap<Room, Double>> data) {
        for (HashMap<Room, Double> user : data.values()) {
            for (Entry<Room, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<Room, Double>());
                    freq.put(e.getKey(), new HashMap<Room, Integer>());
                }
                for (Entry<Room, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (Room j : diff.keySet()) {
            for (Room i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            }
        }
        printData(data);
    }

    /**
     * Based on existing data predict all missing ratings. If prediction is not
     * possible, the value will be equal to -1
     *
     * @param data
     *            existing user data and their items' ratings
     */
    private Map<User, HashMap<Room, Double>> predict(Map<User, HashMap<Room, Double>> data) {
        HashMap<Room, Double> uPred = new HashMap<Room, Double>();
        HashMap<Room, Integer> uFreq = new HashMap<Room, Integer>();
        for (Room j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Entry<User, HashMap<Room, Double>> e : data.entrySet()) {
            for (Room j : e.getValue().keySet()) {
                for (Room k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                    } catch (NullPointerException e1) {
                    }
                }
            }
            HashMap<Room, Double> clean = new HashMap<Room, Double>();
            for (Room j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }

            for (Room j : roomList) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else {
                    clean.put(j, -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }
        return outputData;
//        printData(outputData);
    }

    private void printData(Map<User, HashMap<Room, Double>> data) {
        for (User user : data.keySet()) {
            System.out.println(user.getUsername() + ":");
            print(data.get(user));
        }
    }

    private void print(HashMap<Room, Double> hashMap) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        for (Room j : hashMap.keySet()) {
            Log.d(TAG, " " + j.getName() + " --> " + formatter.format(hashMap.get(j).doubleValue()));
        }
    }

}
