package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;



/**
 * Implements the stopping criterion to stop iterating if an iteration threshold is reached.
 */
public class IterationThresholdStoppingCriterion
    implements StoppingCriterion
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Iteration threshold that defines when to stop iterating.
     */
    private final int iterationThreshold;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link IterationThresholdStoppingCriterion}.
     * 
     * @param theIterationThreshold     iteration threshold that defines when to stop iterating.
     */
    public IterationThresholdStoppingCriterion( int theIterationThreshold )
    {
        this.iterationThreshold = theIterationThreshold;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public void initialize()
    {
        // There is nothing to do.
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean isFinished( IterativeOptimizationAlgorithm<?> iterativeAlgorithm )
    {
        return ( iterativeAlgorithm.getIteration() >= this.iterationThreshold );
    }
    
}
