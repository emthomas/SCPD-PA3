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
	}
	
	///////////////weights///////////////////////////
    double urlweight = 10;
    double titleweight  = 2;
    double bodyweight = 1;
    double headerweight = 8;
    double anchorweight = 7;
    double[] fieldsweight = {urlweight,titleweight,bodyweight,headerweight,anchorweight};
    
    ///////bm25 specific weights///////////////
  //String[] TFTYPES = {"url","title","body","header","anchor"};
    double burl=0.4;
    double btitle=0.2;
    double bheader=0.5;
    double bbody=0.1;
    double banchor=0.1;
    double[] bfields = {burl,btitle,bbody,bheader,banchor};

    double k1=50;
    double pageRankLambda=1;
    double pageRankLambdaPrime=1;
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
    		pagerankScores.put(doc,(double)doc.page_rank);
    		//System.out.println(doc);
    		for(Map.Entry<String, Double> entry : lengths.get(doc).entrySet()) {
    			//System.out.println("\t"+entry.getKey()+": "+entry.getValue());
    			String type = entry.getKey();
    			double length = (double)entry.getValue();
    			if(avgLengths.containsKey(type)) {
    				avgLengths.put(type, avgLengths.get(type) + length);
    			}
    			else {
    				avgLengths.put(type,length);
    			}
    		}
    		//System.out.println();
    	}
    	
		/*
		 * @//TODO : Your code here
		 */
    	
    	//normalize avgLengths
		for (String tfType : this.TFTYPES)
		{
			avgLengths.put(tfType, avgLengths.get(tfType)/lengths.size());
			//System.out.println(tfType+":"+avgLengths.get(tfType)/lengths.size());
			/*
			 * @//TODO : Your code here
			 */
		}

    }
    
    ////////////////////////////////////
    
    
	public double getNetScore(Map<String,Map<String, Double>> tfs, Query q, Map<String,Double> tfQuery,Document d)
	{
		double score = 0.0;
		double wdt = 0.0;
		
		//map from tf type -> queryWord -> score
		//calculate the overall weight of a term in the doc
		for(String term : tfQuery.keySet()) {
			for(int i=0; i<this.TFTYPES.length; i++) {
				try {
				wdt += fieldsweight[i]*tfs.get(this.TFTYPES[i]).get(term);
				}
				catch (Exception e) {
					
				}
			}
			tfQuery.put(term, wdt);
		}
		
		for(Map.Entry<String, Double> entry : tfQuery.entrySet()) {
			//System.out.println(entry.getKey()+":"+entry.getValue());
			String term = entry.getKey();
			double idft = 0;
			double wt = 0;
			double pRank = 0;
			try {
			idft = this.idfs.get(term);
			wt = entry.getValue();
			pRank = this.pagerankScores.get(d);
			} catch (Exception e) {
				
			}
			//score += (wt/(this.k1+wt))*idft + this.pageRankLambda*(Math.log10(this.pageRankLambdaPrime+pRank));
			score += (wt/(this.k1+wt))*idft + this.pageRankLambda*(pRank/(this.pageRankLambdaPrime+pRank));
		}
		
		/*
		 * @//TODO : Your code here
		 */
		
		return score;
	}

	//do bm25 normalization
	public void normalizeTFs(Map<String,Map<String, Double>> tfs,Document d, Query q)
	{
		//System.out.println("Doc: "+d.title);
		for(String field: tfs.keySet()) {
			//System.out.println("\t"+field);
			for(Map.Entry<String, Double> termtf : tfs.get(field).entrySet()) {
				String term = termtf.getKey();
				double tf = termtf.getValue();
			//	System.out.println("\t\t"+term+": "+tf);
				
				double bf = 0;
				double lendf = 0;
				for(int i=0; i<this.TFTYPES.length;i++) {
					if(this.TFTYPES[i].equalsIgnoreCase(field)) {
						bf = bfields[i];
						lendf = (double)d.getLengths()[i];
						break;
					}
				}
				
				//double lendf = lengths.get(d).get(term);
				double avlenf = avgLengths.get(field);
				
				double normTf = tf/(1 + bf*((lendf/avlenf)-1));
				
				tfs.get(field).put(term, normTf);
				//System.out.println(field+":"+term+":"+normTf);
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
