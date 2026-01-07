package numericalLibrary.optimization.robustFunctions;



/**
 * The Fair {@link RobustFunction} defined as:
 * f( x ) = k^2 ( |x|/k - log( 1 + |x|/k ) )
 * Its derivative is
 * f'( x ) = x / ( 1 + |x| / k )
 * with weight function
 * w( x ) = 1 / ( 1 + |x| / k )
 */
public class FairRobustFunction
    implements RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
	/**
     * Parameter k of the Fair robust function.
     */
    private double k;
    
    /**
     * Square of parameter k of the Fair robust function.
     */
    private double kSquared;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link FairRobustFunction}.
     * 
     * @param kParameter    k parameter of the Cauchy function.
     */
    public FairRobustFunction( double kParameter )
    {
    	this.k = kParameter;
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
    	double xAbsOver_k = Math.sqrt( xSquared ) / this.k;
    	return this.kSquared * ( xAbsOver_k - Math.log( 1.0 + xAbsOver_k ) );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double weight( double xSquared )
    {
    	return 1.0 / ( 1.0 + Math.sqrt( xSquared ) / this.k );
    }
    
}
