package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;
import numericalLibrary.optimization.lossFunctions.Loss;



/**
 * Defines the OR operator for {@link StoppingCriterion}s.
 * <p>
 * Stop iterating if some {@link #isFinished(IterativeOptimizationAlgorithm)} returns true.
 */
public class OrOperatorOnStoppingCriteria
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
     * Constructs a {@link OrOperatorOnStoppingCriteria}.
     * 
     * @param first     first {@link StoppingCriterion} to check for {@link #isFinished(IterativeOptimizationAlgorithm)}.
     * @param second    second {@link StoppingCriterion} to check for {@link #isFinished(IterativeOptimizationAlgorithm)}.
     */
    public OrOperatorOnStoppingCriteria( StoppingCriterion<Loss> first , StoppingCriterion<Loss> second )
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
        return ( isFirstFinished || isSecondFinished );
    }
    
}
