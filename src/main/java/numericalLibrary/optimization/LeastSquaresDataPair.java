package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Represents a pair of values related through a {@link OptimizableFunction}.
 * <p>
 * The process of defining the concrete {@link LevenbergMarquardtEmpiricalPair} helps the user to think about a natural definition of the concrete {@link OptimizableFunction}.
 * It is advised to start thinking about the {@link LevenbergMarquardtEmpiricalPair} before thinking about the {@link OptimizableFunction}.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm</a>
 * @see OptimizableFunction
 */
public class LeastSquaresDataPair<T>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Target output of the {@link OptimizableFunction} when {@link #x} is inputed.
     */
    private Matrix y;
    
    
    /**
     * Input to the {@link OptimizableFunction} for which the target {@link #y} is expected.
     */
    private T x;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link LevenbergMarquardtEmpiricalPair}.
     * 
     * @param target    target output of the {@link OptimizableFunction}.
     * @param input    input to the {@link OptimizableFunction}.
     */
    public LeastSquaresDataPair( Matrix target , T input )
    {
        this.y = target;
        this.x = input;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the target of the {@link LevenbergMarquardtEmpiricalPair}.
     * 
     * @return  target of the {@link LevenbergMarquardtEmpiricalPair}.
     */
    public Matrix getTarget()
    {
        return this.y;
    }
    
    
    /**
     * Returns the input of the {@link LevenbergMarquardtEmpiricalPair}.
     * 
     * @return  input of the {@link LevenbergMarquardtEmpiricalPair}.
     */
    public T getInput()
    {
        return this.x;
    }
    
}
