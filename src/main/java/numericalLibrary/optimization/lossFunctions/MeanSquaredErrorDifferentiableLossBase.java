package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ErrorFunction;
import numericalLibrary.types.MatrixReal;



/**
 * Base class for every Mean Squared Error implementing {@link DifferentiableLoss}, but not {@link LocallyQuadraticLoss}.
 * 
 * @param <T>   type of inputs to the {@link ErrorFunction} f.
 */
abstract class MeanSquaredErrorDifferentiableLossBase<T>
	extends MeanSquaredErrorBase<T>
	implements DifferentiableLoss
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredErrorDifferentiableLossBase}.
     * 
     * @param errorFunction   {@link ErrorFunction} used to define the loss.
     */
    public MeanSquaredErrorDifferentiableLossBase( ErrorFunction<T> errorFunction )
    {
    	super( errorFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
	protected void resetMatrices()
	{
		int degreesOfFreedom = this.errorFunction.degreesOfFreedomOfParameterVector();
		this.gradient = MatrixReal.empty( degreesOfFreedom , 1 );
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
	
}
