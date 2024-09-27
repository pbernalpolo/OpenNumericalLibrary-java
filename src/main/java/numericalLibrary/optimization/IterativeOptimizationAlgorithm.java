package numericalLibrary.optimization;


import java.util.List;

import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.MaximumIterationsWithoutImprovementStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.OrOperatorOnStoppingCriteria;
import numericalLibrary.optimization.stoppingCriteria.StoppingCriterion;
import numericalLibrary.types.Matrix;



/**
 * Base class for every iterative optimization algorithm.
 */
public abstract class IterativeOptimizationAlgorithm<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Computes delta using the last values for f and J.
     * Performs a step of the {@link IterativeOptimizationAlgorithm}.
     * 
     * @throws IllegalStateException if {@link #initialize()} has not been called previously.
     * @see #initialize()
     * @see #iterate()
     */
    protected abstract Matrix computeDelta();
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Vector of output values from the {@link #optimizableFunction} at the current {@link #theta}.
     * Recomputed in each {@link #updateFJAndError()}.
     */
    protected Matrix f;
    
    /**
     * Jacobian of the {@link #optimizableFunction} at the current {@link #theta}.
     * Recomputed in each {@link #updateFJAndError()}.
     */
    protected Matrix J;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Vector function whose norm squared will be minimized by this {@link IterativeOptimizationAlgorithm}.
     */
    private OptimizableFunction<T> optimizableFunction;
    
    /**
     * List of input data to {@link #optimizableFunction}.
     */
    private List<T> optimizableFunctionInputList;
    
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
     * @see #errorBest
     */
    private Matrix thetaBest;
    
    /**
     * Last increment obtained from the #{@link IterativeOptimizationAlgorithm}.
     * It is defined as a member variable so that we can provide it to a {@link StoppingCriterion}.
     */
    private Matrix delta;
    
    /**
     * Error obtained with the last value of {@link #theta}.
     */
    private double error;
    
    /**
     * Minimum error obtained so far.
     * 
     * @see #thetaBest
     */
    private double errorBest;
    
    /**
     * Number of iterations performed so far.
     */
    private int iteration;
    
    /**
     * Iteration number that produced the minimum error.
     * 
     * @see #errorBest
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
     * Sets the list of inputs to the {@link #optimizableFunction} whose output is to be optimized.
     * 
     * @param optimizableFunctionInputList     list of inputs to the {@link #optimizableFunction} whose output is to be optimized.
     */
    public void setOptimizableFunctionInputList( List<T> optimizableFunctionInputList )
    {
        this.optimizableFunctionInputList = optimizableFunctionInputList;
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
     *  <li> Allocate space for the {@link OptimizableFunction} output.
     *  <li> Allocate space for the Jacobian of the {@link OptimizableFunction}.
     *  <li> Initialize the number of iterations.
     * </ul>
     * 
     * @throws IllegalArgumentException if the output of the OptimizableFunction is not a column vector, or if the rows of the Jacobian do not match the rows of the output vector.
     * @see #step()
     */
    public void initialize()
    {
        // Initialize parameter vector.
        this.theta = this.optimizableFunction.getParameters();
        // Count number of rows.
        int numberOfRows = 0;
        for( T input : this.optimizableFunctionInputList ) {
            this.optimizableFunction.setInput( input );
            Matrix output = this.optimizableFunction.getOutput();
            Matrix jacobian = this.optimizableFunction.getJacobian();
            if( output.cols() != 1 ) {
                throw new IllegalArgumentException( "Output of OptimizableFunction must be a column vector." );
            }
            if( jacobian.rows() != output.rows() ) {
                throw new IllegalArgumentException( "Jacobian of OptimizableFunction must have same rows as output of OptimizableFunction." );
            }
            numberOfRows += output.rows();
        }
        // Allocate space for f, and J.
        this.f = Matrix.empty( numberOfRows , 1 );
        this.J = Matrix.empty( numberOfRows , this.theta.rows() );
        // Compute initial error.
        this.updateFJAndError();
        // Initialize iterations.
        this.iteration = 0;
        // Initialize best error.
        this.errorBest = f.transpose().multiply( f ).entry( 0,0 );
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
            throw new IllegalStateException( "Called step() without having called initialize() previously. That method needs to be called after calling setOptimizableFunction or setOptimizableFunctionInputList." );
        }
        // Compute delta using the last values for f and J.
        this.delta = this.computeDelta();
        // Update error.
        this.theta = this.optimizableFunction.getParameters();
        this.error = this.f.transpose().multiply( this.f ).entry( 0,0 );
        if( this.error < this.errorBest ) {
            this.errorBest = this.error;
            this.thetaBest = this.theta.copy();
            this.iterationBest = this.iteration;
        }
        // Update theta.
        this.theta.addInplace( this.delta );
        this.optimizableFunction.setParameters( this.theta );
        // Update iteration.
        this.iteration++;
        // Update f, J and error for next step.
        this.updateFJAndError();
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
        return this.iteration-1;
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
    public double getErrorLast()
    {
        return this.error;
    }
    
    
    /**
     * Returns the error associated to the best produced solution.
     * 
     * @return  error associated to the best produced solution.
     */
    public double getErrorBest()
    {
        return this.errorBest;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Updates the values of {@link #f}, and {@link #J} using the current {@link #optimizableFunction} parameters.
     */
    private void updateFJAndError()
    {
        // Build the f vector and Jacobian matrix.
        int index = 0;
        for( T input : this.optimizableFunctionInputList ) {
            this.optimizableFunction.setInput( input );
            Matrix output = this.optimizableFunction.getOutput();
            this.f.setSubmatrix( index,0 , output );
            this.J.setSubmatrix( index,0 , this.optimizableFunction.getJacobian() );
            index += output.rows();
        }
    }
    
}
