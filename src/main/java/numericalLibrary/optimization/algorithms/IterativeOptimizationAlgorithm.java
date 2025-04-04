package numericalLibrary.optimization.algorithms;


import numericalLibrary.optimization.lossFunctions.Loss;
import numericalLibrary.optimization.stoppingCriteria.StoppingCriterion;
import numericalLibrary.types.MatrixReal;



/**
 * Base class for {@link IterativeOptimizationAlgorithm}s.
 * <p>
 * Every {@link IterativeOptimizationAlgorithm} aims to minimize a loss function stepping iteratively in the parameter space, and progressively decreasing the cost value.
 */
public interface IterativeOptimizationAlgorithm<T extends Loss>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the delta in the parameter space computed with the concrete {@link IterativeOptimizationAlgorithm}.
     */
    public abstract MatrixReal getDeltaParameters( T lossFunction );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC DEFAULT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Performs a step in the parameter space that aims to decrease the cost value.
     * 
     * @throws IllegalStateException if {@link #step()} is called without having called previously {@link #initialize()}.
     * 
     * @see #initialize()
     * @see #iterate()
     */
    public default void step( T lossFunction )
    {
        // Compute delta.
        MatrixReal deltaParameters = this.getDeltaParameters( lossFunction );
        // Update theta.
        lossFunction.shift( deltaParameters );
    }
    
    
    /**
     * Iterates using {@link #step()} until a {@link StoppingCriterion} is met.
     * 
     * @see #step()
     * @see StoppingCriterion#isFinished(IterativeOptimizationAlgorithm)
     */
    public default void iterate( T lossFunction , StoppingCriterion<? super T> stoppingCriterion )
    {
    	stoppingCriterion.initialize();
        do {
            this.step( lossFunction );
        } while( !stoppingCriterion.isFinished( lossFunction ) );
    }
    
}
