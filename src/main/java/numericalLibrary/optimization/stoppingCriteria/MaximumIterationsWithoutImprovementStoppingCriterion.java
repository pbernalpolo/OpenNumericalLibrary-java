package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.IterativeOptimizationAlgorithm;



public class MaximumIterationsWithoutImprovementStoppingCriterion
    implements StoppingCriterion
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    private final int maxIterationsWithoutImprovement;
    
    private int iterationsWithoutImprovement;
    
    private double errorBestLast;
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    public MaximumIterationsWithoutImprovementStoppingCriterion( int maximumIterationsWithoutImprovement )
    {
        this.maxIterationsWithoutImprovement = maximumIterationsWithoutImprovement;
        this.errorBestLast = Double.MAX_VALUE;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public boolean isFinished( IterativeOptimizationAlgorithm<?> iterativeAlgorithm )
    {
        double errorBest = iterativeAlgorithm.getErrorBest();
        if( errorBest < this.errorBestLast ) {
            this.errorBestLast = errorBest;
            this.iterationsWithoutImprovement = 0;
        } else {
            this.iterationsWithoutImprovement++;
        }
        return ( this.iterationsWithoutImprovement >= this.maxIterationsWithoutImprovement );
    }
    
}
