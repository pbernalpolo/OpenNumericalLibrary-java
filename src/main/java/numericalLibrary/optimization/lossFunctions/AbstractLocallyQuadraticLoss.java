package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * Base class for some {@link LocallyQuadraticLoss}.
 * <p>
 * In particular, classes extending this class will consider a model function f( x , \theta ) where:
 * - f is the {@link ModelFunction},
 * - x is the input to the {@link ModelFunction} f,
 * - \theta is the parameter vector.
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public abstract class AbstractLocallyQuadraticLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link ModelFunction} that defines this {@link LocallyQuadraticLoss}.
     */
    protected ModelFunction<T> modelFunction;
    
    /**
     * Flag used to implement the Dirty Flag Optimization Pattern.
     */
    protected boolean dirtyFlag;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link AbstractLocallyQuadraticLoss}.
     * 
     * @param modelFunction   {@link ModelFunction} used to define the error.
     */
    public AbstractLocallyQuadraticLoss( ModelFunction<T> modelFunction )
    {
        this.modelFunction = modelFunction;
        this.dirtyFlag = true;
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
        this.dirtyFlag = true;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getParameters()
    {
        return this.modelFunction.getParameters();
    }
    
    
    /**
     * Sets the input of the {@link ModelFunction}.
     * 
     * @param input     input of the {@link ModelFunction}.
     */
    public void setInput( T input )
    {
        this.modelFunction.setInput( input );
        this.dirtyFlag = true;
    }
    
}
