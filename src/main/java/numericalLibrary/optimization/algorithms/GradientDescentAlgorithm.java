package numericalLibrary.optimization.algorithms;


import numericalLibrary.optimization.lossFunctions.DifferentiableLoss;
import numericalLibrary.optimization.lossFunctions.DifferentiableLossResults;
import numericalLibrary.types.MatrixReal;



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
 *  <li> \theta is the parameter vector, represented as a column {@link MatrixReal}.
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
    implements IterativeOptimizationAlgorithm<DifferentiableLoss>
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
    public GradientDescentAlgorithm()
    {
        this.learningRate = 1.0e-3;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
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
    public MatrixReal getDeltaParameters( DifferentiableLoss lossFunction )
    {
    	DifferentiableLossResults results = lossFunction.getDifferentiableLossResults();
    	MatrixReal gradient = results.getGradient();
        return gradient.scaleInplace( -this.learningRate );
    }
    
}
