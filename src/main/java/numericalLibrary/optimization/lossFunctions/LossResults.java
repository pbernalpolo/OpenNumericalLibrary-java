package numericalLibrary.optimization.lossFunctions;



/**
 * Contains the results of evaluating a {@link Loss}: the cost.
 */
public class LossResults
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
	/**
	 * Cost returned by the {@link Loss}.
	 */
    private double cost;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link LossResults} object.
     * 
     * @param cost	cost obtained from the {@link Loss}.
     */
    public LossResults( double cost )
    {
    	this.cost = cost;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the cost stored in this {@link LossResults}.
     * 
     * @return	cost stored in this {@link LossResults}.
     */
    public double getCost()
    {
    	return this.cost;
    }
    
}
