package language;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public final class LanguageFile {
    private final List<String> lines;
    private final String parent;
    private final String fileName;

    public LanguageFile(final List<String> lines, final String parent, final String fileName) {
        this.parent = parent;
        this.lines = lines;
        this.fileName = fileName;
    }

    public String getParent() {
        return parent;
    }

    public final List<String> getLines() {
        return lines;
    }

    public final String getFileName() {
        return fileName;
    }

    public final String getLanguage() {
        return StringUtils.substringBefore(fileName, ".");
    }
}
