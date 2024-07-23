package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.LevenbergMarquardtAlgorithm;



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
    
    public boolean isFinished( LevenbergMarquardtAlgorithm<?> iterativeAlgorithm )
    {
        return ( iterativeAlgorithm.getIterationLast() >= this.iterationThreshold );
    }
    
}
