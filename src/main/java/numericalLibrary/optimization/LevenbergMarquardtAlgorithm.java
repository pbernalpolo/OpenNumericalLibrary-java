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
     * Last value for the parameter vector.
     * Updated in each {@link #step()} of the iterative algorithm.
     * Note that the actual parameters are stored in the {@link LevenbergMarquardtModelFunction}.
     * The reason for doing so is that this allows further flexibility for the user to modify the parameters in the concrete context of the {@link LevenbergMarquardtModelFunction} while the algorithm is running.
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
     * Damping factor used to improve stability.
     * 
     * @see #setDampingFactor(double)
     */
    private double lambda;
    
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
        // Default lambda value.
        this.lambda = 0.0;
        // Initialize initialized flag.
        this.initialized = false;
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
        // Now it will be necessary to call initialize().
        this.initialized = false;
    }
    
    
    /**
     * Sets the {@link LevenbergMarquardtModelFunction} used to fit the empirical pairs.
     * 
     * @param theModelFunction  {@link LevenbergMarquardtModelFunction} used to fit the empirical pairs.
     */
    public void setModelFunction( LevenbergMarquardtModelFunction<T> theModelFunction )
    {
        this.modelFunction = theModelFunction;
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
     * Sets the damping factor.
     * <p>
     * The damping factor controls makes the Levenberg-Marquardt algorithm transition to the gradient descent algorithm:
     * <ul>
     *  <li> A damping factor of 0 makes the algorithm to be purely Levenberg-Marquardt.
     *  <li> The bigger the damping factor, the closer to a gradient descent algorithm.
     * </ul>
     * Introducing a small damping factor usually helps solving problems when matrices are ill-conditioned (producing failures in the Cholesky decomposition).
     * 
     * @param dampingFactor     damping factor to be set.
     */
    public void setDampingFactor( double dampingFactor )
    {
        this.lambda = dampingFactor;
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
     * @throws IllegalArgumentException if the target of some empirical pair is not a column vector, or if its dimension does not match the dimension of the output of the model function.
     * @see #step()
     */
    public void initialize()
    {
        // Initialize parameter vector.
        this.theta = this.modelFunction.getParameters();
        // Count number of rows.
        int numberOfRows = 0;
        for( LevenbergMarquardtEmpiricalPair<T> empiricalPair : this.empiricalPairs ) {
            this.modelFunction.setInput( empiricalPair.getX() );
            Matrix yOfEmpiricalPair = empiricalPair.getY();
            Matrix fOfEmpiricalPair = this.modelFunction.getOutput();
            if( yOfEmpiricalPair.rows() != fOfEmpiricalPair.rows()  ||  yOfEmpiricalPair.cols() != 1  ||  fOfEmpiricalPair.cols() != 1 ) {
                throw new IllegalArgumentException( "Target of LevenbergMarquardtEmpiricalPair must be a column vector with same rows as output of LevenbergMarquardtModelFunction." );
            }
            numberOfRows += yOfEmpiricalPair.rows();
        }
        // Allocate space for y, f, and J.
        this.y = Matrix.empty( numberOfRows , 1 );
        this.f = Matrix.empty( numberOfRows , 1 );
        this.J = Matrix.empty( numberOfRows , this.theta.rows() );
        // Set y vector.
        this.y = Matrix.empty( numberOfRows , 1 );
        int index = 0;
        for( LevenbergMarquardtEmpiricalPair<T> empiricalPair : this.empiricalPairs ) {
            Matrix yOfEmpiricalPair = empiricalPair.getY();
            this.y.setSubmatrix( index,0 , yOfEmpiricalPair );
            index += yOfEmpiricalPair.rows();
        }
        // Initialize best error.
        this.errorBest = Double.MAX_VALUE;
        // Initialize iterations.
        this.iteration = 0;
        // Set initialized flag.
        this.initialized = true;
    }
    
    
    /**
     * Performs a step using the Levenberg-Marquardt algorithm.
     * <p>
     * If an {@link IllegalStateException} is thrown because a {@link Matrix} not being positive-definite, try adding a damping factor with {@link #setDampingFactor(double)}.
     * 
     * @throws IllegalStateException if a non positive-definite {@link Matrix} is obtained. In such a case, try adding a damping factor with {@link #setDampingFactor(double)}.
     * @see #initialize()
     * @see #iterate()
     */
    public void step()
    {
        if( !this.initialized ) {
            throw new IllegalStateException( "Called step() without having previously called initialize(). That method needs to be called after calling setModelFunction or setEmpiricalPairs." );
        }
        // Build the f vector and the Jacobian matrix.
        int index = 0;
        for( LevenbergMarquardtEmpiricalPair<T> empiricalPair : this.empiricalPairs ) {
            this.modelFunction.setInput( empiricalPair.getX() );
            Matrix fOfEmpiricalPair = this.modelFunction.getOutput();
            f.setSubmatrix( index,0 , fOfEmpiricalPair );
            J.setSubmatrix( index,0 , this.modelFunction.getJacobian() );
            index += fOfEmpiricalPair.rows();
        }
        // Compute delta.
        Matrix JT = J.transpose();
        Matrix lambdaIplusJTJ = Matrix.one( JT.rows() ).scaleInplace( this.lambda ).addProduct( JT , J );
        Matrix yMinusF = y.subtract( f );
        Matrix JTyMinusF = JT.multiply( yMinusF );
        try {
            lambdaIplusJTJ.choleskyDecompositionInplace();
        } catch( IllegalArgumentException e ) {
            throw new IllegalStateException( "Cholesky decomposition applied to non positive definite matrix. Setting a small damping factor with setDampingFactor method can help." );
        }
        this.delta = JTyMinusF.divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( lambdaIplusJTJ );
        // Update error.
        this.theta = this.modelFunction.getParameters();
        this.error = yMinusF.transpose().multiply( yMinusF ).entry( 0,0 );
        if( this.error < this.errorBest ) {
            this.errorBest = this.error;
            this.thetaBest = this.theta.copy();
            this.iterationBest = this.iteration;
        }
        // Update theta.
        this.theta.addInplace( this.delta );
        this.modelFunction.setParameters( this.theta );
        // Update iteration.
        this.iteration++;
    }
    
    
    /**
     * Iterates stepping according to the Levenberg-Marquardt algorithm until the stopping criterion is met.
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
