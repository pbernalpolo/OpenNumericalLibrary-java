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
	extends MeanSquaredErrorBase<T>
	implements LocallyQuadraticLoss
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
    
    
	/**
	 * {@inheritDoc}
	 */
	public LossResults getLossResults()
	{
        // Initialize cost.
		this.initializeCost();
		// For each input...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input.
            T input = this.inputList.get( i );
            this.errorFunction.setInput( input );
            // Compute quantities involved in the cost.
            MatrixReal errorFunctionOutput = this.errorFunction.getError();
            double weight = this.weightList.get( i );
            // Add contribution to cost, and gradient.
            this.cost += weight * errorFunctionOutput.normFrobeniusSquared();
        }
        // Note that we do not divide by the number of inputs;
        // the user is responsible for introducing in the weights any scaling factor dependent on the number of inputs.
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
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input.
            T input = this.inputList.get( i );
            this.errorFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            MatrixReal errorFunctionOutput = this.errorFunction.getError();
            MatrixReal J = this.errorFunction.getJacobian();
            double weight = this.weightList.get( i );
            // Add contribution to cost, and gradient.
            this.cost += weight * errorFunctionOutput.normFrobeniusSquared();
            this.gradient.addLeftTransposeTimesRight( J.scaleInplace( weight ) , errorFunctionOutput );
        }
        // Note that we do not divide by the number of inputs;
        // the user is responsible for introducing in the weights any scaling factor dependent on the number of inputs.
		return new DifferentiableLossResults( this.cost , this.gradient );
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public LocallyQuadraticLossResults getLocallyQuadraticLossResults()
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
            // Compute quantities involved in the cost, gradient and Gauss-Newton matrix.
            MatrixReal errorFunctionOutput = this.errorFunction.getError();
            MatrixReal J = this.errorFunction.getJacobian();
            double weight = this.weightList.get( i );
            JW.setTo( J ).scaleInplace( weight );
            // Add contribution to cost, and gradient.
            this.cost += weight * errorFunctionOutput.normFrobeniusSquared();
            this.gradient.addLeftTransposeTimesRight( JW , errorFunctionOutput );
            this.gaussNewtonMatrix.addLeftTransposeTimesRight( JW , J );
        }
        // Note that we do not divide by the number of inputs;
        // the user is responsible for introducing in the weights any scaling factor dependent on the number of inputs.
		return new LocallyQuadraticLossResults( this.cost , this.gradient , this.gaussNewtonMatrix );
	}
	
}
