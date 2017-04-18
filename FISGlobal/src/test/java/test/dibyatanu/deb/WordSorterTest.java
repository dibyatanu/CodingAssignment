package test.dibyatanu.deb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WordSorterTest {
	private static final String FILE_NAME="src/test/resources/lines.txt";
    private static final String[] EXPECTED_SORTED_ARRAY={"cat","mat","on","sat","the","the"};
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void sortFileContent_shouldSortFileContentInAlphabeticalOrder() throws IOException {
		/*arrange**/
		Path path = Paths.get(FILE_NAME);
		/*act*/
		WordSorter wordSorter =new WordSorter();
		String[] sortedListOfFile= wordSorter.sortFileContent(path);
		/* assert*/
        assertTrue(sortedListOfFile.length >0);
        assertEquals(EXPECTED_SORTED_ARRAY, sortedListOfFile);

	}
	@Test
	public void printFileContent_ShouldReturnNoOfWordInTheFile() throws IOException
	{
		/*arrange**/
		Path path = Paths.get(FILE_NAME);
		/*act*/
		WordSorter wordSorter =new WordSorter();
		String noOfWords= wordSorter.printFileContent(path);
		
		/* assert*/
		 assertEquals(Integer.toString(EXPECTED_SORTED_ARRAY.length), noOfWords);
	}

}
