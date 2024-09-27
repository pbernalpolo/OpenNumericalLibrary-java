package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.IterativeOptimizationAlgorithm;



public class IterationThresholdStoppingCriterion
    implements StoppingCriterion
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    private final int iterationThreshold;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    public IterationThresholdStoppingCriterion( int theIterationThreshold )
    {
        this.iterationThreshold = theIterationThreshold;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public boolean isFinished( IterativeOptimizationAlgorithm<?> iterativeAlgorithm )
    {
        return ( iterativeAlgorithm.getIterationLast() >= this.iterationThreshold );
    }
    
}
