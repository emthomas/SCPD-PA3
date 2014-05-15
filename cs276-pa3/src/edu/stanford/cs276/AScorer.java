package edu.stanford.cs276;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.net.*;

import javax.print.DocFlavor.STRING;

public abstract class AScorer 
{
	int corpusCount = 0;
	Map<String,Double> idfs;
	String[] TFTYPES = {"url","title","body","header","anchor"};
	Stemmer stemmer = null;
	
	public AScorer(Map<String,Double> idfs)
	{
		this.idfs = idfs;
	}
	
	public AScorer(Map<String,Double> idfs, int corpusCount)
	{
		this.idfs = idfs;
		corpusCount = corpusCount;
	}
	
	//scores each document for each query
	public abstract double getSimScore(Document d, Query q);
	
	//handle the query vector
	public Map<String,Double> getQueryFreqs(Query q)
	{
		Map<String,Double> tfQuery = new HashMap<String,Double>();
		
		for (String queryWord : q.queryWords){
			if(tfQuery.containsKey(queryWord)){
				Double score = tfQuery.get(queryWord);
				score++;
				tfQuery.put(queryWord, score);
			}else{
				tfQuery.put(queryWord, 1D);
			}
		}
		
		if(Rank.stemmingEnabled()){
			stemmer = Rank.getStemmer();
			for (String queryWord : q.queryWords){
				String sw = stemmer.stemThisWord(queryWord);
				if(tfQuery.containsKey(sw)){
					Double score = tfQuery.get(sw);
					score++;
					tfQuery.put(sw, score);
				}else{
					tfQuery.put(sw, 1D);
				}
			}
		}
		
		/*
		System.out.println();
		System.out.println("Query: "+q.toString());
		for(String term: tfQuery.keySet()){
			System.out.println("\tTerm: "+term+"\t: "+tfQuery.get(term));
		}
		*/
		
		return tfQuery;
	}
	

	
	////////////////////Initialization/Parsing Methods/////////////////////
		
	public Map<String, Double> getTFUrl(Document d, Query q){
		Set<String> ignore = new HashSet<String>();
		ignore.add("http");
		ignore.add("https");
		ignore.add("www");
		ignore.add("file");
		
		if(Rank.stemmingEnabled()){
			stemmer = Rank.getStemmer();
		}
		
		String url = d.url;
		Map<String, Double> urlTf = new HashMap<String, Double>();
		//System.out.println();
		//System.out.println(url);
		String[] urlTerms = url.split("[,.\\s\\-:\\?/]");
		for(String ut: urlTerms){
			ut = ut.toLowerCase();
			if(ut != null && !ut.isEmpty() && !ignore.contains(ut) && q.termExists(ut, Rank.stemmingEnabled())){
				//System.out.println(ut);
				if(Rank.stemmingEnabled()){
					ut = stemmer.stemThisWord(ut);
				}
				if(urlTf.containsKey(ut)){
					Double score = urlTf.get(ut);
					score++;
					urlTf.put(ut, score);
				}else{
					urlTf.put(ut, 1D);
				}
			}
		}
		return urlTf;
	}
	
	public Map<String, Double> getTFTitle(Document d, Query q){
		
		if(Rank.stemmingEnabled()){
			stemmer = Rank.getStemmer();
		}
		
		String title = d.title;
		title = title.toLowerCase();
		Map<String, Double> titleTf = new HashMap<String, Double>();
		//System.out.println();
		//System.out.println("Title: "+title);
		String[] titleTerms = title.split("[w+,.\\s\\-:\\?/]");
		for(String tt: titleTerms){
			if(tt != null && !tt.isEmpty() && q.termExists(tt, Rank.stemmingEnabled())){
				//System.out.println(tt);
				if(Rank.stemmingEnabled()){
					tt = stemmer.stemThisWord(tt);
				}
				if(titleTf.containsKey(tt)){
					Double score = titleTf.get(tt);
					score++;
					titleTf.put(tt, score);
				}else{
					titleTf.put(tt, 1D);
				}
			}
		}
		
		return titleTf;
	}
	
	public Map<String, Double> getTFBody(Document d, Query q){
		
		Map<String, List<Integer>> body= d.body_hits;
		Map<String, Double> bodyTf = new HashMap<String, Double>();
		for(String bterm: body.keySet()){
			bterm = bterm.toLowerCase();
			//System.out.println("bterm: "+bterm);
			if(bterm != null && !bterm.isEmpty() && q.termExists(bterm, false)){
				if(bodyTf.containsKey(bterm)){
					Double score = bodyTf.get(bterm);
					score++;
					bodyTf.put(bterm, score);
				}else{
					Double score = (double) body.get(bterm).size();
					bodyTf.put(bterm,score);
				}
			}
		}
		
		return bodyTf;
	}
	
	public Map<String, Double> getTFHeader(Document d, Query q){
		if(Rank.stemmingEnabled()){
			stemmer = Rank.getStemmer();
		}
		
		List<String> headers = d.headers;
		Map<String, Double> headerTf = new HashMap<String, Double>();
		for(String h: headers){
			h = h.toLowerCase();
			//System.out.println("Header: "+h);
			if(h != null && !h.isEmpty()){
				String[] hterms = h.split("\\s+");
				for(String hterm: hterms){
					//System.out.println("hterm: "+hterm);
					if(Rank.stemmingEnabled()){
						hterm = stemmer.stemThisWord(hterm);
					}
					if(hterm != null && !hterm.isEmpty() && q.termExists(hterm, Rank.stemmingEnabled())){	
						if(headerTf.containsKey(hterm)){
							Double score = headerTf.get(hterm);
							score++;
							headerTf.put(hterm, score);
						}else{
							headerTf.put(hterm,1D);
						}
					}
				}
			}
		}
		
		return headerTf;
	}
	
