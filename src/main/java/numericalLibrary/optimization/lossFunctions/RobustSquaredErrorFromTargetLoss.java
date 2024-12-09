package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.optimization.robustFunctions.RobustFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L(\theta) = g( || f( x , \theta ) - y ||^2 )
 * <br>
 * where:
 * <ul>
 *  <li> f is a {@link ModelFunction},
 *  <li> x is the input to the model function,
 *  <li> \theta is the parameter vector,
 *  <li> y is the target of the model function,
 *  <li> g is a {@link RobustFunction}.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class RobustSquaredErrorFromTargetLoss<T>
    extends SquaredErrorFromTargetLoss<T>
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
     * Target of the {@link ModelFunction}.
     */
    private Matrix y;
    
    /**
     * Last computed error between output of the {@link #modelFunction} and the target.
     */
    private Matrix fMinusTarget;
    
    /**
     * Last computed Jacobian from the {@link #modelFunction}.
     */
    private Matrix J;
    
    /**
     * {@link #J} transposed multiplied by the robust weight.
     */
    private Matrix JTW;
    
    /**
     * Squared norm of {@link #fMinusTarget}.
     */
    private double fMinusTargetNormSquared;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link RobustSquaredErrorFromTargetLoss}.
     * 
     * @param modelFunction   {@link ModelFunction} used to define the error.
     */
    public RobustSquaredErrorFromTargetLoss( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the target of the {@link ModelFunction}.
     * 
     * @param target    target of the {@link ModelFunction}.
     */
    public void setTarget( Matrix target )
    {
        this.y = target;
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @see #clean()
     */
    public double getCost()
    {
        this.clean();
        return this.robustFunction.f( this.fMinusTargetNormSquared );
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @see #clean()
     */
    public Matrix getGradient()
    {
        this.clean();
        return this.JTW.multiply( this.fMinusTarget );
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
        Matrix functionOutput = this.modelFunction.getOutput();
        this.fMinusTarget = functionOutput.subtractInplace( this.y );
        this.fMinusTargetNormSquared = this.fMinusTarget.normFrobeniusSquared();
        double robustWeight = this.robustFunction.f1( this.fMinusTargetNormSquared );
        this.J = this.modelFunction.getJacobian();
        this.JTW = this.J.transpose().scaleInplace( robustWeight );
        // At this point, it is not dirty.
        this.dirtyFlag = false;
    }
    
}
