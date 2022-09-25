package local.lex;

import org.apache.poi.xwpf.usermodel.*;

import java.util.*;

public class DocumentListenerImpl extends DocumentListener {

    private final String regexMarkFragment = "((.*\\$)|(.*\\$\\{.*))|((.*@)|(.*@\\{.*))";
    private final String regexPropertyName = "\\$\\{.*}";
    private final String regexPropertyMark = ".*" + regexPropertyName + ".*";
    private final String regexCollectionName = "@\\{.*}";
    private final String regexCollectionMark = ".*" + regexCollectionName + ".*";

    private final List<String> context = new ArrayList<>();
    private final Set<XWPFTable> tablesWithContext = new HashSet<>();

    @Override
    void init() {
        context.add("pyWorkPage");
    }

    @Override
    void onRunEnter(XWPFRun run) {
        String text = run.getText(0);

        XWPFParagraph currentParagraph = (XWPFParagraph) run.getParent();
        IBody currentBody = currentParagraph.getBody();
        int currentParagraphIndex = currentBody.getBodyElements().indexOf(currentParagraph);

        // Проверка что параграф находится в ячейке таблицы и вычисление индекса строки
        boolean isInRow = currentBody instanceof XWPFTableCell;
        int rowIndex = 0;
        if (isInRow) {
            XWPFTableRow currentRow = ((XWPFTableCell) currentBody).getTableRow();
            rowIndex = currentRow.getTable().getRows().indexOf(currentRow);
        }

        // Обработка отметок
        if (text.matches(regexCollectionMark)) {
            // Извлечение названия коллекции из текста
            String collectionName = text.substring(text.indexOf("@{") + 2, text.lastIndexOf("}"));

            // Когда в документе встречается отметка коллекции нужно удалить метку из текущего рана (1), увеличить
            // количество строк в следующей таблице (2) и переключить контекст (3)

            // (1) Удаление метки из текущего рана
            run.setText(run.getText(0).replaceAll(regexCollectionName, ""), 0);

            // TODO: (2) Увеличение количества строк в следующей таблице
            XWPFTable nextTable = (XWPFTable) currentBody.getBodyElements().get(currentParagraphIndex + 1);

            // (3) Переключение контекста
            switchContextToNext(collectionName, isInRow, nextTable);

        } else if (text.matches(regexPropertyMark)) {
            // Извлечение названия проперти из текста
            String propertyName = text.substring(text.indexOf("${") + 2, text.lastIndexOf("}"));
            String currentContext = context.get(context.size() - 1);

            String fullReference = currentContext + "." + propertyName;
            System.out.println(fullReference);

            // Метка значения должна быть прото заменена на значение, взятое из текущего контекста

            // Замена метки на значение
            run.setText(run.getText(0).replaceAll(regexPropertyName, fullReference), 0);

        } else if (text.matches(regexMarkFragment)) {
            List<XWPFRun> siblings = ((XWPFParagraph) run.getParent()).getRuns();
            XWPFRun nextRun = siblings.get(siblings.indexOf(run) + 1);
            nextRun.setText(text + nextRun.getText(0), 0);
            run.setText("", 0);

        }
    }

    @Override
    void onTableExit(XWPFTable table) {
        switchContextToPrevious(table);
    }

    /**
     * Переключение контекста "вперед".
     * @param collectionName - ключ контекста.
     * @param table - таблица для котоорой переключается контекст.
     */
    private void switchContextToNext(String collectionName, boolean isInRow, XWPFTable table) {
        String currentContext = context.get((context.size() - 1));
        context.add(currentContext + "." + collectionName);
        tablesWithContext.add(table);
    }

    /**
     * Переключение контекста "назад".
     * @param table - таблица для котоорой переключается контекст.
     */
    private void switchContextToPrevious(XWPFTable table) {
        if (tablesWithContext.contains(table)) {
            context.remove(context.get(context.size() - 1));
            tablesWithContext.remove(table);
        }
    }
}
