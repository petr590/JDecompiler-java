# JDecompiler-java
This is a Java decompiler that I've been doing for over a year (intermittently).
Writing it in C++ turned out to be a mistake, so I moved it to Java.
Maybe I'll finish it someday, but I need to add a more functionality.

## Not implemented:
- finally
- break/continue
- Jar support

## Partially implemented:
- for
- switch

## Implemented:
- A system of primitive types (the type of a variable is determined by its use)
- Call via `super`
- Annotations
- if/else
- while
- Ternary operator
- Operators && and ||
- Individual variables
- Generics
- Lambdas
- try/catch

## Implemented features:
- Selection of variable names
- Ability to omit `this` and the current class
- Declaration of fields separated by commas
- Variable names from LocalVariableTable
- Recognition of constants in the current class, as well as standard constants (Math.PI, Double.NaN, etc.)
- Annotation @Override

## Used libraries:
- ArgParser - https://github.com/petr590/ArgParser
- x590.util - https://github.com/petr590/x590.util
- fastutil
- junit (for testing)
