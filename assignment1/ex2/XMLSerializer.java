/**
 * 
   • @XMLable provides information about the class. The presence of this annotation says that
   the objects of this class should be serialized. In this case the corresponding element will
   contain other elements for the instance variables, if any. If instead the annotation is absent,
   the element corresponding to the object must contain only the empty element
   <notXMLable />.
   • @XMLfield identifies serializable fields (i.e., instance variables, only of primitive types or
   strings). The presence of this annotation states that the field must be serialized. The annotation
   has a mandatory argument type, which is the type of the field (a String, for example
   "int", "String",…), and an optional argument name, also of type String, which is the
   XML tag to be used for the field. If the argument is not provided, the variable’s name is used
   as a tag.
 */
package ex2;
// import annotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ex2.annotations.XMLable;
import ex2.annotations.XMLfield;


public class XMLSerializer {
   static void serialize(Object [ ] arr, String fileName) {
      List<String> xmlResult = new ArrayList<>(List.of("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
      for (Object o : arr) {
         
         IntrospectedClass c = classLookup(o);
         if (!c.labeled)
            continue;
         List<String> serializedObj = new ArrayList<>();
         serializedObj.add("<" + c.name + ">");

         for (Pair<Field,XMLfield> p : c.labeledFields) {
            Field f = p.getA();
            XMLfield a = p.getB();
            String type = a.type();
            try {
               String fieldName = a.name().equals("") ? f.getName() : a.name();  
               String serializedField = serializeFieldToXML(type,fieldName, f.get(o));
               serializedObj.add(serializedField);
            } catch (IllegalArgumentException | IllegalAccessException e) {
               continue;
               // e.printStackTrace();
            }
         }
         
         serializedObj.add("</" + c.name + ">\n");
         xmlResult.add(String.join("\n",serializedObj));
      }

      Path filePath = Paths.get(fileName);
      String result = String.join("\n", xmlResult);
      try {
         // Write the string to the file
         Files.write(filePath, result.getBytes());

         System.out.println("File written successfully.");
      } catch (IOException e) {
         e.printStackTrace();
         // System.out.println("File written successfully.");
      }
   }

   private static Map<String,IntrospectedClass> introspectedClasses = new HashMap<>();

   private static IntrospectedClass classLookup (Object o) {
      XMLSerializer tmp = new XMLSerializer(); // used only to instantiate IntrospectedClass
      String className = o.getClass().getName();
      introspectedClasses.putIfAbsent(className, tmp.new IntrospectedClass(o));
      return introspectedClasses.get(className);
   }

   private class IntrospectedClass {
      boolean labeled = false;
      String name = "";
      List<Pair<Field,XMLfield>> labeledFields = null;
      public IntrospectedClass (Object o) {
         this.name = o.getClass().getName();
         if (o.getClass().getAnnotation(XMLable.class) != null){
            this.labeled = true;
            this.labeledFields = Stream.of(o.getClass().getDeclaredFields())
               .map((Field f) -> {
                  XMLfield a = f.getAnnotation(XMLfield.class);
                  if (a != null && XMLfield.FieldType.isValid(a.type())) {
                      return new Pair<Field,XMLfield>(f, a);
                  }
                  return null;
               })
               .filter(pair -> pair != null)
               .collect(Collectors.toList());
         }
      }

   }

   private static String serializeFieldToXML(String type, String name, Object value){
      if (type == "" || name == "")
         throw new IllegalArgumentException();
      String value_str = value.toString();
      return "<" + name + " type= " + type + ">" + value_str + "</" + name + ">";
   }

   private class Pair<A, B> {
      private final A a;
      private final B b;
  
      public Pair(A a, B b) {
          this.a = a;
          this.b = b;
      }
      public A getA() {
          return a;
      }
      public B getB() {
          return b;
      }
  }
  
}