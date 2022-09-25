package local.lex;

import org.apache.poi.xwpf.usermodel.*;

import java.io.*;


public final class DocumentParser {

    /**
     * Обертка для вызова метода onBody(IBody body, DocumentListener listener).
     * @param document - документ или ячейка таблицы.
     * @param listener - слушатель событий парсинга body.
     */
    public void doWork(XWPFDocument document, DocumentListener listener) {
        listener.init();
        onBody(document, listener);
    }

    /**
     * Итерируется по всем элементам документа, вызывая соответсвующие методы-обработчики DocumentListener'a.
     * @param body - документ или ячейка таблицы.
     * @param listener - слушатель событий парсинга body.
     */
    private void onBody(IBody body, DocumentListener listener) {
        listener.onBodyEnter(body);
        for (IBodyElement element: body.getBodyElements()) {

            if (element.getElementType() == BodyElementType.TABLE) {
                XWPFTable table = (XWPFTable) element;
                listener.onTableEnter(table);

                for (XWPFTableRow row: table.getRows()) {
                    listener.onTableRowEnter(row);

                        for(XWPFTableCell cell : row.getTableCells()) {
                            listener.onTableCellEnter(cell);
                            onBody(cell, listener);
                            listener.onTableCellExit(cell);
                        }
                    listener.onTableRowExit(row);
                }
                listener.onTableExit(table);

            } else if (element.getElementType() == BodyElementType.PARAGRAPH) {
                XWPFParagraph paragraph = (XWPFParagraph) element;
                listener.onParagraphEnter(paragraph);

                for (XWPFRun run : paragraph.getRuns()) {
                    listener.onRunEnter(run);
                    listener.onRunExit(run);
                }
                listener.onParagraphExit(paragraph);

            } else {
                System.out.println("Skip unsupported element: " + element.getElementType());
            }
        }
        listener.onBodyExit(body);
    }
}
