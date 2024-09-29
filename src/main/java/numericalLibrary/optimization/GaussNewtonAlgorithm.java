package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Implements the Gauss-Newton algorithm.
 * <p>
 * The Gauss-Newton algorithm aims to minimize the cost function:
 * C( theta ) = \sum_i || f( x_i , theta ) ||^2
 * where,
 * - { x_i }_{i=1}^N is the set of inputs to the {@link OptimizableFunction},
 * - delta is the parameter increment: delta = theta_{k+1} - theta_k.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Gauss%E2%80%93Newton_algorithm</a>
 */
public class GaussNewtonAlgorithm<T>
    extends IterativeOptimizationAlgorithm<T>
{
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if a non positive-definite {@link Matrix} is obtained. In such a case, try using the {@link LevenbergMarquardtAlgorithm} instead.
     */
    protected Matrix computeDelta()
    {
        Matrix JTWJ = this.JTW.multiply( this.J );
        Matrix JTWf = this.JTW.multiply( this.f );
        try {
            JTWJ.choleskyDecompositionInplace();
        } catch( IllegalArgumentException e ) {
            throw new IllegalStateException( "Cholesky decomposition applied to non positive definite matrix. Using the Levenberg-Marquardt algorithm using a small damping factor can help." );
        }
        return JTWf.inverseAdditiveInplace().divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( JTWJ );
    }
    
}
