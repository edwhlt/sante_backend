package fr.hedwin.excel;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;

public class CellProperties extends ArrayList<CellProperty> {

    private XSSFSheet sheet;

    public void updateToGrayBold(XSSFCellStyle style){
        style.setFillForegroundColor(new XSSFColor(new Color(191, 191, 191)));
        style.setFillPattern(SOLID_FOREGROUND);
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);
    }

    public CellProperties(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public void setValuesRow(int beginRow, int beginColumn, Object... objects){
        for(int i = 0; i < objects.length; i++){
            add(new CellProperty(beginRow, beginColumn+i, objects[i], sheet.getWorkbook().createCellStyle()));
        }
    }

    public void setValuesColumn(int beginRow, int beginColumn, Object... objects){
        for(int i = 0; i < objects.length; i++){
            add(new CellProperty(beginRow+i, beginColumn, objects[i], sheet.getWorkbook().createCellStyle()));
        }
    }

    public void addValue(int row, int column, Object objects, Consumer<XSSFCellStyle> style){
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        add(new CellProperty(row, column, objects, cellStyle));
        style.accept(cellStyle);
    }

    public void addValue(int row, int column, Object objects){
        add(new CellProperty(row, column, objects, sheet.getWorkbook().createCellStyle()));
    }

    public void updateStyle(int beginRow, int beginColumn, int lastRow, int lastColumn, Consumer<XSSFCellStyle> style){
        for(int r = beginRow; r <= lastRow; r++){
            for(int c = beginColumn; c <= lastColumn; c++){
                CellProperty cellProperty = get(r, c);
                if(cellProperty == null) {
                    cellProperty = new CellProperty(r, c, "", sheet.getWorkbook().createCellStyle());
                    add(cellProperty);
                }
                style.accept(cellProperty.getCellStyle());
            }
        }
    }

    @Override
    public boolean add(CellProperty cellProperty) {
        CellProperty cell = super.stream().filter(c -> c.getRow() == cellProperty.getRow() && c.getColumn() == cellProperty.getColumn()).findAny().orElse(null);
        if(cell == null) return super.add(cellProperty);
        return false;
    }

    public CellProperty get(int row, int column) {
        return super.stream().filter(c -> c.getRow() == row && c.getColumn() == column).findAny().orElse(null);
    }

    public boolean contains(int row, int column) {
        return super.stream().map(c -> c.getRow() == row && c.getColumn() == column).findAny().orElse(false);
    }

    public Map<Integer, List<CellProperty>> getRows(){
        return super.stream().collect(Collectors.groupingBy(CellProperty::getRow, HashMap::new, Collectors.toCollection(ArrayList::new)));
    }

}