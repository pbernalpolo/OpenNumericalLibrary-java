package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ErrorFunction;
import numericalLibrary.optimization.robustFunctions.RobustFunction;
import numericalLibrary.types.MatrixReal;



/**
 * Base class for every Mean Squared Error {@link Loss}.
 * <p>
 * The Mean Squared Error has the general form:
 * <br>
 * L(\theta) = 1/N \sum_i w_i g( || e( x_i , \theta ) ||^2 )
 * <br>
 * where:
 * <ul>
 *  <li> e is the {@link ErrorFunction} that describes the error,
 *  <li> x_i is the i-th input to the {@link ErrorFunction},
 *  <li> \theta is the parameter vector,
 *  <li> g can be a {@link RobustFunction},
 *  <li> w_i is the weight that measures the importance of the error associated to x_i,
 *  <li> N is the number of inputs.
 * </ul>
 * This base class contains the commonalities to all Mean Squared Error flavors.
 * 
 * @param <T>	type of inputs to the {@link ErrorFunction} to be optimized.
 */
abstract class MeanSquaredErrorBase<T>
	implements DifferentiableLoss
{
    ////////////////////////////////////////////////////////////////
	// PROTECTED VARIABLES
	////////////////////////////////////////////////////////////////
	
	/**
     * {@link ErrorFunction} that defines this {@link DifferentiableLoss}.
     */
    protected ErrorFunction<T> errorFunction;
    
    /**
     * List of inputs to the {@link ErrorFunction}.
     */
    protected List<T> inputList;
    
    /**
     * Used to accumulate the contribution to the gradient from each input.
     */
    protected MatrixReal gradient;
    
    /**
     * Used to accumulate the contribution to the Gauss-Newton matrix from each input.
     */
    protected MatrixReal gaussNewtonMatrix;
    
	/**
     * Used to accumulate the contribution to the cost from each input.
     */
    protected double cost;
    
    
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredErrorBase}.
     * 
     * @param errorFunction   {@link ErrorFunction} used to define this {@link DifferentiableLoss}.
     */
    public MeanSquaredErrorBase( ErrorFunction<T> errorFunction )
    {
    	this.errorFunction = errorFunction;
    	int degreesOfFreedom = errorFunction.degreesOfFreedom();
    	this.gradient = MatrixReal.empty( degreesOfFreedom , 1 );
    	this.gaussNewtonMatrix = MatrixReal.empty( degreesOfFreedom , degreesOfFreedom );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public int degreesOfFreedom()
	{
		return this.errorFunction.degreesOfFreedom();
	}
	
	
    /**
     * {@inheritDoc}
     */
	public void shift( MatrixReal deltaParameters )
	{
		this.errorFunction.shift( deltaParameters );
	}
	
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
	 * Sets the cost to zero.
	 */
    protected void initializeCost()
    {
    	this.cost = 0.0;
    }
    
    
	/**
	 * Sets the cost and the gradient to zero.
	 */
	protected void initializeCostAndGradient()
	{
		this.cost = 0.0;
		this.gradient.setToZero();
	}
	
	
	/**
	 * Sets the cost, the gradient, and the Gauss-Newton matrix to zero.
	 */
	protected void initializeCostGradientAndGaussNewtonMatrix()
	{
		this.cost = 0.0;
		this.gradient.setToZero();
		this.gaussNewtonMatrix.setToZero();
	}
	
	
	/**
	 * Divides the cost by the size of the input list.
	 * <p>
	 * The operation is done in-place so, after calling this method, the cost will be scaled with the inverse of the number of inputs.
	 */
	protected void divideCostByNumberOfInputs()
	{
		this.cost /= this.inputList.size();
	}
	
	
	/**
	 * Divides the cost and the gradient by the size of the input list.
	 * <p>
	 * The operation is done in-place so, after calling this method, the cost and the gradient will be scaled with the inverse of the number of inputs.
	 */
	protected void divideCostAndGradientByNumberOfInputs()
	{
		double oneOverNumberOfInputs = 1.0 / this.inputList.size();
		this.cost *= oneOverNumberOfInputs;
		this.gradient.scaleInplace( oneOverNumberOfInputs );
	}
	
	
	/**
	 * Divides the cost, the gradient, and the Gauss-Newton matrix by the size of the input list.
	 * <p>
	 * The operation is done in-place so, after calling this method, the cost, the gradient, and the Gauss-Newton matrix will be scaled with the inverse of the number of inputs.
	 */
	protected void divideCostGradientAndGaussNewtonMatrixByNumberOfInputs()
	{
		double oneOverNumberOfInputs = 1.0 / this.inputList.size();
		this.cost *= oneOverNumberOfInputs;
		this.gradient.scaleInplace( oneOverNumberOfInputs );
		this.gaussNewtonMatrix.scaleInplace( oneOverNumberOfInputs );
	}
	
}
