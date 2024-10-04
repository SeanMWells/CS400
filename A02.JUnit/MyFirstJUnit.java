import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
/**
 * Some tests for MyList implementation of ListADT.
 */
public class MyFirstJUnit {

    /** 
     * Checks the size of a new list, and also checks that size after adding,
     * and removing elements, and also after clearing it's contents.
     */
    @Test
    public void testSizeOfList() {
	MyList<Boolean> list = new MyList<>();
	Assertions.assertEquals(0, list.size());
	list.add(true);
	list.add(false);
	list.add(true);
	list.add(false);
	Assertions.assertEquals(4, list.size());
	list.remove(0);
	list.remove(1);
	Assertions.assertEquals(2, list.size());
    }
    
    /**
     * By inserting 100 numbers this test should help ensure that the 
     * capacity of the underlying array is able to grow multiple times.
     * We then check that every pair of sequential elements in this list
     * are still in ascending order.
     */
    @Test
    public void testInserting100Numbers() {
	MyList<Integer> list = new MyList<>();
	// fill list with ITERATIONS sequentail and increasing elements
	final int ITERATIONS = 100;
	for(int i=0;i<ITERATIONS;i++)
	    list.add(i+1);
	// ensure that elements are still in increasing order
	for(int i=0;i<ITERATIONS-1;i++)
		//accidentally checking that items are in descending order
	    Assertions.assertEquals(-1, list.get(i+1) - list.get(i));
    }

    /**
     * Removes an element from each end of the array, and then checks what is
     * left in the position of that removed element to test correctnesss.  This
     * method is also relying on and testing size() to compute last index.
     */
    @Test
    public void testRemovingFromEnds() {
	MyList<String> list = new MyList<>();
	list.add("apple");
	list.add("banana");
	list.add("cherry");
	list.add("durian");

	// test removal from the (highest index) end of the list
	String threeWas = list.remove(list.size()-1);
	String twoIs = list.get(list.size()-1);
	Assertions.assertEquals("durian", threeWas);
	Assertions.assertEquals("cherry", twoIs);

    	// test removal from the (lowest index) start of the list
	String zeroWas = list.remove(0);
	String zeroIs = list.get(0);
	Assertions.assertEquals("apple", zeroWas);
	Assertions.assertEquals("banana", zeroIs);
    }
}
