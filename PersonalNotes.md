# Assignment 1
## EX 2 - XML Serializer
 - In XMLfield is there any way to fail compilation in case the typed field is not valid?
 - XML serialize should serialize null values?
 - Better understand annotation in the custom annotations @interface definition 
 ```
 @Retention(RetentionPolicy.RUNTIME)
 
 The @Retention annotation specifies how long annotations with the annotated type are to be retained. The RetentionPolicy.RUNTIME argument indicates that the @XMLfield annotation will be retained by the JVM at runtime, making it available for reflection at runtime.

    RetentionPolicy.RUNTIME: The annotation is available at runtime and can be accessed via reflection.
 
    Other possible values for RetentionPolicy:
        RetentionPolicy.SOURCE: The annotation is discarded by the compiler and is not present in the bytecode.
        RetentionPolicy.CLASS: The annotation is recorded in the class file by the compiler but is not retained by the JVM at runtime
   ```
   ```
   @Target(ElementType.FIELD)

   The @Target annotation specifies the kinds of program elements to which the annotation type is applicable. The ElementType.FIELD argument indicates that the @XMLfield annotation can be applied to fields (i.e., member variables of a class).

    ElementType.FIELD: The annotation can be applied to fields.
   ```
gre
