grammar Expr;

options {
  output=AST;
  ASTLabelType=CommonTree;
}

@header {
package tb.antlr;
}

@lexer::header {
package tb.antlr;
}

prog
    : (stat | block)+ EOF!;
    
block
    : LCB^ (stat | block)* RCB!;

stat
    : expr NL -> expr
    | if_else NL -> if_else

    | VAR ID PODST expr NL -> ^(VAR ID) ^(PODST ID expr)
    | VAR ID NL -> ^(VAR ID)
    | ID PODST expr NL -> ^(PODST ID expr)

    | NL ->
    ;

if_else
    : IF^ expr THEN! (expr | block) (ELSE! (expr | block))?
    ;

expr
    : multExpr
      ( PLUS^ multExpr
      | MINUS^ multExpr
      )*
    ;

multExpr
    : atom
      ( MUL^ atom
      | DIV^ atom
      )*
    ;

atom
    : INT
    | ID
    | LP! expr RP!
    ;

VAR : 'var';

IF  : 'if';

ELSE: 'else';

THEN: 'then';

ID  : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

INT : '0'..'9'+;

NL  : '\r'? '\n' ;

WS  : (' ' | '\t')+ {$channel = HIDDEN;} ;

LP  : '(';

RP  :	')';

LCB : '{';

RCB : '}';

MUL :	'*';

DIV :	'/';

PODST : '=';

PLUS  : '+';

MINUS : '-';
