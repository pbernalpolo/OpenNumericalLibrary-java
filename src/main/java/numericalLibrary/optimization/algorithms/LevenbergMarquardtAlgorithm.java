package numericalLibrary.optimization.algorithms;


import numericalLibrary.optimization.lossFunctions.LocallyQuadraticLoss;
import numericalLibrary.types.Matrix;



/**
 * Implements the Levenberg-Marquardt algorithm.
 * <p>
 * The Levenberg-Marquardt algorithm aims to minimize the loss function:
 * L( \theta ) = F( \theta )
 * where:
 * - F is a {@link LocallyQuadraticLoss},
 * - \theta is the parameter vector, represented as a row {@link Matrix}.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm</a>
 */
public class LevenbergMarquardtAlgorithm
    extends IterativeOptimizationAlgorithm<LocallyQuadraticLoss>
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
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link LevenbergMarquardtAlgorithm}.
     * 
     * @param lossFunction  {@link LocallyQuadraticLoss} to be minimized.
     */
    public LevenbergMarquardtAlgorithm( LocallyQuadraticLoss lossFunction )
    {
        super( lossFunction );
    }
    
    
    
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
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if a non positive-definite {@link Matrix} is obtained. In such a case, try using the {@link LevenbergMarquardtAlgorithm} instead.
     */
    public Matrix getDeltaParameters()
    {
        Matrix gaussNewtonMatrix = this.lossFunction.getGaussNewtonMatrix();
        gaussNewtonMatrix.addInplace( Matrix.one( gaussNewtonMatrix.cols() ).scaleInplace( this.lambda ) );
        Matrix L = null;
        try {
            L = gaussNewtonMatrix.choleskyDecompositionInplace();
        } catch( IllegalArgumentException e ) {
            throw new IllegalStateException( "Cholesky decomposition applied to non positive definite matrix. Setting a larger damping factor with setDampingFactor method might help." );
        }
        Matrix fTWJ = this.lossFunction.getJacobian();
        /*
         * Although the Levenberg-Marquardt update is usually presented in the form:
         * \theta_{k+1} = \theta_k - ( J^T J + lambda I )^{-1} J^T ( r(\theta) )
         * being \theta is a column vector, here \theta is assumed to be a row vector.
         * This avoids the need to transpose the Jacobian, making the algorithm more efficient.
         * The update equation takes the form:
         * \theta_{k+1} = \theta_k - ( r(\theta) )^T J ( J^T J + lambda I )^{-1}
         * this time being \theta a row vector.
         */
        return fTWJ.inverseAdditive().divideRightByPositiveDefiniteUsingItsCholeskyDecompositionInplace( L );
    }
    
}
