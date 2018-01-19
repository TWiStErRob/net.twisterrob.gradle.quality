package net.twisterrob.gradle.common;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilsTest {

	public static class safeAdd {

		public static class Numbers {

			private static final Integer VALUE = 5;
			private static final Integer VALUE2 = 6;
			private static final Integer NULL = null;

			@Test public void addNullValues() {
				Integer result = Utils.safeAdd(NULL, NULL);

				assertNull(result);
			}

			@Test(expected = NullPointerException.class) public void addValueToNullBuiltIn() {
				@SuppressWarnings({"ConstantConditions", "unused"})
				Integer result = VALUE + NULL;
			}

			@Test public void addValueToNull() {
				Integer result = Utils.safeAdd(VALUE, NULL);

				assertEquals(VALUE, result);
			}

			@Test(expected = NullPointerException.class) public void addNullToValueBuiltIn() {
				@SuppressWarnings({"ConstantConditions", "unused"})
				Integer result = NULL + VALUE;
			}

			@Test public void addNullToValue() {
				Integer result = Utils.safeAdd(NULL, VALUE);

				assertEquals(VALUE, result);
			}

			@Test public void addValueToValueBuiltIn() {
				Integer result = VALUE + VALUE2;

				assertEquals((Integer)11, result);
			}

			@Test public void addValueToValue() {
				Integer result = Utils.safeAdd(VALUE, VALUE2);

				assertEquals((Integer)(VALUE + VALUE2), result);
			}
		}

		public static class Strings {

			private static final String VALUE = "5";
			private static final String VALUE2 = "6";
			private static final String NULL = null;

			@Test public void addNullValues() {
				String result = Utils.safeAdd(NULL, NULL);

				assertNull(result);
			}

			@Test public void addValueToNullBuiltIn() {
				String result = VALUE + NULL;

				assertEquals("5null", result);
			}

			@Test public void addValueToNull() {
				String result = Utils.safeAdd(VALUE, NULL);

				assertEquals(VALUE, result);
			}

			@Test public void addNullToValueBuiltIn() {
				String result = NULL + VALUE;

				assertEquals("null5", result);
			}

			@Test public void addNullToValue() {
				String result = Utils.safeAdd(NULL, VALUE);

				assertEquals(VALUE, result);
			}

			@Test public void addValueToValueBuiltIn() {
				String result = VALUE + VALUE2;

				assertEquals("56", result);
			}

			@Test public void addValueToValue() {
				String result = Utils.safeAdd(VALUE, VALUE2);

				assertEquals(VALUE + VALUE2, result);
			}
		}
	}

	public static class nullSafeSum {

		@Test public void sumIntegerList() {
			List<Integer> input = Arrays.asList(null, 5, 6, null, 7, null, null, 8, null);

			Collector<Integer, ?, Integer> sut = Utils.nullSafeSum();
			Integer result = input.stream().collect(sut);

			assertEquals((Integer)(5 + 6 + 7 + 8), result);
		}

		@Test public void sumNulls() {
			List<Integer> input = Arrays.asList(null, null, null, null);

			Collector<Integer, ?, Integer> sut = Utils.nullSafeSum();
			Integer result = input.stream().collect(sut);

			assertEquals(null, result);
		}

		@Test public void sumStringLengths() {
			List<String> input = Arrays.asList(null, "1", "12", null, "123", null, null, "1234", null);
			Function<String, Integer> safeLength = s -> s != null? s.length() : null; // s?.length()

			Collector<String, ?, Integer> sut = Utils.nullSafeSum(safeLength);
			Integer result = input.stream().collect(sut);

			assertEquals((Integer)(1 + 2 + 3 + 4), result);
		}
	}
}