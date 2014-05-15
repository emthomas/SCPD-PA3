package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CosineSimilarityScorer extends AScorer
{
	private static final double BODY_LENGTH_SMOOTHING_FACTOR = 500;
	
	///////////////weights///////////////////////////
    double urlweight = 0.7;
    double titleweight  = 0.9;
    double bodyweight = 0.3;
    double headerweight = 0.5;
    double anchorweight = 0.8;
    
    double smoothingBodyLength = 1000;
    
    Map<String,Double> fieldWeight = new HashMap<String, Double>();
    //////////////////////////////////////////
    
    public CosineSimilarityScorer(Map<String,Double> idfs, int corpusCount)
	{
		super(idfs, corpusCount);
		fieldWeight.put(TFTYPES[0], urlweight);
		fieldWeight.put(TFTYPES[1], titleweight);
		fieldWeight.put(TFTYPES[2], bodyweight);
		fieldWeight.put(TFTYPES[3], headerweight);
		fieldWeight.put(TFTYPES[4], anchorweight);
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
    
	public double getNetScore(Map<String,Map<String, Double>> tfs, Query q, Map<String,Double> tfQuery,Document d)
	{
		//System.out.println("Query: "+q.toString());		
		double score = 0.0;
		
		Map<String, Double> termScore = new HashMap<String, Double>();
		for(String type: tfs.keySet()){
			//System.out.println("<"+type+">");
			Map<String, Double> m = tfs.get(type);
			for(String term: m.keySet()){
				double fieldScore = m.get(term);
				//System.out.println("\t"+term);
				//System.out.println("\t\tfs="+fieldScore);
				if(termScore.containsKey(term)){
					double s = termScore.get(term);
					//System.out.println("\t\ts1="+s);
					s = s + (fieldScore * fieldWeight.get(type));
					//System.out.println("\t\ts2="+s);
					termScore.put(term, s);
				}else{
					double s = fieldScore * fieldWeight.get(type);
					//System.out.println("\t\ts0="+s);
					termScore.put(term, s);
				}
			}
		}
		
		//Dot product Q.D
		for(String term: tfQuery.keySet()){
			if(termScore.containsKey(term)){
				double dScore = termScore.get(term);
				double qTF = sublinearScaling(tfQuery.get(term));
				//double qTF = tfQuery.get(term);
				double qTfIDf = qTF * IDF(term);
				score = score + (dScore*qTfIDf);	
			}
		}
		
		return score;
	}

	private double sublinearScaling(double score){
		if(score == 0.0){
			return 0.0;
		}else{
			return (1+Math.log10(score));
		}
	}
	
	private double normalizationFactor(Document d){
		return d.body_length+smoothingBodyLength;
	}
	
	public void normalizeTFs(Map<String,Map<String, Double>> tfs,Document d, Query q)
	{
		//Normalization will be done only with Document Vector and not for Query Vector
		double nf = normalizationFactor(d);
		
		//System.out.println();
		//System.out.println("Query: "+q.toString());
		//Map<String,Double> termScore = new HashMap<String,Double>();
		
		for(String type: tfs.keySet()){
			//System.out.println();
			//System.out.println("<"+type+">");
			Map<String, Double> m = tfs.get(type);
			//termScore.putAll(m);
			for(String term: m.keySet()){
				double score = m.get(term);
				//System.out.println("\t"+term);
				//System.out.println("\t\t"+score);
				score = sublinearScaling(score);
				//System.out.println("\t\t"+score);
				score /=nf;
				//System.out.println("\t\t"+score);
				m.put(term, score);
			}
			tfs.put(type, m);
		}
		/*
		System.out.println("-----------------------");
		for(String type: tfs.keySet()){
			System.out.println("<"+type+">");
			Map<String, Double> m = tfs.get(type);
			for(String term: m.keySet()){
				double score = m.get(term);
				System.out.println("\t"+term+" : \t"+score);
			}
		}
		System.out.println("-----------------------");
		*/
		
	}

	
	@Override
	public double getSimScore(Document d, Query q) 
	{
		
		Map<String,Map<String, Double>> tfs = this.getDocTermFreqs(d,q);
		
		//Normalization for document frequencies
		this.normalizeTFs(tfs, d, q);
		
		Map<String,Double> tfQuery = getQueryFreqs(q);
		
        return getNetScore(tfs,q,tfQuery,d);
	}

	
	
	
	
}
