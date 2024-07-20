package numericalLibrary.algebraicStructures;



/**
 * {@link MultiplicativeMagmaElement} represents elements of a magma with multiplication as the magma operation.
 * 
 * @param <T>   concrete type of {@link MultiplicativeMagmaElement}. We use CRTP to bound the type to interfaces that extend this interface.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Magma_(algebra)</a>
 */
public interface MultiplicativeMagmaElement<T extends MultiplicativeMagmaElement<T>>
	extends
        SetElement<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Multiplies {@code this} times {@code other}.
     * <p>
     * Result is returned as a new instance.
     * 
     * @param other     multiplier: {@code other} object to be multiplied by {@code this}.
     * @return  product of {@code this} times {@code other} stored in a new instance.
     */
    T multiply( T other );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC DEFAULT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Multiplies in-place {@code this} times {@code other}.
     * <p>
     * Operation done in-place.
     * 
     * @param other     multiplier: {@code other} object to be multiplied by {@code this}.
     * @return  product of {@code this} times {@code other} stored in {@code this}.
     */
    default T multiplyInplace( T other )
    {
        return this.setTo( this.multiply( other ) );
    }
    
    
    /**
     * Multiplies {@code first} and {@code second} and stores the result in {@code this}.
     * 
     * @param first     first factor of the multiplication.
     * @param second    second factor of the multiplication.
     * @return  product of the multiplication stored in {@code this}.
     */
    default T setToProduct( T first , T second )
    {
        return this.setTo( first.multiply( second ) );
    }
    
}
