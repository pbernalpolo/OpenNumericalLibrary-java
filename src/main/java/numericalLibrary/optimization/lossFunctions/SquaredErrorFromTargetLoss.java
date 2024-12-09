package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L( (x,y) , \theta ) = || f( x , \theta ) - y ||^2
 * <br>
 * where:
 * <ul>
 *  <li> f is a {@link ModelFunction},
 *  <li> x is the input to the {@link ModelFunction} f,
 *  <li> y is the target of the {@link ModelFunction} f,
 *  <li> \theta is the parameter vector.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class SquaredErrorFromTargetLoss<T>
    extends AbstractLocallyQuadraticLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
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
     * {@link #J} transposed.
     */
    private Matrix JT;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link SquaredErrorFromTargetLoss}.
     * 
     * @param modelFunction   {@link ModelFunction} used to define the error.
     */
    public SquaredErrorFromTargetLoss( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
        this.y = modelFunction.getOutput().copy();
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
        return this.fMinusTarget.normFrobeniusSquared();
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @see #clean()
     */
    public Matrix getGradient()
    {
        this.clean();
        return this.JT.multiply( this.fMinusTarget );
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
        Matrix functionOutput = this.modelFunction.getOutput();
        this.fMinusTarget = functionOutput.subtractInplace( this.y );
        this.J = this.modelFunction.getJacobian();
        this.JT = this.J.transpose();
        // At this point, it is not dirty.
        this.dirtyFlag = false;
    }
    
}