	public Map<String, Double> getTFAnchor(Document d, Query q){
		if(Rank.stemmingEnabled()){
			stemmer = Rank.getStemmer();
		}
		Map<String, Integer> anchors = d.anchors;
		Map<String, Double> anchorsTf = new HashMap<String, Double>();
		for(String anchor: anchors.keySet()){
			anchor = anchor.toLowerCase();
			//System.out.println("anchor: "+anchor);
			int count = 0;
			
			try {
				count = anchors.get(anchor);
			} catch (Exception e){
				//TODO	catch special characters
			}
			//System.out.println("anchor: "+anchor+"\tCount: "+count);
			String[] aTokens = anchor.split("\\s+");
			for(String aterm: aTokens){
				if(aterm != null && !aterm.isEmpty() && q.termExists(aterm, Rank.stemmingEnabled())){
					if(Rank.stemmingEnabled()){
						aterm = stemmer.stemThisWord(aterm);
					}
					if(anchorsTf.containsKey(aterm)){
						Double score = anchorsTf.get(aterm);
						score += count;
						anchorsTf.put(aterm, score);
					}else{
						Double score = (double) count;
						anchorsTf.put(aterm,score);
					}
				}
			}
		}
		
		return anchorsTf;
	}
	
	public void printTFMap(Map<String, Double> tfMap){
		for(String t: tfMap.keySet()){
			System.out.println(t+"\t"+tfMap.get(t));
		}
	}
	
    ////////////////////////////////////////////////////////
	
	
	/*/
	 * Creates the various kinds of term frequences (url, title, body, header, and anchor)
	 * You can override this if you'd like, but it's likely that your concrete classes will share this implementation
	 */
	public Map<String,Map<String, Double>> getDocTermFreqs(Document d, Query q)
	{
		//map from tf type -> queryWord -> score
		//url -> math -> 2.0
		Map<String,Map<String, Double>> tfs = new HashMap<String,Map<String, Double>>();
		
		////////////////////Initialization/////////////////////
		
		//System.out.println();
		//System.out.println();
		//String query = q.toString();
		//System.out.println("Query:   "+query);
		
		//url		
		Map<String, Double> urlTf = getTFUrl(d,q);
		if(urlTf != null && !urlTf.isEmpty()){
			tfs.put(TFTYPES[0], urlTf);
		}
		//printTFMap(urlTf);

				
		//title
		String title = d.title;
		if(title != null && !title.isEmpty()){
			Map<String, Double> titleTf = getTFTitle(d,q);
			if(titleTf != null && !titleTf.isEmpty()){
				tfs.put(TFTYPES[1], titleTf);
			}
			//printTFMap(titleTf);
		}
		
		
		//body
		Map<String, List<Integer>> body= d.body_hits;
		//System.out.println();
		if(body != null && body.size() > 0){
			
			Map<String, Double> bodyTf =  getTFBody(d,q);
			if(bodyTf != null && !bodyTf.isEmpty()){
				tfs.put(TFTYPES[2], bodyTf);
			}
			//printTFMap(bodyTf);
		}
		
		//header
		List<String> headers = d.headers;
		if(headers!=null && headers.size() > 0){
			
			Map<String, Double> headerTf = getTFHeader(d,q);
			if(headerTf != null && !headerTf.isEmpty()){
				tfs.put(TFTYPES[3], headerTf);
			}
			//printTFMap(headerTf);
		}
		
		//anchor
		Map<String, Integer> anchors = d.anchors;
		if(anchors!=null && anchors.size() > 0){
			Map<String, Double> anchorsTf =  getTFAnchor(d,q);
			if(anchorsTf != null && !anchorsTf.isEmpty()){
				tfs.put(TFTYPES[4], anchorsTf);
			}
			//printTFMap(anchorsTf);
		}
		
		/*
		for(String type: tfs.keySet()){
			System.out.println(type+"->");
			Map<String, Double> m = tfs.get(type);
			for(String term: m.keySet()){
				System.out.println("\t"+term+":"+m.get(term));
			}
		}
		*/
		
	    ////////////////////////////////////////////////////////
		
		//////////handle counts//////
		
		//loop through query terms increasing relevant tfs
		for (String queryWord : q.queryWords)
		{
			//System.out.println(""+queryWord);
			for(String type: tfs.keySet()){
				//System.out.println(type+"->");
				Map<String, Double> m = tfs.get(type);
				for(String term: m.keySet()){
					if(queryWord.equalsIgnoreCase(term)){
						Double score = m.get(term);
						//System.out.println("\t"+term+":"+score);
						score++;
						m.put(term, score);
						//System.out.println("\t"+term+":"+score);
					}		
				}
			}
			
		}
		
		/*
		for(String type: tfs.keySet()){
			System.out.println(type+"->");
			Map<String, Double> m = tfs.get(type);
			for(String term: m.keySet()){
				System.out.println("\t"+term+":"+m.get(term));
			}
		}
		*/
		
		return tfs;
	}
	

}
