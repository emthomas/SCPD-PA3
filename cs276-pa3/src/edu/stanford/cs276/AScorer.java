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
	
	Map<String,Double> idfs;
	String[] TFTYPES = {"url","title","body","header","anchor"};
	
	public AScorer(Map<String,Double> idfs)
	{
		this.idfs = idfs;
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
	
	/*
	 * @//TODO : Your code here
	 */
	
	
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
		Set<String> ignore = new HashSet<String>();
		ignore.add("http");
		ignore.add("https");
		ignore.add("www");
		ignore.add("file");
		
		String url = d.url;
		Map<String, Double> urlTf = new HashMap<String, Double>();
		//System.out.println();
		//System.out.println(url);
		String[] urlTerms = url.split("[,.\\s\\-:\\?/]");
		for(String ut: urlTerms){
			ut = ut.toLowerCase();
			if(ut != null && !ut.isEmpty() && !ignore.contains(ut) && q.termExists(ut)){
				//System.out.println(ut);
				if(urlTf.containsKey(ut)){
					Double score = urlTf.get(ut);
					score++;
					urlTf.put(ut, score);
				}else{
					urlTf.put(ut, 1D);
				}
			}
		}
		if(urlTf != null && !urlTf.isEmpty()){
			tfs.put(TFTYPES[0], urlTf);
		}
		
		/*
		for(String t: urlTf.keySet()){
			System.out.println(t+"\t"+urlTf.get(t));
		}
		*/
		
		
		//title
		String title = d.title;
		if(title != null && !title.isEmpty()){
			title = title.toLowerCase();
			Map<String, Double> titleTf = new HashMap<String, Double>();
			//System.out.println();
			//System.out.println("Title: "+title);
			String[] titleTerms = title.split("[w+,.\\s\\-:\\?/]");
			for(String tt: titleTerms){
				if(tt != null && !tt.isEmpty() && q.termExists(tt)){
					//System.out.println(tt);
					if(titleTf.containsKey(tt)){
						Double score = titleTf.get(tt);
						score++;
						titleTf.put(tt, score);
					}else{
						titleTf.put(tt, 1D);
					}
				}
			}
			if(titleTf != null && !titleTf.isEmpty()){
				tfs.put(TFTYPES[1], titleTf);
			}
			/*
			for(String t: titleTf.keySet()){
				System.out.println(t+"\t"+titleTf.get(t));
			}
			*/
		}
		
		
		
		//body
		
		Map<String, List<Integer>> body= d.body_hits;
		//System.out.println();
		if(body != null && body.size() > 0){
			Map<String, Double> bodyTf = new HashMap<String, Double>();
			for(String bterm: body.keySet()){
				bterm = bterm.toLowerCase();
				//System.out.println("bterm: "+bterm);
				if(bterm != null && !bterm.isEmpty() && q.termExists(bterm)){
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
			if(bodyTf != null && !bodyTf.isEmpty()){
				tfs.put(TFTYPES[2], bodyTf);
			}
			/*
			for(String t: bodyTf.keySet()){
				System.out.println(t+"\t"+bodyTf.get(t));
			}
			*/
		}
		
		//header
		
		List<String> headers = d.headers;
		if(headers!=null && headers.size() > 0){
			Map<String, Double> headerTf = new HashMap<String, Double>();
			for(String h: headers){
				h = h.toLowerCase();
				//System.out.println("Header: "+h);
				if(h != null && !h.isEmpty()){
					String[] hterms = h.split("\\s+");
					for(String hterm: hterms){
						//System.out.println("hterm: "+hterm);
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
			if(headerTf != null && !headerTf.isEmpty()){
				tfs.put(TFTYPES[3], headerTf);
			}
			
			/*
			for(String t: headerTf.keySet()){
				System.out.println(t+"\t"+headerTf.get(t));
			}
			*/
		}
		
		
		
		
		//anchor
		Map<String, Integer> anchors = d.anchors;
		if(anchors!=null && anchors.size() > 0){
			Map<String, Double> anchorsTf = new HashMap<String, Double>();
			for(String anchor: anchors.keySet()){
				anchor = anchor.toLowerCase();
				int count = anchors.get(anchor);
				//System.out.println("anchor: "+anchor+"\tCount: "+count);
				String[] aTokens = anchor.split("\\s+");
				for(String aterm: aTokens){
					if(aterm != null && !aterm.isEmpty() && q.termExists(aterm)){
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
			if(anchorsTf != null && !anchorsTf.isEmpty()){
				tfs.put(TFTYPES[4], anchorsTf);
			}
			/*
			for(String t: anchorsTf.keySet()){
				System.out.println(t+"\t"+anchorsTf.get(t));
			}
			*/
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
