package numericalLibrary.optimization.algorithms;


import numericalLibrary.optimization.lossFunctions.DifferentiableLoss;
import numericalLibrary.types.Matrix;



/**
 * Implements the Gradient Descent algorithm.
 * <p>
 * The Gradient Descent algorithm aims to minimize the loss function:
 * L( \theta ) = f( \theta )
 * where:
 * - f is a {@link DifferentiableLoss},
 * - \theta is the parameter vector, represented as a row {@link Matrix}.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Gradient_descent</a>
 */
public class GradientDescentAlgorithm
    extends IterativeOptimizationAlgorithm<DifferentiableLoss>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Learning rate.
     * 
     * @see #setLearningRate(double)
     */
    private double learningRate;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs an {@link GradientDescentAlgorithm}.
     * 
     * @param lossFunction  {@link DifferentiableLoss} to be minimized.
     */
    public GradientDescentAlgorithm( DifferentiableLoss lossFunction )
    {
        super( lossFunction );
        // Default learning rate.
        this.learningRate = 1.0e-4;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the loss function to be minimized.
     * 
     * @param differentiableLossFunction   loss function to be minimized.
     */
    public void setLossFunction( DifferentiableLoss differentiableLossFunction )
    {
        this.lossFunction = differentiableLossFunction;
    }
    
    
    /**
     * Sets the learning rate.
     * 
     * @param learningRate     learning rate to be set.
     */
    public void setLearningRate( double learningRate )
    {
        this.learningRate = learningRate;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * <p>
     * theta_{k+1} = theta_k - gamma * ( d f / d theta )^T
     */
    public Matrix getDeltaParameters()
    {
        /*
         * Although the Gradient descent update is usually presented in the form:
         * \theta_{k+1} = \theta_k - J^T
         * being \theta a column vector, here \theta is assumed to be a row vector.
         * This avoids the need to transpose the Jacobian, making the algorithm more efficient.
         * The update equation takes the form:
         * \theta_{k+1} = \theta_k - J
         * this time being \theta a row vector.
         */
        return this.lossFunction.getJacobian().scaleInplace( -this.learningRate );
    }
    
}
