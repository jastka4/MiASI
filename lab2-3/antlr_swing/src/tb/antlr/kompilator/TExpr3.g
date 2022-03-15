tree grammar TExpr3;

options {
  tokenVocab=Expr;

  ASTLabelType=CommonTree;

  output=template;
  superClass = TreeParserTmpl;
}

@header {
package tb.antlr.kompilator;
}

@members {
  int count = 0;
}
prog    : (e+=block | e+=expr | d+=decl)* -> program(expressions={$e},declarations={$d});

block   : ^(LCB {enterScope();}(e1+=block | e1+=expr | d1+=decl)* {leaveScope();}) -> block(expressions={$e1}, declarations={$d1});

decl    :
        ^(VAR i1=ID) {globals.newSymbol($ID.text);} -> decl(n={$ID.text})
        ;
    catch [RuntimeException ex] {errorID(ex,$i1);}

expr    : ^(PLUS  e1=expr e2=expr)          -> add(p1={$e1.st},p2={$e2.st})
        | ^(MINUS e1=expr e2=expr)          -> sub(p1={$e1.st},p2={$e2.st})
        | ^(MUL   e1=expr e2=expr)          -> mul(p1={$e1.st},p2={$e2.st})
        | ^(DIV   e1=expr e2=expr)          -> div(p1={$e1.st},p2={$e2.st})
        | ^(PODST i1=ID   e2=expr)
        | ^(IF    e1=expr e2=expr e3=expr) {count++;}  -> if(p1={$e1.st},p2={$e2.st},p3={$e3.st},n={count})
        | INT                               -> int(i={$INT.text})
        | ID                                -> id(i={$ID.text})
        ;
    