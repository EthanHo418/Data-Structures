package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		File exi = new File(docFile);
		if(!exi.exists()) {
			throw new FileNotFoundException("no file found");
		}
		Scanner wurds = new Scanner(new File(docFile));
		HashMap<String, Occurrence> rtnT = new HashMap<>();
		while(wurds.hasNext()) {
			String strng = wurds.next();
			strng = getKeyword(strng);
			if(strng == null) {
				continue;
			}
			
			int x=0;
			if(rtnT.containsKey(strng)) {x=1;}
			else if(!(rtnT.containsKey(strng))) {x=2;}
		
			switch(x)
			{
			case 1:
				rtnT.get(strng).frequency++;
				break;
			case 2:
				rtnT.put(strng, new Occurrence(docFile, 1));
				break;
			}
			
			
		}
		
		wurds.close();
		
	
		return rtnT;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) 
	{
		for (String x :kws.keySet())
		{
			if(!(keywordsIndex.containsKey(x)))
			{
			ArrayList<Occurrence> occ = new ArrayList<Occurrence>();
			occ.add(kws.get(x));
			keywordsIndex.put(x, occ);
			}
			else 
			{
			keywordsIndex.get(x).add(kws.get(x));
			insertLastOccurrence(keywordsIndex.get(x));
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		word = word.toLowerCase();
		int iterate = word.length()-1;
		while(iterate>=0){
			if((word.charAt(iterate)=='!')||(word.charAt(iterate)=='?')||(word.charAt(iterate)==',')||(word.charAt(iterate)==';')||(word.charAt(iterate)==':')||(word.charAt(iterate)=='.'))
			{
				word = word.substring(0,iterate);
			}
			else break;
			iterate--;
		}		
		word = word.toLowerCase();
        
	
        if (noiseWords.contains(word) == true) 
        {
        	return null;
        }
        
        int f = word.length()-1;
        while(f>=0)
        {
            if (!Character.isLetter(word.charAt(f)))
            {
                return null;
            }
            f--;
        }
        return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		
		if (occs.size() == 1) 
		{
			return null;
		}
		
		ArrayList<Integer> centr = new ArrayList<Integer>();
		
		int countr = 0;
		int check = occs.get(occs.size() - 1).frequency;
		
		int c = occs.size() - 2;
		int b = 0;
		int a = 0;
		
		while (a <= c) {
			
			b = (a + c) / 2;
			centr.add(b);
			
			countr = occs.get(b).frequency;
			
			if (countr == check) {
				
				Occurrence real = occs.get(occs.size() - 1);
				
				occs.add(b + 1, real);
				occs.remove(occs.size() - 1);
				break;
			}
			if (countr > check) {
				a = b + 1;
			}
			else {
				c = b - 1;
			}
		}
		
		if (countr < check) {
			Occurrence real = occs.get(occs.size() - 1);
			occs.add(b, real);
			occs.remove(occs.size() - 1);
		}
		else {
			if (countr > check) {
				Occurrence real  = occs.get(occs.size() - 1);
				occs.add(b + 1, real);
				occs.remove(occs.size() - 1);
			}
		}
		
		return centr;
	
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
				
				ArrayList<String> textss = new ArrayList<String>();
				
				int cter = textss.size();
				
				if (keywordsIndex.containsKey(kw2) == false && keywordsIndex.containsKey(kw1) == false ) {
					return null;
				}
				
				ArrayList<Occurrence> compare = null;
				ArrayList<Occurrence> compare2 = null;
				
				
				int dd = 0;

		        if (keywordsIndex.containsKey(kw1) != false) {
		            dd = 1;
		        }

		        switch(dd)
		        {
		        case 1:
		            compare2 = keywordsIndex.get(kw1);
		            break;
		        }

		        
		        dd = 0;

		        
		        if (keywordsIndex.containsKey(kw2) != false) {
		            dd = 1;
		        }

		        switch(dd)
		        {
		        case 1:
		            compare = keywordsIndex.get(kw2);
		            break;
		        }
		
				
				if (compare == null) {
					int runthru=0;
					while (runthru < compare2.size()) {
						textss.add(compare2.get(runthru).document);
						cter++;
						if (cter == 5) {
							break;
						}
						runthru++;
					}
				}
				else if (compare2 == null) {
					int runnthru=0;
					while (runnthru < compare.size()) {
						textss.add(compare.get(runnthru).document);
						cter++;
						if (cter == 5) {
							break;
						}
						runnthru++;
					}
				}
				else {
					int iteration = 0;
					
					int secIteration = 0;
					
					while ( iteration<compare.size()&&secIteration<compare2.size()) {
						
						int greatr = compare2.get(secIteration).frequency;
						int lesr = compare.get(iteration).frequency;
						
						
		                int yo = 1;
		                
		                if (greatr >= lesr)
		                {
		                    yo = 2;
		                }

		                switch(yo)
		                {
		                case 1:
		                    if (textss.contains(compare.get(iteration).document)) {
		                    	iteration++;
		                    }
		                    else {
		                    	textss.add(compare.get(iteration).document);
		                        cter++;
		                        iteration++;
		                    }
		                    break;
		                case 2:
		                    if (textss.contains(compare2.get(secIteration).document)) {
		                    	secIteration++;
		                    }

		                    else 
		                    {
		                    	textss.add(compare2.get(secIteration).document);
		                        cter++;
		                        secIteration++;
		                    }
		                    break;
		                }
						
						if (cter == 5) {
							break;
						}
					}
					
					if (cter < 5) {
						
						if (secIteration <compare2.size()) {
							while ( secIteration < compare2.size()&&cter < 5) {
								
								if (textss.contains(compare2.get(secIteration).document)) {
									secIteration++;
								}
								else {
									textss.add(compare2.get(secIteration).document);
									cter++;
									secIteration++;
								}
							}
						}
						else {
							while ( iteration<compare.size() && cter < 5) {
								
								if (textss.contains(compare.get(iteration).document)) {
									iteration++;
								}
								else {
									textss.add(compare.get(iteration).document);
									cter++;
									iteration++;
								}
							}
						}
					}
				}
				return textss;
			}
	}

