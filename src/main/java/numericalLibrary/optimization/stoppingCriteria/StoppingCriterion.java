package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;
import numericalLibrary.optimization.lossFunctions.Loss;



/**
 * Represents a stopping criterion for an {@link IterativeOptimizationAlgorithm}.
 * <p>
 * Stopping criteria are also known as stopping rule, stopping condition, or termination condition.
 * They define when to stop iterating in an {@link IterativeOptimizationAlgorithm}.
 */
public interface StoppingCriterion<T extends Loss>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Initializes the {@link StoppingCriterion}.
     */
    public void initialize();
    
    
    /**
     * Returns true if the {@link IterativeOptimizationAlgorithm} has finished; false otherwise.
     * 
     * @return  true if the {@link IterativeOptimizationAlgorithm} has finished; false otherwise.
     */
    public boolean isFinished( T lossFunction );
    
}
