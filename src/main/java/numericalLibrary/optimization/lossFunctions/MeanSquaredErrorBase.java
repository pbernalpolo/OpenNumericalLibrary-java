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
    // PROTECTED ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * Resets matrices used by the Mean Squared Error.
	 * This method is called to initialize matrices,
	 * or to reset them after initialization in the event of a change in {@link #errorFunction}.
	 * 
	 * @see #clean()
	 */
	protected abstract void resetMatrices();
	
	
	/**
	 * Updates the internal variables of this {@link DifferentiableLoss}.
	 * This includes {@link #cost}, {@link #gradient} and, in the case of a {@link LocallyQuadraticLoss}, the Gauss-Newton matrix.
	 * 
	 * @see #clean()
	 */
	protected abstract void update();
	
	
	
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
     * Last computed gradient.
     */
    protected MatrixReal gradient;
    
    /**
     * Last computed cost.
     */
    protected double cost;
    
    /**
     * Flag used to implement the Dirty Flag Optimization Pattern.
     */
    private boolean dirtyFlag;
    
    
	
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
        this.dirtyFlag = true;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
	public int degreesOfFreedomOfParameterVector()
	{
		return this.errorFunction.degreesOfFreedomOfParameterVector();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void shift( MatrixReal deltaParameters )
	{
		this.errorFunction.shift( deltaParameters );
		this.dirtyFlag = true;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public double getCost()
	{
		this.clean();
		return this.cost;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public MatrixReal getGradient()
	{
		this.clean();
		return this.gradient;
	}
	
	
	/**
     * Cleans in the sense of the Dirty Flag Optimization Pattern.
     * <p>
     * Resets the matrices such as {@link #gradient} with a new size if it is required after a change in {@link #errorFunction}.
     * It also updates the stored cost, gradient and, in the case of a {@link LocallyQuadraticLoss}, the Gauss-Newton matrix.
     */
    protected void clean()
	{
		if(  this.gradient == null  ||  this.gradient.rows() != this.errorFunction.degreesOfFreedomOfParameterVector()  ) {
			this.resetMatrices();
			this.dirtyFlag = true;
		}
		if( !this.dirtyFlag ) {
			return;
		}
		this.update();
		this.dirtyFlag = false;
	}
	
}
