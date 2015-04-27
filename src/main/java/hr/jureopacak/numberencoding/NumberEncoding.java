package hr.jureopacak.numberencoding;

import java.io.File;
import java.io.IOException;

/**
 * Number encoding task.
 * 
 */
public class NumberEncoding {

	/**
	 * Entry point of Number encoding task. Program can be started in two
	 * different ways.
	 * <p>
	 * 1. With two arguments where first one is file path to dictionary file and
	 * second one is file path to input file with numbers.
	 * <p>
	 * 2. Or without arguments. In this case dictionary file and input file are
	 * provided from resource.
	 * <p>
	 * 
	 * @param args
	 *            arguments for encoder program.
	 * @throws IOException
	 *             if something goes wrong in reading files
	 */
	public static void main(String[] args) throws IOException {

		File dictionaryFile = null;
		File phoneNumbersFile = null;

		if (args.length == 2) {
			dictionaryFile = new File(args[0]);
			phoneNumbersFile = new File(args[1]);
		} else if (args.length == 0) {

			dictionaryFile = new File(NumberEncoding.class.getClassLoader()
					.getResource("dictionary.txt").getFile());
			phoneNumbersFile = new File(NumberEncoding.class.getClassLoader()
					.getResource("input.txt").getFile());
		} else {
			System.err.println("Ivalid argument number!");
			return;
		}

		Encoder encoder = new Encoder(dictionaryFile, phoneNumbersFile);
		encoder.start();

	}

}
