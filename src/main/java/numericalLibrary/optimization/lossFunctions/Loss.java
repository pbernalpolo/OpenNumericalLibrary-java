package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;
import numericalLibrary.types.MatrixReal;



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
     * @param theta     column {@link MatrixReal} containing the parameters of the {@link Loss}.
     */
    public abstract void setParameters( MatrixReal theta );
    
    
    /**
     * Returns the current parameters of the {@link Loss} represented as a column {@link MatrixReal}.
     * <p>
     * Used to get the initial point in the solution space from which a {@link IterativeOptimizationAlgorithm} starts.
     * 
     * @return  current parameters of the {@link Loss} represented as a column {@link MatrixReal}.
     */
    public abstract MatrixReal getParameters();
    
    
    /**
     * Returns the output of the {@link Loss}.
     * <p>
     * The loss function is evaluated at the point defined by the parameters set by {@link #setParameters(MatrixReal)}.
     * 
     * @return  output of the {@link Loss}.
     */
    public abstract double getCost();
    
}
