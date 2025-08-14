package numericalLibrary.optimization.robustFunctions;



/**
 * {@link RobustFunction} defined as:
 * f( x ) = k^2 / 2 ( 1 - exp( -x^2 / k^2 ) )
 * with derivative:
 * f'( x ) = x exp( - x^2 / k^2 )
 * and weight function:
 * w( x ) = exp( -x^2 / k^2 )
 * 
 * @see https://arxiv.org/abs/1810.01474
 */
public class WelschRobustFunction
    implements RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Square parameter k of the Welsch function.
     */
    private double kSquared;
    
    /**
     * Square of parameter k of the Welsch function divided by 2.
     */
    private double kSquaredOver2;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link WelschRobustFunction}.
     * 
     * @param kParameter    k parameter of the Welsch function.
     */
    public WelschRobustFunction( double kParameter )
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
        return this.kSquaredOver2 * ( 1.0 - Math.exp( - xSquared / this.kSquared ) );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double weight( double xSquared )
    {
        return Math.exp( - xSquared / this.kSquared );
    }
    
}
