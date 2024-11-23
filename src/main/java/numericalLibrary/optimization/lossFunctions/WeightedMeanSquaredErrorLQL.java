package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * L(\theta) = \sum_i w_i || f( x_i , \theta ) - y_i ||^2
 * where:
 * - f is a {@link ModelFunction},
 * - x_i is the i-th input to the {@link ModelFunction} f,
 * - \theta is the parameter vector
 * - y_i is the i-th target of the {@link ModelFunction} f,
 * - w_i is the i-th weight that quantifies the importance of the data pair (x_i, y_i).
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class WeightedMeanSquaredErrorLQL<T>
    extends MeanLocallyQuadraticLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * List of targets of the {@link ModelFunction} f.
     */
    private List<Matrix> targetList;
    
    /**
     * List of weights that quantify the importance of each data pair (x_i, y_i).
     */
    private List<Double> weightList;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredErrorLQL}.
     * 
     * @param optimizableFunction   {@link ModelFunction} that models the relation between input data and target data.
     */
    public WeightedMeanSquaredErrorLQL( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs, the list of targets, and the list of weights.
     * 
     * @param modelFunctionInputList    list of inputs to the {@link ModelFunction}.
     * @param modelFunctionTargetList   list of targets for the {@link ModelFunction}.
     * @param weightList    list of weights for each (input_i, target_i) pair.
     * 
     * @throws IllegalArgumentException if lists have different sizes.
     */
    public void setInputListTargetListAndWeightList( List<T> modelFunctionInputList , List<Matrix> modelFunctionTargetList , List<Double> weightList )
    {
        if( modelFunctionInputList.size() != modelFunctionTargetList.size() ) {
            throw new IllegalArgumentException( "Input list and target list must have the same size." );
        }
        if( modelFunctionInputList.size() != weightList.size() ) {
            throw new IllegalArgumentException( "Input list and weight list must have the same size." );
        }
        this.setInputList( modelFunctionInputList );
        this.targetList = modelFunctionTargetList;
        this.weightList = weightList;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    protected void cleanItem( int index )
    {
        T input = this.inputList.get( index );
        Matrix target = this.targetList.get( index );
        double weight = this.weightList.get( index );
        // Introduce input in function.
        this.modelFunction.setInput( input );
        // Obtain output and Jacobian from function.
        Matrix functionOutput = this.modelFunction.getOutput();
        Matrix functionJacobian = this.modelFunction.getJacobian();
        Matrix error = functionOutput.subtractInplace( target );
        // Add contribution to the jacobian.
        this.J.addProduct( error.transpose().scaleInplace( weight ) , functionJacobian );
        // Add contribution to Gauss-Newton matrix.
        this.JTWJ.addProduct( functionJacobian.transpose().scaleInplace( weight ) , functionJacobian );
        // TODO: check if 2 scaleInplace are more efficient than 1 scale functionJacobian.
        // Add contribution to loss.
        this.cost += error.normFrobeniusSquared();
    }
    
}
