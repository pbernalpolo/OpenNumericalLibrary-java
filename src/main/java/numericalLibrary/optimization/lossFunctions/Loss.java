package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;
import numericalLibrary.types.Matrix;



/**
 * Represents a loss function.
 */
public interface Loss
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the parameters of the {@link Loss}.
     * 
     * @param theta     row {@link Matrix} containing the parameters of the {@link Loss}.
     */
    public abstract void setParameters( Matrix theta );
    
    
    /**
     * Returns the current parameters of the {@link Loss} represented as a row {@link Matrix}.
     * <p>
     * Used to get the initial point in the solution space from which a {@link IterativeOptimizationAlgorithm} starts.
     * 
     * @return  current parameters of the {@link Loss} represented as a row {@link Matrix}.
     */
    public abstract Matrix getParameters();
    
    
    /**
     * Returns the output of the {@link Loss}.
     * <p>
     * The function is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}.
     * 
     * @return  output of the {@link Loss}.
     */
    public abstract double getCost();
    
}
