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
      /**
       * @param arr array of objects to be serialized
       * @param fileName path of output file
       */
      static void serialize(Object [ ] arr, String fileName) {
      List<String> xmlResult = new ArrayList<>(List.of("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
      for (Object o : arr) {
         
         // Perform lookup to avoid introspecting multiple times an already encountered class
         IntrospectedClass c = classLookup(o);
         if (!c.labeled){
            xmlResult.add("<notXMLable />");
            continue;   // skip if not XMLable
         }
         // serializedObj contains the lines composing
         // an object's XML serialization
         List<String> serializedObj = new ArrayList<>();
         // add class name first
         serializedObj.add("<" + c.name + ">");

         for (Pair<Field,XMLfield> p : c.labeledFields) {
            Field f = p.getA();
            XMLfield a = p.getB();
            String type = a.type();
            try {
               // check if a field name is specified in the annotation XMLField
               String fieldName = a.name().equals("") ? f.getName() : a.name();
               String serializedField = serializeFieldToXML(type,fieldName, f.get(o));
               serializedObj.add(serializedField);
            } catch (IllegalArgumentException | IllegalAccessException e) {
               // skip if field is not accessible, do not fail whole execution
               continue;
               // e.printStackTrace();
            }
         }
         // Close object serialization
         serializedObj.add("</" + c.name + ">\n");
         // Join serializedObj lines using a newline,
         // and add to the resulting XML which will be written to file
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
      }
   }

   /**
    * introspetedClasses keeps track of the classes encountered so far
    * to avoid having to re-introspect classes already examined
    */
   private static Map<String,IntrospectedClass> introspectedClasses = new HashMap<>();

   /**
    * Checks whether the class whose 'o' is an instance of has been previously introspected,
    * if yes, the corresponding IntrospectedClass object is returned,
    * otherwise a new IntrospectedClass is added to introspectedClasses first
    *
    * @param o
    * @return IntrospectedClass of which Object o is an instance of
    */
   private static IntrospectedClass classLookup (Object o) {
      XMLSerializer tmp = new XMLSerializer(); // used only to instantiate IntrospectedClass
      String className = o.getClass().getName();
      introspectedClasses.putIfAbsent(className, tmp.new IntrospectedClass(o));
      return introspectedClasses.get(className);
   }

   /**
    * This class wraps all the information that must be kept
    * when introspecting a class for the first time
    */
   private class IntrospectedClass {
      boolean labeled = false;   // flag to indicate whether the class is XMLable or not
      String name = "";          // Class.getName()
      List<Pair<Field,XMLfield>> labeledFields = null;
      
      public IntrospectedClass (Object o) {
         this.name = o.getClass().getName();
         if (o.getClass().getAnnotation(XMLable.class) != null){
            this.labeled = true;
            this.labeledFields = Stream.of(o.getClass().getFields())
               // map each Field to a Pair<Field,XMLfield> if possible
               //      otherwise null
               .map((Field f) -> {
                  XMLfield a = f.getAnnotation(XMLfield.class);
                  if (a != null && XMLfield.FieldType.isValid(a.type())) {
                      return new Pair<Field,XMLfield>(f, a);
                  }
                  return null;
               })
               .filter(pair -> pair != null) // Get only XMLable fields
               .collect(Collectors.toList());
         }
      }

   }

   
   /**
    * @param type type name be written
    * @param name name of the field
    * @param value 
    * @return String("<'name' type='type'> 'value' </'name'>") 
    */
   private static String serializeFieldToXML(String type, String name, Object value){
      // value == null is a valid case
      if (type == "" || name == "")
         throw new IllegalArgumentException();
      return "<" + name + " type= " + type + ">" + value + "</" + name + ">";
   }

   // Immutable pair
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