package language;

import java.util.List;

public class LanguageFile {
    private List<String> lines;
    private String language;

    public LanguageFile(List<String> lines, String language) {
        this.lines = lines;
        this.language = language;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getLanguage() {
        return language;
    }
}
