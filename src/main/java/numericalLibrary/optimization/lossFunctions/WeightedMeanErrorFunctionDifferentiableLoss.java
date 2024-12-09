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
public class WeightedMeanErrorFunctionDifferentiableLoss<T>
    implements DifferentiableLoss
{
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link SquaredErrorFunctionLoss} that defines this {@link WeightedMeanErrorFunctionDifferentiableLoss}.
     */
    protected SquaredErrorFunctionLoss<T> squaredErrorLoss;
    
    /**
     * List of inputs to the {@link SquaredErrorFunctionLoss}.
     */
    protected List<T> inputList;
    
    /**
     * List of weights that define the importance of each input in {@link #inputList}.
     */
    protected List<Double> weightList;
    
    /**
     * Last computed gradient from this {@link WeightedMeanErrorFunctionDifferentiableLoss}.
     */
    protected Matrix gradient;
    
    /**
     * Last computed cost from this {@link WeightedMeanErrorFunctionDifferentiableLoss}.
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
     * Constructs a {@link WeightedMeanErrorFunctionDifferentiableLoss}.
     * 
     * @param squaredErrorFunctionLoss   {@link SquaredErrorFunctionLoss} used to define the loss.
     */
    public WeightedMeanErrorFunctionDifferentiableLoss( SquaredErrorFunctionLoss<T> squaredErrorFunctionLoss )
    {
        this.squaredErrorLoss = squaredErrorFunctionLoss;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs to the {@link SquaredErrorFunctionLoss} and their weights.
     * 
     * @param modelFunctionInputList    list of inputs to {@link SquaredErrorFunctionLoss}.
     * @param weightList    list of weights that define the importance of each input.
     */
    public void setInputListAndWeightList( List<T> modelFunctionInputList , List<Double> weightList )
    {
        this.inputList = modelFunctionInputList;
        this.weightList = weightList;
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
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input.
            T input = this.inputList.get( i );
            this.squaredErrorLoss.setInput( input );
            // Add contribution to cost, and gradient.
            double weight = this.weightList.get( i );
            this.cost += weight * this.squaredErrorLoss.getCost();
            this.gradient.addInplace( this.squaredErrorLoss.getGradient().scaleInplace( weight ) );
        }
        // At this point, it is not dirty.
        this.dirtyFlag = false;
    }
    
}
