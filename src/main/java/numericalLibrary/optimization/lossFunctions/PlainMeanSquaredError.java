package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ErrorFunction;
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
 * 
 * @param <T> type of inputs to the {@link ErrorFunction}.
 * 
 * @see PlainMeanSquaredErrorLocallyQuadraticLoss
 */
public class PlainMeanSquaredError<T>
	extends MeanSquaredErrorBase<T>
	implements LocallyQuadraticLoss
{
	////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@link PlainMeanSquaredError}.
	 * 
	 * @param errorFunction		{@link ErrorFunction} used to define the loss.
	 */
	public PlainMeanSquaredError( ErrorFunction<T> errorFunction )
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
	
	
	/**
	 * {@inheritDoc}
	 */
	public LossResults getLossResults()
	{
		// Initialize cost.
		this.initializeCost();
		// For each input...
		for ( T input : this.inputList ) {
			// Set the input.
			this.errorFunction.setInput( input );
			// Compute quantities involved in the cost and gradient.
			MatrixReal errorFunctionOutput = this.errorFunction.getOutput();
			// Add contribution to cost.
			this.cost += errorFunctionOutput.normFrobeniusSquared();
		}
		this.divideCostByNumberOfInputs();
		return new LossResults( this.cost );
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public DifferentiableLossResults getDifferentiableLossResults()
	{
		// Initialize cost and  gradient.
		this.initializeCostAndGradient();
		// For each input...
		for ( T input : this.inputList ) {
			// Set the input.
			this.errorFunction.setInput( input );
			// Compute quantities involved in the cost and gradient.
			MatrixReal errorFunctionOutput = this.errorFunction.getOutput();
			MatrixReal J = this.errorFunction.getJacobian();
			// Add contribution to cost and gradient.
			this.cost += errorFunctionOutput.normFrobeniusSquared();
			this.gradient.addLeftTransposeTimesRight( J , errorFunctionOutput );
		}
		this.divideCostAndGradientByNumberOfInputs();
		return new DifferentiableLossResults( this.cost , this.gradient );
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public LocallyQuadraticLossResults getLocallyQuadraticLossResults()
	{
		// Initialize cost, gradient, and Gauss-Newton matrix.
		this.initializeCostGradientAndGaussNewtonMatrix();
		// For each input...
		for ( T input : this.inputList ) {
			// Set the input.
			this.errorFunction.setInput( input );
			// Compute quantities involved in the cost and gradient.
			MatrixReal errorFunctionOutput = this.errorFunction.getOutput();
			MatrixReal J = this.errorFunction.getJacobian();
			// Add contribution to cost, gradient, and Gauss-Newton matrix.
			this.cost += errorFunctionOutput.normFrobeniusSquared();
			this.gradient.addLeftTransposeTimesRight( J , errorFunctionOutput );
			this.gaussNewtonMatrix.addLeftTransposeTimesRight( J , J );
		}
		this.divideCostGradientAndGaussNewtonMatrixByNumberOfInputs();
		return new LocallyQuadraticLossResults( this.cost , this.gradient , this.gaussNewtonMatrix );
	}
	
}
