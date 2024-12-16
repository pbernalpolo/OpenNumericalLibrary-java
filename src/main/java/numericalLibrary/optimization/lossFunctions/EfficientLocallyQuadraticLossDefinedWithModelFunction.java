package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * Base class for {@link LocallyQuadraticLoss}s that consider a {@link ModelFunction} in their definition.
 * 
 * @param <T>   type of inputs to the {@link ModelFunction}.
 */
abstract class EfficientLocallyQuadraticLossDefinedWithModelFunction<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PROTECTED ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the {@link LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy} that increases performance, but limits the usage of this class to a {@link DifferentiableLoss}.
     * 
     * @return  {@link LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy} that increases performance, but limits the usage of this class to a {@link DifferentiableLoss}.
     */
    protected abstract LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy<T> getDifferentiableLossUpdateStrategy();
    
    /**
     * Returns the {@link LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy} that allows to use this class as a {@link LocallyQuadraticLoss}.
     * 
     * @return  {@link LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy} that allows to use this class as a {@link LocallyQuadraticLoss}.
     */
    protected abstract LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy<T> getLocallyQuadraticLossUpdateStrategy();
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link ModelFunction} that defines this {@link LocallyQuadraticLoss}.
     */
    protected ModelFunction<T> modelFunction;
    
    /**
     * {@link LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy} used to update the internal representation.
     */
    protected LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy<T> updateStrategy;
    
    /**
     * Last computed cost.
     */
    protected double cost;
    
    /**
     * Last computed gradient.
     */
    protected Matrix gradient;
    
    /**
     * Last computed Gauss-Newton matrix.
     */
    protected Matrix gaussNewtonMatrix;
    
    /**
     * Flag used to implement the Dirty Flag Optimization Pattern.
     */
    protected boolean dirtyFlag;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link EfficientLocallyQuadraticLossDefinedWithModelFunction}.
     * 
     * @param modelFunction   {@link ModelFunction} used in the {@link EfficientLocallyQuadraticLossDefinedWithModelFunction} definition.
     */
    public EfficientLocallyQuadraticLossDefinedWithModelFunction( ModelFunction<T> modelFunction )
    {
        Matrix theta = modelFunction.getParameters();
        if( theta.cols() != 1 ) {
            throw new IllegalArgumentException( "Parameter vector returned by modelFunction must be a column matrix." );
        }
        this.modelFunction = modelFunction;
        this.cost = Double.MAX_VALUE;
        this.gradient = Matrix.empty( theta.rows() , 1 );
        // Default object can be used as a DifferentiableLoss and as a LocallyQuadraticLoss.
        this.setUpdateStrategyLocallyQuadraticLoss();
        this.dirtyFlag = true;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the strategy that increases performance, but limits the usage of this class to a {@link DifferentiableLoss}.
     */
    public void setUpdateStrategyDifferentiableLoss()
    {
        this.updateStrategy = this.getDifferentiableLossUpdateStrategy();
        this.gaussNewtonMatrix = null;
        this.dirtyFlag = true;
    }
    
    
    /**
     * Sets the strategy that allows to use this class as a {@link LocallyQuadraticLoss}.
     */
    public void setUpdateStrategyLocallyQuadraticLoss()
    {
        this.updateStrategy = this.getLocallyQuadraticLossUpdateStrategy();
        Matrix theta = this.modelFunction.getParameters();
        this.gaussNewtonMatrix = Matrix.empty( theta.rows() , theta.rows() );
        this.dirtyFlag = true;
    }
    
    
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
     * {@inheritDoc}
     */
    public double getCost()
    {
        this.clean();
        return this.cost;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGradient()
    {
        this.clean();
        return this.gradient;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGaussNewtonMatrix()
    {
        this.clean();
        return this.gaussNewtonMatrix;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Cleans in the sense of the Dirty Flag Optimization Pattern.
     */
    private void clean()
    {
        if( this.dirtyFlag ) {
            this.updateStrategy.update( this );
            this.dirtyFlag = false;
        }
    }
    
}
