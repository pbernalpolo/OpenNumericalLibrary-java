package numericalLibrary.optimization.robustFunctions;



/**
 * The Fair {@link RobustFunction} defined as:
 * f( x ) = k^2 ( sqrt( 1 + x^2 / k^2 ) - 1 )
 * Its derivative is
 * f'( x ) = x / sqrt( 1 + x^2 / k^2 )
 * with weight function
 * w( x ) = 1 / sqrt( 1 + x^2 / k^2 )
 */
public class CharbonnierRobustFunction
    implements RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
	/**
     * Square of parameter k of the Charbonnier robust function.
     */
    private double kSquared;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link CharbonnierRobustFunction}.
     * 
     * @param kParameter    k parameter of the Cauchy function.
     */
    public CharbonnierRobustFunction( double kParameter )
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
    	return this.kSquared * ( Math.sqrt( 1.0 + xSquared / this.kSquared ) - 1.0 );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double weight( double xSquared )
    {
    	return 1.0 / Math.sqrt( 1.0 + xSquared / this.kSquared );
    }
    
}
