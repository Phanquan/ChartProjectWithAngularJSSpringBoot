package com.chart.controller;

import com.chart.model.ExelData;
import com.chart.service.ExelDataService;
import org.apache.sling.commons.json.JSONException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class ExelDataByDayController {

    @GetMapping("/getExelData")
    public String greeting() {
//        String filePath = "/home/phanquan/IdeaProjects/ChartProjectWithAngularJSSpringBoot/tmp/data.xls";
//        try{
//            List<List<ExelData>> ed = ExelDataService.readExel(filePath);
//            String result = ExelDataService.getJsonFromObj(ed);
//            return result;
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return "error parseJson";
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return "error input";
//        }
        return "";
    }
}
