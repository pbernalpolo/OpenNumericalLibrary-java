package numericalLibrary.optimization.robustFunctions;



/**
 * {@link RobustFunction} defined as:
 * f( x ) = x^2/2	if x < k
 *        = k^2/2   if x >= k
 * where k is the value after which the function is constant.
 * The derivative is:
 * f'( x ) = x	if x < k
 * 		   = 0 	if x >= k
 * and its weight function
 * w( x ) = 1	if x < k
 * 		  = 0	if x >= k
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
    
    /**
     * Square of parameter k after which the function is constant divided by 2.
     */
    private double kSquaredOver2;
    
    
    
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
        this.kSquaredOver2 = 0.5 * this.kSquared;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double rho( double xSquared )
    {
        if( xSquared < this.kSquared ) {
            return xSquared/2.0;
        } else {
            return this.kSquaredOver2;
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double weight( double xSquared )
    {
        if( xSquared < this.kSquared ) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
    
}
