package numericalLibrary.optimization.algorithms;


import numericalLibrary.optimization.lossFunctions.LocallyQuadraticLoss;
import numericalLibrary.types.Matrix;



/**
 * Implements the Gauss-Newton algorithm.
 * <p>
 * The Gauss-Newton algorithm aims to minimize the loss function:
 * L( \theta ) = F( \theta )
 * where:
 * - F is a {@link LocallyQuadraticLoss},
 * - \theta is the parameter vector, represented as a row {@link Matrix}.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Gauss%E2%80%93Newton_algorithm</a>
 */
public class GaussNewtonAlgorithm
    extends IterativeOptimizationAlgorithm<LocallyQuadraticLoss>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link GaussNewtonAlgorithm}.
     * 
     * @param lossFunction  {@link LocallyQuadraticLoss} to be minimized.
     */
    public GaussNewtonAlgorithm( LocallyQuadraticLoss lossFunction )
    {
        super( lossFunction );
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
        Matrix L = null;
        try {
            L = gaussNewtonMatrix.choleskyDecompositionInplace();
        } catch( IllegalArgumentException e ) {
            throw new IllegalStateException( "Cholesky decomposition applied to non positive definite matrix. Using the Levenberg-Marquardt algorithm with a small damping factor can help." );
        }
        Matrix fTWJ = this.lossFunction.getJacobian();
        /*
         * Although the Gauss-Newton update is usually presented in the form:
         * \theta_{k+1} = \theta_k - ( J^T J )^{-1} J^T ( r(\theta) )
         * being \theta a column vector, here \theta is assumed to be a row vector.
         * This avoids the need to transpose the Jacobian, making the algorithm more efficient.
         * The update equation takes the form:
         * \theta_{k+1} = \theta_k - ( r(\theta) )^T J ( J^T J )^{-1}
         * this time being \theta a row vector.
         */
        return fTWJ.inverseAdditive().divideRightByPositiveDefiniteUsingItsCholeskyDecompositionInplace( L );
    }
    
}
