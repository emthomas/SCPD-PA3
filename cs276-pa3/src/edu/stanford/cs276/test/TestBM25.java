package edu.stanford.cs276.test;

import edu.stanford.cs276.*;

public class TestBM25 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		double urlweight=0.0;
		double titleweight=0.0;
		double bodyweight=0.0;
		double headerweight=0.0;
		double anchorweight=0.0;
		double burl=0.0;
		double btitle=0.0;
		double bheader=0.0;
		double bbody=0.0;
		double banchor=0.0;
		double k1=0.0;
		double pageRankLambda=0.0;
		double pageRankLambdaPrime=0.0;
		
		String[] argumentsNDCG = new String[2];
		argumentsNDCG[0]="/Users/ethomas35/SCPD/PA3/SCPD-PA3/cs276-pa3/src/edu/stanford/cs276/ranked.txt";
		argumentsNDCG[1]="/Users/ethomas35/SCPD/PA3/SCPD-PA3/cs276-pa3/2013.data/queryDocTrainRel";
		//argumentsNDCG[1]="/Users/ethomas35/SCPD/PA3/SCPD-PA3/cs276-pa3/data/pa3.rel.dev";
		
		String[] argumentsRank = new String[15];
		argumentsRank[0]="/Users/ethomas35/SCPD/PA3/SCPD-PA3/cs276-pa3/queryDocTrainData";
		argumentsRank[1]="bm25";
		
		System.out.println("urlweight titleweight bodyweight headerweight anchorweight burl btitle bheader bbody banchor k1 pageRankLambda pageRankLambdaPrime score");
		for(urlweight=0.0; urlweight<=2; urlweight+=1.0) {
		for(titleweight=0.0; titleweight<=2; titleweight+=1.0) {
		for(bodyweight=0.0; bodyweight<=2; bodyweight+=1.0) {
		for(headerweight=0.0; headerweight<=2; headerweight+=1.0) {
		for(anchorweight=0.0; anchorweight<=2; anchorweight+=1.0) {
		for(burl=0.0; burl<=2; burl+=1.0) {
		for(btitle=0.0; btitle<=2; btitle+=1.0) {
		for(bheader=0.0; bheader<=2; bheader+=1.0) {
		for(bbody=0.0; bbody<=2; bbody+=1.0) {
		for(banchor=0.0; banchor<=2; banchor+=1.0) {									
		for(k1=0.75; k1<=1; k1+=0.5) {
		for(pageRankLambda=0.75; pageRankLambda<=1; pageRankLambda+=0.5) {
		for(pageRankLambdaPrime=0.6; pageRankLambdaPrime<=1; pageRankLambdaPrime+=0.5) {
			argumentsRank[2] = String.valueOf(urlweight);
			argumentsRank[3] = String.valueOf(titleweight);
			argumentsRank[4] = String.valueOf(bodyweight);
			argumentsRank[5] = String.valueOf(headerweight);
			argumentsRank[6] = String.valueOf(anchorweight);
			argumentsRank[7] = String.valueOf(burl);
			argumentsRank[8] = String.valueOf(btitle);
			argumentsRank[9] = String.valueOf(bheader);
			argumentsRank[10] = String.valueOf(bbody);
			argumentsRank[11] = String.valueOf(banchor);
			argumentsRank[12] = String.valueOf(k1);
			argumentsRank[13] = String.valueOf(pageRankLambda);
			argumentsRank[14] = String.valueOf(pageRankLambdaPrime);
			System.out.print(urlweight+" "+titleweight+" "+bodyweight+" "+headerweight+" "+anchorweight+" "+burl+" "+btitle+" "+bheader+" "+bbody+" "+banchor+" "+k1+" "+pageRankLambda+" "+pageRankLambdaPrime+" ");
			Rank.main(argumentsRank);
			NdcgMain.main(argumentsNDCG);
			//System.out.println();
		}}}}}}}}}}}}}
			
//		{
//		System.out.println("urlweight titleweight bodyweight headerweight anchorweight burl btitle bheader bbody banchor k1 pageRankLambda pageRankLambdaPrime score");
//		System.out.print(urlweight+" "+titleweight+" "+bodyweight+" "+headerweight+" "+anchorweight+" "+burl+" "+btitle+" "+bheader+" "+bbody+" "+banchor+" "+k1+" "+pageRankLambda+" "+pageRankLambdaPrime+" ");
//		Rank.main(argumentsRank);
//		NdcgMain.main(argumentsNDCG);
//		System.out.println();
//		}

	}

}
