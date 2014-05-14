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
	
	public Query(String query)
	{	
		this.query = query.toLowerCase();
		queryWords = new ArrayList<String>(Arrays.asList(query.toLowerCase().split(" ")));
	}
	
	public String toString() {
		return query;
	}
	
	public boolean termExists(String term){
		return queryWords.contains(term);
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
