package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    {
    	
    	String add = "";
    	
    	String operations = "-+()/*]";
 
     	
   
    	
    	expr = expr.replaceAll(" ", "");
    	
    	int iterate =0;
    	
    	while (iterate<expr.length()) 
    	{ 	
  
    		if (Character.isDigit(expr.charAt(iterate))) 
    		{
    			iterate ++;
    			continue;
    			
    		}
    		
    		if((delims.contains(Character.toString(expr.charAt(iterate))) == false))
    		{ 
    			add = add + expr.charAt(iterate);
    		}
    		 
    		
    		else if (operations.contains(Character.toString(expr.charAt(iterate))))
    		{
    			if(add != "") 
    			{
    				Variable copy = new Variable(add); 
    				
    				if(vars.contains(copy) == false) 
    				{
		    			vars.add(copy);
    				}
    				add = "";
    			}
    		}
    		
    		else if (expr.charAt(iterate) == '[') 
    		{
    			if(add != "") 
    			{
	    			Array copy = new Array(add); 
	    			arrays.add(copy);
	    			add = "";
    			}
    		}
    		
    		iterate ++;
    	}
    	Variable mini = new Variable(add); 
    	if((delims.contains(add)==false && (vars.contains(mini) != true))) 
    	{
    		
    	vars.add(mini); 
    	}
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    //private static boolean

    private static int pemdas(char x) 
    {
    	int choose = 1;
    	
    	
    	
    	if (x == '+')
    	{choose=2;}
    	
    	else if (x == '-')
    	{choose=2;}
    	
    	else if (x == '*')
    	{choose=3;}
    	
    	else if (x == '/')
    	{choose=3;}
    	
    	return choose;
    	
    	
    }
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    {
    	expr = expr.replaceAll(" ", "");
    	
    	//no divide by 0 needed
    	
    	Stack<Character> operators = new Stack<>();
    	
    	ArrayList<String> values = new ArrayList<String>();
		
    	StringTokenizer runthrough = new StringTokenizer(expr, delims, true);
    	
		int iteration = 0;

		while (runthrough.hasMoreTokens()) 
		{
			String x = runthrough.nextToken();
			
			iteration = iteration + x.length();

			if (Character.isDigit(x.charAt(0)))
			{
				values.add(x);
			}
			
			else if (x.charAt(0) == '-' && values.isEmpty())
			{
				String temp = "";
				String nextToken = runthrough.nextToken();
				
				for (int count = 0; count < nextToken.length(); count++)
				{
					if (Character.isDigit(nextToken.charAt(count)) || nextToken.charAt(count) == '.')
					{
						temp += nextToken.charAt(count);
					}
					
					else if (Character.isLetter(nextToken.charAt(count)))
					{
						boolean checkarr = false;

						if (iteration < expr.length() && expr.charAt(iteration) == '[')
						{
							checkarr = true;
						}

						if (checkarr)
						{
							int iteratearray = arrays.indexOf(new Array(x));

						
							iteration = iteration + runthrough.nextToken().length();
							
							int first = iteration;

							int num = 2;
							
							while (num > 1) 
							{
								String nxtToken = runthrough.nextToken();
								
								char neighbor = nxtToken.charAt(0);
								iteration += nxtToken.length();

								if (neighbor == ']') 
								{
									num--;
								} 
								
								else if (neighbor == '[')
								{
									num++;
								}
							}

							int index = (int) evaluate(expr.substring(first, iteration - 1), vars, arrays);
							
							values.add( "" + arrays.get(iteratearray).values[index]);
						} 
						
						else 
						{
							int countv = vars.indexOf(new Variable(x));
							
							values.add( "" + vars.get(countv).value);
						}
						
					} 
					
					else if (x.charAt(0) == '(')
					{
						int first = iteration;

						int value = 2;
						while (value > 1)
						{
							String nxtToken = runthrough.nextToken();
							char neighbor = nxtToken.charAt(0);
							iteration += nxtToken.length();

							if (neighbor == ')')
							{
								value--;
							} 
							
							else if (neighbor == '(')
							{
								value++;
							}
						}

						float tmp = evaluate(expr.substring(first, iteration - 1), vars, arrays);
						values.add( "" + tmp);
						
					}
					else 
						
						break;
				}
				
				
		
				values.add("-"+ temp);
				
				
			}
			
			else if (Character.isLetter(x.charAt(0))) 
			{
				boolean checkarr = false;

				if (iteration < expr.length() && expr.charAt(iteration) == '[')
				{
					checkarr = true;
				}

				if (checkarr)
				{
					int iteratearray = arrays.indexOf(new Array(x));

				
					iteration = iteration + runthrough.nextToken().length();
					
					int first = iteration;

					int value = 2;
					
					while (value > 1) 
					{
						String nxtToken = runthrough.nextToken();
						
						char neighbor = nxtToken.charAt(0);
						iteration += nxtToken.length();

						if (neighbor == ']') 
						{
							value--;
						} 
						
						else if (neighbor == '[')
						{
							value++;
						}
					}

					int index = (int) evaluate(expr.substring(first, iteration - 1), vars, arrays);
					values.add( "" + arrays.get(iteratearray).values[index]);
				} 
				
				else 
				{
					int countv = vars.indexOf(new Variable(x));
					values.add( "" + vars.get(countv).value);
				}
				
			} 
			
			else if (x.charAt(0) == '(')
			{
				int first = iteration;

				int value = 1;
				while (value > 0)
				{
					String nxtToken = runthrough.nextToken();
					char neighbor = nxtToken.charAt(0);
					iteration += nxtToken.length();

					if (neighbor == ')')
					{
						value--;
					} 
					
					else if (neighbor == '(')
					{
						value++;
					}
				}

				float tmp = evaluate(expr.substring(first, iteration - 1), vars, arrays);
				values.add( "" + tmp);
				
			} 
			
			else if (-1 != "+/-*".indexOf(x.charAt(0)))
			{
				
				while (!operators.isEmpty() && pemdas (operators.peek()) >= pemdas (x.charAt(0)))
				{
					char first = operators.pop();
					
					float prevv = Float.parseFloat(values.get(values.size() -1));
					values.remove(values.size() -1);
					
					float nextt = Float.parseFloat(values.get(values.size() -1));
					values.remove(values.size() -1);
				
					float num = 0;
					
					if (first =='+')
			    	{num= nextt+prevv;}
			    	
			    	else if (first=='-')
			    	{num=nextt-prevv;}
			    	
			    	else if (first=='*')
			    	{num=nextt*prevv;}
			    	
			    	else if (first =='/')
			    	{num=nextt/prevv;}
					values.add("" +num);
				}

				operators.push(x.charAt(0));
			}
		}

		while (!operators.isEmpty()) 
		{
			char firstt = operators.pop();

			float nextt = Float.parseFloat(values.get(values.size() -1));
			
			values.remove(values.size() -1);
			
			float prevv = Float.parseFloat(values.get(values.size() -1));
			
			values.remove(values.size() -1);
			
			float value = 0;
			if (firstt =='+')
	    	{value= prevv+nextt;}
	    	
	    	else if (firstt=='-')
	    		
	    	{value=prevv-nextt;}
	    	
	    	else if (firstt=='*')
	    	{value=prevv*nextt;}
	    	
	    	else if (firstt=='/')
	    	{value=prevv/nextt;}
			
			values.add("" + value);
		}

		return Float.parseFloat(values.get(values.size() -1));
	}
}
