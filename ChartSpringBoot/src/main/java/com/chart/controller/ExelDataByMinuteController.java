package com.chart.controller;

import com.chart.model.ExelData;
import com.chart.model.SeriesData;
import com.chart.service.ExelDataService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ExelDataByMinuteController {
    private String filePath = "C:\\Users\\paquan\\Desktop\\ChartProjectWithAngularJSSpringBoot-master\\tmp\\data.xls";

    @GetMapping("/getDataByMin")
    public String getDataByMinutes() {
        try {
            String chartTitle = "HourlyPA";
            String chartSubTitle = "(W43 24/10/2016 - 30/10/2016)";
            String chartYAxisTitleText = "Appelee (kW)";
            Map<String, List<ExelData>> mapData = ExelDataService.readExel(filePath);
            List<List<List<SeriesData>>> ldbh = ExelDataService.getSeriesDataByHour(mapData);
            return ExelDataService.getJsonFromObjByHour(ldbh, chartTitle, chartSubTitle, chartYAxisTitleText);
        } catch (IOException eio) {
            eio.printStackTrace();
            return "error fileInput";
        }
    }
}
