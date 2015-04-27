package hr.jureopacak.numberencoding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Encoder {

	private File dictionary;

	private File numbersToDecode;

	/**
	 * Collection which is representing cache. Cache is populated in the
	 * beginning and all dictionary words are encoded as numbers to support fast
	 * lookup.
	 */
	private Map<String, ArrayList<String>> cache = new HashMap<String, ArrayList<String>>();

	public Encoder(File dictionary, File numbersToDecode) {
		this.dictionary = dictionary;
		this.numbersToDecode = numbersToDecode;
		loadDictionary();
	}

	/**
	 * Starts encoding.
	 */
	public void start() {
	
		try {
			startEncoding();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Loading dictionary into memmory.
	 * 
	 * returns <code>true</code> if dictionary is properly loaded into memory,
	 * otherwise returns <code>false</code>
	 */
	private boolean loadDictionary() {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(dictionary)))) {
			String word;

			while ((word = br.readLine()) != null) {
				encodeWord(word);
			}

			return true;

		} catch (FileNotFoundException e) {
			System.err.println("Dictionary file: "
					+ dictionary.getAbsolutePath() + " is not found!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Encodes word to number and puting it in a cache.
	 * 
	 * @param word
	 *            to encode in numbers
	 * @return number which is representing encoded word
	 */
	public String encodeWord(String word) {
		String encodedNumber = "";

		for (int i = 0; i < word.length(); i++) {
			switch (word.toLowerCase().charAt(i)) {
			case 'e':
				encodedNumber += "0";
				break;
			case 'j':
			case 'n':
			case 'q':
				encodedNumber += "1";
				break;
			case 'r':
			case 'w':
			case 'x':
				encodedNumber += "2";
				break;
			case 'd':
			case 's':
			case 'y':
				encodedNumber += "3";
				break;
			case 'f':
			case 't':
				encodedNumber += "4";
				break;
			case 'a':
			case 'm':
				encodedNumber += "5";
				break;
			case 'c':
			case 'i':
			case 'v':
				encodedNumber += "6";
				break;
			case 'b':
			case 'k':
			case 'u':
				encodedNumber += "7";
				break;
			case 'l':
			case 'o':
			case 'p':
				encodedNumber += "8";
				break;
			case 'g':
			case 'h':
			case 'z':
				encodedNumber += "9";
				break;

			default:
				break;
			}

		}

		List<String> encodingsForNumber = cache.get(encodedNumber);

		if (encodingsForNumber == null) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(word);
			cache.put(encodedNumber, list);
		} else {
			encodingsForNumber.add(word);
		}

		return encodedNumber;
	}

	/**
	 * Called by {@link #start()} to start encoding process. For every line
	 * (phone number) from file with numbers invokes
	 * {@link #encodeNumber(String number)}. After number is encoded it writes
	 * encoding/s with corresponding telephone number in standard out.
	 */
	private void startEncoding() {

		try (BufferedReader inputFileBufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(numbersToDecode)))) {
			String originalTelephoneNumber;

			while ((originalTelephoneNumber = inputFileBufferedReader
					.readLine()) != null) {

				List<String> encodings = encodeNumber(originalTelephoneNumber);

				for (String string : encodings) {
					System.out.println(originalTelephoneNumber + ": " + string);
				}

			}
		} catch (FileNotFoundException e) {
			System.err.println("Phone numbers file: "
					+ numbersToDecode.getAbsolutePath() + " is not found!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Helper method for cleaning telephone number
	 * {@link #cleanTelephoneNumber(String)} and calling method
	 * {@link #encode(String, boolean)} to start recursive encoding.
	 * 
	 * @param originalTelephoneNumber
	 * @return {@link List} of encodings for given number.
	 */
	public List<String> encodeNumber(String originalTelephoneNumber) {
		return encode(cleanTelephoneNumber(originalTelephoneNumber), false);
	}

	/**
	 * Method for recursiv encoding numbers.
	 * 
	 * @param number
	 *            to encode
	 * @param boolean previousIsNumber <code>true</code> if in previous
	 *        iteration no encodings was found <code>false</code> if encodings
	 *        for given number were found
	 * @return {@link List} of encodings for given number
	 */
	private List<String> encode(String number, boolean previousIsNumber) {

		List<String> encodings = new ArrayList<>();
		if (previousIsNumber && number.length() == 1) {
			return encodings;
		}

		if (!previousIsNumber && number.length() == 1) {
			encodings.add(number);
			return encodings;
		}

		if (number.equals("")) {
			return encodings;
		}

		int lenghtOfNumber = number.length();

		for (int i = 0; i < lenghtOfNumber; i++) {

			String part = number.substring(0, i + 1);

			List<String> encodingsForPart = getEncodings(part);

			if (!encodingsForPart.isEmpty()) {

				String remainToEncode = number.substring(i + 1, lenghtOfNumber);
				if (remainToEncode.equals("")) {
					encodings.addAll(encodingsForPart);
				} else {
					List<String> remainedEncodings = encode(remainToEncode,
							false);

					if (!remainedEncodings.isEmpty()) {
						for (String encodedInThisIteration : encodingsForPart) {
							for (String encodedFromRecursiveCall : remainedEncodings) {
								encodings.add(encodedInThisIteration + " "
										+ encodedFromRecursiveCall);
							}
						}
					}
				}

			}

		}

		if (!previousIsNumber && encodings.isEmpty()) {
			String remainToEncode = number.substring(1);
			String encodedAsNumber = number.substring(0, 1);

			List<String> remainedEncodings = encode(remainToEncode, true);
			if (!remainedEncodings.isEmpty()) {

				for (String encodedFromRecursiveCall : remainedEncodings) {
					encodings.add(encodedAsNumber + " "
							+ encodedFromRecursiveCall);
				}

			}
		}

		return encodings;
	}

	/**
	 * Search for encodings of provided number.
	 * 
	 * @param number
	 *            for lookup
	 * @return {@link List} of encodings for given number
	 */
	public List<String> getEncodings(String number) {
		ArrayList<String> encodingsForPart = cache.get(number);
		if (encodingsForPart == null) {
			return new ArrayList<String>();
		} else {
			return encodingsForPart;
		}
	}

	/**
	 * Clean telephone number from "-" character and "/" character.
	 * 
	 * @param telephoneNumberToClean
	 * @return cleaned telephone number
	 */
	public String cleanTelephoneNumber(String telephoneNumberToClean) {
		return telephoneNumberToClean.replace("-", "").replace("/", "");
	}

	public Map<String, ArrayList<String>> getCache() {
		return cache;
	}

	public void setCache(Map<String, ArrayList<String>> cache) {
		this.cache = cache;
	}

}
