This is a Java decompiler that I've been doing for about a year (intermittently).
Writing it in C++ turned out to be a mistake, so I moved it to Java.
Maybe I'll finish it someday, but I need to add a lot more functionality.

Currently not implemented:
- Operators && and ||
- Ternary operator
- for/while
- try/catch/finally
- switch
- break/continue
- Lambdas
- Jar support

Implemented:
- Annotations
- if
- else
- Separate variables (the compiler allocates one cell for two disjoint variables)
