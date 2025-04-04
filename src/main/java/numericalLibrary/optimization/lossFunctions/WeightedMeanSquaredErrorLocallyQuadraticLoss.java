package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ErrorFunction;
import numericalLibrary.types.MatrixReal;



/**
 * Mean Squared Error defined as:
 * <br>
 * L(\theta) = \sum_i w_i || e( x_i , \theta ) ||^2
 * <br>
 * where:
 * <ul>
 * 	<li> e is an {@link ErrorFunction},
 * 	<li> x_i is the i-th input to the {@link ErrorFunction},
 * 	<li> \theta is the parameter vector,
 *  <li> w_i is the weight that measures the importance of the error associated to x_i.
 * </ul>
 * 
 * @param <T> type of inputs to the {@link ErrorFunction}.
 */
public class WeightedMeanSquaredErrorLocallyQuadraticLoss<T>
	extends MeanSquaredErrorLocallyQuadraticLossBase<T>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
	
	/**
     * List of weights that define the importance of each input in {@link #inputList}.
     */
    private List<Double> weightList;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
	
    /**
     * Constructs a {@link WeightedMeanSquaredErrorLocallyQuadraticLoss}.
     * 
     * @param errorFunction		{@link ErrorFunction} used to define the loss.
     */
    public WeightedMeanSquaredErrorLocallyQuadraticLoss( ErrorFunction<T> errorFunction )
	{
		super( errorFunction );
	}
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
	 * Sets the list of inputs used to evaluate the loss, and their associated weights.
	 * 
	 * @param inputList		list of inputs used to evaluate the loss.
     * @param weightList	list of weights associated to each input.
     */
    public void setInputListAndWeightList( List<T> inputList , List<Double> weightList )
    {
    	this.inputList = inputList;
    	this.weightList = weightList;
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
		this.errorFunction.setInput( this.inputList.get(0) );
        MatrixReal JW = MatrixReal.emptyWithSizeOf( this.errorFunction.getJacobian() );
        // Initialize cost, gradient, and Gauss-Newton matrix.
		this.initializeCostGradientAndGaussNewtonMatrix();
		// For each input...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input.
            T input = this.inputList.get( i );
            this.errorFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            MatrixReal modelFunctionOutput = this.errorFunction.getOutput();
            MatrixReal J = this.errorFunction.getJacobian();
            double weight = this.weightList.get( i );
            JW.setTo( J ).scaleInplace( weight );
            // Add contribution to cost, and gradient.
            this.cost += weight * modelFunctionOutput.normFrobeniusSquared();
            this.gradient.addLeftTransposeTimesRight( JW , modelFunctionOutput );
            this.gaussNewtonMatrix.addLeftTransposeTimesRight( JW , J );
        }
        // Note that we do not divide by the number of inputs;
        // the user is responsible for introducing in the weights any scaling factor dependent on the number of inputs.
	}
	
}
