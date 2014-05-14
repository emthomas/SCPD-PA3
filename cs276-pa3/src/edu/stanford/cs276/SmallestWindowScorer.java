package edu.stanford.cs276;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//doesn't necessarily have to use task 2 (could use task 1, in which case, you'd probably like to extend CosineSimilarityScorer instead)
public class SmallestWindowScorer extends BM25Scorer
{

	/////smallest window specifichyperparameters////////
    double B = -1;    	    
    double boostmod = -1;
    Map<Document,Map<Query,Double>> smallestWindowDict = null;
    
    //////////////////////////////
	
	public SmallestWindowScorer(Map<String, Double> idfs,Map<Query,Map<String, Document>> queryDict) 
	{
		super(idfs, queryDict);
		handleSmallestWindow();
	}

	
	public void handleSmallestWindow()
	{
		smallestWindowDict = new HashMap<Document,Map<Query,Double>>();
		
		for(Query q : queryDict.keySet()) {
			//if(q.queryWords.size()==1)
			//System.out.println("Query: "+q);
			for(String url : queryDict.get(q).keySet()) {
				//System.out.println("\turl: "+url);
				Document doc = queryDict.get(q).get(url);
				//if(q.queryWords.size()==1)
				//System.out.println(doc);
				double minWindow = (double)doc.getSmallestWindow(q);
				//if(q.queryWords.size()==1)
				//System.out.println(minWindow);
				//Map<Document,Map<Query,Double>> smallestWindowDict = null;
				Map<Query,Double> queryWin = new HashMap<Query,Double>();
				queryWin.put(q, minWindow);
				smallestWindowDict.put(doc, queryWin);
				//System.out.println();
			}
		}
		/*
		 * @//TODO : Your code here
		 */
	}
	
//	public double checkWindow(Query q, String docstr, double curSmallestWindow, boolean isBodyField)
//	{
//	
//		/*
//		 * @//TODO : Your code here
//		 */
//		return -1;
//	}
	
	public double getBoost(Document d, Query q) {
		B = (((double)Integer.MAX_VALUE)/smallestWindowDict.get(d).get(q)==1?1:10);
		boostmod = smallestWindowDict.get(d).get(q)/Integer.MAX_VALUE;
		//return B*boostmod;
		double window = smallestWindowDict.get(d).get(q);
		if(window==Integer.MAX_VALUE) {
			boostmod = 1;
		}
		else if(window==q.getUniqueTermsCount()) {
			boostmod = 10;
		}
		else {
			boostmod=1+9/window;
		}
		
		return boostmod;
	}
	
	@Override
	public double getSimScore(Document d, Query q) {
		Map<String,Map<String, Double>> tfs = this.getDocTermFreqs(d,q);
		
		this.normalizeTFs(tfs, d, q);
		
		Map<String,Double> tfQuery = getQueryFreqs(q);
		
		//System.out.println(q.query);
		//if()
		//System.out.println("\t"+getNetScore(tfs,q,tfQuery,d)+"\t"+B+"\t"+getBoost(d,q));
		
		return getNetScore(tfs,q,tfQuery,d)*getBoost(d,q);
		//return 0;
	}

}
