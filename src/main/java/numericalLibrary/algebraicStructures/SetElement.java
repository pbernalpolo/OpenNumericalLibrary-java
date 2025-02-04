package numericalLibrary.algebraicStructures;



/**
 * {@link SetElement} is the root interface for all classes that represent elements of a set.
 * 
 * @param <T>   concrete type of {@link SetElement}. We use CRTP to bound the type to interfaces that extend this interface.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Set_(mathematics)</a>
 */
public interface SetElement<T extends SetElement<T>>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns a {@code String} that represents {@code this}.
     * 
     * @return  {@code String} that represents {@code this}.
     */
    String toString();
    
    
    /**
     * Returns true if {@code other} is equal to {@code this}; returns false otherwise.
     * 
     * @param other     object to be compared with {@code this}.
     * @return  true if {@code other} is equal to {@code this}; false otherwise.
     */
    boolean equals( T other );
    
    
    /**
     * Returns true if {@code other} is approximately equal to {@code this} within a given tolerance; returns false otherwise.
     * 
     * @param other     object to be compared with {@code this}.
     * @param tolerance     tolerance used to determine if the elements are approximately equal.
     * @return  true if {@code other} is approximately equal to {@code this} within a given tolerance; false otherwise.
     */
    boolean equalsApproximately( T other , double tolerance );
    
    
    /**
     * Returns true if {@code this} contains some NaN value; returns {@code false} otherwise.
     * 
     * @return  true if {@code this} contains some NaN value; false otherwise.
     */
    boolean isNaN();
    
    
    /**
     * Returns a copy of {@code this}.
     * <p>
     * The copy must be a new instance, but {@link #equals(SetElement)} must return true when compared to {@code this}.
     * 
     * @return  copy of {@code this}.
     */
    T copy();
    
    
    /**
     * Sets {@code this} to be equal to {@code other}.
     * <p>
     * After using {@link #setTo(SetElement)} with {@code other} as argument, {@code this.equals(other)} must return {@code true}.
     * 
     * @param other     instance to which {@code this} will be equal.
     * @return  {@code this}.
     */
    T setTo( T other );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC DEFAULT METHODS
    ////////////////////////////////////////////////////////////////
    
    default T print()
    {
        System.out.println( this.toString() );
        return (T)this;
    }
    
}
