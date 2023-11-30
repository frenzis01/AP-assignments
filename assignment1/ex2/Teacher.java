package ex2;

/**
 * Dummy class which is annotated with 
 * @XMLable
 */
public class Teacher {
   public String firstName;
   public String lastName;

   public Teacher() {
      super();
   }

   public Teacher(String fn, String ln, int age) {
      this.firstName = fn;
      this.lastName = ln;
   }
}
