package algebraicStructures;



/**
 * {@link AdditiveAbelianGroupElement} represents elements of an Abelian group with addition as the group operation.
 * 
 * @param <T>   concrete type of {@link AdditiveAbelianGroupElement}. We use CRTP to bound the type to interfaces that extend this interface.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Abelian_group</a>
 */
public interface AdditiveAbelianGroupElement<T extends AdditiveAbelianGroupElement<T>>
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
    
    
    /**
     * Subtracts {@code other} to {@code this}.
     * <p>
     * Result is returned as a new instance.
     * 
     * @param other     {@code other} object to be subtracted from {@code this}.
     * @return  difference between {@code this} and {@code other} stored in a new instance.
     */
    T subtract( T other );
    
    
    /**
     * Returns the identity element under the multiplication operation.
     * <p>
     * That is the element e in the group such that for every element a in the group  e * a == a  and  a * e == a .
     * Result is returned as a new instance.
     * 
     * @return  identity element under the multiplication operation.
     */
    T identityAdditive();
    
    
    /**
     * Returns the additive inverse, also known as opposite.
     * <p>
     * Result is returned as a new instance.
     * 
     * @return  additive inverse, also known as opposite stored in a new instance.
     */
    public T inverseAdditive();
    
    
    
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
     * Sets {@code this} to the zero element.
     * <p>
     * Operation done in-place.
     * 
     * @return  zero element stored in {@code this}.
     */
    default T setToZero()
    {
        return this.setTo( this.identityAdditive() );
    }
    
    
    /**
     * Returns the additive inverse of {@code this} stored in {@code this}.
     * <p>
     * Operation done in-place.
     * 
     * @return  additive inverse of {@code this} stored in {@code this}.
     */
    default T inverseAdditiveInplace()
    {
        return this.setTo( this.inverseAdditive() );
    }
    
    
    /**
     * Subtracts in-place {@code other} from {@code this}.
     * <p>
     * Operation done in-place.
     * 
     * @param other     {@code other} object to be subtracted from {@code this}.
     * @return  
     */
    default T subtractInplace( T other )
    {
        return this.setTo( this.subtract( other ) );
    }
    
}
