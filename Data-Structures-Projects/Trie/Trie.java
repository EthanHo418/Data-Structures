package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	
	private static ArrayList<String> makeNew(String target, ArrayList<String> words)

	{
		
	    ArrayList<String> x = new ArrayList<String>();
		int j=0;
		
		j = ((CharSequence) target).length();
		for(int i=0;i<words.size();i++)
		{
			if (words.get(i).substring(0, j) == target)
			{
				x.add(words.get(i));
				words.remove(i);
				i--;
			}
			
		} 	
		return x;
	}
	
	
	private static int same(String word1, String word2, int index) {
		
		
		int i = 0;
		int samenum = 0;
		int small = Math.min(word1.length(), word2.length() );
		while  (i < (small)) {
			
				
			if (word1.charAt(0) != word2.charAt(0)) {
				return -1;
			}
			
			if (word1.charAt(i) == word2.charAt(i)) {
				samenum++;
			}
			else {
				return samenum;
			}
			i++;
		}
		return samenum;
	
	
}
	
	private static int index(String[] original, String searchtarget)
	{
		int i=0;
		int index=0;
		while (i<original.length)
		{
			if (original[i].equals(searchtarget))
			{
				index=i;
			}
			i++;
		}
		return index;
	}
	
	
	public static TrieNode buildTrie(String[] allWords) {
		
		TrieNode root = new TrieNode (null, null, null);
		if (allWords.length==0)
			{return null;}
		TrieNode x = null;
		int increment=0;
		while (increment < allWords.length) {
			ArrayList<String> matchUp = new ArrayList<String>();
			
			Indexes holder;
		
				if (increment == 0) {
					holder = new Indexes( increment, (short) 0, (short) (allWords[0].length() - 1) );
					root.firstChild = new TrieNode(holder, null, null);
					x = root.firstChild;
				}
				else {
				
					int goodToGo = 2;
					
					TrieNode above = root;
					
					x = root.firstChild;
					
					int insert=2;
					
					TrieNode whichOne = null;				
					
					int under = 1;
		
					while (x != null) {
						
						
						String compareTo = allWords[x.substr.wordIndex].substring(0, x.substr.endIndex+1);
						String compareWith = allWords[increment];
						int counter = index(allWords, compareWith);
						int shared = same(compareTo, compareWith, counter);
					
						
						if (shared == -1) {
							above = x;
							x = x.sibling;
							under = 2;
							counter ++;
						}
						else {
							if (shared - 1 == x.substr.endIndex) {
								
								above = x;
								x = x.firstChild;
								under = 1;
								goodToGo = 1;
								whichOne = above;
								counter--;
							}
							else {
								Indexes target = null;
								TrieNode word = null;
								
								if (goodToGo == 2) {
									Indexes update = new Indexes(x.substr.wordIndex, (short) shared, (short) x.substr.endIndex);
									
									Indexes newWord = new Indexes(increment, (short) shared, (short) (allWords[increment].length() - 1)); 
									
									target = new Indexes(x.substr.wordIndex, (short) 0, (short) (shared - 1));
									
									ArrayList<String> matchers = new ArrayList<String>(); 
									matchers=(makeNew("", matchers));
									x = new TrieNode(update, x.firstChild, x.sibling);
									
									word = new TrieNode(newWord, null, null);
									
									TrieNode otherTarget= new TrieNode(target, x, x.sibling);
									
									x.sibling = word;
									
									if(under == 2) {
										above.sibling = otherTarget;
									}
									else {
										above.firstChild = otherTarget;
									}
									matchUp = matchers;
									insert = 1;
									break;
								}
								else {
									if (shared - 1 != whichOne.substr.endIndex) {
										target = new Indexes(x.substr.wordIndex, (short) (whichOne.substr.endIndex + 1), (short) (shared - 1));
										Indexes newWord = new Indexes(increment, (short) shared, (short) (allWords[increment].length() - 1)); 
										Indexes update = new Indexes(x.substr.wordIndex, (short) shared, (short) x.substr.endIndex);
										x = new TrieNode(update, x.firstChild, x.sibling);
										word = new TrieNode(newWord, null, null);
										
										TrieNode newTarget = new TrieNode(target, x, x.sibling);
										x.sibling = word;
									
										if (under == 2) {
											above.sibling = newTarget;
										}
										else {
											above.firstChild = newTarget;
										}
									insert=1;
									break;
									}
									else {
										above = x;
										x = x.sibling;
										under = 2;
									
									}
								}
							}	
						}
					}
					
					if (insert == 2) {
						if (goodToGo==1) {
							Indexes nextWord = new Indexes(increment, (short) (whichOne.substr.endIndex+1), (short) (allWords[increment].length() - 1));
							TrieNode otherOne = new TrieNode(nextWord, null, null);
							above.sibling = otherOne;
						}
						else {
							Indexes nextWord = new Indexes(increment, (short) 0, (short) (allWords[increment].length() - 1));
							TrieNode otherOne = new TrieNode(nextWord, null, null);
							above.sibling = otherOne;
						}
					}
					
				}
				
				increment++;
			}
		
			return root;
		}
		
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		
		
		if(root == null) return null;
	
		TrieNode tracker = root;
		
		ArrayList<TrieNode> samePrefixes = new ArrayList<>();
		
		while(tracker != null) {
		
			if(tracker.substr == null) 
				tracker = tracker.firstChild;
			
			String firstWord = allWords[tracker.substr.wordIndex];
			String secondWord = firstWord.substring(0, tracker.substr.endIndex+1);
			
			if( prefix.startsWith(secondWord)||firstWord.startsWith(prefix) ) 
			{
				if(tracker.firstChild != null) 
				{ 
					samePrefixes.addAll(completionList(tracker.firstChild, allWords, prefix));
					tracker = tracker.sibling;
					
				} 
				else 
				{ 
					samePrefixes.add(tracker);
					tracker = tracker.sibling;
					
				}
				
			} 
			else {
				tracker = tracker.sibling;
			 
			}
		}
		if (samePrefixes.isEmpty())
		{return null;}
		
		return samePrefixes;
	}
		
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
