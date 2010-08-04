/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.tools.grammar;

import java.io.*;
import java.util.*;

/**
 * A grammar object is constructed from a BNF language grammar. In the grammar 
 * guided evolutionary approaches, a <code>Grammar</code> instance defines the 
 * valid syntax of a program's source being evolved. As well as defining the 
 * syntax of solutions, the grammar also essentially determines the function 
 * and terminal sets which are explicitly defined in tree GP.
 * 
 * <p>A file or string containing a BNF grammar is parsed upon construction and 
 * a form of derivation tree with all possible options is created. The root of 
 * this tree is determined as the first rule in the grammar string and is 
 * retrieveable with a call to the <code>getStartSymbol()</code> method. The 
 * <code>GrammarNode</code> this method returns may be either a 
 * <code>GrammarLiteral</code> or a <code>GrammarRule</code>. Terminal symbols 
 * are represented as <code>GrammarLiterals</code> and simply consist of a 
 * string value which matches the string from the BNF grammar. It is these 
 * terminals that will form the source code of any program that uses it. 
 * Non-literals are represented as <code>GrammarRules</code> and each have a set
 * of <code>GrammarProductions</code>. Each production describes a valid syntax 
 * for that non-terminal rule.
 * 
 * <p>Productions can also be provided with attributes. Attributes are simply 
 * key/value pairs which are then stored in the resultant 
 * <code>GrammarProduction</code> instance. They provide a basic mechanism to 
 * implement semantic constraints such as those used in attribute grammars or 
 * to otherwise provide meta-data about a production, e.g. weights. The format 
 * to provide attributes is to include within the production a special grammar 
 * rule. The rule should begin and end with a question mark character '?'. 
 * Between those question marks a series of key/values, made up of the key and 
 * an equals character '=' followed by the value. Multiple key/values can be 
 * provided separated by a semi-colon ';' character. 
 * 
 * <p>An example rule:</p>
 * 
 * <blockquote><code>
 * &lt;example-rule&gt; ::= abc <?key1=32;key2=true?> | cde | &lt;ruleA&gt; &lt;ruleB&gt;
 * </code></blockquote>
 * 
 * <p>Most of the features known as EBNF (extended BNF) are not currently 
 * supported.
 */
public class Grammar {

	// Index into the rulesets.
	private final Map<String, GrammarLiteral> literals;
	private final Map<String, GrammarRule> rules;
	
	// The starting symbol - the root of the parse tree.
	private GrammarRule start;
	
	/**
	 * Constructs a <code>Grammar</code> with the given string as the BNF 
	 * grammar to be parsed.
	 * 
	 * @param grammarStr a <code>String</code> containing a BNF language grammar.
	 */
	public Grammar(final String grammarStr) {
		literals = new HashMap<String, GrammarLiteral>();
		rules = new HashMap<String, GrammarRule>();
		
		parseGrammar(grammarStr);
	}
	
	/**
	 * Constructs a <code>Grammar</code> with the given file as a reference to a
	 * text file containing a BNF grammar, which will be read and parsed.
	 * 
	 * @param grammarFile a <code>File</code> pointing to a text file containing
	 * a BNF language grammar.
	 * @throws IOException if there was a problem reading the file.
	 */
	public Grammar(final File grammarFile) throws IOException {
		final String grammar = readGrammarFile(grammarFile);
		
		literals = new HashMap<String, GrammarLiteral>();
		rules = new HashMap<String, GrammarRule>();
		
		parseGrammar(grammar);
	}
	
	/**
	 * Returns the root of the grammar parse tree. This will always be a 
	 * <code>GrammarRule</code> for a valid BNF grammar.
	 * 
	 * @return the starting GrammarRule that is at the root of the grammar parse 
	 * tree.
	 */
	public GrammarRule getStartRule() {
		return start;
	}
	
