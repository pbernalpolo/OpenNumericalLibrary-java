package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.LevenbergMarquardtAlgorithm;



/**
 * Represents a stopping criterion for an iterative optimization algorithm.
 * <p>
 * Stopping criteria are also known as stopping rule, stopping condition, or termination condition.
 */
public interface StoppingCriterion
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    public boolean isFinished( LevenbergMarquardtAlgorithm<?> iterativeAlgorithm );
    
}
