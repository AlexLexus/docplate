package local.lex;

import org.apache.poi.xwpf.usermodel.*;

public abstract class DocumentListener {

    void init() {
        // Инициализация начальных параметров
        // System.out.println("Init DocumentListener");
    }

    void onBodyEnter(IBody element) {
        // System.out.println("Enter body");
    }

    void onBodyExit(IBody element) {
        // System.out.println("Exit body");
    }

    void onTableEnter(XWPFTable table) {
        // System.out.println("Enter table[" + table.getBody().getBodyElements().indexOf(table) + "]");
    }

    void onTableExit(XWPFTable table) {
        // System.out.println("Exit table[" + table.getBody().getBodyElements().indexOf(table) + "]");
    }

    void onParagraphEnter(XWPFParagraph paragraph) {
        // System.out.println("Enter paragraph[" + paragraph.getBody().getBodyElements().indexOf(paragraph) + "]");
    }

    void onParagraphExit(XWPFParagraph paragraph) {
        // System.out.println("Exit paragraph[" + paragraph.getBody().getBodyElements().indexOf(paragraph) + "]");
    }

    void onTableRowEnter(XWPFTableRow row) {
        // System.out.println("Enter row[" + row.getTable().getRows().indexOf(row) + "]");
    }

    void onTableRowExit(XWPFTableRow row) {
        // System.out.println("Exit row[" + row.getTable().getRows().indexOf(row) + "]");
    }

    void onTableCellEnter(XWPFTableCell cell) {
        // System.out.println("Enter table cell[" + cell.getTableRow().getTableCells().indexOf(cell) + "]");
    }

    void onTableCellExit(XWPFTableCell cell) {
        // System.out.println("Exit table cell[" + cell.getTableRow().getTableCells().indexOf(cell) + "]");
    }

    void onRunEnter(XWPFRun run) {
        // System.out.println("Enter run[" + ((XWPFParagraph) run.getParent()).getRuns().indexOf(run) + "]: "  + run.getText(0));
    }

    void onRunExit(XWPFRun run) {
        // System.out.println("Exit run[" + ((XWPFParagraph) run.getParent()).getRuns().indexOf(run) + "]: "  + run.getText(0));
    }
}
