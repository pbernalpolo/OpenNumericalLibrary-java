package numericalLibrary.algebraicStructures;



/**
 * {@link MultiplicativeAbelianGroupElement} represents elements of an Abelian group with multiplication as the group operation.
 * 
 * @param <T>   concrete type of {@link MultiplicativeAbelianGroupElement}. We use CRTP to bound the type to interfaces that extend this interface.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Abelian_group</a>
 */
public interface MultiplicativeAbelianGroupElement<T extends MultiplicativeAbelianGroupElement<T>>
	extends
	    MultiplicativeGroupElement<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Divides {@code this} by {@code other}.
     * <p>
     * Result is returned as a new instance.
     * 
     * @param other     divisor: {@code other} object that will divide {@code this}.
     * @return  quotient that results from division of {@code this} by {@code other} stored in a new instance.
     */
    public T divide( T other );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC DEFAULT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Divides in-place {@code this} by {@code other}.
     * <p>
     * Operation done in-place.
     * 
     * @param other     divisor: {@code other} object that will divide {@code this}.
     * @return  quotient that results from division of {@code this} by {@code other} stored in {@code this}.
     */
    default T divideInplace( T other )
    {
        return this.setTo( this.divide( other ) );
    }
    
}
