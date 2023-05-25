package com.lisi4ka.utils;

import com.lisi4ka.models.City;

import java.util.ArrayList;
import java.util.Date;

import static com.lisi4ka.common.ServerApp.cities;
import static com.lisi4ka.utils.CityLinkedList.idRepeat;

public class IdGenerator {
    public static long getUniqueId (Long id) {
        if (id == null) {
            throw new NullPointerException("Field id can not be null!");
        }
        if (id<1){
            throw new IllegalArgumentException("Field id can not be less than 1!");
        }
        ArrayList<Long> list = new ArrayList<>();
        for (City city : cities) {
            list.add(city.getId());
            if (list.contains(id)) {
                Date date = new Date();
                id = date.getTime() + idRepeat;
                idRepeat += 1;
                return id;
            }
        }
        return id;
    }
}
