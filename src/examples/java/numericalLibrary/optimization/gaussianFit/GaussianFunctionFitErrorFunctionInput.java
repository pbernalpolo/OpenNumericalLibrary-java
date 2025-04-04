package numericalLibrary.optimization.gaussianFit;



/**
 * Contains the input to a {@link GaussianFunctionFitErrorFunction}.
 * <p>
 * In particular, it holds
 * <ul>
 * 	<li> input to the {@link GaussianFunction},
 * 	<li> target value for the {@link GaussianFunction} given the input.
 * </ul>
 * Both together constitute the input to the {@link GaussianFunctionFitErrorFunction}.
 */
public class GaussianFunctionFitErrorFunctionInput
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
	/**
	 * Input to the {@link GaussianFunction}.
	 */
	private double input;
	
	/**
	 * Target for the {@link GaussianFunction}.
	 */
	private double target;
	
    
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@link GaussianFunctionFitErrorFunctionInput}.
	 * 
	 * @param input		input to the {@link GaussianFunction}.
	 * @param target	target for the {@link GaussianFunction} given the input.
	 */
	public GaussianFunctionFitErrorFunctionInput( double input , double target )
	{
		this.input = input;
		this.target = target;
	}
	
	
	
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
	 * Returns the input to the {@link GaussianFunction}.
	 * 
	 * @return	input to the {@link GaussianFunction}.
	 */
    public double getInput()
    {
    	return this.input;
    }
    
    
    /**
     * Returns the target for the {@link GaussianFunction} given the input returned by {@link #getInput()}.
     * 
     * @return	target for the {@link GaussianFunction} given the input returned by {@link #getInput()}.
     */
    public double getTarget()
    {
    	return this.target;
    }
    
}
