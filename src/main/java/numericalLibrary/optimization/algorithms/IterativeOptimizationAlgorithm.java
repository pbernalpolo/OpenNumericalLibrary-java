package numericalLibrary.optimization.algorithms;


import numericalLibrary.optimization.lossFunctions.Loss;
import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.MaximumIterationsWithoutImprovementStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.OrOperatorOnStoppingCriteria;
import numericalLibrary.optimization.stoppingCriteria.StoppingCriterion;
import numericalLibrary.types.MatrixReal;



/**
 * Base class for {@link IterativeOptimizationAlgorithm}s.
 * <p>
 * Every {@link IterativeOptimizationAlgorithm} aims to minimize a loss function stepping iteratively in the parameter space, and progressively decreasing the cost value.
 */
public abstract class IterativeOptimizationAlgorithm<T extends Loss>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the delta in the parameter space computed with the concrete {@link IterativeOptimizationAlgorithm}.
     */
    public abstract MatrixReal getDeltaParameters();
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Loss function to be minimized by this {@link IterativeOptimizationAlgorithm}.
     */
    protected T lossFunction;
    
    /**
     * {@link StoppingCriterion} that defines when to stop iterating.
     */
    private StoppingCriterion stoppingCriterion;
    
    /**
     * Number of iterations performed so far.
     */
    private int iteration;
    
    /**
     * True if there was a call to the {@link #initialize()} method.
     * Used to throw an {@link IllegalStateException} if {@link #step()} is called without having previously called {@link #initialize()}.
     */
    private boolean initialized;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs an {@link IterativeOptimizationAlgorithm}.
     * 
     * @param lossFunction  {@link Loss} to be minimized.
     */
    public IterativeOptimizationAlgorithm( T lossFunction )
    {
        this.lossFunction = lossFunction;
        // Default stopping criterion.
        StoppingCriterion sc1 = new IterationThresholdStoppingCriterion( 10000 );
        StoppingCriterion sc2 = new MaximumIterationsWithoutImprovementStoppingCriterion( 50 );
        this.stoppingCriterion = new OrOperatorOnStoppingCriteria( sc1 , sc2 );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the {@link Loss} to be minimized.
     * 
     * @param lossFunction  {@link Loss} to be minimized.
     */
    public void setLossFunction( T lossFunction )
    {
        this.lossFunction = lossFunction;
    }
    
    
    /**
     * Sets the {@link StoppingCriterion} that defines when to stop iterating.
     * 
     * @param stoppingCriterion     {@link StoppingCriterion} that defines when to stop iterating.
     */
    public void setStoppingCriterion( StoppingCriterion stoppingCriterion )
    {
        this.stoppingCriterion = stoppingCriterion;
    }
    
    
    /**
     * Initializes the {@link IterativeOptimizationAlgorithm}.
     * 
     * @throws IllegalArgumentException if a wrong parameter format is detected in the {@link Loss}.
     * 
     * @see #step()
     */
    public void initialize()
    {
        // Check that the parameter vector is well defined.
        MatrixReal theta = this.lossFunction.getParameters();
        if( theta.cols() != 1 ) {
            throw new IllegalArgumentException( "Parameter vector returned by loss function must be a column matrix." );
        }
        // Initialize stopping criterion.
        this.stoppingCriterion.initialize();
        // Initialize iterations.
        this.iteration = 0;
        // Set initialized flag.
        this.initialized = true;
    }
    
    
    /**
     * Performs a step in the parameter space that aims to decrease the cost value.
     * 
     * @throws IllegalStateException if {@link #step()} is called without having called previously {@link #initialize()}.
     * 
     * @see #initialize()
     * @see #iterate()
     */
    public void step()
    {
        if( !this.initialized ) {
            throw new IllegalStateException( "Called step() without initialize() having been called previously. Method initialize() must be called after calling set methods in this IterativeOptimizationAlgorithm." );
        }
        // Compute delta.
        MatrixReal delta = this.getDeltaParameters();
        // Update theta.
        MatrixReal theta = this.lossFunction.getParameters();
        theta.addInplace( delta );
        this.lossFunction.setParameters( theta );
        // Update iteration.
        this.iteration++;
    }
    
    
    /**
     * Iterates using {@link #step()} until a {@link StoppingCriterion} is met.
     * 
     * @see #step()
     * @see StoppingCriterion#isFinished(IterativeOptimizationAlgorithm)
     */
    public void iterate()
    {
        do {
            this.step();
        } while( !this.stoppingCriterion.isFinished( this ) );
    }
    
    
    /**
     * Returns the last iteration number.
     * 
     * @return  last iteration number.
     */
    public int getIteration()
    {
        return this.iteration;
    }
    
    
    /**
     * Returns the current cost of the {@link Loss}.
     * 
     * @return  current cost of the {@link Loss}.
     */
    public double getCost()
    {
        return this.lossFunction.getCost();
    }
    
}
