package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;
import numericalLibrary.optimization.lossFunctions.Loss;



/**
 * Defines the AND operator for {@link StoppingCriterion}s.
 * <p>
 * Stop iterating if all {@link #isFinished(IterativeOptimizationAlgorithm)} return true.
 */
public class AndOperatorOnStoppingCriteria
    implements StoppingCriterion<Loss>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * First {@link StoppingCriterion} to check.
     */
    private final StoppingCriterion<Loss> firstStoppingCriterion;
    
    /**
     * Second {@link StoppingCriterion} to check.
     */
    private final StoppingCriterion<Loss> secondStoppinCriterion;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link AndOperatorOnStoppingCriteria}.
     * 
     * @param first     first {@link StoppingCriterion} to check for {@link #isFinished(IterativeOptimizationAlgorithm)}.
     * @param second    second {@link StoppingCriterion} to check for {@link #isFinished(IterativeOptimizationAlgorithm)}.
     */
    public AndOperatorOnStoppingCriteria( StoppingCriterion<Loss> first , StoppingCriterion<Loss> second )
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
    public boolean isFinished( Loss lossFunction )
    {
        boolean isFirstFinished = this.firstStoppingCriterion.isFinished( lossFunction );
        boolean isSecondFinished = this.secondStoppinCriterion.isFinished( lossFunction );
        return ( isFirstFinished && isSecondFinished );
    }
    
}
