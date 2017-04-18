package test.dibyatanu.deb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

/**
 * @author Dibyatanu Deb
 *
 */
public class WordSorter {
	private static final String INVALID_ARGUMENT = "Usage: WordSorter file";
	private static final String INVALID_FILE = "Invalid File: ";
	private static final String REGEX_BLANK_SPACE = "\\s+";
	private static final String PROCERSSED_MESSAGE = "Processed %s words in %s ms.";
	/*
	 * Reads and sorts file content in alphabetic order 
	 */
	public String[] sortFileContent(final Path path) throws IOException {
		String fileContent = FileUtils.readFileToString(path.toFile());
		String[] fileArray = fileContent.split(REGEX_BLANK_SPACE);
		Arrays.sort(fileArray);
		return fileArray;
	}
	/*
	 * print file content and returns no of words
	 */
	public String printFileContent(final Path path) throws IOException {
		String[] sortedList = sortFileContent(path);
		for (String letter : sortedList) {
			printInfo(letter);
		}
		return Integer.toString(sortedList.length);
	}


	/*
	 *  validates command line argument
	 *  Exist if no argument or invalid file name
	 */
	private static Path validateArgument(String[] args) {
		if (args.length == 0) {
			printError(INVALID_ARGUMENT);
			System.exit(1);
		}
		Path path = Paths.get(args[0]);
		if (!Files.isReadable(path)) {
			printError(INVALID_FILE + args[0]);
			System.exit(1);
		}
		return path;
	}
	/* 
	 * print method for info , can be replaced by logging 
	 */
	private static void printInfo(String message) {
		System.out.println(message);
	}
	/* 
	 * print method for error , can be replaced by logging 
	 */
	private static void printError(String message) {
		System.err.println(message);
	}

	public static void main(String[] args) {
		Path path = validateArgument(args);
		long starTime = System.currentTimeMillis();
		WordSorter wordsorted = new WordSorter();
		try {
			String nofOfWord = wordsorted.printFileContent(path);
			long endTime = System.currentTimeMillis();
			long timeTaken = endTime - starTime;
			printInfo(String.format(PROCERSSED_MESSAGE, nofOfWord, timeTaken));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
