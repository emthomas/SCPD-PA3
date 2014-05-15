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
    public void setParameters(String[] args) 
	{
		urlweight = Double.parseDouble(args[2]);
	    titleweight  = Double.parseDouble(args[3]);
	    bodyweight = Double.parseDouble(args[4]);
	    headerweight = Double.parseDouble(args[5]);
	    anchorweight = Double.parseDouble(args[6]);
	    double[] fieldsweight = {urlweight,titleweight,bodyweight,headerweight,anchorweight};
	    this.fieldsweight = fieldsweight;
	    ///////bm25 specific weights///////////////
	  //String[] TFTYPES = {"url","title","body","header","anchor"};
	    burl=Double.parseDouble(args[7]);
	    btitle=Double.parseDouble(args[8]);
	    bheader=Double.parseDouble(args[9]);
	    bbody=Double.parseDouble(args[10]);
	    banchor=Double.parseDouble(args[11]);
	    double[] bfields = {burl,btitle,bbody,bheader,banchor};
	    this.bfields = bfields;

	    k1=Double.parseDouble(args[12]);
	    pageRankLambda=Double.parseDouble(args[13]);
	    pageRankLambdaPrime=Double.parseDouble(args[14]);
	    
	    B = Double.parseDouble(args[15]);
	}
    
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
		boostmod = smallestWindowDict.get(d).get(q)/Integer.MAX_VALUE;
		//return B*boostmod;
		double window = smallestWindowDict.get(d).get(q);
		if(window==Integer.MAX_VALUE) {
			boostmod = 1;
		}
		else if(window==q.getUniqueTermsCount()) {
			boostmod = B;
		}
		else {
			boostmod=1+(B-1)/window;
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
