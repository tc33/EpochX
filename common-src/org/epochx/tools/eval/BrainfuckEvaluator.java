/**
 * 
 */
package org.epochx.tools.eval;

import java.util.Arrays;


public class BrainfuckEvaluator implements Evaluator {

	private byte[] memory;
	private int pointer;
	
	public BrainfuckEvaluator() {
		memory = new byte[30000];
		pointer = 0;
	}
	
	private void reset() {
		Arrays.fill(memory, (byte) 0);
		pointer = 0;
	}
	
	@Override
	public Object eval(String program, String[] argNames, Object[] argValues) {
		// Reset the environment.
		reset();
		
		// Set inputs as first x memory cells.
		for (int i=0; i<argValues.length; i++) {
			memory[i] = (Byte) argValues[i];
		}

		// Execute the program.
		execute(program);
		
		// Return output as first memory cell.
		//TODO Consider returning the whole memory array as the result.
		return memory;
	}

	@Override
	public Object[] eval(String program, String[] argNames,
			Object[][] argValues) {
		return null;
	}

	private void execute(String source) {
		if (source == null)
			return;
		
		for (int i=0; i<source.length(); i++) {
			char c = source.charAt(i);
			
			switch (c) {
			case '>':
				pointer = ++pointer % memory.length;
				break;
			case '<':
				pointer--;
				if (pointer < 0) {
					pointer = memory.length - 1;
				}
				break;
			case '+':
				memory[pointer]++;
				break;
			case '-':
				memory[pointer]--;
				break;
			case ',':
				// Not supported.
				break;
			case '.':
				// Not supported.
				break;
			case '[':
				int bracketIndex = findClosingBracket(source.substring(i+1)) + (i+1);
				String loopSource = source.substring((i+1), bracketIndex);
				while(memory[pointer] != 0) {
					execute(loopSource);
				}
				i = bracketIndex;
				break;
			case ']':
				// Implemented as part of '['.
				break;
			default:
				break;
			}
		}
	}
	
	private int findClosingBracket(String source) {
		int open = 1;
		for (int i=0; i<source.length(); i++) {
			char c = source.charAt(i);
			
			if (c == '[') {
				open++;
			} else if (c == ']') {
				open--;
				if (open == 0) {
					return i;
				}
			}
		}
		// There is no closing bracket.
		return -1;
	}
}
