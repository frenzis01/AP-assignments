package ex2;

import java.util.Random;

public class Main {
   public static void main(String[] args) {
      int n = 10;
      Student[] students = new Student[n];
      var r = new Random();
      for(int i = 0; i < n; i++){
         students[i] = new Student(randstr(10), randstr(10),r.nextInt(5,100));
      }
      XMLSerializer.serialize(students, "assignment1/ex2/students.xml");
   }


   private static String randstr(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }
   
}