	/*
	 * Reads the contents of the given File and returns it as a String.
	 */
	private String readGrammarFile(final File grammarFile) throws IOException {
		final BufferedReader input = new BufferedReader(new FileReader(grammarFile));
		final StringBuilder grammarStr = new StringBuilder();
		
		String line = null;
		try {
			while ((line = input.readLine()) != null){
				grammarStr.append(line);
				grammarStr.append(System.getProperty("line.separator"));
			}
		} finally {
			input.close();
		}
		
		return grammarStr.toString();
	}
	
	/*
	 * The different states/modes the parser can be in.
	 */
	private enum State {
		START,
		START_RULE,
		LHS_READ,
		PRODUCTION,
		START_OF_LINE
	}
	
	/*
	 * Parses the given grammar string into a Grammar parse tree, rooted at 
	 * the 'start' symbol and made up of Symbols and Productions.
	 * 
	 * This is based upon the grammar parser found in the 
	 * Mapper.ContextFreeGrammar class of GEVA v.1.0.
	 */
	protected void parseGrammar(final String grammar) throws MalformedGrammarException {
		State state = State.START;
		StringBuilder buffer = new StringBuilder();
		GrammarRule lhs = null;
		GrammarProduction grammarProduction = new GrammarProduction();
		
		boolean quoted = false;
		boolean terminal = true;
		boolean special = false;
		
		for (int i=0; i<grammar.length(); i++) {
			char ch = grammar.charAt(i);
			
			if (grammar.charAt(i) == '\\') {
				// Start of an escape sequence - test next character.
                i++;
                if (i >= grammar.length()){
                	// Escape sequence as last char is invalid.
                    throw new MalformedGrammarException("Escape sequence as last char is invalid");
                } else if ((!terminal) && (grammar.charAt(i) != '\n')){
                    // Only escaped newline allowed inside non-terminal
                    throw new MalformedGrammarException("Only escaped newline allowed inside non-terminal");
                }
                
                boolean skip = false;
                if (grammar.charAt(i)=='\''){// Single quote
                    ch='\'';
                } else if (grammar.charAt(i)=='\''){// Double quote
                    ch='\'';
                } else if (grammar.charAt(i)=='\\'){// Backslash
                    ch='\\';
                } else if (grammar.charAt(i)=='0'){// Null character
                    ch='\0';
                } else if (grammar.charAt(i)=='a'){// Audible bell
                    ch='\007';
                } else if (grammar.charAt(i)=='b'){// Backspace
                    ch='\b';
                } else if (grammar.charAt(i)=='f'){// Formfeed
                    ch='\f';
                } else if (grammar.charAt(i)=='n'){// Newline
                    ch='\n';
                } else if (grammar.charAt(i)=='r'){// Carriage return
                    ch='\r';
                } else if (grammar.charAt(i)=='t'){// Horizontal tab
                    ch='\t';
                } else if (grammar.charAt(i)=='v'){// Vertical tab
                    ch='\013';
                } else if (grammar.charAt(i)=='\n'){// Escaped newline
                    skip=true;// Ignore newline
                } else if(grammar.charAt(i)=='\r'){// Escaped DOS return
                    skip=true;// Ignore newline
                    if(grammar.charAt(++i) != '\n'){
                        throw new MalformedGrammarException("No newline");
                    }
                } else {// Normal character
                    ch=grammar.charAt(i);
                }
                
                if(!skip){
                    buffer.append(ch);
                }
                
                continue;
			}
			
			switch(state) {
				case START:
					if (ch == '\r') {
						// Ignore DOS newline.
					} else if (ch == '#') {
						// Comment - skip to end of line.
						while(i < grammar.length() && grammar.charAt(i) != '\n') {
							i++;
						}
					} else if (ch == ' ' || ch == '\t' || ch == '\n') {
						// Ignore whitespace, tabs and newlines.
					} else if (ch == '<') {
						state = State.START_RULE;
					} else {
						// No other valid chars in this state.
						throw new MalformedGrammarException("Illegal character: " + ch);
					}
				break;

				
				case START_RULE:
					if (ch == '\r') {
						// Ignore DOS newline.
					} else if (ch == '\n') {
						// Newlines are illegal here.
						throw new MalformedGrammarException("Misplaced newline");
					} else if (ch == '>') {
						// Possible end of non-terminal.
						final String symbolName = buffer.toString();
						if (!rules.containsKey(symbolName)) {
							lhs = new GrammarRule(symbolName);
							rules.put(symbolName, lhs);
						} else {
							lhs = rules.get(symbolName);
							// LHS might have been a production already, but shouldn't have been a LHS.
							if (lhs.getNoProductions() > 0) {
								throw new MalformedGrammarException("Duplicate rule: " + symbolName);
							}
						}
						// The first LHS becomes the start symbol.
						if (start == null) {
							start = lhs;
						}
						// Clear the buffer.
						buffer = new StringBuilder();
						// Move to next stage.
						state = State.LHS_READ;
					} else if (ch == '"' || ch == '|' || ch == '<') {
						throw new MalformedGrammarException("Non-escaped special char: " + ch);
					} else {
						// Append char to buffer.
						buffer.append(ch);
					}
				break;
				
				
				case LHS_READ:
					if (ch == '\r') {
						// Ignore DOS newline.
					} else if (ch == ' ' || ch == '\t' || ch == '\n') {
						// Ignore whitespace, tabs and newlines.
					} else if (ch == ':') {
						// Part of ::= token.
						buffer.append(ch);
					} else if (ch == '=') {
						// Should be end of ::= token.
						buffer.append(ch);
						
						if (!buffer.toString().equals("::=")) {
							throw new MalformedGrammarException("Expected '::=' " +
									"but found: " + buffer.toString());
						}
						// Clear the buffer.
						buffer = new StringBuilder();
						// Move onto the next stage.
						state = State.PRODUCTION;
					} else {
						throw new MalformedGrammarException("Illegal character: " + ch);
					}
				break;
				
				
				case PRODUCTION:
					if (ch == '\r') {
						// Ignore DOS newline.
					} else if (ch == '|' && quoted) {
						buffer.append(ch); // This might not be needed.
					} else if (ch == '\n' || ch == '|') {
						// End of production and possibly rule.
						if (buffer.length() != 0) {
							String symbolName = buffer.toString();
							if (terminal) {
								GrammarLiteral newSymbol = literals.get(symbolName);
								if (newSymbol == null) {
									newSymbol = new GrammarLiteral(symbolName);
									literals.put(symbolName, newSymbol);
								}
								grammarProduction.addGrammarNode(newSymbol);
							} else {
								/*GrammarNode newSymbol = rules.get(symbolName);
								if (newSymbol == null) {
									newSymbol = new GrammarRule(symbolName);
								}
								production.addSymbol(newSymbol);
								terminal = true;*/
								throw new MalformedGrammarException("Unterminated non-terminal");
							}
							buffer = new StringBuilder();
						}
						// Add the production to the current rule's LHS.
						lhs.addProduction(grammarProduction);
						grammarProduction = new GrammarProduction();
						
						// Move onto next stage - either another production or new rule.
						if (ch == '\n') {
							state = State.START_OF_LINE;
						}
					} else if (ch == '<') {
						if (quoted) {
							buffer.append(ch);
						} else if (buffer.length() == 0) {
							terminal = false;
						} else {
							throw new MalformedGrammarException("Non-escaped " +
									"special char: " + ch);
						}
					} else if (ch == '?') {
						//if (quoted) {
						//	buffer.append(ch);
						//} else if (special) {
						if (special) {
							// This should be the closing '?'.
							String specialCommand = buffer.toString().trim();
							// Parse and process the command.
							processSpecialRule(specialCommand, grammarProduction);
						} else if (!terminal) {
							// This should be the opening '?'.
							special = true;
						} else {
							// Otherwise is outside <> so treat as normal char.
							buffer.append(ch);
							/*throw new MalformedGrammarException("Non-escaped " +
									"special char: " + ch);*/
						}
					} else if (ch == '>') {
						if (quoted) {
							buffer.append(ch);
						} else if (special) {
							// End of special - no symbol to save.
							special = false;
							terminal = true;
							buffer = new StringBuilder();
						} else if (!terminal) {
							// End of non-terminal.
							String symbolName = buffer.toString();
							GrammarRule newSymbol = rules.get(symbolName);
							if (newSymbol == null) {
								newSymbol = new GrammarRule(symbolName);
								rules.put(symbolName, newSymbol);
							}
							grammarProduction.addGrammarNode(newSymbol);
							terminal = true;
							// Clear buffer.
							buffer = new StringBuilder();
						} else {
							throw new MalformedGrammarException("Non-escaped " +
									"special char: " + ch);
						}
					} else if (ch == ' ' || ch == '\t') {
						if (quoted || !terminal) {
							buffer.append(ch);
						} else if (buffer.length() != 0) {
							// Token separator.
							String symbolName = buffer.toString();
							GrammarLiteral newSymbol = literals.get(symbolName);
							if (newSymbol == null) {
								newSymbol = new GrammarLiteral(symbolName);
								literals.put(symbolName, newSymbol);
							}
							grammarProduction.addGrammarNode(newSymbol);
							// Clear buffer.
							buffer = new StringBuilder();
						} else {
							// Excess whitespace, ignore.
						}
					} else if (ch == '"') {
						// Start or end quoted section.
						quoted = !quoted;
					} else {
						// All other characters just append to buffer to become symbol.
						buffer.append(ch);
					}
				break;
				
				
				case START_OF_LINE:
					if (ch == '#') {
						// Comment - skip to end of line.
						while(i < grammar.length() && grammar.charAt(i) != '\n') {
							i++;
						}
					} else if (ch == '\r') {
						// Ignore DOS newline.
					} else if (ch == ' ' || ch == '\t' || ch == '\n') {
						// Ignore whitespace, tabs and newlines.
					} else if (ch == '|') {
						// Start of new production.
						state = State.PRODUCTION;
					} else if (ch == '<') {
						// Start of LHS of new rule.
						state = State.START_RULE;
					} else {
						throw new MalformedGrammarException("Illegal character: " + ch);
					}
				break;
				
				default:
					throw new MalformedGrammarException("Impossible error, quit program now.");
			}
		}
		
		setRecursiveness();
		setMinDepths();
		
		// Check the validity of the grammar.
		// Test a start rule was found.
		if (start == null) {
			throw new MalformedGrammarException("No valid rules found in grammar string");
		}
		
		// Test that all rules have at least one valid production.
		Collection<GrammarRule> ruleList = rules.values();
		for (GrammarRule rule: ruleList) {
			if (rule.getNoProductions() == 0) {
				throw new MalformedGrammarException("Grammar rule " + rule.getName() + " has no productions");
			}
		}
		
		/*
		 * TODO Need to check at the end that the whole grammar is valid 
		 * 	- infinitely recursive? 
		 * 	- Empty productions? 
		 *  - No Productions?
		 */
	}
	
