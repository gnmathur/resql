[![Java CI with Maven](https://github.com/gnmathur/resql/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/gnmathur/resql/actions/workflows/maven.yml)

# resql
A Java library for translating custom query grammar into target DB where clauses

## Feature
* Provide a more succinct way to express query argument "where" clauses
* Has multiple DB bindings including `Postgres`, `MongoDB`, and `MySQL`
* Default and extensible safeguards to filter out unwanted queries. For example, the PG adapter has defaults to prevent queries that look like SQL injection
* In-built syntactic and semantic checks. The query argument has a well-defined grammar enforcing the checks. Malformed queries can be indicated back to the user at the API layer itself.
 
## Build

### Package
```bash
 $ mvn package
```

### Test
```bash
 $ mvn test
```

## Query Language
The query language is expressed as an infix expression.

* Keywords
    * Nil Absence of a value. Example: `field` != Nil

### Conditional Operators
#### greater than `>`
```bash
field > 10
```
#### less than `<`
```bash
field < 10
```
#### less than or equal to `<=`
```bash
field <= 10
```
#### greater than or equal to `>=`
```bash
field >= 10
```
#### between >< (exclusive)
```bash
field >< (1, 5)
```
will search for fields with values matching 1, 2, 3, or 4, but not 5
### Not equal `!=`
```bash
field != 10
field != 'foo'
```

### Regular expression
`resql` supports the following subset of Regular Expression abilities defined in POSIX 1003.2 and other DB engines

| Atom        | Description              |
| ----------- | ------------------------ |
| .           | match a single character |


| Quantifier  | Description                               |
| ----------- | ----------------------------------------- |
| *           | 0 or more matches of atom                 |
| +           | 1 or more matches of atom                 |
| ?           | 0 or 1 match of the arom                  |

| Constraint | Description                |
| ---------- | -------------------------- |
| ^          | Match beginning of String  |
| $          | Match end of string        |

## Logical Operators

### In `^`
```bash
field ^[10, 11, 12]
field ^["foo", "bar", "baz"]
```

### 
### And '&&'
```bash
field1 == 10 && field2 == 11
field1 == 10 && field3 == 'foo'
```
### Or '||'
```bash
field1 == 10 || field2 == 11
field1 == 10 || field3 == 'foo'
```

### Parenthesis
```bash
(field1 = 10 and field2 = 20) || (field3 = 30 and field4 = 40)
```
