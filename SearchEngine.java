package finalproject;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here



		//adds the input URL to the internet graph
		this.internet.addVertex(url);
		//System.out.println(url + " has been added as a VERTEX.");

		//add the input URL as having been visited
		this.internet.setVisited(url, true);
		//System.out.println(url+" has been set to VISITED");

		//for each word in the urls word content list
		for (String word : this.parser.getContent(url)){
			//convert the word to its lowercase
			word = word.toLowerCase(Locale.ROOT);

			//if the word is not already in the WordIndex
			if (!this.wordIndex.containsKey(word)){
				//add the word to the WordIndex, and add a new arraylist with the current url as its value
				this.wordIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(url);
			}
			//if the word is already in the index
			else{
				//if the current url is not already associated with the word
				if(!this.wordIndex.get(word).contains(url)) {
					//add the current url to the word's value list
					this.wordIndex.computeIfPresent(word, (key, value) -> value).add(url);
				}
			}
		}





		//for each unvisited link in the graph, run the crawl and index method
		for (String link : this.parser.getLinks(url)){
			if (!internet.getVisited(link)){
				crawlAndIndex(link);
			}

			//adds edges from input URL to all links in its page
			this.internet.addEdge(url,link);
			//System.out.println("Connecting " + url + " to " + link);
		}






	}


	
	
	
	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here

		for(String url : this.internet.getVertices()){
			this.internet.setPageRank(url, 1);
		}

		//ArrayList<Double> firstRanking = new ArrayList<>();
		//ArrayList<Double> epsilonDifference = new ArrayList<>();

		Double initialPR = 0.0;
		Double newPR = 0.0;
		Double PRdiff = 0.0;

		//Iterator<Double> it = computeRanks(this.internet.getVertices()).iterator();

		/*do {
			for (String url : this.internet.getVertices()){
				firstRanking.add(this.internet.getPageRank(url));
				this.internet.setPageRank(url, pageRank(url));
				firstRanking.g
			}
		}while()*/
		do {
			for (int i = 0; i < this.internet.getVertices().size(); i++) {

				String currentURL = this.internet.getVertices().get(i);
				initialPR = this.internet.getPageRank(currentURL);
				//set the page rank of each i vertex to be equal to the page rank of i vertex after running compute ranks
				this.internet.setPageRank(currentURL, this.pageRank(currentURL));
				//System.out.println("Current page rank of: " + currentURL + " is " + this.internet.getPageRank(currentURL));
				newPR = this.internet.getPageRank(currentURL);
				PRdiff = Math.abs(newPR - initialPR);
			}
		}while(PRdiff > epsilon);

		int j = 4;
		while(j > 1){
			for (int i = 0; i < this.internet.getVertices().size(); i++) {

				String currentURL = this.internet.getVertices().get(i);
				//initialPR = this.internet.getPageRank(currentURL);
				//set the page rank of each i vertex to be equal to the page rank of i vertex after running compute ranks
				this.internet.setPageRank(currentURL, this.pageRank(currentURL));
				//System.out.println("Current page rank of: " + currentURL + " is " + this.internet.getPageRank(currentURL));
				//newPR = this.internet.getPageRank(currentURL);
				//PRdiff = Math.abs(newPR - initialPR);
			}
			j--;
		}




	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here

		ArrayList<Double> initialRankList = new ArrayList<Double>();

		for (String url : vertices){
			initialRankList.add(pageRank(url));
		}

		return initialRankList;
	}

	public double pageRank(String vertex) {

		double pagerank = this.internet.getPageRank(vertex);
		double temp = 0.0;
		//System.out.println("The current PR of "+vertex+" is "+pagerank);

		//this.internet.getEdgesInto(vertex);
/*
		if(!(this.internet.getEdgesInto(vertex).isEmpty())) {
			System.out.println(vertex + "has the following links pointing to it: " + this.internet.getEdgesInto(vertex));
			for (String url : this.internet.getEdgesInto(vertex)) {
				System.out.println("I am dividing " + pageRank(url) + " by " + this.internet.getOutDegree(url) );
				pagerank /= pageRank(url) / this.internet.getOutDegree(url);
				System.out.println("I am setting "+ vertex + " to be " + pagerank);
			}

			pagerank += 0.5 + (0.5 * pagerank);
			System.out.println("I am setting "+ vertex + " after damping to be " + pagerank);
		}
		else{
			System.out.println("I am setting "+ vertex + " to be 0.5");
			pagerank = 0.5;
		}

 */
		if (!(this.internet.getEdgesInto(vertex).isEmpty())) {
			for (String url : this.internet.getEdgesInto(vertex)) {
				temp += this.internet.getPageRank(url) / this.internet.getOutDegree(url);
			}

			pagerank = 0.5 + temp * 0.5;

			//System.out.println("The current PR of " + vertex + " is " + pagerank);
		}

		else{
			pagerank = 0.5;
		}

		return pagerank;
	}


	/*	// Performs one iteration of the PageRank algorithm and updates the values in the array 'pageranks'.
		private void iterateOnce(double damping) {
			
			Double [] pageranks = new Double[this.internet.getVertices().size()];
			// Pre-divide by number of outgoing links
			for (int i = 0; i < this.internet.getEdgesInto(); i++) {
				if (numOutgoingLinks[i] > 0)
					pageranks[i] /= numOutgoingLinks[i];
			}

			// Distribute PageRanks over links (main calculation)
			//Arrays.fill(newPageranks, 0);
			for (int i = 0; i < links.length; ) {
				int numIncoming = links[i + 1];
				double sum = 0;
				for (int j = 0; j < numIncoming; j++) {
					int src = links[i + 2 + j];
					sum += pageranks[src];
				}
				int dest = links[i];
				newPageranks[dest] = sum;
				i += numIncoming + 2;
			}

			// Apply bias and damping to all active pages
			//double temp = bias * damping + (1 - damping) / numActive;  // Factor out some arithmetic
			for (int i = 0; i < idLimit; i++) {
				if (isActive[i])
					pageranks[i] = newPageranks[i] * damping + 0.5;
			}
		}*/

	
	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 */
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		query = query.toLowerCase(Locale.ROOT);

		HashMap<String, Double> urlRankings = new HashMap<String, Double>();

		if(!this.wordIndex.containsKey(query)){
			return new ArrayList<>();
		}

		for (String url : this.wordIndex.get(query)) {
			urlRankings.put(url, this.internet.getPageRank(url));
		}

		return Sorting.fastSort(urlRankings);
	}
}
