package edu.stanford.cs276;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BM25Scorer extends AScorer
{
	Map<Query,Map<String, Document>> queryDict;
	
	public BM25Scorer(Map<String,Double> idfs,Map<Query,Map<String, Document>> queryDict)
	{
		super(idfs);
		this.queryDict = queryDict;
		this.calcAverageLengths();
	}

	
	///////////////weights///////////////////////////
    double urlweight = -1;
    double titleweight  = -1;
    double bodyweight = -1;
    double headerweight = -1;
    double anchorweight = -1;
    
    ///////bm25 specific weights///////////////
    double burl=-1;
    double btitle=-1;
    double bheader=-1;
    double bbody=-1;
    double banchor=-1;

    double k1=-1;
    double pageRankLambda=-1;
    double pageRankLambdaPrime=-1;
    //////////////////////////////////////////
    
    ////////////bm25 data structures--feel free to modify ////////
    
    Map<Document,Map<String,Double>> lengths;
    Map<String,Double> avgLengths;
    Map<Document,Double> pagerankScores;
    
    //////////////////////////////////////////
    
    //sets up average lengths for bm25, also handles pagerank
    public void calcAverageLengths()
    {
    	lengths = new HashMap<Document,Map<String,Double>>();
    	avgLengths = new HashMap<String,Double>();
    	pagerankScores = new HashMap<Document,Double>();
    	
    	//loop over the queries
    	for(Query q : this.queryDict.keySet()) {
    		//loop over the docs
    		for(Document doc : this.queryDict.get(q).values()) {
    			//get the length for each type
				int[] typelen = doc.getLengths();
				Map<String,Double> len = new HashMap<String,Double>();
				for(int i=0; i<this.TFTYPES.length; i++) {
					len.put(this.TFTYPES[i], (double)typelen[i]);
				}
				lengths.put(doc, len);
			}
    	}
    	
    	for(Document doc : lengths.keySet()) {
    		System.out.println(doc);
    		for(Map.Entry<String, Double> entry : lengths.get(doc).entrySet()) {
    			System.out.println("\t"+entry.getKey()+": "+entry.getValue());
    			String type = entry.getKey();
    			double length = (double)entry.getValue();
    			if(avgLengths.containsKey(type)) {
    				avgLengths.put(type, avgLengths.get(type) + length);
    			}
    			else {
    				avgLengths.put(type,length);
    			}
    		}
    		System.out.println();
    	}
    	
		/*
		 * @//TODO : Your code here
		 */
    	
    	//normalize avgLengths
		for (String tfType : this.TFTYPES)
		{
			avgLengths.put(tfType, avgLengths.get(tfType)/lengths.size());
			/*
			 * @//TODO : Your code here
			 */
		}

    }
    
    ////////////////////////////////////
    
    
	public double getNetScore(Map<String,Map<String, Double>> tfs, Query q, Map<String,Double> tfQuery,Document d)
	{
		double score = 0.0;
		
		/*
		 * @//TODO : Your code here
		 */
		
		return score;
	}

	//do bm25 normalization
	public void normalizeTFs(Map<String,Map<String, Double>> tfs,Document d, Query q)
	{
		//System.out.println("Doc: "+d.toString());
		for(String field: tfs.keySet()) {
			//System.out.println(field);
			for(Map.Entry<String, Double> termtf : tfs.get(field).entrySet()) {
				//System.out.println("\t"+termtf.getKey()+": "+termtf.getValue());
			}
		}
		/*
		 * @//TODO : Your code here
		 */
	}

	
	@Override
	public double getSimScore(Document d, Query q) 
	{
		
		Map<String,Map<String, Double>> tfs = this.getDocTermFreqs(d,q);
		
		this.normalizeTFs(tfs, d, q);
		
		Map<String,Double> tfQuery = getQueryFreqs(q);
		
		
        return getNetScore(tfs,q,tfQuery,d);
	}

	
	
	
}
