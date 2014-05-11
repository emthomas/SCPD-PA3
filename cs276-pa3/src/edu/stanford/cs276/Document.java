package edu.stanford.cs276;

import java.util.List;
import java.util.Map;

public class Document {
	public String url = null;
	public String title = null;
	public List<String> headers = null;
	public Map<String, List<Integer>> body_hits = null; // term -> [list of positions]
	public int body_length = 0;
	public int page_rank = 0;
	public Map<String, Integer> anchors = null; // term -> anchor_count

	public Document(String url)
	{
		this.url=url;
	}
	
	public int[] getLengths() {
		//String[] TFTYPES = {"url","title","body","header","anchor"};
		int[] TYPESLEN = {0,0,0,0,0};
		
		if(url!=null) {
			TYPESLEN[0] = url.split("[,.\\s\\-:\\?/]").length;
		}
		
		if(title!=null) {
			TYPESLEN[1] = title.split("[w+,.\\s\\-:\\?/]").length;
		}
		
		TYPESLEN[2] = body_length;
		
		if(headers!=null) {
			for(String header : headers) {
				TYPESLEN[3] += header.split("\\s+").length;
			}
		}
		
		if(anchors!=null) {
			for(int count : anchors.values()) {
				TYPESLEN[4] += count;
			}
		}
		
		return TYPESLEN;
	}
	
	// For debug
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");
		if (url != null) result.append("url: " + url + NEW_LINE);
		if (title != null) result.append("title: " + title + NEW_LINE);
		if (headers != null) result.append("headers: " + headers.toString() + NEW_LINE);
		if (body_hits != null) result.append("body_hits: " + body_hits.toString() + NEW_LINE);
		if (body_length != 0) result.append("body_length: " + body_length + NEW_LINE);
		if (page_rank != 0) result.append("page_rank: " + page_rank + NEW_LINE);
		if (anchors != null) result.append("anchors: " + anchors.toString() + NEW_LINE);
		return result.toString();
	}
}
