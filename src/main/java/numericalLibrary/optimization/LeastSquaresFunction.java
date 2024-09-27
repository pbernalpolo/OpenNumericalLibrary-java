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
public class LeastSquaresFunction<T>
    implements OptimizableFunction<LeastSquaresDataPair<T>>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    
    private OptimizableFunction<T> modelFunction;
    
    
    private Matrix target;
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    public LeastSquaresFunction( OptimizableFunction<T> optimizableFunction )
    {
        this.modelFunction = optimizableFunction;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public void setParameters( Matrix theta )
    {
        this.modelFunction.setParameters( theta );
    }
    
    
    public Matrix getParameters()
    {
        return this.modelFunction.getParameters();
    }
    
    
    public void setInput( LeastSquaresDataPair<T> xy )
    {
        this.modelFunction.setInput( xy.getInput() );
        this.target = xy.getTarget();
    }
    
    
    public Matrix getOutput()
    {
        return this.modelFunction.getOutput().subtract( this.target );
    }
    
    
    public Matrix getJacobian()
    {
        return this.modelFunction.getJacobian();
    }
    
}
