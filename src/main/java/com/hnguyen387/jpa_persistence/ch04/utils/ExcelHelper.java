package com.hnguyen387.jpa_persistence.ch04.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.hnguyen387.jpa_persistence.ch04.dtos.UserDto;

public class ExcelHelper {
	private static final String POSITIVE_Y = "Y";
	private static final String POSITIVE_YES = "YES";
	private static final String NEGATIVE_N = "N";
	private static final String NEGATIVE_NO = "NO";
	
	
	public static void applyValue(Object value, UserDto object, BiConsumer<Object, UserDto> setter) {
		if (value instanceof CellError error) {
			object.getErrMessage().add(error);
			return;
		}
        setter.accept(value, object);
    }
	public static Object readCellData(Cell cell) {
		Object value = null;
		FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case FORMULA:
                value = handleFormulaCell(cell, evaluator);
                break;
            case BLANK:
                value = "";
                break;
            case ERROR:
            	byte errCode = cell.getErrorCellValue();
                value = new CellError(String.format("Cell error with error code: %d", (int)errCode));
                break;
            default:
                value = new CellError("Unknown cell type");
                break;
        }
        return value;
    }
	public static Object handleFormulaCell(Cell cell, FormulaEvaluator evaluator) {
		Object value = null;
        CellValue cellValue = evaluator.evaluate(cell);
        switch (cellValue.getCellType()) {
            case STRING:
            	value = cellValue.getStringValue();
                break;
            case NUMERIC:
            	if (DateUtil.isCellDateFormatted(cell)) {
                    value = DateUtil.getJavaDate(cellValue.getNumberValue());
                } else {
                    value = cellValue.getNumberValue();
                }
            case BOOLEAN:
            	value = cellValue.getBooleanValue();
                break;
            case ERROR:
            	byte errCode = cellValue.getErrorValue();
            	value = new CellError(String.format("Formula error with error code : %d", (int) errCode));
                break;
            default:
                value = new CellError("Unknown formula result type");
                break;
        }
        return value;
    }
	public static Object parseBoolean(Object cellValue) {
		if (cellValue instanceof CellError error) {
			return error;
		}
		if (cellValue instanceof Boolean value) {
			return value;
		}
		String message = "Value must be: YES, Y, NO, N, TRUE, FALSE";
		if (cellValue instanceof String value) {
			if (value.equalsIgnoreCase(POSITIVE_Y) || value.equalsIgnoreCase(POSITIVE_YES)) {
				return true;
			} else if (value.equalsIgnoreCase(NEGATIVE_N) || value.equalsIgnoreCase(NEGATIVE_NO)) {
				return false;
			} else {
				return new CellError(message);
			}
		}
		return new CellError(message);
	}
	public static Object parseDouble(Object cellValue) {
		if (cellValue instanceof CellError error) {
			return error;
		}
		if (cellValue instanceof Double value) {
            return value;
        }
        try {
            return Double.parseDouble(cellValue.toString());
        } catch (Exception e) {
            return new CellError(e.getMessage());
        }
    }
	public static Object parseLong(Object cellValue) {
		var value = parseDouble(cellValue);
		if (value instanceof Double valueLong) {
			return valueLong.longValue();
		}
		return value;
	}
	public static Object parseInt(Object cellValue) {
        var value = parseDouble(cellValue);
		if (value instanceof Double valueInt) {
			return valueInt.intValue();
		}
		return value;
	}
	public static Object parseDate(Object cellValue) {
		if (cellValue instanceof CellError error) {
			return error;
		}
		Date date = null;
		LocalDate localDate = null;
		if (cellValue instanceof Date value) {
			date = value;
		} else {
			String dateStr = cellValue.toString();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				return new CellError(e.getMessage());
			}
		}
		localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate;
	}
	public static CellStyle setFormatForCell(Workbook workbook, String format) {
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(format));
		return cellStyle;
	}
	public static Date fromLocalDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	public static void setAutoSize(Sheet sheet, int[] columns) {
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(columns[i]);
		}
	}
}