package numericalLibrary.optimization;


import java.util.List;

import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.MaximumIterationsWithoutImprovementStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.OrOperatorOnStoppingCriteria;
import numericalLibrary.optimization.stoppingCriteria.StoppingCriterion;
import numericalLibrary.types.Matrix;



/**
 * Implements the Levenberg-Marquardt algorithm.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm</a>
 */
public class LevenbergMarquardtAlgorithm<T>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Model function used to fit the data.
     */
    private LevenbergMarquardtModelFunction<T> modelFunction;
    
    /**
     * List of empirical pairs of data to which {@link #modelFunction} will be fitted.
     */
    private List<LevenbergMarquardtEmpiricalPair<T>> empiricalPairs;
    
    /**
     * Stopping criterion used to stop the iterative algorithm.
     */
    private StoppingCriterion stoppingCriterion;
    
    /**
     * Current value for the parameter vector.
     * Updated in each {@link #step()} of the iterative algorithm.
     */
    private Matrix theta;
    
    /**
     * Value of the parameter vector for which the minimum error is obtained.
     * 
     * @see #errorBest
     */
    private Matrix thetaBest;
    
    /**
     * Vector of target values.
     * Computed in {@link #initialize()}.
     */
    private Matrix y;
    
    /**
     * Vector of output values from the {@link #modelFunction} at the current {@link #theta}.
     * Recomputed in each {@link #step()}.
     */
    private Matrix f;
    
    /**
     * Jacobian of the {@link #modelFunction} at the current {@link #theta}.
     * Recomputed in each {@link #step()}.
     */
    private Matrix J;
    
    /**
     * Last increment obtained from the Levenberg-Marquardt algorithm.
     * It is defined as a member variable so that we can provide it for a {@link StoppingCriterion}.
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
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link LevenbergMarquardtAlgorithm}.
     */
    public LevenbergMarquardtAlgorithm()
    {
        // Default stopping criteria.
        MaximumIterationsWithoutImprovementStoppingCriterion first = new MaximumIterationsWithoutImprovementStoppingCriterion( 20 );
        IterationThresholdStoppingCriterion second = new IterationThresholdStoppingCriterion( 1000 );
        this.stoppingCriterion = new OrOperatorOnStoppingCriteria( first , second );
        // Initialize best error.
        this.errorBest = Double.MAX_VALUE;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of empirical pairs used to fit the {@link #modelFunction}.
     * 
     * @param theEmpiricalPairs     list of empirical pairs used to fit the {@link #modelFunction}.
     */
    public void setEmpiricalPairs( List<LevenbergMarquardtEmpiricalPair<T>> theEmpiricalPairs )
    {
        this.empiricalPairs = theEmpiricalPairs;
    }
    
    
    /**
     * Sets the {@link LevenbergMarquardtModelFunction} used to fit the empirical pairs.
     * 
     * @param theModelFunction  {@link LevenbergMarquardtModelFunction} used to fit the empirical pairs.
     */
    public void setModelFunction( LevenbergMarquardtModelFunction<T> theModelFunction )
    {
        this.modelFunction = theModelFunction;
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
     * Initializes the iterative algorithm.
     * <p>
     * This includes:
     * <ul>
     *  <li> Initialize the {@link #theta} parameter vector.
     *  <li> Build the y vector.
     *  <li> Allocate space for the model function output.
     *  <li> Allocate space for the Jacobian of the model function.
     *  <li> Initialize the number of iterations.
     * </ul>
     * 
     * @see #step()
     */
    public void initialize()
    {
        this.theta = this.modelFunction.getParameters().copy();
        this.thetaBest = this.modelFunction.getParameters().copy();
        // Build y vector.
        this.y = Matrix.empty( this.empiricalPairs.size() , 1 );
        for( int i=0; i<this.empiricalPairs.size(); i++ )
        {
            this.y.setEntry( i,0 , this.empiricalPairs.get( i ).getY() );
        }
        // Allocate space for f and J.
        this.f = Matrix.empty( this.empiricalPairs.size() , 1 );
        this.J = Matrix.empty( this.empiricalPairs.size() , this.theta.rows() );
        // Initialize iterations.
        this.iteration = 0;
    }
    
    
    /**
     * Performs a step using the Levenberg-Marquardt algorithm.
     * 
     * @see #initialize()
     * @see #iterate()
     */
    public void step()
    {
        // Build the f vector and the Jacobian matrix.
        this.modelFunction.setParameters( this.theta );
        for( int i=0; i<this.empiricalPairs.size(); i++ )
        {
            this.modelFunction.setInput( this.empiricalPairs.get( i ).getX() );
            f.setEntry( i,0 , this.modelFunction.getOutput() );
            J.setSubmatrix( i,0 , this.modelFunction.getJacobian() );
        }
        // Compute delta.
        Matrix JT = J.transpose();
        Matrix JTJ = JT.multiply( J );
        Matrix fMinusY = f.subtractInplace( y );
        Matrix JTfMinusY = JT.multiply( fMinusY );
        JTJ.choleskyDecompositionInplace();
        this.delta = JTfMinusY.divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( JTJ );
        // Update theta.
        this.theta.subtractInplace( this.delta );
        // Update iteration.
        this.iteration++;
        // Update error.
        this.error = fMinusY.transpose().multiply( fMinusY ).entry( 0,0 );
        if( this.error < this.errorBest ) {
            this.errorBest = this.error;
            this.thetaBest = this.theta;
            this.iterationBest = this.iteration;
        }
    }
    
    
    /**
     * Iterates stepping according to the Levenberg-Marquardt algorithm until the stopping criterion is met.
     */
    public void iterate()
    {
        this.initialize();
        // Iterate.
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
    
}
