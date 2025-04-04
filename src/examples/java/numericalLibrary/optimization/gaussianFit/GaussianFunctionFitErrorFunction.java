package numericalLibrary.optimization.gaussianFit;


import numericalLibrary.optimization.ErrorFunction;
import numericalLibrary.types.MatrixReal;



/**
 * Implements the {@link ErrorFunction} used to fit a {@link GaussianFunction} to some data.
 */
public class GaussianFunctionFitErrorFunction
	implements ErrorFunction<GaussianFunctionFitErrorFunctionInput>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
	/**
	 * {@link GaussianFunction} that contains the current parameterization, and that will be used to fit the data.
	 */
	private GaussianFunction f;
	
	/**
	 * Current input to the {@link ErrorFunction}.
	 */
	private GaussianFunctionFitErrorFunctionInput input;
	
    
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@link GaussianFunctionFitErrorFunction}.
	 * 
	 * @param gaussianFunction	{@link GaussianFunction} that will be used to fit the data.
	 */
	public GaussianFunctionFitErrorFunction( GaussianFunction gaussianFunction )
	{
		this.f = gaussianFunction;
	}
	
	
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
	 * {@inheritDoc}
	 */
	public int degreesOfFreedomOfParameterVector()
	{
		return 3;
	}
	
    
	/**
	 * {@inheritDoc}
	 */
    public void shift( MatrixReal deltaParameters )
    {
    	this.f.setHeight( this.f.getHeight() + deltaParameters.entry( 0 , 0 ) );
    	this.f.setCenter( this.f.getCenter() + deltaParameters.entry( 1 , 0 ) );
    	this.f.setStandardDeviation( this.f.getStandardDeviation() + deltaParameters.entry( 2 , 0 ) );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void setInput( GaussianFunctionFitErrorFunctionInput input )
    {
    	this.input = input;
    	this.f.setInput( input.getInput() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal getOutput()
    {
    	return MatrixReal.one( 1 ).scaleInplace( this.f.getOutput() - this.input.getTarget() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal getJacobian()
    {
    	return this.f.getJacobian();
    }
    
}
