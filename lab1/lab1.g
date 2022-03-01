grammar lab1;

@members {
	class DivisionByZeroException extends ArithmeticException {
		DivisionByZeroException() {
			super("Cannot divide by 0!");
		}
	}
}

prog
	:	(expr {System.out.println("Result: " + $expr.out);} NL+)* EOF
	;	catch [DivisionByZeroException e] { System.out.println(e.getMessage()); }

expr returns [Integer out]
	:	t1 = term {$out = $t1.out;}
		( PLUS t2 = term { $out += $t2.out; }
		| MINUS t2 = term {$out -= $t2.out; }
		)*
	;

term returns [Integer out]
	:	a1 = atom { $out = $a1.out; }
		( MUL a2 = atom { $out *= $a2.out; }
		| DIV a2 = atom { if ($a2.out == 0) throw new DivisionByZeroException(); $out /= $a2.out; }
		| MOD a2 = atom { if ($a2.out == 0) throw new DivisionByZeroException(); $out \%= $a2.out; }
		)*
	;

atom returns [Integer out]
	:	INT { $out = Integer.parseInt($INT.text); }
		| (LP expr RP) { $out = $expr.out; }
	;

ID	:	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

INT	:	'0'..'9'+ ; 

PLUS:	'+' ;

MINUS:	'-' ;

MUL	:	'*' ;

DIV	:	'/' ;

MOD	:	'%';

NL	:	'\n' ;

LP	:	'(' ;

RP	:	')' ;

COMMENT
	:   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        ) {$channel=HIDDEN;}
    ;
