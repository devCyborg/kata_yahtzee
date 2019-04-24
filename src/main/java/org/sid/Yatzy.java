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
	/**
	 *  dices 
	 */
	private int[] dice;
    
	/**
	 * value of score if equals number dices
	 */
    private static final int RESULT_ALL_EQUALS_NUMBER = 50;
    
    private static final int POINT_SMALL_STRAIGHT = 15;
    
    private static final int POINT_LARGE_STRAIGHT = 20;
    

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
     * 		tab of dices (params 5 elements) 
     * @return
     * 	 	if all values are equal return result = 50 
     * 		else resut = 0
     */
    public static int yatzy(int... dice) {
    	if (dice[0]==dice[1] && dice[0]==dice[2] && dice[0]==dice[3]&& dice[0]==dice[4]) {
    		 return RESULT_ALL_EQUALS_NUMBER;
    	}else {
    		return 0;
    	}
        
    }
    /**
     * calculate sum of 1
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
     * 	return sum of 1 
     */
    public static int ones(int d1, int d2, int d3, int d4, int d5) {
        return sumXelementDices(d1, d2, d3, d4, d5, 1);
    }
    /**
     * calculate sum of 2
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
     * 	return sum of 2 
     */
    public static int twos(int d1, int d2, int d3, int d4, int d5) {
        return sumXelementDices(d1, d2, d3, d4, d5, 2);
    }
    
    /**
     * calculate sum of 3
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
     * 	return sum of 3
     */
    public static int threes(int d1, int d2, int d3, int d4, int d5) {
        return sumXelementDices(d1, d2, d3, d4, d5, 3);
    }

    /**
     * calculate sum of 4
     * 
     * @return
     * 	return sum of 4
     */
    public int fours() {
        return sumXelementDices(4);
    }
    
    /**
     * calculate sum of 5
     * 
     * @return
     * 	return sum of 5
     */
    public int fives() {
        return sumXelementDices(5);
    }

    /**
     * calculate sum of 6
     * 
     * @return
     * 	return sum of 6
     */
    public int sixes() {
        return sumXelementDices(6);
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
    public static int scoreTwoPair(int d1, int d2, int d3, int d4, int d5) {
    	
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
    public static int scoreFourOfAKind(int d1, int d2, int d3, int d4, int d5) {
    	
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
    /**
     * score Three Of A Kind 
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
     * 		score 
     */
    public static int scoreThreeOfAKind(int d1, int d2, int d3, int d4, int d5) {

    	Map<Integer, Long> result = getMapsNumberOfDuplicateElementsDices(d1, d2, d3, d4, d5);
    		
    	Optional<Integer> totalElements= result.entrySet().stream()
				  .filter(e -> e.getValue()>=3 && e.getValue()<=5 )
				  .map(e-> e.getKey())
				  .reduce(Integer::sum);
    	
    	return totalElements.get()*3;
    }
    /**
     * sequence number (not max)
     * @param d1
     * @param d2
     * @param d3
     * @param d4
     * @param d5
     * @return
     * 		result 15 
     */
    public static int smallStraight(int d1, int d2, int d3, int d4, int d5) {
    	if (Stream.of(d1,d2,d3,d4,d5).distinct().count()==5) {
    		return POINT_SMALL_STRAIGHT;
    	}else {
    		return 0;
    	}
    }
    /**
     * sequence number max
     * @param d1
     * @param d2
     * @param d3
     * @param d4
     * @param d5
     * @return
     */
    public static int largeStraight(int d1, int d2, int d3, int d4, int d5) {
    	if (Stream.of(d1,d2,d3,d4,d5).filter(d -> d>=2).distinct().count()==5) {
    		return POINT_LARGE_STRAIGHT;
    	}else {
    		return 0;
    	}
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
    
    
    /**
     * 	calculate sum of element x in dices
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
     * @param x
     * 		element to sum 
     * @return
     * 		sum of element x in dices
     */
    private static int sumXelementDices(int d1, int d2, int d3, int d4, int d5, int x) {
        int[] dice = {d1, d2, d3, d4, d5};
        
        return Arrays.stream(dice).filter(e -> e == x).sum();
    }
    
    /**
     * 	calculate sum of element x in dices
     * @param x
     * 		element to sum 
     * @return
     * 		sum of element x in dices
     */
    private int sumXelementDices(int x) {
    	return Arrays.stream(this.dice).filter(e -> e == x).sum();
    }
    
    /**
     * getMapsNumberOfDuplicateElementsDices
     * key : element of dices
     * value : number of repetitions of this elements
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
     * 		map of number of duplicates elements
     */
    private static Map<Integer, Long> getMapsNumberOfDuplicateElementsDices(int d1, int d2, int d3, int d4, int d5) {
		Map<Integer, Long> result = Stream.of(d1,d2,d3,d4,d5)
										  .collect(Collectors
										  .groupingBy(Function.identity(),Collectors.counting())
    									);
		return result;
	}
}


