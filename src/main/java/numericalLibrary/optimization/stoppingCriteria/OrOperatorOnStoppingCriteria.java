package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;



/**
 * Defines the OR operator for {@link StoppingCriterion}s.
 * <p>
 * Stop iterating if some {@link #isFinished(IterativeOptimizationAlgorithm)} returns true.
 */
public class OrOperatorOnStoppingCriteria
    implements StoppingCriterion
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * First {@link StoppingCriterion} to check.
     */
    private final StoppingCriterion firstStoppingCriterion;
    
    /**
     * Second {@link StoppingCriterion} to check.
     */
    private final StoppingCriterion secondStoppinCriterion;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link OrOperatorOnStoppingCriteria}.
     * 
     * @param first     first {@link StoppingCriterion} to check for {@link #isFinished(IterativeOptimizationAlgorithm)}.
     * @param second    second {@link StoppingCriterion} to check for {@link #isFinished(IterativeOptimizationAlgorithm)}.
     */
    public OrOperatorOnStoppingCriteria( StoppingCriterion first , StoppingCriterion second )
    {
        this.firstStoppingCriterion = first;
        this.secondStoppinCriterion = second;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public void initialize()
    {
        this.firstStoppingCriterion.initialize();
        this.secondStoppinCriterion.initialize();
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean isFinished( IterativeOptimizationAlgorithm<?> iterativeAlgorithm )
    {
        boolean isFirstFinished = this.firstStoppingCriterion.isFinished( iterativeAlgorithm );
        boolean isSecondFinished = this.secondStoppinCriterion.isFinished( iterativeAlgorithm );
        return ( isFirstFinished || isSecondFinished );
    }
    
}
