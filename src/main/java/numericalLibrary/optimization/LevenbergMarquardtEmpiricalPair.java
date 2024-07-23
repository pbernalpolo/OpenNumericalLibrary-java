package numericalLibrary.optimization;



/**
 * Represents a pair of values related through a {@link LevenbergMarquardtModelFunction}.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm</a>
 */
public class LevenbergMarquardtEmpiricalPair<T>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Target output of the {@link LevenbergMarquardtModelFunction} when {@link #x} is inputed.
     */
    private double y;
    
    
    /**
     * Input to the {@link LevenbergMarquardtModelFunction} for which the target {@link #y} is expected.
     */
    private T x;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link LevenbergMarquardtEmpiricalPair}.
     * 
     * @param target    target output of the {@link LevenbergMarquardtModelFunction}.
     * @param sample    input to the {@link LevenbergMarquardtModelFunction}.
     */
    public LevenbergMarquardtEmpiricalPair( double target , T sample )
    {
        this.y = target;
        this.x = sample;
    }



    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the target of the {@link LevenbergMarquardtEmpiricalPair}.
     * 
     * @return  target of the {@link LevenbergMarquardtEmpiricalPair}.
     */
    public double getY()
    {
        return this.y;
    }
    
    
    /**
     * Returns the input of the {@link LevenbergMarquardtEmpiricalPair}.
     * 
     * @return  input of the {@link LevenbergMarquardtEmpiricalPair}.
     */
    public T getX()
    {
        return this.x;
    }
    
}
