package algebraicStructures;



/**
 * {@link MultiplicativeGroupElement} represents elements of a group with multiplication as the group operation.
 * 
 * @param <T>   concrete type of {@link MultiplicativeGroupElement}. We use CRTP to bound the type to interfaces that extend this interface.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Group_(mathematics)</a>
 */
public interface MultiplicativeGroupElement<T extends MultiplicativeGroupElement<T>>
	extends
        MultiplicativeMagmaElement<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the identity element under the multiplication operation.
     * <p>
     * That is the element e in the group such that for every element a in the group  e * a == a  and  a * e == a .
     * Result is returned as a new instance.
     * 
     * @return  identity element under the multiplication operation.
     */
    T identityMultiplicative();
    
    
    /**
     * Returns the multiplicative inverse, also known as reciprocal.
     * <p>
     * Result is returned as a new instance.
     * 
     * @return  multiplicative inverse, also known as reciprocal, stored in a new instance.
     */
    T inverseMultiplicative();
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC DEFAULT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets {@code this} to the identity under multiplication.
     * <p>
     * Operation done in-place.
     * 
     * @return  identity under multiplication stored in {@code this}.
     */
    default T setToOne()
    {
        return this.setTo( this.identityMultiplicative() );
    }
    
    
    /**
     * Returns the multiplicative inverse of {@code this} stored in {@code this}.
     * <p>
     * Operation done in-place.
     * 
     * @return  multiplicative inverse of {@code this} stored in {@code this}.
     */
    default T inverseMultiplicativeInplace()
    {
        return this.setTo( this.inverseMultiplicative() );
    }
    
}
