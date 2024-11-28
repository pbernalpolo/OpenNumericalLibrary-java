package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * L(\theta) = \sum_i || f( x_i , \theta ) - y_i ||^2
 * where:
 * - f is a {@link ModelFunction},
 * - x_i is the i-th input to the {@link ModelFunction} f,
 * - \theta is the parameter vector
 * - y_i is the i-th target of the {@link ModelFunction} f.
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class MeanSquaredErrorLQL<T>
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
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredErrorLQL}.
     * 
     * @param modelFunction   {@link ModelFunction} that models the relation between input data and target data.
     */
    public MeanSquaredErrorLQL( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs and the list of targets.
     * 
     * @param modelFunctionInputList    list of inputs to the {@link ModelFunction}.
     * @param modelFunctionTargetList   list of targets for the {@link ModelFunction}.
     * 
     * @throws IllegalArgumentException if lists have different sizes.
     */
    public void setInputListAndTargetList( List<T> modelFunctionInputList , List<Matrix> modelFunctionTargetList )
    {
        if( modelFunctionInputList.size() != modelFunctionTargetList.size() ) {
            throw new IllegalArgumentException( "Input list and target list must have the same size." );
        }
        this.setInputList( modelFunctionInputList );
        this.targetList = modelFunctionTargetList;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    protected void cleanItem( int index )
    {
        T input = this.inputList.get( index );
        Matrix target = this.targetList.get( index );
        // Introduce input in function.
        this.modelFunction.setInput( input );
        // Obtain output and Jacobian from function.
        Matrix functionOutput = this.modelFunction.getOutput();
        Matrix functionJacobian = this.modelFunction.getJacobian();
        Matrix error = functionOutput.subtractInplace( target );
        // Add contribution to the jacobian.
        this.J.addProduct( error.transpose() , functionJacobian );
        // Add contribution to Gauss-Newton matrix.
        this.JTWJ.addProduct( functionJacobian.transpose() , functionJacobian );
        // Add contribution to loss.
        this.cost += error.normFrobeniusSquared();
    }
    
}
