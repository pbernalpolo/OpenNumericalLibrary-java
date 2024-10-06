package numericalLibrary.optimization;


import java.util.List;

import numericalLibrary.optimization.robustFunctions.IdentityRobustFunction;
import numericalLibrary.optimization.robustFunctions.RobustFunction;
import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.MaximumIterationsWithoutImprovementStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.OrOperatorOnStoppingCriteria;
import numericalLibrary.optimization.stoppingCriteria.StoppingCriterion;
import numericalLibrary.types.Matrix;



/**
 * Base class for every iterative optimization algorithm.
 * <p>
 * Every {@link IterativeOptimizationAlgorithm} aims to minimize a cost function of the form:
 * C( theta ) = \sum_i g( || f( x_i , theta ) ||^2 )
 * where,
 * - { x_i }_{i=1}^N is the set of inputs to the {@link OptimizableFunction} f,
 * - theta is the parameter vector,
 * - g is a robust function that defines the cost of each error squared.
 */
public abstract class IterativeOptimizationAlgorithm<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Allocates space for variables used in the concrete {@link IterativeOptimizationAlgorithm}.
     */
    public abstract void allocateSpace( int numberOfParameters );
    
    
    /**
     * Computes quantities that are necessary to obtain the delta vector (parameters increment).
     * <p>
     * This method allows to improve performance avoiding the computation of the cost.
     */
    public abstract void updateDelta();
    
    
    /**
     * Computes quantities that are necessary to obtain the value of the cost and the delta vector (parameters increment).
     * <p>
     * This method allows to compute the cost and the value of delta at the same time.
     */
    public abstract void updateCostAndDelta();
    
    
    /**
     * Returns the cost obtained after the last call to {@link #updateCostAndDelta()}.
     * 
     * @return  cost obtained after the last call to {@link #updateCostAndDelta()}.
     */
    public abstract double cost();

    
    /**
     * Returns the delta in the parameter space computed with the concrete {@link IterativeOptimizationAlgorithm}.
     * <p>
     * The value returned is the one obtained after the last call to {@link #updateDelta()} or {@link #updateCostAndDelta()}.
     */
    public abstract Matrix deltaParameters();
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Vector function whose norm squared will be minimized by this {@link IterativeOptimizationAlgorithm}.
     */
    protected OptimizableFunction<T> optimizableFunction;
    
    /**
     * Robust function that defines the cost of the error squared.
     */
    protected RobustFunction robustFunction;
    
    /**
     * List of input data to {@link #optimizableFunction}.
     */
    protected List<T> inputList;
    
    /**
     * List of weights associated to each input data in {@link #inputList}.
     */
    protected List<Double> weightList;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Stopping criterion used to stop the iterative algorithm.
     */
    private StoppingCriterion stoppingCriterion;
    
    /**
     * Last value for the parameter vector.
     * Updated in each {@link #step()} of the iterative algorithm.
     * Note that the actual parameters must be stored in the {@link OptimizableFunction}.
     * The reason for doing so is that this allows further flexibility for the user to modify the parameters in the concrete context of the {@link OptimizableFunction} while the algorithm is running.
     */
    private Matrix theta;
    
    /**
     * Value of the parameter vector for which the minimum error is obtained.
     * 
     * @see #costBest
     */
    private Matrix thetaBest;
    
    /**
     * Last increment obtained from the #{@link IterativeOptimizationAlgorithm}.
     * It is defined as a member variable so that we can provide it to a {@link StoppingCriterion}.
     */
    private Matrix delta;
    
    /**
     * Cost obtained with the last value of {@link #theta}.
     */
    private double costCurrent;
    
    /**
     * Minimum cost obtained so far.
     * 
     * @see #thetaBest
     */
    private double costBest;
    
    /**
     * Number of iterations performed so far.
     */
    private int iteration;
    
    /**
     * Iteration number that produced the minimum error.
     * 
     * @see #costBest
     */
    private int iterationBest;
    
    /**
     * True if there was a call to the {@link #initialize()} method.
     * Used to throw an {@link IllegalStateException} if {@link #step()} is called without having previously called {@link #initialize()}.
     */
    private boolean initialized;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs an {@link IterativeOptimizationAlgorithm}.
     */
    protected IterativeOptimizationAlgorithm()
    {
        // Default robust function.
        this.robustFunction = new IdentityRobustFunction();
        // Default stopping criteria.
        MaximumIterationsWithoutImprovementStoppingCriterion first = new MaximumIterationsWithoutImprovementStoppingCriterion( 20 );
        IterationThresholdStoppingCriterion second = new IterationThresholdStoppingCriterion( 1000 );
        this.stoppingCriterion = new OrOperatorOnStoppingCriteria( first , second );
        // Initialize initialized flag.
        this.initialized = false;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the {@link OptimizableFunction} to be optimized.
     * 
     * @param theOptimizableFunction  {@link OptimizableFunction} whose output is to be minimized.
     */
    public void setOptimizableFunction( OptimizableFunction<T> theOptimizableFunction )
    {
        this.optimizableFunction = theOptimizableFunction;
        // Now it will be necessary to call initialize().
        this.initialized = false;
    }
    
    
    /**
     * Sets the robust function that defines the cost of the error squared.
     * 
     * @param theRobustFunction     robust function that defines the cost of the error squared.
     */
    public void setRobustFunction( RobustFunction theRobustFunction )
    {
        this.robustFunction = theRobustFunction;
        // Now it will be necessary to call initialize().
        this.initialized = false;
    }
    
    
    /**
     * Sets the list of inputs to the {@link #optimizableFunction} whose output is to be optimized.
     * 
     * @param optimizableFunctionInputList     list of inputs to the {@link #optimizableFunction} whose output is to be optimized.
     */
    public void setInputList( List<T> optimizableFunctionInputList , List<Double> inputWeightList )
    {
        if( optimizableFunctionInputList.size() != inputWeightList.size() ) {
            throw new IllegalArgumentException( "Incompatible list sizes in optimizableFunctionInputList and inputWeightList." );
        }
        this.inputList = optimizableFunctionInputList;
        this.weightList = inputWeightList;
        // Now it will be necessary to call initialize().
        this.initialized = false;
    }
    
    
    /**
     * Sets the stopping criterion used to terminate the iterative algorithm.
     * 
     * @param theStoppingCriterion  stopping criterion used to terminate the iterative algorithm.
     */
    public void setStoppingCriterion( StoppingCriterion theStoppingCriterion )
    {
        this.stoppingCriterion = theStoppingCriterion;
    }
    
    
    /**
     * Initializes the {@link IterativeOptimizationAlgorithm}.
     * <p>
     * This includes:
     * <ul>
     *  <li> Initialize the {@link #theta} parameter vector.
     *  <li> Allocate space for the variables of the concrete {@link IterativeOptimizationAlgorithm}.
     *  <li> Computing the initial error.
     *  <li> Initialize the number of iterations.
     * </ul>
     * 
     * @see #step()
     */
    public void initialize()
    {
        // Initialize parameter vector.
        this.theta = this.optimizableFunction.getParameters();
        // Initialize algorithm.
        this.allocateSpace( this.theta.rows() );
        // Compute initial error.
        this.updateCostAndDelta();
        this.costCurrent = this.cost();
        // Initialize iterations.
        this.iteration = 0;
        // Initialize best error.
        this.costBest = this.costCurrent;
        this.thetaBest = this.theta.copy();
        this.iterationBest = this.iteration;
        // Set initialized flag.
        this.initialized = true;
    }
    
    
    /**
     * Performs a step using the {@link IterativeOptimizationAlgorithm}.
     * 
     * @throws IllegalStateException if {@link #step()} is called without having called previously {@link #initialize()}.
     * @see #initialize()
     * @see #iterate()
     */
    public void step()
    {
        if( !this.initialized ) {
            throw new IllegalStateException( "Called step() without initialize() having been called previously. That method needs to be called after calling setOptimizableFunction or setOptimizableFunctionInputList." );
        }
        // Compute delta.
        this.delta = this.deltaParameters();
        // Update theta.
        this.theta = this.optimizableFunction.getParameters();
        this.theta.addInplace( this.delta );
        this.optimizableFunction.setParameters( this.theta );
        // Update iteration.
        this.iteration++;
        // Update error.
        this.updateCostAndDelta();
        this.costCurrent = this.cost();
        if( this.costCurrent < this.costBest ) {
            this.costBest = this.costCurrent;
            this.thetaBest = this.theta.copy();
            this.iterationBest = this.iteration;
        }
    }
    
    
    /**
     * Iterates stepping according to the {@link IterativeOptimizationAlgorithm} until the stopping criterion is met.
     */
    public void iterate()
    {
        do {
            this.step();
        } while( !this.stoppingCriterion.isFinished( this ) );
    }
    
    
    /**
     * Returns the last produced solution.
     * 
     * @return  last produced solution.
     */
    public Matrix getSolutionLast()
    {
        return this.theta;
    }
    
    
    /**
     * Returns the best produced solution so far.
     * 
     * @return  best produced solution so far.
     */
    public Matrix getSolutionBest()
    {
        return this.thetaBest;
    }
    
    
    /**
     * Returns the last iteration count.
     * 
     * @return  last iteration count.
     */
    public int getIterationLast()
    {
        return this.iteration;
    }
    
    
    /**
     * Returns the iteration count when the best solution was found.
     * 
     * @return  iteration count when the best solution was found.
     */
    public int getIterationBest()
    {
        return this.iterationBest;
    }
    
    
    /**
     * Returns the error associated to the last produced solution.
     * 
     * @return  error associated to the last produced solution.
     */
    public double getCostLast()
    {
        return this.costCurrent;
    }
    
    
    /**
     * Returns the error associated to the best produced solution.
     * 
     * @return  error associated to the best produced solution.
     */
    public double getCostBest()
    {
        return this.costBest;
    }
    
}
