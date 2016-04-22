import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Stack;

public aspect Main {
	pointcut allexecution(Object o) : !within(CustomStack) && !within(Main) && target(o) && execution(public * *.*(..))
	 && !execution(*.new(..));

	pointcut allConstructorCalls() : !within(CustomStack) && !within(Main) && execution(*.new(..));	
	
	public static class AspectState {
		private int xdepth =0 ;
		private int depth = 0;
		private Stack<String> xStack = new Stack<String>();
		private String  finalMsg = "";
		private CustomStack<Integer> id = new CustomStack<>();
		private int idVal = 1;
		
		public void incrementConstructorCall() {
			depth++;
		}
		
		public void decrementConstructorCall() {
			depth--;
		}
		
		public void popStackVal() {
			if(depth ==0) {
				updateStackVal();
				printSequenceDiagram();
			}
		}

		private void printSequenceDiagram() {
			if(xdepth == 0) {
				getSequenceDiagram(finalMsg, "C:\\Users\\Koushik\\Desktop\\SequenceDiagram\\sequence_diag.wsd", "qsd");
			}
		}

		private void getSequenceDiagram(String res, String fileName, String format) {
			FileWriter fw = null; 
			try { 
				fw = new FileWriter(fileName);
				fw.write(res.toCharArray());
				fw.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}

		private void updateStackVal() {
			xdepth--;
			if(!xStack.isEmpty()) {
				xStack.pop();
			}
			if(!id.isEmpty()) {
				idVal = (Integer)id.pop()+1;
			}
		}
		
		public String getLeftClassName() {
			return xStack.isEmpty()?"Main":(String)xStack.peek();
		}
		
		public void pushStackVal(String joinPoint) {
			if(depth ==0) {
				id.push(idVal);
				idVal=1;
				String result = getLeftClassName() + " ->" + getClass(joinPoint) + ":" + id.printAll() + " " + getMessage(joinPoint) + "\n";
				System.out.print(result);
				finalMsg += result;
				xdepth += 1;
				xStack.push(getClass(joinPoint));
			}
		}
		
		public static String getClass(String joinPoint) {
			String components[] = extractMethodSignature(joinPoint).split(" ");
			return components[1].substring(0, components[1].indexOf('.'));
		}

		private static String extractMethodSignature(String joinPoint) {
			return joinPoint.substring(9,joinPoint.lastIndexOf(')'));
		}
		
		public static String getMessage(String joinPoint) {
			String components[] = extractMethodSignature(joinPoint).split(" ");
			return components[1].substring(components[1].indexOf('.')+1) + " : "+ components[0];
		}
		
	}

	
	AspectState aspectState = new AspectState();

	before() : allConstructorCalls() {
		aspectState.incrementConstructorCall();
	}
	
	after() :  allConstructorCalls() {
		aspectState.decrementConstructorCall();
	}
	
	before(Object o)  : allexecution(o) { 
		aspectState.pushStackVal(thisJoinPoint.toString());
	}
	
	after(Object o) : allexecution(o){
		aspectState.popStackVal();
	}
}
