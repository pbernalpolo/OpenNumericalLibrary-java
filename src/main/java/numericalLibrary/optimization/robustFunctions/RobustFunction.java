package numericalLibrary.optimization.robustFunctions;


import numericalLibrary.optimization.lossFunctions.Loss;



/**
 * Represents a robust function used to define a robust cost in a {@link Loss}.
 */
public interface RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the output of the robust function.
     * 
     * @param xSquared  evaluation point. It is usually a square error.
     * @return  output of the robust function.
     */
    public double rho( double xSquared );
    
    
    /**
     * Returns the output of the weight function.
     * <p>
     * The weight function is the derivative of the robust function divided by x: f'(x)/x.
     * For example, the weight function of x^2/2 is 1.
     * This definition comes from considering the derivative of the cost function:
     * C( \theta ) = \sum_i \rho( ||r_i|| )
     * assuming that
     * r_i( \theta ) \approx f_i( \theta_0 ) + d f_i / d \theta |_{\theta_0} ( \theta - \theta_0 )
     * the derivative of C( \theta ) is approximately given by
     * d C( \theta ) / d \theta \approx \sum_i d \rho( ||r_i|| ) / d ||r_i|| * d ||r_i|| / d \theta =
     * = d \rho( ||r_i|| ) / d ||r_i|| * r_i^T/||r_i|| * d f / d \theta |_{\theta_0} =
     * = d \rho( ||r_i|| ) / d ||r_i|| / ||r_i|| * r_i^T * d f / d \theta |_{\theta_0}
     * The term d \rho( ||r_i|| ) / d ||r_i|| / ||r_i|| defines the weight function.
     * 
     * @param xSquared  evaluation point. It is usually a square error.
     * @return  output of the weight function.
     */
    public double weight( double xSquared );
    
}
