package numericalLibrary.optimization.robustFunctions;



/**
 * {@link RobustFunction} defined as:
 * f( ||e||^2 ) = k^2 / 2 ( 1 - exp( -||e||^2 / k^2 ) )
 * and derivative
 * f'( ||e||^2 ) = exp( - ||e||^2 / k^2 ) / 2
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
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double f( double xSquared )
    {
        return this.kSquared / 2.0 * ( 1.0 - Math.exp( - xSquared / this.kSquared ) );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double f1( double xSquared )
    {
        return Math.exp( - xSquared / this.kSquared ) / 2.0;
    }
    
}
