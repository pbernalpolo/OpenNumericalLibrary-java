package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.lossFunctions.Loss;



/**
 * Implements the stopping criterion to stop iterating if an iteration threshold is reached.
 */
public class IterationThresholdStoppingCriterion
    implements StoppingCriterion<Loss>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Iteration threshold that defines when to stop iterating.
     */
    private final int iterationThreshold;
    
    private int iteration;
    
    
    
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
        this.iteration = 0;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean isFinished( Loss lossFunction )
    {
    	this.iteration++;
        return ( this.iteration >= this.iterationThreshold );
    }
    
}
