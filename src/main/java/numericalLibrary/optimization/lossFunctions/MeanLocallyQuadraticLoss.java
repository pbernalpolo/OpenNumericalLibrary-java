package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * Base class for all {@link LocallyQuadraticLoss} defined as:
 * L(\theta) = \sum_i F( x_i , \theta )
 * where:
 * - F is a locally quadratic loss function,
 * - x_i is the i-th input to F,
 * - \theta is the parameter vector.
 * 
 * @param <T>   type of inputs to the locally quadratic loss function F.
 */
public abstract class MeanLocallyQuadraticLoss<T>
    extends MeanDifferentiableLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PROTECTED ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * It also adds the contribution to the Gauss-Newton matrix.
     */
    protected abstract void cleanItem( int index );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Last Gauss-Newton matrix computed from this {@link LocallyQuadraticLoss}.
     * 
     * @see #clean()
     * @see #cleanItem(int)
     */
    protected Matrix JTWJ;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanLocallyQuadraticLoss}.
     * 
     * @param modelFunction   {@link ModelFunction} used to define the locally quadratic loss function F( x_i , \theta ).
     */
    public MeanLocallyQuadraticLoss( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
        int numberOfParameters = modelFunction.getParameters().cols();
        this.JTWJ = Matrix.empty( numberOfParameters , numberOfParameters );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double getCost()
    {
        // It is necessary to override this method so that this.clean() calls the clean method in this class.
        this.clean();
        return this.cost;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getJacobian()
    {
        // It is necessary to override this method so that this.clean() calls the clean method in this class.
        this.clean();
        return this.J;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGaussNewtonMatrix()
    {
        // It is necessary to override this method so that this.clean() calls the clean method in this class.
        this.clean();
        return this.JTWJ;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Implementation of the Dirty Flag Optimization Pattern.
     */
    private void clean()
    {
        if( this.dirtyFlag ) {
            this.J.setToZero();
            this.JTWJ.setToZero();
            this.cost = 0.0;
            for( int i=0; i<this.inputList.size(); i++ ) {
                this.cleanItem( i );
            }
            this.dirtyFlag = false;
        }
    }
    
}
