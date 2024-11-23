package numericalLibrary.optimization.robustFunctions;


import numericalLibrary.optimization.lossFunctions.RobustMeanSquaredErrorLQL;



/**
 * Represents a robust function to be used in {@link RobustMeanSquaredErrorLQL}.
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
    public double f( double xSquared );
    
    
    /**
     * Returns the output of the derivative of the robust function.
     * 
     * @param xSquared  evaluation point. It is usually a square error.
     * @return  output of the derivative of the robust function.
     */
    public double f1( double xSquared );
    
}
