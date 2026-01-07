package numericalLibrary.optimization.robustFunctions;



/**
 * {@link RobustFunction} defined as:
 * f( x ) = x^2 / 2
 * with derivative:
 * f'( x ) = x
 * and weight function:
 * w( x ) = 1
 * 
 * @see https://arxiv.org/abs/1810.01474
 */
public class L2RobustFunction
    implements RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double rho( double xSquared )
    {
        return xSquared;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double weight( double xSquared )
    {
        return 1.0;
    }
    
}
