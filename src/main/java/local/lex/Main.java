package local.lex;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;


public class Main {

    private static final String pathToTemplate = "src/main/resources/template.docx";
    private static final String pathToResult = "src/main/resources/template-after.docx";

    public static void main(String[] args) {
        // Загрузка шаблона
        XWPFDocument template = loadTemplate(pathToTemplate);

        DocumentParser documentParser = new DocumentParser();
        DocumentListener documentListener = new DocumentListenerImpl();
        documentParser.doWork(template, documentListener);

        // Сохранение результата
        saveTemplate(template, pathToResult);

    }

    /**
     * Загрузка документа.
     * @param path - путь к документу.
     * @return загруженный документ.
     */
    public static XWPFDocument loadTemplate(String path) {
        XWPFDocument template;
        try (InputStream in = new FileInputStream(path)) {
            template = new XWPFDocument(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return template;
    }

    /**
     * Сохранение результата в файл.
     * @param template - обработанный документ.
     * @param path - путь по которому будет сохранен шаблон.
     */
    public static void saveTemplate(XWPFDocument template, String path) {
        try (OutputStream out = new FileOutputStream(path)) {
            template.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}