package no.rmz.robotics.arrays;


/**
 * A comparator.
 * @param <T> 
 */
public interface Comparator<T> {
    /**
     * Return -1 if t < t1, +1 if t1 > t2, 0 if they are equal.
     * @param t
     * @param t1
     * @return 
     */
     public int compare(final T t, final T t1);    
}
