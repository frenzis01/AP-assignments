package ex2.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Stream;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XMLfield {
   String type(); // primitive type of the field (e.g., "int", "String", ...)
   String name() default ""; // (Optional) argument for the XML tag to be used for the field

   public static class FieldType {
      private static String[] allowedTypes = {"int","long","double","float","char","boolean","String"};
      public static boolean isValid (String s){
         return Stream.of(allowedTypes).anyMatch((t) -> t.equals(s));
      }
   }
}