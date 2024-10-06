package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Implements the Gauss-Newton algorithm.
 * <p>
 * The Gauss-Newton algorithm aims to minimize the cost function:
 * C( theta ) = \sum_i g( || f( x_i , theta ) ||^2 )
 * where,
 * - { x_i }_{i=1}^N is the set of inputs to the {@link OptimizableFunction} f,
 * - theta is the parameter vector,
 * - g is a robust function that defines the cost of each error squared.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Gauss%E2%80%93Newton_algorithm</a>
 */
public class GaussNewtonAlgorithm<T>
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
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
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
        this.JTWJ.setToZero();
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
        this.JTWJ.setToZero();
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
            // Add contribution to JTWJ and JTWf.
            Matrix JTW = jacobian.transpose().scaleInplace( robustWeight );
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
     * @throws IllegalStateException if a non positive-definite {@link Matrix} is obtained. In such a case, try using the {@link LevenbergMarquardtAlgorithm} instead.
     */
    public Matrix deltaParameters()
    {
        try {
            this.JTWJ.choleskyDecompositionInplace();
        } catch( IllegalArgumentException e ) {
            throw new IllegalStateException( "Cholesky decomposition applied to non positive definite matrix. Using the Levenberg-Marquardt algorithm using a small damping factor can help." );
        }
        return this.JTWf.inverseAdditiveInplace().divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( this.JTWJ );
    }
    
}
