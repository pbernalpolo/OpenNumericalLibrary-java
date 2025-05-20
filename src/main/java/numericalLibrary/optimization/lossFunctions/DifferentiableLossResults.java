package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.types.MatrixReal;



/**
 * Contains the results of evaluating a {@link DifferentiableLoss}: the cost, and its gradient.
 */
public class DifferentiableLossResults
	extends LossResults
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
	/**
	 * Gradient of the {@link DifferentiableLoss} stored as a column {@link MatrixReal}.
	 */
	private MatrixReal gradient;
	
	
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@link DifferentiableLossResults} object.
	 * 
	 * @param cost	cost obtained from the {@link DifferentiableLoss}.
	 * @param gradient	gradient obtained from the {@link DifferentiableLoss}.
	 */
	public DifferentiableLossResults( double cost , MatrixReal gradient )
	{
		super( cost );
		this.gradient = gradient;
	}
	
	
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the gradient stored in this {@link DifferentiableLossResults}.
	 * 
	 * @return	gradient stored in this {@link DifferentiableLossResults}.
	 */
	public MatrixReal getGradient()
	{
		return this.gradient;
	}
    
}
