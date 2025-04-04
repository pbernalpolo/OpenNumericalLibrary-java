package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ErrorFunction;
import numericalLibrary.optimization.algorithms.GaussNewtonAlgorithm;
import numericalLibrary.optimization.algorithms.GradientDescentAlgorithm;
import numericalLibrary.optimization.algorithms.LevenbergMarquardtAlgorithm;
import numericalLibrary.optimization.robustFunctions.IdentityRobustFunction;
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
 * This class implements the {@link LocallyQuadraticLoss} interface,
 * so it can be optimized using {@link GaussNewtonAlgorithm} or {@link LevenbergMarquardtAlgorithm}.
 * Although it is also possible to optimize it using {@link GradientDescentAlgorithm}, using {@link RobustMeanSquaredErrorDifferentiableLoss} will be more efficient, as it does not compute the Gauss-Newton matrix.
 * 
 * @param <T> type of inputs to the {@link ErrorFunction}.
 * 
 * @see RobustMeanSquaredErrorDifferentiableLoss
 */
public class RobustMeanSquaredErrorLocallyQuadraticLoss<T>
	extends MeanSquaredErrorLocallyQuadraticLossBase<T>
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
     * Constructs a {@link RobustMeanSquaredErrorLocallyQuadraticLoss}.
     * 
     * @param errorFunction		{@link ErrorFunction} used to define the loss.
     */
	public RobustMeanSquaredErrorLocallyQuadraticLoss( ErrorFunction<T> errorFunction )
	{
		super( errorFunction );
		this.robustFunction = new IdentityRobustFunction();
	}
	
	
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
	 * Sets the {@link RobustFunction} used to shape the error.
	 * 
     * @param robustFunction	{@link RobustFunction} that shapes the error.
	 */
	public void setRobustFunction( RobustFunction robustFunction )
	{
		this.robustFunction = robustFunction;
	}
    
	
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
		// Create matrix to speed up computations.
		this.errorFunction.setInput( this.inputList.get( 0 ) );
		MatrixReal JW = MatrixReal.emptyWithSizeOf( this.errorFunction.getJacobian() );
        // Initialize cost, gradient, and Gauss-Newton matrix.
		this.initializeCostGradientAndGaussNewtonMatrix();
		// For each input...
		for ( T input : this.inputList ) {
            // Set the input.
			this.errorFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            MatrixReal modelFunctionOutput = this.errorFunction.getOutput();
            double errorSquared = modelFunctionOutput.normFrobeniusSquared();
            double robustWeight = this.robustFunction.f1( errorSquared );
            MatrixReal J = this.errorFunction.getJacobian();
            JW.setTo( J ).scaleInplace( robustWeight );
            // Add contribution to cost, gradient, and Gauss-Newton matrix.
            this.cost += this.robustFunction.f( errorSquared );
            this.gradient.addLeftTransposeTimesRight( JW , modelFunctionOutput );
            this.gaussNewtonMatrix.addLeftTransposeTimesRight( JW , J );
		}
		this.divideCostGradientAndGaussNewtonMatrixByNumberOfInputs();
	}
	
}
