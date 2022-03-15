package tb.antlr.symbolTable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;

public class LocalSymbols {
	
	Deque<HashMap<String, Integer>> memory = new ArrayDeque<>();

	/**
	 * Default constructor
	 * declare the symbol table for global scope 
	 */
	public LocalSymbols() {
		memory.push(new HashMap<>()); //the global scope
	}
	
	/**
	 * creates new symbol table on the stack
	 * 
	 * @return the depth of the new scope
	 */
	public Integer enterScope() {
		memory.push(new HashMap<>());
		return memory.size();
	}
	
	/**
	 * remove the top level symbol table
	 * 
	 * @return the current depth
	 * @throws RuntimeException if we try to leave the last scope
	 */
	public Integer leaveScope() throws RuntimeException {
		if (memory.size()<=1)
			throw new RuntimeException("Cannot leave the global scope!");
		memory.remove();
		return memory.size();
	}
	
	/**
	 * Returns <b>true</b> if symbol table on the top of
	 * symbol stack contains the symbol asked for.
	 * 
	 * @param name the name of the symbol looked for
	 * @return     the existence of symbol
	 */
	public boolean hasSymbol(String name) {
		return memory.peek().containsKey(name);
	}

	/**
	 * Returns <b>HashMap</b> in which the symbol
	 * asked for was found. Else it returns <b>null</b>.
	 * 
	 * @param name the name of the symbol looked for
	 * @return     the Symbol Table containing the symbol
	 */
	public HashMap<String, Integer> hasSymbolDepth(String name) {
		Iterator<HashMap<String, Integer>> it = memory.iterator();
		while(it.hasNext())
		{
			HashMap<String, Integer> symTab = it.next();
			if (symTab.containsKey(name))
				return symTab;
		}
		return null;
	}

	/**
	 * Creates new symbol in the current scope
	 *  
	 * @param name the name of symbol to create
	 * @throws RuntimeException if the symbol exists already in the same scope
	 */
	public void newSymbol(String name) throws RuntimeException{
		if (! hasSymbol(name))
			memory.peek().put(name, null);
		else
			throw new RuntimeException("Variable " + name +" exists in the same scope!");
	}
	
	/**
	 * Sets value of the symbol if it exists in any symbol table
	 * else it throws exception.
	 * 
	 * @param name  Name of the symbol
	 * @param value Value to put into the symbol
	 * @return      the value inserted into the symbol
	 * @throws RuntimeException if symbol is not found
	 */
	public Integer setSymbol(String name, Integer value) throws RuntimeException {
		HashMap<String, Integer> symTab = hasSymbolDepth(name);
		if(symTab != null) {
			symTab.put(name, value);
			return value;
		}
		else
			throw new RuntimeException("Variable " + name +" does not exist!");
	}
	
	/**
	 * Gets value of the symbol if it exists in any symbol table
	 * else it throws exception.
	 * 
	 * @param name  Name of the symbol
	 * @return      The value of the symbol
	 * @throws RuntimeException if symbol is not found
	 */
	public Integer getSymbol(String name) throws RuntimeException {
		HashMap<String, Integer> symTab = hasSymbolDepth(name);
		if(symTab != null) {
			return symTab.get(name);
		}
		else
			throw new RuntimeException("Variable " + name +" does not exist!");
	}
	
}
