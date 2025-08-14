package numericalLibrary.optimization.robustFunctions;



/**
 * The Huber {@link RobustFunction} defined as:
 * f( x ) = x^2/2			if x < k
 *        = k |x| - k^2/2   if x >= k
 * where k is the value after which the function is constant.
 * The derivative is:
 * f'( x ) = x			if x < k
 * 		   = k x/|x|	if x >= k
 * and its weight function
 * w( x ) = 1		if x < k
 * 		  = k/|x|	if x >= k
 * 
 * @see https://arxiv.org/abs/1810.01474
 */
public class HuberRobustFunction
    implements RobustFunction
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
	
	/**
     * Parameter k of the Huber function.
     */
    private double k;
    
    /**
     * Square of parameter k of the Huber function.
     */
    private double kSquared;
    
    /**
     * {@link #k}^2/2.
     */
    private double kSquaredOver2;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link HuberRobustFunction}.
     * 
     * @param kParameter    k parameter of the Huber function.
     */
    public HuberRobustFunction( double kParameter )
    {
    	this.k = kParameter;
    	this.kSquared = kParameter * kParameter;
    	this.kSquaredOver2 = 0.5 * this.kSquared;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double rho( double xSquared )
    {
    	if( xSquared < this.kSquared ) {
    		return xSquared/2.0;
    	} else {
    		return this.k * Math.sqrt( xSquared ) - this.kSquaredOver2;
    	}
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double weight( double xSquared )
    {
    	if( xSquared < this.kSquared ) {
    		return 1.0;
    	} else {
    		return this.k / Math.sqrt( xSquared );
    	}
    }
    
}
