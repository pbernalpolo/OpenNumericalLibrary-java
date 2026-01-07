package numericalLibrary.optimization.robustFunctions;



/**
 * {@link RobustFunction} defined as:
 * f( x ) = k^2/6 ( 1 - ( 1 - x^2 / k^2 )^3 )	if x < k
 *        = k^2/6   							if x >= k
 * where k is the value after which the function is constant.
 * The derivative is:
 * f'( x ) = x ( 1 - x^2 / k^2 )^2	if x < k
 * 		   = 0 						if x >= k
 * and its weight function
 * w( x ) = ( 1 - x^2 / k^2 )^2		if x < k
 * 		  = 0						if x >= k
 * 
 * @see https://arxiv.org/abs/1810.01474
 */
public class TukeyRobustFunction
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
     * Square of parameter k of the Tukey function divided by 6.
     */
    private double kSquaredOver6;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link TukeyRobustFunction}.
     * 
     * @param kParameter    value after which the function is constant.
     */
    public TukeyRobustFunction( double kParameter )
    {
        this.kSquared = kParameter * kParameter;
        this.kSquaredOver6 = this.kSquared / 6.0;
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
        	double y = 1.0 - xSquared / this.kSquared;
            return this.kSquaredOver6 * ( 1.0 - y * y * y );
        } else {
            return this.kSquaredOver6;
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double weight( double xSquared )
    {
        if( xSquared < this.kSquared ) {
        	double y = 1.0 - xSquared / this.kSquared;
            return this.kSquaredOver6 * ( 1.0 - y * y );
        } else {
            return 0.0;
        }
    }
    
}
