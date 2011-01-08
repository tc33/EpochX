package org.epochx.test;

import static org.junit.Assert.fail;

public class ModelTest {

	public void assertBetween(String msg, final int lower, final int upper, final int actual) {
		if (msg == null) {
			msg = "";
		}

		if ((actual < lower) || (actual > upper)) {
			fail(msg + " lower=" + lower + " upper=" + upper + " actual=" + actual);
		}
	}

	public void assertBetween(final int lower, final int upper, final int actual) {
		assertBetween("", lower, upper, actual);
	}

	public void assertBetween(String msg, final double lower, final double upper, final double actual) {
		if (msg == null) {
			msg = "";
		}

		if ((actual < lower) || (actual > upper)) {
			fail(msg + " lower=" + lower + " upper=" + upper + " actual=" + actual);
		}
	}

	public void assertBetween(final double lower, final double upper, final double actual) {
		assertBetween("", lower, upper, actual);
	}
}
