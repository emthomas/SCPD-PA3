package edu.stanford.cs276;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExtraCreditScorer extends AScorer
{

	public ExtraCreditScorer(Map<String,Double> idfs)
	{
		super(idfs);
	}
	
	public ExtraCreditScorer(Map<String,Double> idfs, int corpusCount)
	{
		super(idfs, corpusCount);
	}
	
	public double IDF(String term){
    	if(idfs.containsKey(term)){
    		return idfs.get(term);
    	}else{
    		return laplaceSmoothing();
    	}
    }
	
	private double laplaceSmoothing(){
    	return Math.log10((corpusCount+1));
    }
	
	public double getNetScore(Map<String,Map<String, Double>> tfs,Query q, Document d){
		
		double score = 0.0;
		Set<String> matchedTerm = new HashSet<String>();
		
		for(String type: tfs.keySet()){
			//System.out.println("<"+type+">");
			Map<String, Double> m = tfs.get(type);
			for(String term: m.keySet()){
				//No stemming should be performed
				if(q.termExists(term, false)){
					matchedTerm.add(term);
				}
			}
		}
		
		//Calulate IDFs and Sum the score
		for(String term: matchedTerm){
			score = score + IDF(term);
		}
		
		//Commenting out the normalization as it decreases the performance
		/*
		int numberOfMatchedTerms = matchedTerm.size();
		if(numberOfMatchedTerms > 0){
			score = score/numberOfMatchedTerms;
		}
		*/
		return score;
	}
	
	@Override
	public double getSimScore(Document d, Query q) {
		
		Map<String,Map<String, Double>> tfs = this.getDocTermFreqs(d,q);
		
		return getNetScore(tfs,q,d);
	}

}
