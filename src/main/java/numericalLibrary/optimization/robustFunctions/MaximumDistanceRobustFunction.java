package numericalLibrary.optimization.robustFunctions;



/**
 * {@link RobustFunction} defined as:
 * f( ||e||^2 ) = ||e||^2   if ||e|| < k
 *              = k^2       if ||e|| >= k
 * where k is the value after which the function is constant.
 * 
 * @see https://arxiv.org/abs/1810.01474
 */
public class MaximumDistanceRobustFunction
    implements RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Square of parameter k after which the function is constant.
     */
    private double kSquared;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MaximumDistanceRobustFunction}.
     * 
     * @param kParameter    value after which the function is constant.
     */
    public MaximumDistanceRobustFunction( double kParameter )
    {
        this.kSquared = kParameter * kParameter;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double f( double xSquared )
    {
        if( xSquared < this.kSquared ) {
            return xSquared;
        } else {
            return this.kSquared;
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double f1( double xSquared )
    {
        if( xSquared < this.kSquared ) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
    
}
