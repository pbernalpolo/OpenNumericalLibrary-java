package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.types.MatrixReal;



/**
 * Contains the results of evaluating a {@link LocallyQuadraticLoss}: the cost, its gradient, and its Gauss-Newton matrix.
 */
public class LocallyQuadraticLossResults
	extends DifferentiableLossResults
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
	/**
	 * Gauss-Newton matrix obtained from the {@link LocallyQuadraticLoss}.
	 */
	private MatrixReal gaussNewtonMatrix;
    
	
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@link LocallyQuadraticLossResults} object.
	 * 
	 * @param cost	cost obtained from the {@link LocallyQuadraticLoss}.
	 * @param gradient	gradient obtained from the {@link LocallyQuadraticLoss}.
	 * @param gaussNewtonMatrix		Gauss-Newton matrix obtained from the {@link LocallyQuadraticLoss}.
	 */
	public LocallyQuadraticLossResults( double cost , MatrixReal gradient , MatrixReal gaussNewtonMatrix )
	{
		super( cost , gradient );
		this.gaussNewtonMatrix = gaussNewtonMatrix;
	}
	
	
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the Gauss-Newton matrix stored in this {@link LocallyQuadraticLossResults}.
	 * 
	 * @return	Gauss-Newton matrix stored in this {@link LocallyQuadraticLossResults}.
	 */
	public MatrixReal getGaussNewtonMatrix()
	{
		return this.gaussNewtonMatrix;
	}
	
}
