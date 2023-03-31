package fr.hedwin.excel;

import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class CellProperty {

    private final int row;
    private final int column;
    private Object value;
    private XSSFCellStyle cellStyle;

    public CellProperty(int row, int column, Object value, XSSFCellStyle cellStyle){
        this.row = row;
        this.column = column;
        this.value = value;
        this.cellStyle = cellStyle;
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public XSSFCellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(XSSFCellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
