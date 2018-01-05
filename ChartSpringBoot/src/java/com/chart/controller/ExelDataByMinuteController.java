package com.chart.controller;

import com.chart.model.ExelData;
import com.chart.model.SeriesData;
import com.chart.service.ExelDataService;
import org.apache.sling.commons.json.JSONException;
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
    private String filePath = "/home/phanquan/IdeaProjects/ChartProjectWithAngularJSSpringBoot/tmp/data.xls";

    @GetMapping("/getDataByMin/{iOfSheet}/{iOfColumn}")
    public String getDataByMinutes(@PathVariable("iOfSheet") int ioS, @PathVariable("iOfColumn") int ioC) {
        try {
            String chartTitle = "HourlyPA";
            String chartSubTitle = "(W43 24/10/2016 - 30/10/2016)";
            List<String> chartXAxisCate = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
            String chartYAxisTitleText = "Appelee (kW)";
            Map<String, List<ExelData>> mapData = ExelDataService.readExel(filePath);
            List<List<List<SeriesData>>> ldbh = ExelDataService.getSeriesDataByHour(mapData);
            return ExelDataService.getJsonFromObjByHour(ldbh, ioS, ioC, chartTitle, chartSubTitle, chartXAxisCate, chartYAxisTitleText);
        } catch (JSONException ejson) {
            ejson.printStackTrace();
            return "error parseJson";
        } catch (IOException eio) {
            eio.printStackTrace();
            return "error fileInput";
        }
    }
}
