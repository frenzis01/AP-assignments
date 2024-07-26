package ex2;

import ex2.annotations.XMLable;
import ex2.annotations.XMLfield;

/**
 * Additional test class annotated with @XMLable
 */
@XMLable
public class Teacher {
   public String firstName;
   @XMLfield(type = "String", name = "surname")
   public String lastName;

   public Teacher() {
      super();
   }

   public Teacher(String fn, String ln, int age) {
      this.firstName = fn;
      this.lastName = ln;
   }
}
