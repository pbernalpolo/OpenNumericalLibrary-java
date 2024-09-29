package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Implements the Levenberg-Marquardt algorithm.
 * <p>
 * The Levenberg-Marquardt algorithm aims to minimize the cost function:
 * C( theta ) = \sum_i || f( x_i , theta ) ||^2 + lambda || delta ||^2
 * where,
 * - { x_i }_{i=1}^N is the set of inputs to the {@link OptimizableFunction},
 * - delta is the parameter increment: delta = theta_{k+1} - theta_k.
 * <p>
 * Including the ||delta||^2 term adds a cost to the step in the parameter space;
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
     * 
     * @throws IllegalStateException if a non positive-definite {@link Matrix} is obtained. In such a case, try adding a damping factor with {@link #setDampingFactor(double)}.
     */
    protected Matrix computeDelta()
    {
        Matrix lambdaIplusJTWJ = Matrix.one( this.JTW.rows() ).scaleInplace( this.lambda ).addProduct( this.JTW , this.J );
        Matrix JTWf = this.JTW.multiply( this.f );
        try {
            lambdaIplusJTWJ.choleskyDecompositionInplace();
        } catch( IllegalArgumentException e ) {
            throw new IllegalStateException( "Cholesky decomposition applied to non positive definite matrix. Setting a small damping factor with setDampingFactor method can help." );
        }
        return JTWf.inverseAdditiveInplace().divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( lambdaIplusJTWJ );
    }
    
}
