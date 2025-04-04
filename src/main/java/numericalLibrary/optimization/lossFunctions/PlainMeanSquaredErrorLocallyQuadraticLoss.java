package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ErrorFunction;
import numericalLibrary.optimization.algorithms.GaussNewtonAlgorithm;
import numericalLibrary.optimization.algorithms.GradientDescentAlgorithm;
import numericalLibrary.optimization.algorithms.LevenbergMarquardtAlgorithm;
import numericalLibrary.types.MatrixReal;



/**
 * Mean Squared Error defined as:
 * <br>
 * L(\theta) = 1/N \sum_i || e( x_i , \theta ) ||^2
 * <br>
 * where:
 * <ul>
 * 	<li> e is an {@link ErrorFunction},
 * 	<li> x_i is the i-th input to the {@link ErrorFunction},
 * 	<li> \theta is the parameter vector,
 *  <li> N is the number of inputs.
 * </ul>
 * <p>
 * This class implements the {@link LocallyQuadraticLoss} interface,
 * so it can be optimized using {@link GaussNewtonAlgorithm} or {@link LevenbergMarquardtAlgorithm}.
 * Although it is also possible to optimize it using {@link GradientDescentAlgorithm}, using {@link PlainMeanSquaredErrorDifferentiableLoss} will be more efficient, as it does not compute the Gauss-Newton matrix.
 * 
 * @param <T> type of inputs to the {@link ErrorFunction}.
 * 
 * @see PlainMeanSquaredErrorDifferentiableLoss
 */
public class PlainMeanSquaredErrorLocallyQuadraticLoss<T>
	extends MeanSquaredErrorLocallyQuadraticLossBase<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@link PlainMeanSquaredErrorLocallyQuadraticLoss}.
	 * 
	 * @param errorFunction		{@link ErrorFunction} used to define the loss.
	 */
	public PlainMeanSquaredErrorLocallyQuadraticLoss( ErrorFunction<T> errorFunction )
	{
		super( errorFunction );
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
		this.initializeCostGradientAndGaussNewtonMatrix();
		// For each input...
		for ( T input : this.inputList ) {
			// Set the input.
			this.errorFunction.setInput( input );
			// Compute quantities involved in the cost and gradient.
			MatrixReal modelFunctionOutput = this.errorFunction.getOutput();
			MatrixReal J = this.errorFunction.getJacobian();
			// Add contribution to cost, gradient, and Gauss-Newton matrix.
			this.cost += modelFunctionOutput.normFrobeniusSquared();
			this.gradient.addLeftTransposeTimesRight( J , modelFunctionOutput );
			this.gaussNewtonMatrix.addLeftTransposeTimesRight( J , J );
		}
		this.divideCostGradientAndGaussNewtonMatrixByNumberOfInputs();
	}
	
}