	/*
	 * Process a special rule. Currently the only supported special rule is 
	 * key value pairs.
	 */
	private void processSpecialRule(final String command, final GrammarProduction production) {
		final String[] commands = command.split(";");
		
		for (final String c: commands) {
			String[] keyAndValue = c.split("=");
			production.setAttribute(keyAndValue[0], keyAndValue[1]);
		}
	}
	
	/*
	 * Iterates through the grammar and searches for cases of recursiveness and
	 * sets symbol's recursive flag accordingly.
	 */
	private void setRecursiveness() {
		if (start instanceof GrammarRule) {
			setRecursiveness(new ArrayList<GrammarRule>(), (GrammarRule) start);
		}
	}
	
	/*
	 * Recursive helper for setRecursiveness().
	 */
	private void setRecursiveness(final List<GrammarRule> path, final GrammarRule current) {
		// Check for recursiveness then step down.
		if (path.contains(current)) {
			// Then everything in the path is recursive.
			for (GrammarRule s: path) {
				s.setRecursive(true);
			}
		} else {
			path.add(current);
			
			for (int i=0; i<current.getNoProductions(); i++) {
				GrammarProduction p = current.getProduction(i);
				for (int j=0; j<p.getNoChildren(); j++) {
					// We only care about non-terminal symbols here.
					if (!(p.getGrammarNode(j) instanceof GrammarRule)) {
						continue;
					}
					
					GrammarRule nt = (GrammarRule) p.getGrammarNode(j);
					
					setRecursiveness(new ArrayList<GrammarRule>(path), nt);
				}
			}
		}
	}
	
