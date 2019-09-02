package com.alisha.roomfinderapp.utils;

import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.Date;
import java.util.Random;

public class AutomateData {

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    public static final String[] names = {
            "Sanjeeta",
            "Alisha",
            "Muna",
            "Rukshana",
            "Priya",
            "Hari",
            "Shanti",
            "Menuka",
            "Renuka",
            "Devaki",
            "Manish",
            "Pawon",
            "Rimisha",
            "Manu",
    };


    public static final String[] images = {
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmB3_-FJss4EU5m9UcS?alt=media&token=9d568f72-d6ea-41fc-bb38-b303b82c7650",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmCx6pSIe-gqh_xdCq_?alt=media&token=4fefcc6e-aa44-435f-845d-8ebe9bd232ac",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmCy0ObopR4nDIH8gPd?alt=media&token=9ea4d687-057b-4e77-9d3b-09aa90115a3b",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmCypbdvvWF_DdOqxst?alt=media&token=5b1f5ad4-14c6-4745-863b-db8e58cc82c4",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmCznCPnvDt4pjdwWR4?alt=media&token=c278580f-edcb-465d-931d-40288125ff41",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmD0BOCfZFOy-V6AbeW?alt=media&token=0920534d-2a6f-43a2-af46-e6ffa4af88e2",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmD24NKvDFQjJIGzM4G?alt=media&token=9748b0c5-e893-4f0e-90cc-1c9796ee8855",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmD3NiKXK-IF4yyaoj1?alt=media&token=ba49fd62-d99e-4d08-82d2-9aa17d5d07d7",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmD4Z8VpkcG3r5GoQ6F?alt=media&token=17be7395-d43b-4702-8725-22b83855c7cc",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmB3_-FJss4EU5m9UcS?alt=media&token=9d568f72-d6ea-41fc-bb38-b303b82c7650",
            "https://firebasestorage.googleapis.com/v0/b/kathfordroomfinder.appspot.com/o/rooms%2Froom-LmD3NiKXK-IF4yyaoj1?alt=media&token=ba49fd62-d99e-4d08-82d2-9aa17d5d07d7",

    };

    public static final String[] room_names = {
            "Beautiful room at Lalitpur",
            "Stunning room at Kathmandu",
            "Room 909",
            "Room 909",
            "Need tenant at Bagbazar",
            "Beautiful room at Lalitpur",
            "Room 3932",
            "Beautiful room at Lalitpur",
            "Beautiful room at Lalitpur",
            "Room 404",
    };

    public static final String[] districts = {
            "Kathmandu",
            "Lalitpur",
            "Kathmandu",
            "Bhaktapur",
            "Kathmandu",
            "Bhaktapur",
            "Lalitpur",
            "Lalitpur",
            "Lalitpur",
            "Bhaktapur",
    };
    public static final String[] locations = {
            "Lalitpur,kharibot",
            "Lalitpur,satobato",
            "New Baneswor",
            "Bhaktapur Durbar Square, Durbar square, Bhaktapur",
            "Bhaktapur Cancer Hospital, Bhaktapur",
            "Lalitpur Nursing Campus, Milap Road, Lalitpur",
            "Baneshwor Spa Swimming Pool",
            "Lagankhel Satdobato Road, Lalitpur",
            "SallaGhari, Bhaktapur",
            "Bhaktapur Heart & Diabetic Center, Araniko Highway, SallaGhari, Suryabinayak",
            "Civil Service Hospital Of Nepal, Minbhawan Marg, Kathmandu",
            "Old Baneshwor Photo Studio, Devkota Sadak, Kathmandu",
            "Kathmandu Forestry College, Koteshwor, Kathmandu",
            "Tinkune Park, Ring Road, Kathmandu",
    };
    public static final String[] users_id = {
            "8S1oRCvFIKOETS0KoIuK6OzFxRE3",
            "9tmf1eobp9QwC97zsO6itqR99Xt2",
            "F6XpPw3v52Xru40T2qirtryNm683",
            "GzrwL7xCxeSHWEEjIj1Xj9Vu5Tk2",
            "LCVf48oQvdee7lIodX4NQWCA9eV2",
            "PlJBuxfC97h2agWtBuIdyQLlBRy2",
            "SG0XMtZqJFglarCUjACldSCQeqy1",
            "ai7uwB8zopcBLQBtTomxXYPy5c42",
            "ai7uwB8zopcBLQBtTomxXYPy5c42",
            "ai7uwB8zopcBLQBtTomxXYPy5c42",
            "ai7uwB8zopcBLQBtTomxXYPy5c42",

    };

    public static final String[] latitudes = {
            "27.647742",
            "27.6520426",
            "27.693742",
            "27.6720744",
            "27.6730889",
            "27.6810742",
            "27.6931967",
            "27.6678589",
            "27.6724328",
            "27.671995",
            "27.6863272",
            "27.7014349",
            "27.674822",
            "27.6853659",
    };

    public static final String[] longitudes = {
            "85.3337993",
            "85.3197133",
            "85.3346323",
            "85.4259136",
            "85.4199622",
            "85.303104",
            "85.342734",
            "85.3196534",
            "85.4043844",
            "85.4043373",
            "85.3366222",
            "85.3378961",
            "85.3389752",
            "85.3466791",

    };
    public static final String[] description = {
            "Includes balcony with fresh air. The room contains sufficient sunlight and very cool bathroom.",
            "Very good community with all necessary facilities around with equally good and attractive room.",
            "This room is specially designed for a family. If you have a family and looking for room then this is the best option.",
            "If you are a student and looking for a peaceful room, then this room will be a best option for you. Every necesssary facilities are available inside the room. with profficient sunlight an fresh air around.",
            "The room is wide with profficient sunlight. The area of room is 10 x 10. You will find big space for your belongings.",
            "Area of this room is 10 x 12. The room is attached with a loveley balcony with a fresh air. A washroom is attached for convinience.",
            "This room is specially for students and youngsters to find a peaceful space which will help in their studies. We will like to clearly state that its a peaceful place and person who would like to party  hard and disturb the peace are not recoomended or this room.",
            "The location of this room makes the room a great space for families and students at the same time. Also the faclites available inside the room will make you happy and satidfied.",
            "If you are looking for a lovely room. Then this is the best option for you. The facilities of water, 24 hr electricty supplies and good locality makes the room even more cool.",
            "If you are looking for a lovely room. Then this is the best option for you. The facilities of water, 24 hr electricty supplies and good locality makes the room even more cool."

    };
    public static final long no_rooms = 4;
    public static final String date = DateTimeUtils.formatDate(new Date());
    public static final String[] reviews = {
            "Not good",
            "Cozy and dark",
            "Comfortable space but noisy environment",
            "Beautiful Environment and best for family",
            "Happy with spacious room",
            "Suitable room with parking facility",
            "Good but shortage of water",
            "Very good",
            "Nice",
            "Matched with my expectation, really happy :) :)",
            "Dimension of room is good and nice view ",
            "Size of room is ok but kitchen is so small and congested",
            "Large space room with fresh air. so happy.",
            "Room is good",
    };

    public static final String[] price = {
            "4000",
            "5000",
            "6000",
            "4500",
            "6000",
            "5500",
            "7000",
            "8000",
            "6500",
            "7500",
            "3500",
            "8500",
            "9000",
            "7000",
    };


}
