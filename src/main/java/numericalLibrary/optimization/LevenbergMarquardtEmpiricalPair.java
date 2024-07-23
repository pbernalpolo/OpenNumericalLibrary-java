package numericalLibrary.optimization;



/**
 * Represents a pair of values related through a function.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm</a>
 */
public class LevenbergMarquardtEmpiricalPair<T>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    private double y;
    
    private T x;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    public LevenbergMarquardtEmpiricalPair( double target , T sample )
    {
        this.y = target;
        this.x = sample;
    }



    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public double getY()
    {
        return this.y;
    }
    
    
    public T getX()
    {
        return this.x;
    }
    
}
