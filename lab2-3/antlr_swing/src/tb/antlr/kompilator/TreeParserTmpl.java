/**
 * 
 */
package tb.antlr.kompilator;

import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.runtime.tree.TreeParser;

import tb.antlr.symbolTable.GlobalSymbols;
import tb.antlr.symbolTable.LocalSymbols;

/**
 * @author tb
 *
 */
public class TreeParserTmpl extends TreeParser {

	protected GlobalSymbols globals = new GlobalSymbols();
	protected LocalSymbols locals = new LocalSymbols();
	
	/**
	 * @param input
	 */
	public TreeParserTmpl(TreeNodeStream input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param input
	 * @param state
	 */
	public TreeParserTmpl(TreeNodeStream input, RecognizerSharedState state) {
		super(input, state);
		// TODO Auto-generated constructor stub
	}

	protected void errorID(RuntimeException ex, CommonTree id) {
		System.err.println(ex.getMessage() + " in line " + id.getLine());
	}
	
	protected void enterScope() {
		locals.enterScope();
	}

	protected void leaveScope() {
		locals.leaveScope();
	}
}
