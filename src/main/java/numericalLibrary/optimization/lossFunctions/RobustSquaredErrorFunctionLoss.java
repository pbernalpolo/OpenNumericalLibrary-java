package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.optimization.robustFunctions.RobustFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L(\theta) = g( || f( x , \theta ) ||^2 )
 * <br>
 * where:
 * <ul>
 *  <li> f is a {@link ModelFunction},
 *  <li> x is the input to the model function,
 *  <li> \theta is the parameter vector,
 *  <li> g is a {@link RobustFunction}.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class RobustSquaredErrorFunctionLoss<T>
    extends SquaredErrorFunctionLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link RobustFunction} used to define the {@link LocallyQuadraticLoss}.
     */
    private RobustFunction robustFunction;
    
    /**
     * Last computed output of the {@link #modelFunction}.
     */
    private Matrix f;
    
    /**
     * Last computed Jacobian from the {@link #modelFunction}.
     */
    private Matrix J;
    
    /**
     * {@link #J} transposed multiplied by the robust weight.
     */
    private Matrix JTW;
    
    /**
     * Squared norm of {@link #f}.
     */
    private double fNormSquared;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link RobustSquaredErrorFunctionLoss}.
     * 
     * @param modelFunction   {@link ModelFunction} used to define the error.
     */
    public RobustSquaredErrorFunctionLoss( ModelFunction<T> modelFunction )
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
        return this.robustFunction.f( this.fNormSquared );
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @see #clean()
     */
    public Matrix getGradient()
    {
        this.clean();
        return this.JTW.multiply( this.f );
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @see #clean()
     */
    public Matrix getGaussNewtonMatrix()
    {
        this.clean();
        return this.JTW.multiply( this.J );
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
        this.fNormSquared = this.f.normFrobeniusSquared();
        double robustWeight = this.robustFunction.f1( this.fNormSquared );
        this.J = this.modelFunction.getJacobian();
        this.JTW = this.J.transpose().scaleInplace( robustWeight );
        // At this point, it is not dirty.
        this.dirtyFlag = false;
    }
    
}
