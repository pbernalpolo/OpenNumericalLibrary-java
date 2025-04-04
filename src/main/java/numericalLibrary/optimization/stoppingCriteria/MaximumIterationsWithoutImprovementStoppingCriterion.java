package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.lossFunctions.Loss;



/**
 * Implements the stopping criterion to stop iterating if the best cost obtained so far has not been improved after a number of iterations.
 */
public class MaximumIterationsWithoutImprovementStoppingCriterion
    implements StoppingCriterion<Loss>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * The algorithm is considered finished if the number of iterations without improving the best cost obtained so far surpasses this number.
     */
    private final int maxIterationsWithoutImprovement;
    
    /**
     * Best cost obtained so far.
     */
    private double costBest;
    
    /**
     * Current number of iterations without improving the best cost obtained so far.
     */
    private int iterationsWithoutImprovement;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MaximumIterationsWithoutImprovementStoppingCriterion}.
     * 
     * @param maximumIterationsWithoutImprovement   maximum number of iterations without improving the best cost obtained so far.
     */
    public MaximumIterationsWithoutImprovementStoppingCriterion( int maximumIterationsWithoutImprovement )
    {
        this.maxIterationsWithoutImprovement = maximumIterationsWithoutImprovement;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public void initialize()
    {
        this.costBest = Double.MAX_VALUE;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean isFinished( Loss lossFunction )
    {
        double cost = lossFunction.getCost();
        if( cost < this.costBest ) {
            this.costBest = cost;
            this.iterationsWithoutImprovement = 0;
        } else {
            this.iterationsWithoutImprovement++;
        }
        return ( this.iterationsWithoutImprovement >= this.maxIterationsWithoutImprovement );
    }
    
}
