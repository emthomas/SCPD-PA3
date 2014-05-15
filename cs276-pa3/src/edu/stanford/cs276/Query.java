package edu.stanford.cs276;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Query
{
	String query;
	List<String> queryWords;
	Set<String> stemmedWord = new HashSet<String>();
	Stemmer stemmer;
	
	public Query(String query)
	{	
		this.query = query.toLowerCase();
		String[] queryTokens = query.toLowerCase().split(" ");
		queryWords = new ArrayList<String>(Arrays.asList(queryTokens));
		//queryWords = new ArrayList<String>(Arrays.asList(query.toLowerCase().split(" ")));
		if(Rank.stemmingEnabled()){
			this.stemmer = Rank.getStemmer();
			for(String token: queryTokens){
				token = stemmer.stemThisWord(token);
				stemmedWord.add(token);
			}
		}
		
		
	}
	
	public Query(String query, Stemmer stemmer){
		this.stemmer = Rank.getStemmer();
		String[] queryTokens = query.toLowerCase().split(" ");
		queryWords = new ArrayList<String>(Arrays.asList(queryTokens));
		for(String token: queryTokens){
			token = stemmer.stemThisWord(token);
			stemmedWord.add(token);
		}
		
	}
	
	public String toString() {
		return query;
	}
	
	public boolean termExists(String term, boolean checkStemmedWords){
		if(checkStemmedWords){
			String stemmedTerm = stemmer.stemThisWord(term);
			return (queryWords.contains(term) | stemmedWord.contains(stemmedTerm));
		}else{
			return queryWords.contains(term);
		}
	}
	
	public int getUniqueTermsCount() {
		Set<String> terms = new HashSet<String>();
		if(query==null)
			return 0;
		for(String term : queryWords) {
			terms.add(term);
		}
		return terms.size();
	}
}
