package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */

		
		
		private static ArrayList<String> depthFirstSearch(ArrayList<String> cnex, Graph g, Person start, boolean[] checkAlready, int[] interate, int[] sCounter, int[] last, ArrayList<String> rewind, boolean checkS)
		{
			int e = 0;
			
			
			
			Friend nextOne = start.first;
			
			checkAlready[g.map.get(start.name)] = true;
			
			sCounter[g.map.get(start.name)] = interate[0];
			
			last[g.map.get(start.name)] = interate[1];
			
			while (nextOne != null) {
				
				int rnthru = 0;
				if (checkAlready[nextOne.fnum] != true)
				{
					rnthru =1;
				}
				switch(rnthru)
				
				{
				case 1:
					interate[1]++;
					interate[0]++;
					
				
					cnex = depthFirstSearch(cnex, g, g.members[nextOne.fnum], checkAlready, interate, sCounter, last, rewind, false);
					
					int fillx = 0;
					if (sCounter[g.map.get(start.name)] <= last[nextOne.fnum]) 
					{
						fillx=1;
					}
					
					switch (fillx)
					{case 1:
					
						
						if ((cnex.contains(start.name) != true && checkS == false) || (cnex.contains(start.name) != true && rewind.contains(start.name)) )
						{
							cnex.add(start.name);
						}
					break;
					
					case 0:
					 
						int nNum = last[nextOne.fnum];
						
						int sNum = last[g.map.get(start.name)];
						
						if (nNum > sNum) {
							last[g.map.get(start.name)] = sNum;
						}
						else {
							last[g.map.get(start.name)] = nNum;
						} 
					 	break;
					}		
				rewind.add(start.name);
				break;
				
				case 0:
					int secondNum = last[g.map.get(start.name)];
					
					int thirdNum = sCounter[nextOne.fnum];
					
					if (secondNum < thirdNum) {
						last[g.map.get(start.name)] = secondNum;
					}
					else {
						last[g.map.get(start.name)] = thirdNum;
					}
					break;
				}
				nextOne = nextOne.next;
			}
			return cnex;
		}
	
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
			int a = 0;
			int b = 0;
			int c = 0;
			
			if (g==null)
			{
				a=1;
			}
			switch(a)
			{
			case 1:
				return null;
			}
			
			if (p1 == null)
			{
				b=1;
			}
			switch (b)
			{
			case 1:
				return null;
			}
			
			if (p2 == null)
			{
				c=1;
			}
			switch (c)
			{
			case 1:
				return null;
			}
				
			ArrayList<Person> listHolder = new ArrayList<Person>();
				
				boolean[] alreadyChecked = new boolean[g.members.length];
				
				Person[] runBack = new Person[g.members.length];
				
				ArrayList<String> answer = new ArrayList<String>();
				
			
				
				int countr = g.map.get(p1);
				
			
				listHolder.add(g.members[countr]);
				
			
				alreadyChecked[countr] = true;
				
				
				while (listHolder.isEmpty() != true) {
					
				
					Person vertx = listHolder.remove(0);
					
					int chckVertx = g.map.get(vertx.name);
					alreadyChecked[chckVertx] = true;
					
				
					Friend nxt = vertx.first;
					
					int chck = 0;
					if (nxt == null) 
					{
						chck = 1;
						
					}
					else
					{
						chck=0;
					}
					
					switch(chck)
					{
					case 1: 
						return null;
					}
					if (nxt == null) {
						return null;
					}
					
					while (nxt != null) {
						
						if (alreadyChecked[nxt.fnum] != true) {
							alreadyChecked[nxt.fnum] = true;
							runBack[nxt.fnum] = vertx; 
							listHolder.add(g.members[nxt.fnum]);
							
						
							if (g.members[nxt.fnum].name.equals(p2)) {
								vertx = g.members[nxt.fnum];
								
								while (vertx.name.equals(p1) != true) {
									answer.add(0, vertx.name);
									vertx = runBack[g.map.get(vertx.name)];
								}
								answer.add(0, p1);
								return answer;
							}
						}
						nxt = nxt.next;
					}
				}
				return null;
		
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
//		int c1 = 0;
//		int c2=0;
//				if (g == null) {
//					c1=1;
//				}
//				if (school == null) {
//					c2=1;
//				}
//				switch(c1)
//				{case 1:
//					return null;}
//				switch(c2)
//				{case 1:
//					return null;}
				
			
		
		ArrayList<Person> personList = new ArrayList<>();
		
		

		boolean checker = false;
		
		ArrayList<ArrayList<String>> gang = new ArrayList<>();
		
		boolean[] nameCheck = new boolean[g.members.length];
		
		int len = 0;

		for(int tLen = 0; tLen < nameCheck.length; tLen++)
		{
		if(g.members[tLen].school!=null && g.members[tLen].school.equals(school))
		{
		len = tLen;

		checker = true;
		break;
		}	
		
		}
		int wy = 0;
		if(checker != true && len == 0)
		{

		wy=1;

		}
		switch (wy)
		{
		case 1:
			return null;
		}
		
		

		while(nameCheck.length > len)
		{

		

		nameCheck[len] = true;
		ArrayList<String> teList = new ArrayList<>();
		personList.add(g.members[len]);

		teList.add(g.members[len].name);


		while (personList.size() != 0)
		{

		Person rem = personList.remove(0);

		Friend firstt = rem.first;


		while ((firstt != null)&&(g.members[firstt.fnum].school!=null) &&
		(g.members[firstt.fnum].school.equals(school))   )
		{
		if (!nameCheck[firstt.fnum])
		{
			nameCheck[firstt.fnum] = true;

			personList.add(g.members[firstt.fnum]);

			teList.add(g.members[firstt.fnum].name);

			firstt = firstt.next;
		}
		else
		{
			firstt = firstt.next;

		}
		}
		}

		if(teList != null)
		{

		gang.add(teList);

		}
		while(len < nameCheck.length)

		{

		if((!nameCheck[len] && g.members[len].school!=null) &&
		(g.members[len].school.equals(school)))

		{
			nameCheck[len] = true;

			personList.add(g.members[len]);
		break;
		}
		len++;
		}
		}
		return gang;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		
		int k = 0;
		
		
				if (g == null) {
					k=1;
				}
				switch(k)
				{case 1:
					return null;}
				
				
				int[] prevCounter = new int[g.members.length];
				
				int[] counter= new int[g.members.length];
				
				ArrayList<String> allBefore = new ArrayList<String>();
				
				boolean[] alreadyChecked = new boolean[g.members.length];

				ArrayList<String> cAnswer = new ArrayList<String>();
				
			
			
				
				int itr=0;
				while (itr < g.members.length){
					if (alreadyChecked[itr] == false) {
						cAnswer = depthFirstSearch(cAnswer, g, g.members[itr], alreadyChecked, new int[] {0,0}, counter, prevCounter, allBefore, true);
					}
					itr++;
				}
				
				return cAnswer;
		
	}
}


