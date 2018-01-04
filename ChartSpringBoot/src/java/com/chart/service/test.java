package com.chart.service;


import com.chart.model.ExelData;
import com.chart.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) {
        try {
            String filePath = "/home/phanquan/IdeaProjects/ChartProjectWithAngularJSSpringBoot/tmp/data.xls";
            Map<String,Map<String, List<ExelData>>> list = ExelDataService.readExel(filePath);
            Map<String,List<ExelData>> newList = ExelDataService.getExelDatabyAvg(list);
            System.out.print(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, List<Person>> map = new HashMap<>();

        put(map, "first", new Person(0,"a"));
        put(map, "first",  new Person(1,"b"));
        put(map, "second",  new Person(2,"c"));
        put(map, "first",  new Person(3,"d"));

        System.out.print(map.toString());

    }

    private static <KEY, VALUE> void put(Map<KEY, List<VALUE>> map, KEY key, VALUE value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
