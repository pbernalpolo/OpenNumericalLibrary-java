package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Implements the Levenberg-Marquardt algorithm.
 * <p>
 * The Levenberg-Marquardt algorithm aims to minimize the cost function:
 * C( theta ) = \sum_i g( || f( x_i , theta ) ||^2 ) + lambda || delta ||^2
 * where,
 * - { x_i }_{i=1}^N is the set of inputs to the {@link OptimizableFunction} f,
 * - theta is the parameter vector,
 * - g is a robust function that defines the cost of each error squared,
 * - delta is the parameter increment: delta = theta_{k+1} - theta_k.
 * <p>
 * Including the ||delta||^2 term adds a cost to the step in the parameter space, preventing the occurrence of large steps that could lead to instabilities;
 * the value of lambda controls the importance given to that term.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm</a>
 */
public class LevenbergMarquardtAlgorithm<T>
    extends IterativeOptimizationAlgorithm<T>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * \sum_n J_n^T * w_n * J_n
     */
    private Matrix JTWJ;
    
    /**
     * \sum_n J_n^T * w_n * f_n
     */
    private Matrix JTWf;
    
    /**
     * Cost obtained in the last call to {@link #updateCostAndDelta()}.
     */
    private double loss;
    
    /**
     * Damping factor used to improve stability.
     * 
     * @see #setDampingFactor(double)
     */
    private double lambda;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the damping factor.
     * <p>
     * The damping factor controls the transition from Gauss-Newton algorithm to the gradient descent algorithm:
     * <ul>
     *  <li> A damping factor of 0 makes the algorithm be purely Gauss-Newton.
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
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public void allocateSpace( int numberOfParameters )
    {
        this.JTWJ = Matrix.empty( numberOfParameters , numberOfParameters );
        this.JTWf = Matrix.empty( numberOfParameters , 1 );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateDelta()
    {
        // Initialize the variables for the current step.
        this.JTWJ.setTo( Matrix.one( this.JTWJ.cols() ).scaleInplace( this.lambda ) );
        this.JTWf.setToZero();
        // Iterate on the input list.
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Introduce input in function.
            T input = this.inputList.get( i );
            this.optimizableFunction.setInput( input );
            // Obtain output and Jacobian from function.
            Matrix output = this.optimizableFunction.getOutput();
            Matrix jacobian = this.optimizableFunction.getJacobian();
            double errorSquared = output.transpose().multiply( output ).entry( 0,0 );
            double robustWeight = this.weightList.get( i ) * this.robustFunction.f1( errorSquared );
            // Add contribution to JTWJ and JTWf.
            Matrix JTW = jacobian.transpose().scaleInplace( robustWeight );
            this.JTWJ.addProduct( JTW , jacobian );
            this.JTWf.addProduct( JTW , output );
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateCostAndDelta()
    {
        // Initialize the variables for the current step.
        this.JTWJ.setTo( Matrix.one( this.JTWJ.cols() ).scaleInplace( this.lambda ) );
        this.JTWf.setToZero();
        this.loss = 0.0;
        // Iterate on the input list.
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Introduce input in function.
            T input = this.inputList.get( i );
            this.optimizableFunction.setInput( input );
            // Obtain output and Jacobian from function.
            Matrix output = this.optimizableFunction.getOutput();
            Matrix jacobian = this.optimizableFunction.getJacobian();
            double errorSquared = output.transpose().multiply( output ).entry( 0,0 );
            double weight = this.weightList.get( i );
            double robustWeight = weight * this.robustFunction.f1( errorSquared );
            Matrix JTW = jacobian.transpose().scaleInplace( robustWeight );
            // Add contribution to JTWJ and JTWf.
            this.JTWJ.addProduct( JTW , jacobian );
            this.JTWf.addProduct( JTW , output );
            // Add contribution to loss.
            this.loss += weight * this.robustFunction.f( errorSquared );
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double cost()
    {
        return this.loss;
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if a non positive-definite {@link Matrix} is obtained. In such a case, try adding a larger damping factor with {@link #setDampingFactor(double)}.
     */
    public Matrix deltaParameters()
    {
        try {
            this.JTWJ.choleskyDecompositionInplace();
        } catch( IllegalArgumentException e ) {
            throw new IllegalStateException( "Cholesky decomposition applied to non positive definite matrix. Setting a larger damping factor with setDampingFactor method might help." );
        }
        return this.JTWf.inverseAdditiveInplace().divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( this.JTWJ );
    }
    
}
