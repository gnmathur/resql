grammar ResqlLang;

qexp    : subexp EOF
        | EOF
        ;

subexp : OPENPAREN subexp CLOSEPAREN
        | subexp AND subexp
        | subexp OR subexp
        | gtexp | ltexp | gteexp | lteexp
        | between | in
        | equal | notequal
        ;

gtexp       : FIELD GT  (NUMBER | STRING);
ltexp       : FIELD LT  (NUMBER | STRING);
lteexp      : FIELD GTE (NUMBER | STRING);
gteexp      : FIELD LTE (NUMBER | STRING);
equal       : FIELD EQ  (NUMBER | STRING);
notequal    : FIELD NEQ (NUMBER | STRING);
// Field is between a range of values
between     : FIELD BTW tuple;
// Field is one of the values in the range
in          : FIELD IN (arrayN | arrayS);
// An array or a range of numbers
arrayN       : SQOPEN NUMBER (SEP | NUMBER)* SQCLOSE;
arrayS       : SQOPEN  (SEP | STRING)* SQCLOSE;

// 2-Tuple
tuple       : OPENPAREN NUMBER SEP NUMBER CLOSEPAREN;
/** Lexer Rules **/

// Operators

// Comparison operators - comparing values yielding a true or false result
EQ      : '=='; // equal to
NEQ     : '!='; // not equal to
GTE     : '>='; // greater than or equal to
LTE     : '<='; // less than or equal to
GT      : '>';  // greater than
LT      : '<';  // less than
BTW     : '><'; // between two elements
IN      : '^^';  // match an element in a defined range
MATCH   : '~~';  // match

// Logical operators - combine boolean expressions
AND : '&&';
OR  : '||';

// Base tokens
fragment DIGIT          : [0-9];
fragment UPPERCASE      : [A-Z];
fragment LOWERCASE      : [a-z];
fragment QUOTE          : '\'';
fragment UNDERSCORE     : '_';

SEP         : ',';
SQOPEN      : '[';
SQCLOSE     : ']';
OPENPAREN   : '(';
CLOSEPAREN  : ')';
HYPHEN      : '-';
CARET       : '^';
PERCENT     : '%';
PERIOD      : '.';
STAR        : '*';
QUESTION    : '?';
OPENBRACE   : '{';
CLOSEBRACE  : '}';

// Integer and floating point numbers
NUMBER  : DIGIT+
            | DIGIT+ '.' DIGIT+
            ;
// String identifiers
STRING  : QUOTE (UPPERCASE | LOWERCASE | DIGIT | HYPHEN)+ QUOTE;
REGEX   : QUOTE QUOTE (CARET | PERCENT | PERIOD | STAR | UPPERCASE | LOWERCASE )+ QUOTE QUOTE;
FIELD   : (UPPERCASE | LOWERCASE | DIGIT | UNDERSCORE )+;

WS      : [ \t\r\n]+ -> skip;
