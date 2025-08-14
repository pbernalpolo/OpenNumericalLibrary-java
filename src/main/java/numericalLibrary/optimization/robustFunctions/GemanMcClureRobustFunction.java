package numericalLibrary.optimization.robustFunctions;



/**
 * The Geman-McClure {@link RobustFunction} defined as:
 * f( x ) = 1/2 x^2 /( 1 + x^2 / k^2 )
 * Its derivative is
 * f'( x ) = x / ( 1 + x^2 / k^2 )^2
 * with weight function
 * w( x ) = 1 / ( 1 + x^2 / k^2 )^2
 * 
 * @see https://arxiv.org/abs/1810.01474
 */
public class GemanMcClureRobustFunction
    implements RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Square of parameter k of the Geman-McClure robust function.
     */
    private double kSquared;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link GemanMcClureRobustFunction}.
     * 
     * @param kParameter    k parameter of the Cauchy function.
     */
    public GemanMcClureRobustFunction( double kParameter )
    {
        this.kSquared = kParameter * kParameter;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double rho( double xSquared )
    {
    	return xSquared/2.0 / ( 1.0 + xSquared / this.kSquared );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double weight( double xSquared )
    {
    	double denominatorTerm = 1.0 + xSquared / this.kSquared;
    	return 1.0 / ( denominatorTerm * denominatorTerm );
    }
    
}
