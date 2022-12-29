This is a Java decompiler that I've been doing for about a year (intermittently).
Writing it in C++ turned out to be a mistake, so I moved it to Java.
Maybe I'll finish it someday, but I need to add a lot more functionality.

Not implemented:
- for/while
- try/catch/finally
- switch
- break/continue
- Generics
- Lambdas
- Jar support

Implemented:
- A system of primitive types (the type of a variable is determined by its use)
- Call via `super` (so far only for superclass)
- Annotations
- if/else
- Ternary operator
- Operators && and ||
- Individual variables

Implemented features:
- Selection of variable names
- Ability to omit `this` and the current class
- Declaration of fields separated by commas
