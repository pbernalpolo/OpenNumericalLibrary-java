package numericalLibrary.optimization.robustFunctions;



/**
 * {@link RobustFunction} defined as the identity:
 * f( ||e||^2 ) = ||e||^2
 */
public class IdentityRobustFunction
    implements RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double f( double xSquared )
    {
        return xSquared;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double f1( double xSquared )
    {
        return 1.0;
    }
    
}
