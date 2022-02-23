# TODO

! - In progress
X - Done

## P1
- [x] Refactor grammar to replace use of RFC 3986 reserved characters with keywords
- [x] Field names should support underscore `_`
- [x] Add support for "like" and "not like" matches
- [x] Fix NOT LIKE support
- [x] Create default `resql` exception handler
- [x] Refactor core `resql` context API with a fluent style

## P2
- [x] Fix handling of greater than or equal to `>=` and less than or equal to `<=`
- [x] Custom exception handling to trap Lexer and Parser errors so that they can be translated into application exceptions
- [x] In `^` clause String column values should support hyphen as a character in the string
- [x] Add tests for floating point column values
- [x] Add support in the grammar for Substring matching
- [x] Add basic negative test cases
- [ ] Beef-up negative test cases
- [x] String type currently supports a limited set of characters. Expand the notion of string without breaking the special character support like `==`, `>` etc.
- [ ] Revisit supplied exception handling capability - it's not a checked exception and it would be best if it were
- [ ] One of the Resql cache tests needs a `sleep` operation. See how we can avoid that and how we could test cache 
expiry more reliably

## P3
- [x] Complete letter fragments in grammar
- [ ] Explore if we can supply in-memory cache parameters a little more elegantly
- [ ] Should all parameters be fed to the `ResqlWhereProcessor`? Can/Should some reside in the `Resql` context

# Features
- [ ] Add statistics collection - either Prometheus or StatsD exporting capability
- [ ] Add MongoDB support
- [ ] Add MySQL support
- [!] In-memory caching
- [ ] Add Jacoco code coverage to the build process