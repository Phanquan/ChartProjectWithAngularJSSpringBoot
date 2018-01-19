package com.chart.controller;

import com.chart.model.ExelData;
import com.chart.model.SeriesData;
import com.chart.service.ExelDataService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ExelDataByDayController {

    private String filePath = "C:\\Users\\paquan\\Desktop\\ChartProjectWithAngularJSSpringBoot-master\\tmp\\data.xls";

    @GetMapping("/getExelData")
    public String greeting() {
        try {
            String chartTitle = "WeeklyPA";
            String chartSubTitle = "(W43 24/10/2016 - 30/10/2016)";
            List<String> chartXAxisCate = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
            String chartYAxisTitleText = "Puissance Appelee (kW)";
            Map<String, List<ExelData>> mapData = ExelDataService.readExel(filePath);
            List<SeriesData> sd = ExelDataService.getSeriesDataByDay(mapData);
            return ExelDataService.getJsonFromObjByDay(sd,chartTitle,chartSubTitle,chartXAxisCate,chartYAxisTitleText);
        } catch (IOException eio) {
            eio.printStackTrace();
            return "error fileInput";
        }
    }
}
