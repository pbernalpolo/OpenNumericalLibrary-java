package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * Base class for {@link LocallyQuadraticLoss}s that consider a {@link ModelFunction} in their definition.
 * 
 * @param <T>   type of inputs to the {@link ModelFunction}.
 */
abstract class AbstractLocallyQuadraticLossDefinedWithModelFunction<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link ModelFunction} that defines this {@link LocallyQuadraticLoss}.
     */
    public ModelFunction<T> modelFunction;
    
    /**
     * Last computed cost.
     */
    public double cost;
    
    /**
     * Last computed gradient.
     */
    public Matrix gradient;
    
    /**
     * Last computed Gauss-Newton matrix.
     */
    public Matrix gaussNewtonMatrix;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link AbstractLocallyQuadraticLossDefinedWithModelFunction}.
     * 
     * @param modelFunction   {@link ModelFunction} used in the {@link AbstractLocallyQuadraticLossDefinedWithModelFunction} definition.
     */
    public AbstractLocallyQuadraticLossDefinedWithModelFunction( ModelFunction<T> modelFunction )
    {
        Matrix theta = modelFunction.getParameters();
        if( theta.cols() != 1 ) {
            throw new IllegalArgumentException( "Parameter vector returned by modelFunction must be a column matrix." );
        }
        this.modelFunction = modelFunction;
        this.cost = Double.MAX_VALUE;
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
    public double getCost()
    {
        return this.cost;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGradient()
    {
        return this.gradient;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGaussNewtonMatrix()
    {
        return this.gaussNewtonMatrix;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void allocateGradient()
    {
        Matrix theta = modelFunction.getParameters();
        this.gradient = Matrix.empty( theta.rows() , 1 );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void allocateGaussNewtonMatrix()
    {
        Matrix theta = modelFunction.getParameters();
        this.gaussNewtonMatrix = Matrix.empty( theta.rows() , theta.rows() );
    }
    
}
