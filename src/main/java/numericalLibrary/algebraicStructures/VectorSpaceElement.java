package numericalLibrary.algebraicStructures;



/**
 * {@link VectorSpaceElement} represents an element of a vector space over the real numbers.
 * <p>
 * This class is defined to increase performance, knowing that the primitive data type {@code double} can represent the field of real numbers.
 * 
 * @param <T>   concrete type of {@link VectorSpaceElement}. We use CRTP to bound the type to interfaces that extend this interface.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Vector_space</a>
 */
public interface VectorSpaceElement< T extends VectorSpaceElement<T> >
	extends AdditiveAbelianGroupElement<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Scales {@code this} by {@code scalar}.
     * <p>
     * Result is returned as a new instance.
     * 
     * @param scalar    scalar that scales {@code this}.
     * @return  {@code this} scaled by {@code scalar}, stored in a new instance.
     */
    T scale( double scalar );
    
    
    /**
     * Scales in-place {@code this} by {@code other}.
     * <p>
     * Operation done in-place.
     * 
     * @param scalar    scalar that scales {@code this}.
     * @return  {@code this} scaled by {@code scalar}, stored in {@code this}.
     */
    T scaleInplace( double scalar );
    
}
