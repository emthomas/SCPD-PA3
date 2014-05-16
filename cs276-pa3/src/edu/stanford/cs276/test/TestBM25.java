package edu.stanford.cs276.test;

import edu.stanford.cs276.NdcgMain;
import edu.stanford.cs276.Rank;

public class TestBM25 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		double urlweight;
	    double titleweight;
	    double bodyweight;
	    double headerweight;
	    double anchorweight;
	    double burl;
	    double btitle;
	    double bheader;
	    double bbody;
	    double banchor;
	    double k1;
	    double pageRankLambda;
	    double pageRankLambdaPrime;
	    double B;
	    
	    String[] rankArgs = new String[16];
	    rankArgs[0] = "/Users/ethomas35/SCPD/PA3/SCPD-PA3/cs276-pa3/2013.data/queryDocTrainData";
	    rankArgs[1] = "bm25";
	    
	    String[] ndcgArgs = new String[2];
	    ndcgArgs[0] = "ranked.txt";
	    ndcgArgs[1] = "/Users/ethomas35/SCPD/PA3/SCPD-PA3/cs276-pa3/2013.data/queryDocTrainRel";
	    
	    System.out.print("task2_W_url\t");
	    System.out.print("task2_W_title\t");
	    System.out.print("task2_W_body\t");
	    System.out.print("task2_W_header\t");
	    System.out.print("task2_W_anchor\t");
	    System.out.print("task2_B_url\t");
	    System.out.print("task2_B_title\t");
	    System.out.print("task2_B_header\t");
	    System.out.print("task2_B_body\t");
	    System.out.print("task2_B_anchor\t");
	    System.out.print("task2_k1\t");
	    System.out.print("task2_pageRankLambda\t");
	    System.out.print("task2_pageRankLambdaPrime\t");
	    System.out.print("B\t");
	    System.out.println("score");
	    
	    for(urlweight=0.0; urlweight<= 10.0; urlweight+=2.0) { 
		    for(titleweight=2.0; titleweight<= 2.0; titleweight+=1.0) { 
		    for(bodyweight=1.0; bodyweight<= 1.0; bodyweight+=1.0) { 
		    for(headerweight=8.0; headerweight<= 8.0; headerweight+=1.0) { 
		    for(anchorweight=7.0; anchorweight<= 7.0; anchorweight+=1.0) { 
		    for(burl=0.4; burl<= 0.4; burl+=0.1) { 
		    for(btitle=0.2; btitle<= 0.2; btitle+=0.1) { 
		    for(bheader=0.5; bheader<= 0.5; bheader+=0.01) { 
		    for(bbody=0.1; bbody<= 0.1; bbody+=1.0) { 
		    for(banchor=0.1; banchor<= 0.1; banchor+=0.1) { 
		    for(k1=50.0; k1<= 50.0; k1+=10.0) { 
		    for(pageRankLambda=1.0; pageRankLambda<= 1.0; pageRankLambda+=1.0) { 
		    for(pageRankLambdaPrime=1.0; pageRankLambdaPrime<= 1.0; pageRankLambdaPrime+=0.1) { 
		    for(B=10.0; B<=10.0; B+=10.0) {
				rankArgs[2]=String.valueOf(urlweight);
			    rankArgs[3]=String.valueOf(titleweight);
			    rankArgs[4]=String.valueOf(bodyweight);
			    rankArgs[5]=String.valueOf(headerweight);
			    rankArgs[6]=String.valueOf(anchorweight);
			    rankArgs[7]=String.valueOf(burl);
			    rankArgs[8]=String.valueOf(btitle);
			    rankArgs[9]=String.valueOf(bheader);
			    rankArgs[10]=String.valueOf(bbody);
			    rankArgs[11]=String.valueOf(banchor);
			    rankArgs[12]=String.valueOf(k1);
			    rankArgs[13]=String.valueOf(pageRankLambda);
			    rankArgs[14]=String.valueOf(pageRankLambdaPrime);
			    rankArgs[15]=String.valueOf(B);
			    System.out.print( urlweight+"\t");
			    System.out.print( titleweight+"\t");
			    System.out.print( bodyweight+"\t");
			    System.out.print( headerweight+"\t");
			    System.out.print( anchorweight+"\t");
			    System.out.print( burl+"\t");
			    System.out.print( btitle+"\t");
			    System.out.print( bheader+"\t");
			    System.out.print( bbody+"\t");
			    System.out.print( banchor+"\t");
			    System.out.print( k1+"\t");
			    System.out.print( pageRankLambda+"\t");
			    System.out.print( pageRankLambdaPrime+"\t");
			    System.out.print( B+"\t");
		    	Rank.main(rankArgs);
				NdcgMain.main(ndcgArgs);
		    }}}}}}}}}}}}}}

	}

}
