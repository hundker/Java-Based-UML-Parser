import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
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

public class AspectState1 {
		private int calldepth =0 ;
		private int depth = 0;
		private Stack<String> callStack = new Stack<String>();
		private String  finalMessage = "";
		private CustomStack<Integer> identifier = new CustomStack<>();
		private int i = 1;
		
		public void incrementConstructorCall() {
			depth++;
		}
		
		public void decrementConstructorCall() {
			depth--;
		}
		
		public void popStackVal() {
			if(depth ==0) {
				calldepth--;
				if(!callStack.isEmpty()) {
					callStack.pop();
				}
				if(!identifier.isEmpty()) {
					i = (Integer)identifier.pop()+1;
				}
				if(calldepth == 0) {
					getSequenceDiagram(finalMessage, "seq_diag.png", "qsd");
				}
			}
		}
		
		public void pushStackVal(String joinPoint) {
			if(depth ==0) {
				String leftClass = callStack.isEmpty()?"Main":(String)callStack.peek(),
						rightClass = getClass(joinPoint),
						message = getMessage(joinPoint);
				identifier.push(i);
				i=1;
				String id = identifier.printAll();
				String result = leftClass + " ->" + rightClass + ":" + id + " " +message + "\n";
				System.out.print(result);
				finalMessage += result;
				calldepth++;
				callStack.push(rightClass);
			}
		}
		
		public static String getClass(String joinPoint) {
			String result = "";	
			String methodSignature = extractMethodSignature(joinPoint);
			String components[] = methodSignature.split(" ");
			String resul =  components[1];
			int indexOfDot = resul.indexOf('.');
			result = resul.substring(0,indexOfDot);
			return result;
		}

		private static String extractMethodSignature(String joinPoint) {
			return joinPoint.substring(9,joinPoint.lastIndexOf(')'));
		}
		
		public static String getMessage(String joinPoint) {
			String components[] = extractMethodSignature(joinPoint).split(" ");
//			String returnType = ;
//			String methodCall = ;
			return components[1].substring(components[1].indexOf('.')+1) + " : "+ components[0];
		}
		
		  public static void getSequenceDiagram( String text, String outFile, String style) 
		    {
		        try {
		            String data = "style=" + style + "&message=" + URLEncoder.encode(text, "UTF-8") + "&apiVersion=1";
		            URL url = new URL("http://www.websequencediagrams.com");
		            URLConnection conn = url.openConnection();
		            conn.setDoOutput(true);
		            OutputStreamWriter writer = new OutputStreamWriter(
		                    conn.getOutputStream());
		            writer.write(data);
		            writer.flush();
		            StringBuffer answer = new StringBuffer();
		            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		            String line;
		            while ((line = reader.readLine()) != null) {
		                answer.append(line);
		            }
		            writer.close();
		            reader.close();
		            String json = answer.toString();
		            int start = json.indexOf( "?png=" );
		            int end = json.indexOf( "\"", start );
		            url = new URL( "http://www.websequencediagrams.com/" + 
		                json.substring(start, end) );
		            OutputStream out = new BufferedOutputStream( new FileOutputStream(
		                        outFile ));
		            InputStream in = url.openConnection().getInputStream();
		            byte[] buffer = new byte[1024];
		            int numRead;
		            while((numRead = in.read(buffer)) != -1 ) {
		                out.write( buffer, 0, numRead );
		            }
		            in.close();
		            out.close();
		        } catch (MalformedURLException ex) {
		            ex.printStackTrace();
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    }
	}

