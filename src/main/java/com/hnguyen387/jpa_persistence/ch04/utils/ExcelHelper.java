package com.hnguyen387.jpa_persistence.ch04.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ExcelHelper {
	private static final Map<String, Boolean> BOOLEAN_MAP = new HashMap<>();
	private final Map<Short, CellStyle> styleCache = new HashMap<>();
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	static {
        BOOLEAN_MAP.put("Y", true);
        BOOLEAN_MAP.put("YES", true);
        BOOLEAN_MAP.put("TRUE", true);
        BOOLEAN_MAP.put("N", false);
        BOOLEAN_MAP.put("NO", false);
        BOOLEAN_MAP.put("FALSE", false);
    }
	private FormulaEvaluator evaluator;
	
	public ExcelHelper() {
	}
	
	public ExcelHelper(Workbook workbook) {
		this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	}
	// Factory method to create ExcelHelper with Workbook
    public static ExcelHelper createWithWorkbook(Workbook workbook) {
        return new ExcelHelper(workbook);
    }

    // Factory method to create ExcelHelper without Workbook
    public static ExcelHelper create() {
        return new ExcelHelper();
    }
	public Object readCell(Row row, int cellNumber) {
		Cell cell = row.getCell(cellNumber);
		if(cell == null)
			return null;
		return getCellValue(cell);
    }
	private Object getCellValue(Cell cell) {
		return switch (cell.getCellType()) {
	        case STRING -> cell.getStringCellValue().trim();
	        case NUMERIC -> DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : cell.getNumericCellValue();
	        case BOOLEAN -> cell.getBooleanCellValue();
	        case FORMULA -> handleFormulaCell(cell);
	        case BLANK -> String.valueOf("");
	        case ERROR -> new CellError(String.format("Cell error with error code: %d", (int) cell.getErrorCellValue()));
	        default -> new CellError("Unknown cell type");
		};
	}
	public Object handleFormulaCell(Cell cell) {
		CellValue cellValue = evaluator.evaluate(cell);
		return switch (cellValue.getCellType()) {
	        case STRING -> cellValue.getStringValue().trim();
	        case NUMERIC -> DateUtil.isCellDateFormatted(cell) ? DateUtil.getJavaDate(cellValue.getNumberValue()) : cellValue.getNumberValue();
	        case BOOLEAN -> cellValue.getBooleanValue();
	        case ERROR -> new CellError(String.format("Formula error with error code : %d", (int) cellValue.getErrorValue()));
	        default -> new CellError("Unknown formula result type");
		};
    }
	public Object parseBoolean(Object cellValue) {
		if (cellValue instanceof CellError error) {
			return error;
		}
		if (cellValue instanceof Boolean value) {
			return value;
		}
		String message = "Invalid input type - type must be: YES, NO, Y, N, TRUE, FALSE";
		if (cellValue instanceof String value) {
			Boolean booleanValue = BOOLEAN_MAP.get(value.toUpperCase());
			if (booleanValue != null) {
                return booleanValue;
            } else {
                return new CellError(message);
            }
		}
		return new CellError(message);
	}
	public Object parseDouble(Object cellValue) {
		if (cellValue instanceof CellError error) {
			return error;
		}
		if (cellValue instanceof Double value) {
            return value;
        }
		if (cellValue instanceof String value) {
	        try {
	            return Double.parseDouble(value);
	        } catch (NumberFormatException e) {
	            return new CellError("Invalid double format: " + e.getMessage());
	        }
	    }
		return new CellError("Unsupported type for Double parsing");
    }
	public Object parseLong(Object cellValue) {
		var value = parseDouble(cellValue);
		if (value instanceof Double valueLong) {
			return valueLong.longValue();
		}
		return value;
	}
	public Object parseInt(Object cellValue) {
        var value = parseDouble(cellValue);
		if (value instanceof Double valueInt) {
			return valueInt.intValue();
		}
		return value;
	}
	public Object parseDate(Object cellValue) {
		if (cellValue instanceof CellError error) {
			return error;
		}
		Date date = null;
		if (cellValue instanceof Date value) {
			date = value;
		} else if (cellValue instanceof String value) {
			try {
                date = DATE_FORMAT.parse(value);
            } catch (ParseException e) {
                return new CellError("Failed to parse date: " + cellValue + ", error: " + e.getMessage());
            }
		} else {
            return new CellError("Unsupported type for Date parsing");
        }
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	public CellStyle setFormatForCell(Workbook workbook, String format) {
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(format));
		return cellStyle;
	}
	public Date fromLocalDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	public void setAutoSize(Sheet sheet, int[] columns) {
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(columns[i]);
		}
	}
	public Validator buildValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
	public void createHeader(Row headerRow, String headers) {
		String[] titles = headers.split(",");
		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];
			headerRow.createCell(i).setCellValue(title);
		}
	}
	public void copyRow(Row sourceRow, Row newRow) {
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell oldCell = sourceRow.getCell(i);
            if (oldCell != null) {
                Cell newCell = newRow.createCell(i);
                copyCell(oldCell, newCell);
            }
        }
    }
	private void copyCell(Cell oldCell, Cell newCell) {
        switch (oldCell.getCellType()) {
            case STRING:
                newCell.setCellValue(oldCell.getStringCellValue());
                break;
            case NUMERIC:
            	handleNumericCell(oldCell, newCell);
                break;
            case BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            case BLANK:
                newCell.setBlank();
                break;
            case ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            default:
                newCell.setCellValue(oldCell.toString());
                break;
        }
        
    }
	private void copyCellFormat(Cell oldCell, Cell newCell) {
        Workbook newWorkbook = newCell.getSheet().getWorkbook();
        Short oldCellStyle = oldCell.getCellStyle().getDataFormat();

        CellStyle newCellStyle = styleCache.computeIfAbsent(oldCellStyle, k -> {
            CellStyle style = newWorkbook.createCellStyle();
            DataFormat dataFormat = newWorkbook.createDataFormat();
            style.setDataFormat(dataFormat.getFormat(oldCell.getCellStyle().getDataFormatString()));
            return style;
        });

        newCell.setCellStyle(newCellStyle);
    }
	private void handleNumericCell(Cell oldCell, Cell newCell) {
		if (DateUtil.isCellDateFormatted(oldCell)) {
            copyCellFormat(oldCell, newCell);
            newCell.setCellValue(oldCell.getDateCellValue());
        } else {
            newCell.setCellValue(oldCell.getNumericCellValue());
        }
	}
}
