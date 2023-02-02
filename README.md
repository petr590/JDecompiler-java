# JDecompiler-java
This is a Java decompiler that I've been doing for about a year (intermittently).
Writing it in C++ turned out to be a mistake, so I moved it to Java.
Maybe I'll finish it someday, but I need to add a more functionality.

## Not implemented:
- for
- try/catch/finally
- switch
- break/continue
- Lambdas
- Jar support

## Implemented:
- A system of primitive types (the type of a variable is determined by its use)
- Call via `super` (so far only for superclass)
- Annotations
- if/else
- while
- Ternary operator
- Operators && and ||
- Individual variables
- Generics

## Implemented features:
- Selection of variable names
- Ability to omit `this` and the current class
- Declaration of fields separated by commas
- Variable names from LocalVariableTable
- Recognition of constants in the current class, as well as standard constants (Math.PI, Double.NaN, etc.)

## Used libraries:
- ArgParser - https://github.com/petr590/ArgParser
- x590.util - https://github.com/petr590/x590.util
