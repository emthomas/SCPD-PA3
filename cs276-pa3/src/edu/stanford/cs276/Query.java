package edu.stanford.cs276;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Query
{
	String query;
	List<String> queryWords;
	
	public Query(String query)
	{	
		this.query = query;
		queryWords = new ArrayList<String>(Arrays.asList(query.split(" ")));
	}
	
	public String toString() {
		return query;
	}
	
	
}
