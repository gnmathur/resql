# TODO

! - In progress
X - Done

## P1
- [x] Refactor grammar to replace use of RFC 3986 reserved characters with keywords
- [x] Field names should support underscore `_`
- [x] Add support for "like" and "not like" matches
- [x] Fix NOT LIKE support
- [ ] Create default `resql` exception handler
- [ ] Refactor core `resql` context API with a fluent style

## P2
- [x] Fix handling of greater than or equal to `>=` and less than or equal to `<=`
- [x] Custom exception handling to trap Lexer and Parser errors so that they can be translated into application exceptions
- [x] In `^` clause String column values should support hyphen as a character in the string
- [x] Add tests for floating point column values
- [x] Add support in the grammar for Substring matching
- [x] Add basic negative test cases
- [ ] Beef-up negative test cases
- [x] String type currently supports a limited set of characters. Expand the notion of string without breaking the special character support like `==`, `>` etc.

## P3
- [x] Complete letter fragments in grammar


# Features
- [ ] Add MongoDB support
- [ ] Add MySQL support