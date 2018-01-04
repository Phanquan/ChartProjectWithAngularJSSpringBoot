package com.chart.service;


import com.chart.model.ExelData;
import com.chart.model.SeriesData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        try {
            String filePath = "/home/phanquan/IdeaProjects/ChartProjectWithAngularJSSpringBoot/tmp/data.xls";
            Map<String, Map<String, List<ExelData>>> list = ExelDataService.readExel(filePath);
            Map<String, List<ExelData>> newList = ExelDataService.getExelDatabyAvg(list);
//            System.out.print(list);
            List<SeriesData> sd = ExelDataService.getSeriesDataByDay(newList);
            System.out.print(sd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <KEY, VALUE> void put(Map<KEY, List<VALUE>> map, KEY key, VALUE value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
