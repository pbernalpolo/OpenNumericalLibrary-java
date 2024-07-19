package algebraicStructures;



/**
 * {@link MetricSpaceElement} represents an element of a metric space.
 * <p>
 * A metric space is composed of a set, and a metric that defines the distance between set elements.
 * Such distance operation induces the notion of closeness, which can be used to check if two elements are approximately equal (see {@link SetElement#equalsApproximately(SetElement, double)}).
 * 
 * @param <T>   concrete type of {@link MetricSpaceElement}. We use CRTP to bound the type to interfaces that extend this interface.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Metric_space</a>
 */
public interface MetricSpaceElement<T extends MetricSpaceElement<T>>
    extends SetElement<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the distance from {@code other} to {@code this}.
     * 
     * @param other     {@code other} object from which the distance to {@code this} is computed.
     * @return  distance from {@code other} to {@code this}.
     */
    double distanceFrom( T other );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC DEFAULT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    default boolean equalsApproximately( T other , double tolerance )
    {
        return ( this.distanceFrom( other ) <= tolerance );
    }
    
}
