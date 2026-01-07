package numericalLibrary.optimization.robustFunctions;



/**
 * The Cauchy {@link RobustFunction} defined as:
 * f( x ) = k^2 / 2 log( 1 + x^2 / k^2 )
 * Its derivative is
 * f'( x ) = x / ( 1 + x^2 / k^2 )
 * and its weight function
 * w( x ) = 1 / ( 1 + x^2 / k^2 )
 * 
 * @see https://arxiv.org/abs/1810.01474
 */
public class CauchyRobustFunction
    implements RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Square of parameter k of the Cauchy function.
     */
    private double kSquared;
    
    /**
     * Square of parameter k of the Cauchy function divided by 2.
     */
    private double kSquaredOver2;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link CauchyRobustFunction}.
     * 
     * @param kParameter    k parameter of the Cauchy function.
     */
    public CauchyRobustFunction( double kParameter )
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
    	return this.kSquaredOver2 * Math.log( 1.0 + xSquared / this.kSquared );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double weight( double xSquared )
    {
    	return 1.0 / ( 1.0 + xSquared / this.kSquared );
    }
    
}