	/*
	 * Calculates and sets the minimum depths of all grammar rules in the parse
	 * tree.
	 */
	private void setMinDepths() {
		Collection<GrammarRule> symbols = rules.values();
		
		for (GrammarRule nt: symbols) {
			nt.setMinDepth(getMinDepth(new ArrayList<GrammarRule>(), nt));
		}
	}
	
	/*
	 * Recursive helper that calculates the minimum depth of the current symbol.
	 */
	private int getMinDepth(List<GrammarRule> path, GrammarNode currentSymbol) {
		if (!(currentSymbol instanceof GrammarRule)) {
			return 0;
		}
		
		GrammarRule current = (GrammarRule) currentSymbol;
		
		// Check for recursiveness then step down.
		if (path.contains(current)) {
			// Is recursive, cannot possibly be route to min depth - return impossibly high min depth.
			return Integer.MAX_VALUE;
		} else {
			// Continue down this route looking for smaller minimum depth.
			path.add(current);
			
			int minDepth = Integer.MAX_VALUE;
			
			for (int i=0; i<current.getNoProductions(); i++) {
				GrammarProduction p = current.getProduction(i);
				int productionsMinDepth = -1;
				for (int j=0; j<p.getNoChildren(); j++) {
					// The largest of production's symbols min depths, is productions min depth.
					int d = getMinDepth(new ArrayList<GrammarRule>(path), p.getGrammarNode(j));
					
					if (d > productionsMinDepth) {
						productionsMinDepth = d;
					}
				}
				
				// The smallest of productions min depths is the symbols min depth.
				if (productionsMinDepth < minDepth) {
					minDepth = productionsMinDepth;
				}
			}
			
			// Plus one to include this current symbol. Protect against overflow.
			if (minDepth != Integer.MAX_VALUE)
				minDepth++;
			
			// Set the minimum depth on the actual symbol.
			//current.setMinDepth(minDepth);
			
			return minDepth;
		}
	}
	
