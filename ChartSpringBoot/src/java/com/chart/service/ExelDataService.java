package com.chart.service;

import com.chart.model.ExelData;
import com.chart.model.SeriesData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.sling.commons.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExelDataService {

    public static Map<String, List<ExelData>> readExel(String filePath) throws IOException {
        FileInputStream is = new FileInputStream(new File(filePath));
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        Map<String, Map<String, List<ExelData>>> rawData = getExelDataWithMap(workbook);
        Map<String, List<ExelData>> exelData = getExelDatabyAvg(rawData);
        return exelData;
    }

    public static Map<String, Map<String, List<ExelData>>> getExelDataWithMap(HSSFWorkbook workbook) {
        Map<String, Map<String, List<ExelData>>> ed = new LinkedHashMap<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Map<String, List<ExelData>> mapOfExelData = new LinkedHashMap<>();
            HSSFSheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            if (sheet.getSheetName().equals("Example")) break;
            Iterator<Row> rowIter = sheet.rowIterator();
            while (rowIter.hasNext()) {
                Row row = rowIter.next();
                if (isRowEmpty(row)) break;
                if (row.getRowNum() == 0) continue;

                for (int j = 5; j >= 0; j--) {
                    String datetime = row.getCell(j).toString();
                    if (!checkDate(datetime)) continue;
                    putToMap(
                            mapOfExelData,
                            datetime,
                            new ExelData(
                                    row.getCell(j).toString(),
                                    row.getCell(j + 6).toString().replaceAll("[^.0-9]+", ""),
                                    row.getCell(12).toString().replaceAll("[^.0-9]+", "")
                            )
                    );
                }
            }
            ed.put(sheetName, mapOfExelData);
        }
        return ed;
    }


    public static Map<String, List<ExelData>> getExelDatabyAvg(Map<String, Map<String, List<ExelData>>> mapOfData) {
        Map<String, List<ExelData>> eda = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, List<ExelData>>> map : mapOfData.entrySet()) {
            List<ExelData> led = new ArrayList<>();
            for (Map.Entry<String, List<ExelData>> pair : map.getValue().entrySet()) {
                float countPA = 0;
                float countPS = 0;
                for (ExelData ex : pair.getValue()) {
                    countPA += Float.parseFloat(ex.getPa());
                    countPS += Float.parseFloat(ex.getPs());
                }
                led.add(new ExelData(pair.getKey(), Float.toString(countPA / pair.getValue().size()), Float.toString(countPS / pair.getValue().size())));
            }
            eda.put(map.getKey(), led);
        }
        return eda;
    }

    public static List<SeriesData> getSeriesDataByDay(Map<String, List<ExelData>> ld) {
        List<SeriesData> seriesDataByDay = new ArrayList<>();
        int k = 0;
        for (Map.Entry<String, List<ExelData>> entry : ld.entrySet()) {
            String name = entry.getKey();
            List<ExelData> ed = entry.getValue();
            List<Double> dataPA = new ArrayList<>();
            for (int i = 0; i < ed.size(); i += 144) {
                double countPA = 0;
                for (int j = i; j < i + 144; j++) {
                    countPA += Double.parseDouble(ed.get(j).getPa());
                }
                dataPA.add(Double.parseDouble(new DecimalFormat(".##").format(countPA / 6)));
            }
            seriesDataByDay.add(new SeriesData(k, name, dataPA));
            k++;
        }
        return seriesDataByDay;
    }

    public static List<List<List<SeriesData>>> getSeriesDataByHour(Map<String, List<ExelData>> md) {
        List<List<List<SeriesData>>> listDataByHour = new ArrayList<>();
        for (Map.Entry<String, List<ExelData>> entryOfMD : md.entrySet()) {
            List<List<SeriesData>> listDataBySheetIndex = new ArrayList<>();
            for (int i = 0; i < entryOfMD.getValue().size(); i += 144) {
                List<Double> dataPA = new ArrayList<>();
                List<Double> dataPS = new ArrayList<>();
                for (int j = i; j < i + 144; j++) {
                    dataPA.add(Double.parseDouble(entryOfMD.getValue().get(j).getPa()));
                    dataPS.add(Double.parseDouble(entryOfMD.getValue().get(j).getPs()));
                }
                listDataBySheetIndex.add(Arrays.asList(new SeriesData("PA", dataPA), new SeriesData("PS", dataPS)));
            }
            listDataByHour.add(listDataBySheetIndex);
        }
        return listDataByHour;
    }


    private static <KEY, VALUE> void putToMap(Map<KEY, List<VALUE>> map, KEY key, VALUE value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    private static boolean checkDate(String date) {

        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss+0000");
        int day = LocalDateTime.from(f.parse(date)).getDayOfMonth();
        return day >= 24 && day <= 30;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    public static String getJsonFromObjByDay(List<SeriesData> lsd, String chartTitle, String chartSubTitle, List<String> chartXAxisCate, String chartYAxisTitleText) throws JSONException {

        JSONObject jo = new JSONObject();

        JSONArray jr = new JSONArray();
        for (int i = 0; i < lsd.size(); i++) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", lsd.get(i).getId());
            jsonObj.put("data", lsd.get(i).getData());
            jsonObj.put("name", lsd.get(i).getName());
            jr.add(jsonObj);
        }

        jo.put("seriesData", jr);
        jo.put("chartTitle", chartTitle);
        jo.put("chartSubTitle", chartSubTitle);
        jo.put("chartXAxisCate", chartXAxisCate);
        jo.put("chartYAxisTitleText", chartYAxisTitleText);
        return jo.toJSONString();
    }

    public static String getJsonFromObjByHour(List<List<List<SeriesData>>> ldbh, int indexOfSheet, int indexOfColumn, String chartTitle, String chartSubTitle, List<String> chartXAxisCate, String chartYAxisTitleText) throws JSONException {
        JSONObject jo = new JSONObject();
        JSONArray jaFinal = new JSONArray();

        for (int i = 0; i < ldbh.size(); i++) {
            if (i == indexOfSheet) {
                for (int j = 0; j < ldbh.get(i).size(); j++) {
                    if (j == indexOfColumn) {
                        JSONObject joPA = new JSONObject();
                        JSONObject joPS = new JSONObject();
                        joPA.put("name", ldbh.get(i).get(j).get(0).getName());
                        joPA.put("data", ldbh.get(i).get(j).get(0).getData());
                        joPS.put("name", ldbh.get(i).get(j).get(1).getName());
                        joPS.put("data", ldbh.get(i).get(j).get(1).getData());
                        jaFinal.add(Arrays.asList(joPA, joPS));
                    }
                }
            }
        }

        jo.put("seriesDataByHour", jaFinal);
        jo.put("chartTitle", chartTitle);
        jo.put("chartSubTitle", chartSubTitle);
        jo.put("chartXAxisCate", chartXAxisCate);
        jo.put("chartYAxisTitleText", chartYAxisTitleText);

        return jo.toJSONString();
    }
}
