package tb.antlr.interpreter;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

public class MyTreeParser extends TreeParser {

    public MyTreeParser(TreeNodeStream input) {
        super(input);
    }

    public MyTreeParser(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    protected void drukuj(String text) {
        System.out.println(text.replace('\r', ' ').replace('\n', ' '));
    }

	protected Integer getInt(String text) {
		return Integer.parseInt(text);
	}
}
