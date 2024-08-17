package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Represents a pair of values related through a {@link LevenbergMarquardtModelFunction}.
 * <p>
 * The process of defining the concrete {@link LevenbergMarquardtEmpiricalPair} helps the user to think about a natural definition of the concrete {@link LevenbergMarquardtModelFunction}.
 * It is advised to start thinking about the {@link LevenbergMarquardtEmpiricalPair} before thinking about the {@link LevenbergMarquardtModelFunction}.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm</a>
 * @see LevenbergMarquardtModelFunction
 */
public class LevenbergMarquardtEmpiricalPair<T>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Target output of the {@link LevenbergMarquardtModelFunction} when {@link #x} is inputed.
     */
    private Matrix y;
    
    
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
    public LevenbergMarquardtEmpiricalPair( Matrix target , T sample )
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
    public Matrix getY()
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
