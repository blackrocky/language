package language;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class FileTest {
    @Test
    public void should_return_upper_case_filename() {
        File file = new File(Arrays.asList(new String[]{"hello", "world"}), "directory", "english");

        assertThat(file.getParent(), is("directory"));
        assertThat(file.getLanguage(), is("ENGLISH"));
        assertThat(file.getLines(), notNullValue());
        assertThat(file.getLines().get(0), is("hello"));
        assertThat(file.getLines().get(1), is("world"));
    }
}