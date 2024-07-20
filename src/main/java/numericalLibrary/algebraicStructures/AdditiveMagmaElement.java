package numericalLibrary.algebraicStructures;



/**
 * {@link AdditiveMagmaElement} represents elements of a magma with addition as the magma operation.
 * 
 * @param <T>   concrete type of {@link AdditiveMagmaElement}. We use CRTP to bound the type to interfaces that extend this interface.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Magma_(algebra)</a>
 */
public interface AdditiveMagmaElement<T extends AdditiveMagmaElement<T>>
    extends
        SetElement<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Adds {@code other} to {@code this}.
     * <p>
     * Result is returned as a new instance.
     * 
     * @param other     {@code other} object to be added to {@code this}.
     * @return  sum of {@code this} and {@code other} stored in a new instance.
     */
    public T add( T other );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC DEFAULT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Adds in-place {@code other} to {@code this}.
     * <p>
     * Operation done in-place.
     * 
     * @param other     {@code other} object to be added to {@code this}.
     * @return  sum of {@code this} and {@code other} stored in {@code this}.
     */
    default T addInplace( T other )
    {
        return this.setTo( this.add( other ) );
    }
    

    /**
     * Adds {@code first} and {@code second} and stores the result in {@code this}.
     * 
     * @param first     first term of the addition.
     * @param second    second term of the addition.
     * @return  sum of {@code first} and {@code second} stored in {@code this}.
     */
    default T setToSum( T first , T second )
    {
        return this.setTo( first.add( second ) );
    }
    
}
