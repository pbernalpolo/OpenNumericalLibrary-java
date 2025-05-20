package numericalLibrary.optimization.robustFunctions;



/**
 * {@link RobustFunction} defined as:
 * f( ||e||^2 ) = k^2 / 2 log( 1 + ||e||^2/k^2 )
 * Its derivative is
 * f'( ||e||^2 ) = 1/2 1/( 1 + ||e||^2/k^2 )
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
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double f( double xSquared )
    {
    	return this.kSquared/2.0 * Math.log( 1.0 + xSquared / this.kSquared );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double f1( double xSquared )
    {
    	return 0.5 / ( 1.0 + xSquared / this.kSquared );
    }
    
}
