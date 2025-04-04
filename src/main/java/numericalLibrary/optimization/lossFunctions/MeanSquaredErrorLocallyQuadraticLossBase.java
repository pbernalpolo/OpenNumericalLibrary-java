package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ErrorFunction;
import numericalLibrary.types.MatrixReal;



/**
 * Base class for every Mean Squared Error implementing the {@link LocallyQuadraticLoss} interface.
 * 
 * @param <T>   type of inputs to the {@link ErrorFunction}.
 */
abstract class MeanSquaredErrorLocallyQuadraticLossBase<T>
	extends MeanSquaredErrorBase<T>
    implements LocallyQuadraticLoss
{
	////////////////////////////////////////////////////////////////
	// PROTECTED VARIABLES
	////////////////////////////////////////////////////////////////
	
    /**
     * Last computed Gauss-Newton matrix.
     */
    protected MatrixReal gaussNewtonMatrix;
    
	
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredErrorLocallyQuadraticLossBase}.
     * 
     * @param modelFunction   {@link ErrorFunction} used to define the loss.
     */
    public MeanSquaredErrorLocallyQuadraticLossBase( ErrorFunction<T> modelFunction )
    {
    	super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
	public MatrixReal getGaussNewtonMatrix()
	{
		this.clean();
		return this.gaussNewtonMatrix;
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
		this.gaussNewtonMatrix = MatrixReal.empty( degreesOfFreedom , degreesOfFreedom );
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
