package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.types.Matrix;



/**
 * {@link DifferentiableLoss} defined as:
 * <br>
 * L(\theta) = \sum_i F( x_i , \theta )
 * <br>
 * where:
 * <ul>
 *  <li> F is a {@link SquaredErrorFromTargetLoss},
 *  <li> x_i is the i-th input to the {@link SquaredErrorFromTargetLoss} F,
 *  <li> \theta is the parameter vector.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link SquaredErrorFromTargetLoss} F.
 */
public class MeanSquaredErrorFromTargetsDifferentiableLoss<T>
    implements DifferentiableLoss
{
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link SquaredErrorFromTargetLoss} used to define this {@link MeanSquaredErrorFromTargetsDifferentiableLoss}.
     */
    protected SquaredErrorFromTargetLoss<T> squaredErrorLoss;
    
    /**
     * List of inputs to the {@link SquaredErrorFromTargetLoss}.
     */
    protected List<T> inputList;
    
    /**
     * List of targets for the {@link SquaredErrorFromTargetLoss}.
     */
    protected List<Matrix> targetList;
    
    /**
     * Last computed gradient from this {@link MeanSquaredErrorFromTargetsDifferentiableLoss}.
     */
    protected Matrix gradient;
    
    /**
     * Last computed cost from this {@link MeanSquaredErrorFromTargetsDifferentiableLoss}.
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
     * Constructs a {@link MeanSquaredErrorFromTargetsDifferentiableLoss}.
     * 
     * @param squaredErrorFromTargetLoss   {@link SquaredErrorFunctionLoss} used to define the loss.
     */
    public MeanSquaredErrorFromTargetsDifferentiableLoss( SquaredErrorFromTargetLoss<T> squaredErrorFromTargetLoss )
    {
        this.squaredErrorLoss = squaredErrorFromTargetLoss;
        this.gradient = squaredErrorFromTargetLoss.getGradient().copy();
        this.dirtyFlag = true;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs and the list of targets of the {@link #squaredErrorLoss} that define this {@link MeanSquaredErrorFromTargetsDifferentiableLoss}.
     * 
     * @param modelFunctionInputList    list of inputs to the {@link #squaredErrorLoss} that define this {@link MeanSquaredErrorFromTargetsDifferentiableLoss}.
     * @param modelFunctionTargetList   list of targets of the {@link #squaredErrorLoss} that define this {@link MeanSquaredErrorFromTargetsDifferentiableLoss}.
     */
    public void setInputListAndTargetList( List<T> modelFunctionInputList , List<Matrix> modelFunctionTargetList )
    {
        if( modelFunctionInputList.size() != modelFunctionTargetList.size() ) {
            throw new IllegalArgumentException( "Incompatible list sizes: modelFunctionInputList has " + modelFunctionInputList.size() + " elements; modelFunctionTargetList has " + modelFunctionTargetList.size() + " elements." );
        }
        this.inputList = modelFunctionInputList;
        this.targetList = modelFunctionTargetList;
        this.dirtyFlag = true;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void setParameters( Matrix theta )
    {
        this.squaredErrorLoss.setParameters( theta );
        this.dirtyFlag = true;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getParameters()
    {
        return this.squaredErrorLoss.getParameters();
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
        // Initialize cost, and gradient.
        this.cost = 0.0;
        this.gradient.setToZero();
        // For each input-target pair...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input and target.
            T input = this.inputList.get( i );
            this.squaredErrorLoss.setInput( input );
            Matrix target = this.targetList.get( i );
            this.squaredErrorLoss.setTarget( target );
            // Add contribution to cost, and gradient.
            this.cost += this.squaredErrorLoss.getCost();
            this.gradient.addInplace( this.squaredErrorLoss.getGradient() );
        }
        // At this point, it is not dirty.
        this.dirtyFlag = false;
    }
    
}
