import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;


public class MethodFinder extends VoidVisitorAdapter {


    
    @Override
    public void visit(MethodDeclaration n, Object arg) {
    
    	if(n.getName()!=null)
    		Umlgen.methodlist.add(n.getName().toLowerCase());
    	String param= "";
    
    	if (n.getBody() !=null && n.getBody().getStmts()!=null) {
    	
    		for(Statement x : n.getBody().getStmts())
    		{
    			if(x!=null)
    			{
    			String k = x.toString();
    			String delims = "[ .,?!]+";
    		//	System.out.println("k :"+k);
		        String[] tokens = k.split(delims);
		       // System.out.println("token0"+tokens[0]);
		        if(tokens[0]!=null)
		        {
		        if(Umlgen.list.contains(tokens[0]))//list contains all class names.
		        	//comparing it with tokens[0] to find if the class name matches.
		        	//this will check dependency in methods
		        	//System.out.println("list :"+Umlgen.list);
		        	Umlgen.s = Umlgen.s + tokens[0] +"<.. " + Umlgen.classname + "\n";
		        }
    			}
    		}
    		
    	}
    
    	if(n.getParameters()!=null)
    	{
    	for(Parameter x : n.getParameters())
    	{
    		if(param != "")
        		param = param + "," + x.toString();
        		else 
        			param = x.toString();
    	String check =  x.getType().toString();
    	//System.out.println("Interfacelist : "+Umlgen.interfacelist);
    //	System.out.println("Classname : "+Umlgen.classname);
    	
    	if(Umlgen.list.contains(check))
    	{
    		if(!Umlgen.s.contains(check + "<.. "  + Umlgen.classname + ":uses") 
    				&& Umlgen.interfacelist.contains(check) 
    				&& !Umlgen.interfacelist.contains(Umlgen.classname))//note
    	Umlgen.s = Umlgen.s + check + "<.. "  + Umlgen.classname + ":uses" + "\n";
    	}
    	}	
    	}
    	//System.out.println(n.toString());
    	//System.out.println("getmodifiers : "+n.getModifiers());
    
    	if(n.getModifiers()==1)
    	{
    		Umlgen.s = Umlgen.s + Umlgen.classname + " : "+ "+" + n.getName() + "("+ param +")" + ":" + n.getType();
    		Umlgen.s = Umlgen.s + "\n";
    	}
    }
    
    


}
