package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */

	//public method to return an array list containing all keys, quickly ordered in descending order
	public static <K extends Comparable<K>, V> ArrayList<K> fastSort(HashMap<K, V> results)
	{
		//create a new array list of type K to store the ordered key list in
		ArrayList<K> sortedUrls = new ArrayList<K>();

		//insert all the keys of the specified hash map to the new list
		sortedUrls.addAll(results.keySet());

		//call the quicksort method to sort the array list
		quicksort(sortedUrls,0, sortedUrls.size()-1);

		return sortedUrls;
    }

	private static <K extends Comparable<K>> void swap(ArrayList<K> elements, int i, int j){
		//Method to swap 2 elements in an arraylist
		K temp = elements.get(i);
		elements.set(i, elements.get(j));
		elements.set(j, temp);
	}

	private static <K extends Comparable<K>> void quicksort(ArrayList<K> elements, int beg, int end){
		//make sure that beginning and end indexes are proper
		if(beg>=end) return;
		if(beg<0) return;
		if(end>elements.size()-1) return;

		int size = (end+1 - beg);

		//incorporating InsertionSort for smaller lists
		//if (size <= 10){
		//	insertionSort(elements, beg, end);
		//}

		//else use QuickSort for larger lists
		//else {
			//update the pivot and swap appropriate elements using the partition helper method
			int pivot = partition(elements, beg, end);

			//recursively call quicksort on either side of the pivot
			quicksort(elements, beg, pivot - 1);
			quicksort(elements, pivot + 1, end);
		//}
		//}
	}

	private static <K extends Comparable<K>> int partition(ArrayList<K> elements, int beg, int end){

		//Get a random pivot between beg and end
		int random = beg + (int)Math.random()*(elements.size())/(end-beg+1);

		//New position of pivot element
		int last=end;

		//median position for pivot
		int median = (beg + end)/2;

		//Move the pivot element to right edge of the array
		swap(elements, median, end);
		end--;

		while(beg<=end){
			if(elements.get(beg).compareTo(elements.get(last)) > 0) beg++; //Accumulate smaller elements to the left
			else {
				swap(elements, beg, end);
				end--;
			}
		}

		//Move pivot element to its correct position
		swap(elements, beg, last);

		return beg;
	}

	/*private static <K extends Comparable<K>> void insertionSort(ArrayList<K> elements, int beg, int end){
		for (int i = beg + 1; i <= end; i++){
			K currentElement = elements.get(i);

			while (i >= beg && elements.get(i).compareTo(currentElement) < 0){
				elements.set(i+1, elements.get(i));
				i--;
			}
			elements.set(i+1,currentElement);
		}*/

	/*public static int getMedian(int left,int right){
		int center = (left+right)/2;

		if(a[left] > a[center])
			swap(left,center);

		if(a[left] > a[right])
			swap(left, right);

		if(a[center] > a[right])
			swap(center, right);

		swap(center, right);
		return a[right];
	}*/
	}



    

