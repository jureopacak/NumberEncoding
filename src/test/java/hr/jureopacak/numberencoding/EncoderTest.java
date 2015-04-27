package hr.jureopacak.numberencoding;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EncoderTest {

	private Encoder encoder;

	@Before
	public void before() {
		File dictionaryFile = new File(NumberEncoding.class.getClassLoader()
				.getResource("test/dictionary_test.txt").getFile());
		encoder = new Encoder(dictionaryFile, null);
	}

	@Test
	public void testEncodeWord() {
		String wordUppercase = "ABCDEFGHJIKLMNOPQRSTUVWXYZ";
		String wordLowercase = wordUppercase.toLowerCase();

		String expectedEncoding = "57630499167851881234762239";

		assertEquals(expectedEncoding, encoder.encodeWord(wordUppercase));
		assertEquals(expectedEncoding, encoder.encodeWord(wordLowercase));
	}

	@Test
	public void testCleanTelephoneNumber() {
		String number = "/6/-7-7";
		String expected = "677";
		assertEquals(expected, encoder.cleanTelephoneNumber(number));
	}

	@Test
	public void testEncoder() {
		
		ArrayList<String> emptyList = new ArrayList<String>();
		
		List<String> number1 = encoder.encodeNumber("112");
		assertEquals(number1, emptyList);
		
		List<String> number2 = encoder.encodeNumber("5624-82");
		assertEquals(number2, Arrays.asList("mir Tor", "Mix Tor"));
		
		List<String> number3 = encoder.encodeNumber("4824");
		assertEquals(number3, Arrays.asList("Tor 4", "fort", "Torf"));
		
		List<String> number4 = encoder.encodeNumber("0721/608-4067");
		assertEquals(number4, emptyList);
		
		List<String> number5 = encoder.encodeNumber("10/783--5");
		assertEquals(number5, Arrays.asList("je Bo\" da", "je bo\"s 5", "neu o\"d 5"));
		
		List<String> number6 = encoder.encodeNumber("1078-913-5");
		assertEquals(number6, emptyList);
		
		List<String> number7 = encoder.encodeNumber("381482");
		assertEquals(number7, Arrays.asList("so 1 Tor"));
		
		List<String> number8 = encoder.encodeNumber("04824");
		assertEquals(number8, Arrays.asList("0 Tor 4", "0 fort", "0 Torf"));

	}

}
