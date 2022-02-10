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
        | like
        ;

// Field is greater than a numeric or string value
gtexp       : FIELD GT  (NUMBER | STRING);
// Field is less than a numeric or string value
ltexp       : FIELD LT  (NUMBER | STRING);
// Field is less than or equal to a numeric or string value
lteexp      : FIELD GTE (NUMBER | STRING);
// Field is greather than or equal to a numeric or string value
gteexp      : FIELD LTE (NUMBER | STRING);
// Field is equal to a numeric or string value
equal       : FIELD EQ  (NUMBER | STRING);
notequal    : FIELD NEQ (NUMBER | STRING);
// Field is between a range of values
between     : FIELD BTW tuple;
// Field is one of the values in the range
in          : FIELD IN (arrayN | arrayS);
// An array or a range of numbers
arrayN      : SQOPEN NUMBER (SEP | NUMBER)* SQCLOSE;
arrayS      : SQOPEN  (SEP | STRING)* SQCLOSE;
// The regex string is not lexed as a valid regex string adhering to a standard, but instead as a STRING. A valid regex
// string will have to be checked by the respected adapters
like        : FIELD (MATCH | NEGMATCH) STRING;
// 2-Tuple
tuple       : OPENPAREN NUMBER SEP NUMBER CLOSEPAREN;


/** Lexer Rules **/

// Non-tokens
fragment A :('A'|'a');
fragment D :('D'|'d');
fragment E :('E'|'e');
fragment G :('G'|'g');
fragment I :('I'|'i');
fragment K :('K'|'k');
fragment L :('L'|'l');
fragment N :('N'|'n');
fragment O :('O'|'o');
fragment Q :('Q'|'q');
fragment R :('R'|'r');
fragment T :('T'|'t');

fragment DIGIT          : [0-9];
fragment UPPERCASE      : [A-Z];
fragment LOWERCASE      : [a-z];
fragment QUOTE          : '\'';
fragment UNDERSCORE     : '_';
fragment EQUAL          : '=';
fragment HYPHEN      : '-';
fragment CARET       : '^';
fragment PERCENT     : '%';
fragment PERIOD      : '.';
fragment STAR        : '*';
fragment QUESTION    : '?';
fragment OPENBRACE   : '{';
fragment CLOSEBRACE  : '}';
fragment DOLLAR      : '$';
fragment PLUS        : '+';

// Tokens
SEP         : ',';
SQOPEN      : '[';
SQCLOSE     : ']';
OPENPAREN   : '(';
CLOSEPAREN  : ')';

// Comparison operators - comparing values yielding a true or false result
EQ          : E Q; // equal to
NEQ         : '!='; // not equal to
GTE         : '>='; // greater than or equal to
LTE         : '<='; // less than or equal to
GT          : G T;  // greater than
LT          : L T;  // less than
BTW         : '><'; // between two elements
MATCH       : '~~';  // match
NEGMATCH    : '!~';  // dont match

// Logical operators - combine boolean expressions
AND : A N D;
OR  : O R;

// Unary operators
IN  : I N; // match an element in a defined range

// Integer and floating point numbers
NUMBER  : DIGIT+
            | DIGIT+ '.' DIGIT+
            ;
// String identifiers
STRING      : QUOTE
            (UPPERCASE | LOWERCASE | DIGIT | HYPHEN | STAR | PERIOD | PLUS | CARET | PERCENT | EQUAL | GT | LT )+
            QUOTE;
FIELD       : (UPPERCASE | LOWERCASE | DIGIT | UNDERSCORE )+;

WS      : [ \t\r\n]+ -> skip;
