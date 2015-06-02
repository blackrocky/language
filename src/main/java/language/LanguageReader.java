package language;

import org.apache.commons.lang.StringUtils;

public class LanguageReader {

    public static String getWord(String word) {
        if (StringUtils.isBlank(word)) {
            throw new IllegalStateException();
        } 

        return word;
    }
}
