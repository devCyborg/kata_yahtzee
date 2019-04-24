package org.sid;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Yatzy {
    // pour  l'instant il n y a pas d'heritage
    private int[] dice;

    public Yatzy(int d1, int d2, int d3, int d4, int d5) {
        this.dice = new int[5];
        this.dice[0] = d1;
        this.dice[1] = d2;
        this.dice[2] = d3;
        this.dice[3] = d4;
        this.dice[4] = d5;
    }

    public static int chance(int d1, int d2, int d3, int d4, int d5) {
        return d1 + d2 + d3 + d4 + d5;

    }
    
    /**
     * 
     * @param dice
     * 		tab of dices (params)
     * @return
     * 	 	if all values are equal return result = 50 
     * 		else resut = 0
     */
    public static int yatzy(int... dice) {
    	if (dice[0]==dice[1] && dice[0]==dice[2] && dice[0]==dice[3]&& dice[0]==dice[4]) {
    		 return 50;
    	}else {
    		return 0;
    	}
        
    }


    public static int ones(int d1, int d2, int d3, int d4, int d5) {
        return genericCalculate(d1, d2, d3, d4, d5, 1);
    }

    public static int twos(int d1, int d2, int d3, int d4, int d5) {
        return genericCalculate(d1, d2, d3, d4, d5, 2);
    }

    public static int threes(int d1, int d2, int d3, int d4, int d5) {
        return genericCalculate(d1, d2, d3, d4, d5, 3);
    }


    public int fours() {
        return genericCalculateSecond(4);
    }

    public int fives() {
        return genericCalculateSecond(5);
    }

    public int sixes() {
        return genericCalculateSecond(6);
    }
    /**
     * calculate score with one pair 
     * @param d1
     * 		dice 1
     * @param d2
     * 		dice 2
     * @param d3
     * 		dice 3
     * @param d4
     * 		dice 4
     * @param d5
     * 		dice 5
     * @return
     */
    public static int scorePair(int d1, int d2, int d3, int d4, int d5) {
    	
    	//get map of all element of stream "key : integer" and number that this element is duplicated "values : Long"
    	Map<Integer, Long> result = getMapsNumberOfDuplicateElementsDices(d1, d2, d3, d4, d5);
    	
    	//get List of duplicated twice only (list of pairs)
    	List<Integer> listValuesDuplicates= result.entrySet().stream()
    								.filter(e -> e.getValue()==2)
    								.map(e-> e.getKey())
    								.collect(Collectors.toList());
    	//get MaxOfPairs
    	Optional<Integer> maxOfPairs = listValuesDuplicates.stream().max(Comparator.naturalOrder());
    	int maxPairs=0;
    	if (maxOfPairs.isPresent()) {
    		maxPairs=maxOfPairs.get();
    	}
    		
     	return maxPairs*2;
 
    }
    /**
     * calculate score with two pairs 
     * @param d1
     * 		dice 1
     * @param d2
     * 		dice 2
     * @param d3
     * 		dice 3
     * @param d4
     * 		dice 4
     * @param d5
     * 		dice 5
     * @return
     * 		score for two pairs
     */
    public static int twoPair(int d1, int d2, int d3, int d4, int d5) {
    	
    	//get map of all element of stream "key : integer" and number that this element is duplicated "values : Long"
    	Map<Integer, Long> result = getMapsNumberOfDuplicateElementsDices(d1, d2, d3, d4, d5);
     	
    	//filter to save elements of two or three repetitions
    	Optional<Integer> totalElements= result.entrySet().stream()
				  .filter(e -> e.getValue()>=2 && e.getValue()<=3 )
				  .map(e-> e.getKey())
				  .reduce(Integer::sum);
    	if (totalElements.isPresent()) {
    		return totalElements.get()*2;
    	}else {
    		return 0;
    	}
    }
    
    /**
     * calculate score with fourOfAKind
     * @param d1
     * 		dice 1
     * @param d2
     * 		dice 2
     * @param d3
     * 		dice 3
     * @param d4
     * 		dice 4
     * @param d5
     * 		dice 5
     * @return
     * 		score for fourOfAKind
     */
    public static int fourOfAKind(int d1, int d2, int d3, int d4, int d5) {
    	
    	//get map of all element of stream "key : integer" and number that this element is duplicated "values : Long"
    	Map<Integer, Long> result = getMapsNumberOfDuplicateElementsDices(d1, d2, d3, d4, d5);
    	
    	//filter to save elements of 4 repetitions
    	Optional<Integer> totalElements= result.entrySet().stream()
				  .filter(e -> e.getValue()==4)
				  .map(e-> e.getKey())
				  .findFirst(); //one element for 4 repetitions
				  
    	if (totalElements.isPresent()) {
    		return totalElements.get()*4;
    	}else {
    		return 0;
    	}
    }

    public static int threeOfAKind(int d1, int d2, int d3, int d4, int d5) {

    	Map<Integer, Long> result = getMapsNumberOfDuplicateElementsDices(d1, d2, d3, d4, d5);
    		
    	Optional<Integer> totalElements= result.entrySet().stream()
				  .filter(e -> e.getValue()>=3 && e.getValue()<=5 )
				  .map(e-> e.getKey())
				  .reduce(Integer::sum);
    	
    	return totalElements.get()*3;
    }

    public static int smallStraight(int d1, int d2, int d3, int d4, int d5) {
        int[] tallies = initCountsArrray(d1, d2, d3, d4, d5);
        return largeSmallStraight(tallies, 0, 15);
    }

    public static int largeStraight(int d1, int d2, int d3, int d4, int d5) {
        int[] tallies = initCountsArrray(d1, d2, d3, d4, d5);
        return largeSmallStraight(tallies, 1, 20);
    }

    /**
     * calculate full house
     * @param d1
     * 		dice 1
     * @param d2
     * 		dice 2
     * @param d3
     * 		dice 3
     * @param d4
     * 		dice 4
     * @param d5
     * 		dice 5
     * @return
     * 		score fullHouse
     */
    public static int fullHouse(int d1, int d2, int d3, int d4, int d5) {
    	
    	//get map of all element of stream "key : integer" and number that this element is duplicated "values : Long"
    	Map<Integer, Long> result = getMapsNumberOfDuplicateElementsDices(d1, d2, d3, d4, d5);
   	
    	Optional<Integer> totalFullHouse = result.entrySet().stream()
				 .filter(e -> e.getValue()==3 || e.getValue()==2) //save element repetition 2 et 3
				 .map(e-> e.getKey())
				 .reduce((x,y) -> x*3 +y* 2);
    	if (totalFullHouse.isPresent()) {
    		return totalFullHouse.get();
    	}else {
    		return 0;
    	}
    }


    private static int[] initCountsArrray(int d1, int d2, int d3, int d4, int d5) {
        int[] counts = new int[6];
        counts[d1 - 1]++;
        counts[d2 - 1]++;
        counts[d3 - 1]++;
        counts[d4 - 1]++;
        counts[d5 - 1]++;
        return counts;
    }

    private static int genericCalculate(int d1, int d2, int d3, int d4, int d5, int x) {
        int[] dice = {d1, d2, d3, d4, d5};
        return Arrays.stream(dice).filter(e -> e == x).sum();
    }

    private int genericCalculateSecond(int x) {
        return Arrays.stream(this.dice).filter(e -> e == x).sum();
    }

    public static int largeSmallStraight(int[] tallies, int starterIndex, int result) {
        if (tallies[0 + starterIndex] == 1 && tallies[1 + starterIndex] == 1 && tallies[2 + starterIndex] == 1 && tallies[3] == 1 && tallies[4 + starterIndex] == 1)
            return result;
        return 0;
    }
    
    private static Map<Integer, Long> getMapsNumberOfDuplicateElementsDices(int d1, int d2, int d3, int d4, int d5) {
		Map<Integer, Long> result = Stream.of(d1,d2,d3,d4,d5)
										  .collect(Collectors
										  .groupingBy(Function.identity(),Collectors.counting())
    									);
		return result;
	}
}

