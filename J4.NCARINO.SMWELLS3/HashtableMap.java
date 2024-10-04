// == CS400 Spring 2024 File Header Information ==
// Name: Sean Wells
// Email: smwells3@wisc.edu
// Lecturer: Florian Heimerl
// Notes to Grader: 

import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This data type represents a collection that maps keys to values,
 * in which duplicate keys are not allowed (each key maps to exactly one value).
 */
public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

  /**
   * Keys and values are stored collectively as a single Pair object
   */
  protected class Pair {
    public KeyType key;
    public ValueType value;
    
    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
    
  }
  
  /**
   * The hash table itself, a collection of Pair (Key, Value) objects.
   */
  protected LinkedList<Pair>[] table;
  
  /**
   * The total number of pairs currently stored in the hash table. 
   */
  protected int size;
  
  /**
   * The maximum number of objects that may be stored in this table; will automatically be doubled
   * every time the load factor reaches 80%. 
   */
  protected int capacity;
  
  /**
   * Constructor for the hash table class, will create a hash table of Pair (Key, Value) objects with
   * an initial capacity specified. 
   * @param capacity the initial maximum number of objects that are able to be stored in this table
   */
  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
    this.capacity = capacity;
    table = (LinkedList<Pair>[]) new LinkedList[capacity];
    size = 0;
  }
  
  /**
   * Default constructor for the hash table class, will create a table with a maximum capacity of 64. 
   */
  public HashtableMap() {
    this(64);
  }
  
  /**
   * Adds a new key,value pair/mapping to this collection.
   * @param key the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException if key is null
   */
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    if (key == null) {
      throw new NullPointerException("Key cannot be null");
    }
    if(containsKey(key)) {
      throw new IllegalArgumentException("Key already exists");
    }
    int index = Math.abs(key.hashCode()) % capacity;
    if (table[index] == null) {
      table[index] = new LinkedList<>();
    }
    table[index].add(new Pair(key, value));
    size++;
    if ((double) getSize() / getCapacity() >= 0.8) {
      int newCapacity = capacity * 2;
      LinkedList<Pair>[] newTable = (LinkedList<Pair>[]) new LinkedList[newCapacity];
      for(int i = 0; i < capacity; i++) {
        if(table[i] != null) {
          for (Pair pair : table[i]) {
            int newIndex = Math.abs(pair.key.hashCode()) % newCapacity;
            if (newTable[newIndex] == null) {
              newTable[newIndex] = new LinkedList<>();
            }
            newTable[newIndex].add(pair);
          }
        }
      }
      table = newTable;
      capacity = newCapacity;
    }
  }

  /**
   * Checks whether a key maps to a value in this collection.
   * @param key the key to check
   * @return true if the key maps to a value, and false is the
   *         key doesn't map to a value
   */
  @Override
  public boolean containsKey(KeyType key) {
    int index = Math.abs(key.hashCode()) % capacity;
    if (table[index] != null) {
      for (Pair pair : table[index]) {
        if (pair.key.equals(key)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Retrieves the specific value that a key maps to.
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    int index = Math.abs(key.hashCode()) % capacity;
    if(table[index] != null) {
      for (Pair pair : table[index]) {
        if(pair.key.equals(key)) {
          return pair.value;
        }
      }
    }
    throw new NoSuchElementException("Key not found");
  }

  /**
   * Remove the mapping for a key from this collection.
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    int index = Math.abs(key.hashCode()) % capacity;
    if(table[index] != null) {
      for (Pair pair : table[index]) {
        if(pair.key.equals(key)) {
          ValueType value = pair.value;
          table[index].remove(pair);
          size--;
          return value;
        }
      }
    }
    throw new NoSuchElementException("Key not found");
  }

  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    for (int i = 0; i < capacity; i++) {
      if(table[i] != null) {
        table[i].clear();
      }
    }
    size = 0;
  }

  /**
   * Retrieves the number of keys stored in this collection.
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    return size;
  }

  /**
   * Retrieves this collection's capacity.
   * @return the size of the underlying array for this collection
   */
  @Override
  public int getCapacity() {
    return capacity;
  }

  /**
   * This tester method aims to ensure that the constructor creates a hash table of the proper 
   * capacity when given no arguments. We will then ensure that this capacity isn't set regardless
   * and will initialize a table with a specific capacity. 
   */
  @Test
  public void testDefault() {
    HashtableMap<Integer, String> map = new HashtableMap<Integer, String>();
    Assertions.assertEquals(map.getCapacity(), 64);
    HashtableMap<Integer, String> map2 = new HashtableMap<Integer, String>(11);
    Assertions.assertEquals(map2.getCapacity(), 11);
  }
  
  /**
   * Here, we are testing that the hash table automatically resizes when an object is added that
   * causes the load factor to equal or exceed 80%.
   */
  @Test
  public void testResizing() {
    //situation 1: capacity equals 0.8
    HashtableMap<Integer, String> map = new HashtableMap<Integer, String>(5);
    map.put(1, "a");
    map.put(2, "b");
    map.put(3, "c");
    map.put(4, "d"); // lf = 0.8
    Assertions.assertEquals(map.getCapacity(), 10);
    
    //situation 2: capacity exceeds 0.8
    map = new HashtableMap<Integer, String>(11);
    map.put(1, "a");
    map.put(2, "b");
    map.put(3, "c");
    map.put(4, "d");
    map.put(5, "e");
    map.put(6, "f");
    map.put(7, "g");
    map.put(8, "h");
    map.put(9, "i"); // lf = 0.81
    Assertions.assertEquals(map.getCapacity(), 22);
  }
  
  /**
   * This tester method aims to ensure that our put() method throws the proper exceptions given the 
   * right circumstances. In particular, we are checking the case that a null key is provided and
   * an attempt to add a (key, value) pair with a key that is already in the table. 
   */
  @Test
  public void testKeyErrors() {
    boolean nullKey = false;
    boolean sameKey = false;
    HashtableMap<Integer, String> map = new HashtableMap<Integer, String>();
    try {
      map.put(null, "one");
    } catch (NullPointerException e) {
      nullKey = true;
    }
    map.put(1, "one");
    try {
      map.put(1, "two");
    } catch (IllegalArgumentException e) {
      sameKey = true;
    }
    Assertions.assertEquals(true, nullKey);
    Assertions.assertEquals(true, sameKey);
  }
  
  /**
   * This tester method will test both the functionality of get() and clear() methods. In particular, 
   * we are checking that clear() sets the size to zero but the capacity remains the same. Additionally, 
   * we want to make sure that old (key, value) pairs cannot be accessed and the proper exception is
   * thrown when nonexistent keys are attempted to be accessed. 
   */
  @Test
  public void testGetClear() {
    HashtableMap<Integer, String> map = new HashtableMap<Integer, String>();
    map.put(1, "one");
    map.put(2, "two");
    map.put(3, "three");
    Assertions.assertEquals(map.getCapacity(), 64);
    Assertions.assertEquals(map.getSize(), 3);
    Assertions.assertEquals(map.get(1), "one"); // ensure the object is retrievable
    map.clear();
    boolean retrieved = false;
    try {
      map.get(1);
    } catch (NoSuchElementException e) {
      retrieved = true;
    }
    Assertions.assertEquals(map.getCapacity(), 64);
    Assertions.assertEquals(map.getSize(), 0);
    Assertions.assertEquals(retrieved, true);
  }
  
  /**
   * This tester method will check the implementation of the remove() and containsKey() methods. 
   * Specifically, we want to ensure that objects that have since been removed no longer exist in the table.
   * We will also check that remove updates the size of the table and returns the proper value.  
   */
  @Test
  public void testRemoveContains() {
    HashtableMap<Integer, String> map = new HashtableMap<Integer, String>();
    map.put(1, "one");
    map.put(2, "two");
    Assertions.assertEquals(map.containsKey(1), true);
    Assertions.assertEquals(map.containsKey(2), true);
    Assertions.assertEquals(map.getSize(), 2);
    String two = map.remove(2);
    Assertions.assertEquals(two, "two");
    Assertions.assertEquals(map.containsKey(1), true);
    Assertions.assertEquals(map.containsKey(2), false);
    Assertions.assertEquals(map.getSize(), 1);
    
  }
  
}
