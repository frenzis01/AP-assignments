package ex2;

import java.util.Random;

public class Main {
   public static void main(String[] args) {
      int nStudents = 10;
      int nTeachers = 4;
      Object[] objs = new Object[nStudents+nTeachers+1];

      // Prepend the requested Jane Doe
      objs[0] = new Student("Jane","Doe",42);

      // Add random students
      var r = new Random();
      for(int i = 0; i < nStudents; i++){
         objs[i + 1] = new Student(randstr(r.nextInt(5,15)), randstr(r.nextInt(5,15)),r.nextInt(5,100));
      }
      // Add random teachers
      for(int i = 0; i < nTeachers; i++){
         objs[i + nStudents + 1] = new Teacher(randstr(r.nextInt(5,15)), randstr(r.nextInt(5,15)),r.nextInt(5,100));
      }

      XMLSerializer.serialize(objs, "assignment1/ex2/serialized.xml");
   }


   private static String randstr(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randChar = i == 0
               ? Character.toUpperCase(characters.charAt(randomIndex))
               : characters.charAt(randomIndex);
            randomString.append(randChar);
        }

        return randomString.toString();
    }
   
}
