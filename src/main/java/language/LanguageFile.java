package language;

import java.util.List;

public class LanguageFile {
    private List<String> lines;
    private String fileName;
    // TODO private Set<String> words;

    public LanguageFile(List<String> lines, String fileName) {
        this.lines = lines;
        this.fileName = fileName;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getFileName() {
        return fileName;
    }
}
