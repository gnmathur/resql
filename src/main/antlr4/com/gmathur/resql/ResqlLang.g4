grammar ResqlLang;

qexp    : subexp EOF
        | EOF
        ;

subexp : OPENPAREN subexp CLOSEPAREN
        | subexp AND subexp
        | subexp OR subexp
        | gtexp | ltexp | gteexp | lteexp
        | between | in | notin
        | equal | notequal
        | like | notlike
        ;

// Field is greater than a numeric or string value
gtexp       : FIELD GT  (NUMBER | STRING);
// Field is less than a numeric or string value
ltexp       : FIELD LT  (NUMBER | STRING);
// Field is less than or equal to a numeric or string value
lteexp      : FIELD LTE (NUMBER | STRING);
// Field is greather than or equal to a numeric or string value
gteexp      : FIELD GTE (NUMBER | STRING);
// Field is equal to a numeric or string value
equal       : FIELD EQ  (NUMBER | STRING);
notequal    : FIELD NEQ (NUMBER | STRING);
// Field is between a range of values
between     : FIELD BTW tuple;
// Field is one of the values in the range
in          : FIELD IN (arrayN | arrayS);
// Field is neither one of the values in the range
notin       : FIELD NOTIN (arrayN | arrayS);

// An array or a range of numbers
arrayN      : SQOPEN NUMBER (SEP | NUMBER)* SQCLOSE;
arrayS      : SQOPEN (SEP | STRING)* SQCLOSE;
// The regex string is not lexed as a valid regex string adhering to a standard, but instead as a STRING. A valid regex
// string will have to be checked by the respected adapters
like        : FIELD (MATCH) STRING;
notlike     : FIELD (NEGMATCH) STRING;
// 2-Tuple
tuple       : OPENPAREN NUMBER SEP NUMBER CLOSEPAREN;


/** Lexer Rules **/

// Non-tokens
fragment A :('A'|'a');
fragment B :('B'|'b');
fragment C :('C'|'c');
fragment D :('D'|'d');
fragment E :('E'|'e');
fragment F :('F'|'f');
fragment G :('G'|'g');
fragment H :('H'|'h');
fragment I :('I'|'i');
fragment J :('J'|'j');
fragment K :('K'|'k');
fragment L :('L'|'l');
fragment M :('M'|'m');
fragment N :('N'|'n');
fragment O :('O'|'o');
fragment P :('P'|'p');
fragment Q :('Q'|'q');
fragment R :('R'|'r');
fragment S :('S'|'s');
fragment T :('T'|'t');
fragment U :('U'|'u');
fragment V :('V'|'v');
fragment W :('W'|'w');
fragment X :('X'|'x');
fragment Y :('Y'|'y');
fragment Z :('Z'|'z');

fragment DIGIT          : [0-9];
fragment UPPERCASE      : [A-Z];
fragment LOWERCASE      : [a-z];
fragment QUOTE          : '\'';
fragment UNDERSCORE     : '_';
fragment EQUAL          : '=';
fragment HYPHEN         : '-';
fragment CARET          : '^';
fragment PERCENT        : '%';
fragment PERIOD         : '.';
fragment STAR           : '*';
fragment QUESTION       : '?';
fragment DOLLAR         : '$';
fragment PLUS           : '+';
fragment EXCL           : '!';

// Tokens
SEP         : ',';
SQOPEN      : '[';
SQCLOSE     : ']';
OPENPAREN   : '(';
CLOSEPAREN  : ')';
CLOSEBRACE  : '}';
OPENBRACE   : '{';

// Comparison operators - comparing values yielding a true or false result
EQ          : '='   ; // equal to
NEQ         : '!=' ; // not equal to
GTE         : '>=' ;  // greater than or equal to
LTE         : '<=' ; // less than or equal to
GT          : '>'   ;  // greater than
LT          : '<'   ;  // less than
BTW         : '><' ; // between two elements
MATCH       : '~'  ;  // match
NEGMATCH    : '!~'  ;  // dont match

// Logical operators - combine boolean expressions
AND     : '&&';
OR      : '||';
IN      : CARET;
NOTIN   : EXCL CARET;

// Integer and floating point numbers
NUMBER  : DIGIT+
            | DIGIT+ '.' DIGIT+
            ;
// String identifiers
STRING      : QUOTE
            (UPPERCASE | LOWERCASE | DIGIT | HYPHEN | STAR | PERIOD | PLUS | CARET | PERCENT | EQUAL | GT | LT | UNDERSCORE)+
            QUOTE;
FIELD       : (UPPERCASE | LOWERCASE | DIGIT | UNDERSCORE )+;

WS      : [ \t\r\n]+ -> skip;
