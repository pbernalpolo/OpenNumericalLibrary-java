package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * Base class for all {@link DifferentiableLoss} defined as:
 * L(\theta) = \sum_i F( x_i , \theta )
 * where:
 * - F is a differentiable loss function,
 * - x_i is the i-th input to F,
 * - \theta is the parameter vector.
 * 
 * @param <T>   type of inputs to the differentiable loss function F.
 */
public abstract class MeanDifferentiableLoss<T>
    implements DifferentiableLoss
{
    ////////////////////////////////////////////////////////////////
    // PROTECTED ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Adds the contribution to the cost and the Jacobian of the differentiable loss function when we set the index-th input.
     * 
     * @param index     index of the input to be set prior to add the contribution to the cost and the Jacobian.
     */
    protected abstract void cleanItem( int index );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Model function used to define the differentiable loss function F( x_i , \theta ).
     */
    protected ModelFunction<T> modelFunction;
    
    /**
     * List of inputs to the differentiable loss function F.
     */
    protected List<T> inputList;
    
    /**
     * Last Jacobian computed from this {@link MeanDifferentiableLoss}.
     * 
     * @see #clean()
     */
    protected Matrix J;
    
    /**
     * Last cost computed from this {@link MeanDifferentiableLoss}.
     * 
     * @see #clean()
     */
    protected double cost;
    
    /**
     * Flag used to implement the Dirty Flag Optimization Pattern.
     */
    protected boolean dirtyFlag;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanDifferentiableLoss}.
     * 
     * @param optimizableFunction   {@link ModelFunction} used to define the differentiable loss function F( x_i , \theta ).
     */
    public MeanDifferentiableLoss( ModelFunction<T> modelFunction )
    {
        this.modelFunction = modelFunction;
        int numberOfParameters = modelFunction.getParameters().cols();
        this.J = Matrix.empty( 1 , numberOfParameters );
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
        if( theta.rows() != 1 ) {
            throw new IllegalArgumentException( "Parameter vector must be a row matrix; found a " + theta.rows() + " x " + theta.cols() + " matrix." );
        }
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
    public Matrix getJacobian()
    {
        this.clean();
        return this.J;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs to the differentiable loss function.
     * 
     * @param modelFunctionInputList    list of inputs to the differentiable loss function.
     */
    protected void setInputList( List<T> modelFunctionInputList )
    {
        this.inputList = modelFunctionInputList;
        this.dirtyFlag = true;
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
            this.cost = 0.0;
            for( int i=0; i<this.inputList.size(); i++ ) {
                this.cleanItem( i );
            }
            this.dirtyFlag = false;
        }
    }
    
}
