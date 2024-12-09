package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L(\theta) = \sum_i F( x_i , \theta )
 * <br>
 * where:
 * <ul>
 *  <li> F is a {@link SquaredErrorFunctionLoss},
 *  <li> x_i is the i-th input to the {@link SquaredErrorFunctionLoss} F,
 *  <li> \theta is the parameter vector.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link SquaredErrorFunctionLoss} F.
 */
public class MeanSquaredErrorFunctionDifferentiableLoss<T>
    implements DifferentiableLoss
{
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link SquaredErrorFunctionLoss} used to define this {@link MeanSquaredErrorFunctionDifferentiableLoss}.
     */
    protected SquaredErrorFunctionLoss<T> squaredErrorLoss;
    
    /**
     * List of inputs to the {@link SquaredErrorFunctionLoss}.
     */
    protected List<T> inputList;
    
    /**
     * Last computed gradient from this {@link MeanSquaredErrorFunctionDifferentiableLoss}.
     */
    protected Matrix gradient;
    
    /**
     * Last computed cost from this {@link MeanSquaredErrorFunctionDifferentiableLoss}.
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
     * Constructs a {@link MeanSquaredErrorFunctionDifferentiableLoss}.
     * 
     * @param squaredErrorFunctionLoss   {@link SquaredErrorFunctionLoss} used to define this {@link MeanSquaredErrorFunctionDifferentiableLoss}.
     */
    public MeanSquaredErrorFunctionDifferentiableLoss( SquaredErrorFunctionLoss<T> squaredErrorFunctionLoss )
    {
        this.squaredErrorLoss = squaredErrorFunctionLoss;
        this.dirtyFlag = true;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs to the {@link SquaredErrorFunctionLoss} that defines this {@link MeanSquaredErrorFunctionDifferentiableLoss}.
     * 
     * @param modelFunctionInputList     list of inputs to the {@link SquaredErrorFunctionLoss} that defines this {@link MeanSquaredErrorFunctionDifferentiableLoss}.
     */
    public void setInputList( List<T> modelFunctionInputList )
    {
        this.inputList = modelFunctionInputList;
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
        // For each input...
        for( T input : this.inputList ) {
            // Set the input.
            this.squaredErrorLoss.setInput( input );
            // Add contribution to cost, and gradient.
            this.cost += this.squaredErrorLoss.getCost();
            this.gradient.addInplace( this.squaredErrorLoss.getGradient() );
        }
        // At this point, it is not dirty.
        this.dirtyFlag = false;
    }
    
}
