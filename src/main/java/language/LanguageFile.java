package language;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Objects;

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
        return StringUtils.upperCase(StringUtils.substringBefore(fileName, "."));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageFile that = (LanguageFile) o;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, fileName);
    }
}
