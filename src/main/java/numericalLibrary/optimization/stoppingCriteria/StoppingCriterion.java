package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;



/**
 * Represents a stopping criterion for an {@link IterativeOptimizationAlgorithm}.
 * <p>
 * Stopping criteria are also known as stopping rule, stopping condition, or termination condition.
 * They define when to stop iterating in an {@link IterativeOptimizationAlgorithm}.
 */
public interface StoppingCriterion
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
    public boolean isFinished( IterativeOptimizationAlgorithm<?> iterativeAlgorithm );
    
}
