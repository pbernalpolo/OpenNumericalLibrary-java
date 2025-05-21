package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ErrorFunction;
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
 * 
 * @param <T> type of inputs to the {@link ErrorFunction}.
 * 
 * @see RobustMeanSquaredErrorLocallyQuadraticLoss
 */
public class RobustMeanSquaredError<T>
	extends MeanSquaredErrorBase<T>
	implements LocallyQuadraticLoss
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
     * Constructs a {@link RobustMeanSquaredError}.
     * 
     * @param errorFunction		{@link ErrorFunction} used to define the loss.
     * @param robustFunction	{@link RobustFunction} that shapes the error.
     */
	public RobustMeanSquaredError( ErrorFunction<T> errorFunction , RobustFunction robustFunction )
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
			this.cost += this.robustFunction.f( errorFunctionOutput.normFrobeniusSquared() );
		}
		this.divideCostByNumberOfInputs();
		return new LossResults( this.cost );
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public DifferentiableLossResults getDifferentiableLossResults()
	{
        // Initialize cost, and gradient.
		this.initializeCostAndGradient();
		// For each input...
		for ( T input : this.inputList ) {
            // Set the input.
			this.errorFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            MatrixReal errorFunctionOutput = this.errorFunction.getOutput();
            double errorSquared = errorFunctionOutput.normFrobeniusSquared();
            double robustWeight = this.robustFunction.f1( errorSquared );
            MatrixReal J = this.errorFunction.getJacobian();
            // Add contribution to cost, and gradient.
            this.cost += this.robustFunction.f( errorSquared );
            this.gradient.addLeftTransposeTimesRight( J , errorFunctionOutput.scaleInplace( robustWeight ) );
		}
		this.divideCostAndGradientByNumberOfInputs();
		return new DifferentiableLossResults( this.cost , this.gradient );
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public LocallyQuadraticLossResults getLocallyQuadraticLossResults()
	{
		// Create matrix to speed up computations.
		this.errorFunction.setInput( this.inputList.get( 0 ) );
		MatrixReal JW = MatrixReal.emptyWithSizeOf( this.errorFunction.getJacobian() );
        // Initialize cost, gradient, and Gauss-Newton matrix.
		this.initializeCostGradientAndGaussNewtonMatrix();
		// For each input...
		for ( T input : this.inputList ) {
            // Set the input.
			this.errorFunction.setInput( input );
            // Compute quantities involved in the cost, gradient, and Gauss-Newton matrix.
            MatrixReal errorFunctionOutput = this.errorFunction.getOutput();
            double errorSquared = errorFunctionOutput.normFrobeniusSquared();
            double robustWeight = this.robustFunction.f1( errorSquared );
            MatrixReal J = this.errorFunction.getJacobian();
            JW.setTo( J ).scaleInplace( robustWeight );
            // Add contribution to cost, gradient, and Gauss-Newton matrix.
            this.cost += this.robustFunction.f( errorSquared );
            this.gradient.addLeftTransposeTimesRight( JW , errorFunctionOutput );
            this.gaussNewtonMatrix.addLeftTransposeTimesRight( JW , J );
		}
		this.divideCostGradientAndGaussNewtonMatrixByNumberOfInputs();
		return new LocallyQuadraticLossResults( this.cost , this.gradient , this.gaussNewtonMatrix );
	}
	
}
