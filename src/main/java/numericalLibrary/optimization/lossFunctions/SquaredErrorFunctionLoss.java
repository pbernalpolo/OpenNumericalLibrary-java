package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L( x , \theta ) = || f( x , \theta ) ||^2
 * <br>
 * where:
 * <ul>
 *  <li> f is a {@link ModelFunction},
 *  <li> x is the input to the {@link ModelFunction} f,
 *  <li> \theta is the parameter vector.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class SquaredErrorFunctionLoss<T>
    extends AbstractLocallyQuadraticLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Last computed output from the {@link #modelFunction}.
     */
    private Matrix f;
    
    /**
     * Last computed Jacobian from the {@link #modelFunction}.
     */
    private Matrix J;
    
    /**
     * {@link #J} transposed.
     */
    private Matrix JT;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link SquaredErrorFunctionLoss}.
     * 
     * @param modelFunction   {@link ModelFunction} used to define the error.
     */
    public SquaredErrorFunctionLoss( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * 
     * @see #clean()
     */
    public double getCost()
    {
        this.clean();
        return this.f.normFrobeniusSquared();
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @see #clean()
     */
    public Matrix getGradient()
    {
        this.clean();
        return this.JT.multiply( this.f );
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @see #clean()
     */
    public Matrix getGaussNewtonMatrix()
    {
        this.clean();
        return this.JT.multiply( this.J );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Implementation of the Dirty Flag Optimization Pattern.
     */
    private void clean()
    {
        // If not dirty, then there is nothing new to compute.
        if( !this.dirtyFlag ) {
            return;
        }
        // Update variables used to compute cost, gradient, and Gauss-Newton matrix.
        this.f = this.modelFunction.getOutput();
        this.J = this.modelFunction.getJacobian();
        this.JT = this.J.transpose();
        // At this point, it is not dirty.
        this.dirtyFlag = false;
    }
    
}
