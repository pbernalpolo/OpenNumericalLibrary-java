package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * {@link LeastSquaresFunction} is the {@link OptimizableFunction} to be used when using an {@link IterativeOptimizationAlgorithm} to solve a least squares problem.
 * <p>
 * In particular, the {@link OptimizableFunction} is defined as:
 * f( x_i , theta ) = f_model( x_i , theta ) - y_i
 * so by optimizing the squared norm of f( x_i , theta ) we obtain the parameter vector theta that best approximates the relation
 * f_model( x_i , theta ) = y_i
 * 
 * @see OptimizableFunction
 */
public class LeastSquaresFunction<T>
    implements OptimizableFunction<LeastSquaresDataPair<T>>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Function f_model( x_i , theta ) that models the relation between the input data and the target data.
     */
    private OptimizableFunction<T> modelFunction;
    
    /**
     * Target output of the {@link #modelFunction}.
     */
    private Matrix target;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link LeastSquaresFunction}.
     * 
     * @param optimizableFunction   {@link OptimizableFunction} that models the relation between input data and target data.
     */
    public LeastSquaresFunction( OptimizableFunction<T> optimizableFunction )
    {
        this.modelFunction = optimizableFunction;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public void setParameters( Matrix theta )
    {
        this.modelFunction.setParameters( theta );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getParameters()
    {
        return this.modelFunction.getParameters();
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void setInput( LeastSquaresDataPair<T> xy )
    {
        this.modelFunction.setInput( xy.getInput() );
        this.target = xy.getTarget();
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getOutput()
    {
        return this.modelFunction.getOutput().subtract( this.target );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getJacobian()
    {
        return this.modelFunction.getJacobian();
    }
    
}
