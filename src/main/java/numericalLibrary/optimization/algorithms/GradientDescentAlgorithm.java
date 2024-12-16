package numericalLibrary.optimization.algorithms;


import numericalLibrary.optimization.lossFunctions.DifferentiableLoss;
import numericalLibrary.types.Matrix;



/**
 * Implements the Gradient Descent algorithm.
 * <p>
 * The Gradient Descent algorithm aims to minimize the loss function:
 * <br>
 * L( \theta ) = F( \theta )
 * <br>
 * where:
 * <ul>
 *  <li> F is a {@link DifferentiableLoss},
 *  <li> \theta is the parameter vector, represented as a column {@link Matrix}.
 * </ul>
 * <p>
 * The gradient descent parameter update step takes the form:
 * <br>
 * \theta_{k+1} = \theta_k - \gamma g
 * <br>
 * where:
 * <ul>
 *  <li> g is the gradeint of F,
 *  <li> gamma is the learning rate.
 * </ul>
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
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The increment in the parameter space is computed as:
     * <br>
     * \theta_{k+1} = \theta_k - gamma * ( d f / d theta )
     */
    public Matrix getDeltaParameters()
    {
        return this.lossFunction.getGradient().scaleInplace( -this.learningRate );
    }
    
}
