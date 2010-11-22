package org.epochx.test;

import org.epochx.life.*;

public class SuccessCounter extends RunAdapter {
	private int noSuccess;
	
	@Override
	public void onSuccess() {
		noSuccess++;
	}
	
	public int getNoSuccess() {
		return noSuccess;
	}
};
