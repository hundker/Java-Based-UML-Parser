

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Umlgen {

	public static String s = "@startuml\n";
	public static String classname;
	public static  List<String> list = new ArrayList<String>();
	public static  List<String> methodlist = new ArrayList<String>();
	public static  List<String> interfacelist = new ArrayList<String>();
	 public static void main(String[] args) throws Exception {
	        // creates an input stream for the file to be parsed
		 
		 //String  folderpath = args[0];
		 String folderpath = "C:\\Users\\Koushik\\Desktop\\Assignments\\"
		 		+ "202\\uml-parser-test-5\\";
		 File folder = new File(folderpath);
		 File[] listOfFiles = folder.listFiles();
		 s = s + "skinparam classAttributeIconSize 0 \n";//For public +, private -
		 //specific to Plant UML

		     for (int i = 0; i < listOfFiles.length; i++) {
		       
		       String  k= listOfFiles[i].getName();
		       k = k.replaceAll(".java", "");
		       list.add(k);
		       k="Collection<"+k+">";
		       list.add(k);
		       //System.out.println("print------"+k);
		      
		     }
		     System.out.println("listin main"+list);
		     //System.out.println(list);
		     //File dir = new File(args[0]);
		 File dir = new File(folderpath);
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		    	if(child.getName().contains(".java"))
		    	{
		    		
		    	FileInputStream in = new FileInputStream(child.getAbsolutePath());
		        CompilationUnit cu;//the object that can traverse ur file and can get
		        //all your method names etc..
		        try {
		           
		            cu = JavaParser.parse(in);
		        	}
		        finally {
		            in.close();
		        }
		        String temp = cu.toString();
		        //System.out.println(temp);
		        String lines[] = temp.split("\\r?\\n");
		        String delims = "[ .,?!]+";
		        String[] tokens = lines[0].split(delims);
		        List types = cu.getTypes(); 
		        //List types1 = cu.getTypes();
		         
		        
		        TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
		        //part of ast, Java Parser
		  
		        classname = typeDec.getName();
		        
		        if(tokens[1].equals("interface"))
		        	s = s + "interface" + " " + classname + "\n";
		        if(tokens[1].equals("class"))
		        	 s = s + "class" + " " + classname + "\n";
		        // visit and print the methods names
		        new InterfaceFinder().visit(cu, null);
		        new ClassFinder().visit(cu, null);
		        new MethodFinder().visit(cu, null);
		        new FieldFinder().visit(cu, null);
		        new ConstructorFinder().visit(cu, null);
		       // System.out.println("List in main function"+Umlgen.list);
		    }
		    }
		    s = s + "@enduml\n";
		    PlantumlTest p = new PlantumlTest();
		    p.umlCreator(s,folderpath);
		    System.out.println(s); 
	    }
	 }

}