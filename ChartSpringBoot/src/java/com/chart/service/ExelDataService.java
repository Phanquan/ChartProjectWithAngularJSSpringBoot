package com.chart.service;

import com.chart.model.ExelData;
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
import java.util.*;

public class ExelDataService {
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

    public static  Map<String,Map<String, List<ExelData>>> readExel(String filePath) throws IOException {
        FileInputStream is = new FileInputStream(new File(filePath));
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        Map<String,Map<String, List<ExelData>>> cellValues = getExelDataWithMap(workbook);
        return cellValues;

    }

    private static List<List<ExelData>> getExelData(HSSFWorkbook workbook) {
        List<List<ExelData>> ed = new ArrayList<List<ExelData>>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

            List<ExelData> listOfExelData = new ArrayList<ExelData>();
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().equals("Example")) break;
            Iterator<Row> rowIter = sheet.rowIterator();
            while (rowIter.hasNext()) {
                Row row = rowIter.next();
                if (isRowEmpty(row)) break;
                if (row.getRowNum() == 0) continue;
                for (int j = 5; j >= 0; j--) {
                    listOfExelData.add(new ExelData(row.getCell(j).toString(), row.getCell(j + 6).toString(), row.getCell(12).toString()));
                }
            }
            ed.add(listOfExelData);
        }
        return ed;
    }

    public static Map<String,Map<String, List<ExelData>>> getExelDataWithMap(HSSFWorkbook workbook) {
        Map<String,Map<String, List<ExelData>>> ed = new LinkedHashMap<>();
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
            ed.put(sheetName,mapOfExelData);
        }
        return ed;
    }

    public static String getJsonFromObj(List<List<ExelData>> led) throws JSONException {

        JSONArray js = new JSONArray();

        for (int i = 0; i < led.size(); i++) {
            JSONArray jr = new JSONArray();
            for (int j = 0; j < led.get(i).size(); j++) {
                JSONObject jobj = new JSONObject();
                jobj.put("datapa", led.get(i).get(j).getDatapa());
                jobj.put("pa", led.get(i).get(j).getPa());
                jobj.put("ps", led.get(i).get(j).getPs());
                jr.add(jobj);
            }
            js.add(jr);
        }

        return js.toJSONString();
    }

    private static <KEY, VALUE> void putToMap(Map<KEY, List<VALUE>> map, KEY key, VALUE value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public static Map<String,List<ExelData>> getExelDatabyAvg(Map<String,Map<String, List<ExelData>>> mapOfData) {
        Map<String,List<ExelData>> eda = new LinkedHashMap<>();
        for (Map.Entry<String,Map<String, List<ExelData>>> map : mapOfData.entrySet()) {
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
            eda.put(map.getKey(),led);
        }

        return eda;
    }

}