	/**
	 * Returns a list of the grammar's terminal symbols.
	 * 
	 * @return a complete list of the literals in this grammar.
	 */
	public List<GrammarLiteral> getGrammarLiterals() {
		return new ArrayList<GrammarLiteral>(literals.values());
	}
	
	/**
	 * Returns a specific terminal from the grammar, according to the name 
	 * label of the symbol. 
	 * 
	 * @param name the label that refers to the terminal symbol to return.
	 * @return the terminal symbol with the given name label, or null if a 
	 * terminal with that name does not exist in the grammar.
	 */
	public GrammarLiteral getGrammarLiteral(final String name) {
		return literals.get(name);
	}
	
	/**
	 * Returns a list of the grammars non-terminal symbols.
	 * 
	 * @return a complete list of the non-literals in this grammar.
	 */
	public List<GrammarRule> getGrammarRules() {
		return new ArrayList<GrammarRule>(rules.values());
	}
	
	/**
	 * Returns a specific non-terminal from the grammar, according to the name 
	 * label of the symbol.
	 * 
	 * @param name the label that refers to the non-terminal symbol to return.
	 * @return the non-terminal symbol with the given name label, or null if 
	 * a non-terminal with that name does not exist in the grammar.
	 */
	public GrammarRule getGrammarRule(final String name) {
		return rules.get(name);
	}
	
	/**
	 * Returns the minimum depth of this grammr. The minimum depth of a grammar 
	 * is equal to the minimum depth of its start symbol, which is the minimum 
	 * depth required to reach all terminal symbols.
	 * 
	 * @return the minimum depth required by this grammar to reach all terminal
	 * symbols.
	 */
	public int getMinimumDepth() {
		int minDepth = 0;
		if (start instanceof GrammarRule) {
			minDepth = ((GrammarRule) start).getMinDepth();
		}
		return minDepth;
	}
}
