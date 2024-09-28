package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Represents a pair of values related through a {@link OptimizableFunction}.
 * 
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
     * Constructs a {@link LeastSquaresDataPair}.
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
     * Returns the target of the {@link LeastSquaresDataPair}.
     * 
     * @return  target of the {@link LeastSquaresDataPair}.
     */
    public Matrix getTarget()
    {
        return this.y;
    }
    
    
    /**
     * Returns the input of the {@link LeastSquaresDataPair}.
     * 
     * @return  input of the {@link LeastSquaresDataPair}.
     */
    public T getInput()
    {
        return this.x;
    }
    
}
