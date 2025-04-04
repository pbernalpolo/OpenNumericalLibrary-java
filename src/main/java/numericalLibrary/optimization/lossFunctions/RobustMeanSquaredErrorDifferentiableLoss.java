package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ErrorFunction;
import numericalLibrary.optimization.algorithms.GaussNewtonAlgorithm;
import numericalLibrary.optimization.algorithms.GradientDescentAlgorithm;
import numericalLibrary.optimization.algorithms.LevenbergMarquardtAlgorithm;
import numericalLibrary.optimization.robustFunctions.RobustFunction;
import numericalLibrary.types.MatrixReal;



/**
 * Mean Squared Error defined as:
 * <br>
 * L(\theta) = 1/N \sum_i g( || e( x_i , \theta ) ||^2 )
 * <br>
 * where:
 * <ul>
 * 	<li> e is an {@link ErrorFunction},
 * 	<li> x_i is the i-th input to the {@link ErrorFunction},
 * 	<li> \theta is the parameter vector,
 *  <li> g is a {@link RobustFunction} that shapes the error,
 *  <li> N is the number of inputs.
 * </ul>
 * <p>
 * This class implements the {@link DifferentiableLoss} interface,
 * so it can be optimized using the {@link GradientDescentAlgorithm}.
 * Calling {@link #getGradient()} with this class is more efficient than doing it with {@link RobustMeanSquaredErrorLocallyQuadraticLoss}.
 * The downside is that this class can not provide a Gauss-Newton matrix, so it is not possible to optimize using {@link GaussNewtonAlgorithm} or {@link LevenbergMarquardtAlgorithm}.
 * 
 * @param <T> type of inputs to the {@link ErrorFunction}.
 * 
 * @see RobustMeanSquaredErrorLocallyQuadraticLoss
 */
public class RobustMeanSquaredErrorDifferentiableLoss<T>
	extends MeanSquaredErrorDifferentiableLossBase<T>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
	
	/**
     * {@link RobustFunction} used to define the {@link LocallyQuadraticLoss}.
     */
    private RobustFunction robustFunction;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
	
    /**
     * Constructs a {@link RobustMeanSquaredErrorDifferentiableLoss}.
     * 
     * @param errorFunction		{@link ErrorFunction} used to define the loss.
     * @param robustFunction	{@link RobustFunction} that shapes the error.
     */
	public RobustMeanSquaredErrorDifferentiableLoss( ErrorFunction<T> errorFunction , RobustFunction robustFunction )
	{
		super( errorFunction );
		this.robustFunction = robustFunction;
	}
	
	
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
	 * Sets the list of inputs used to evaluate the loss.
	 * 
	 * @param inputList		list of inputs used to evaluate the loss.
	 */
	public void setInputList( List<T> inputList )
	{
		this.inputList = inputList;
	}
	
	
	
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * {@inheritDoc}
	 */
	protected void update()
	{
        // Initialize cost, gradient, and Gauss-Newton matrix.
		this.initializeCostAndGradient();
		// For each input...
		for ( T input : this.inputList ) {
            // Set the input.
			this.errorFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            MatrixReal modelFunctionOutput = this.errorFunction.getOutput();
            double errorSquared = modelFunctionOutput.normFrobeniusSquared();
            double robustWeight = this.robustFunction.f1( errorSquared );
            MatrixReal J = this.errorFunction.getJacobian();
            // Add contribution to cost, gradient, and Gauss-Newton matrix.
            this.cost += this.robustFunction.f( errorSquared );
            this.gradient.addLeftTransposeTimesRight( J , modelFunctionOutput.scaleInplace( robustWeight ) );
		}
		this.divideCostAndGradientByNumberOfInputs();
	}
	
}
