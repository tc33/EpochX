package org.epochx.test;

import junit.framework.*;

public class ModelTest extends TestCase {

	public void assertBetween(String msg, int lower, int upper, int actual) {
		if (msg == null) {
			msg = "";
		}
		
		if (actual < lower || actual > upper) {
			fail(msg + " lower=" + lower + " upper=" + upper + " actual=" + actual);
		}
	}
	
	public void assertBetween(int lower, int upper, int actual) {
		assertBetween("", lower, upper, actual);
	}

	public void assertBetween(String msg, double lower, double upper, double actual) {
		if (msg == null) {
			msg = "";
		}
		
		if (actual < lower || actual > upper) {
			fail(msg + " lower=" + lower + " upper=" + upper + " actual=" + actual);
		}
	}
	
	public void assertBetween(double lower, double upper, double actual) {
		assertBetween("", lower, upper, actual);
	}
}
