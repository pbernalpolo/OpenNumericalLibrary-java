package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.optimization.robustFunctions.IdentityRobustFunction;
import numericalLibrary.optimization.robustFunctions.RobustFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * L(\theta) = \sum_i g( || f( x_i , \theta ) - y_i ||^2 )
 * where:
 * - f is a {@link ModelFunction},
 * - x_i is the i-th input to the model function,
 * - \theta is the parameter vector,
 * - y_i is the i-th target of the model function,
 * - g is a {@link RobustFunction}.
 */
public class RobustMeanSquaredErrorLQL<T>
    extends MeanLocallyQuadraticLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link RobustFunction} used to define the {@link LocallyQuadraticLoss}.
     */
    private RobustFunction robustFunction;
    
    /**
     * List of targets of the {@link ModelFunction} f.
     */
    private List<Matrix> targetList;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link RobustMeanSquaredErrorLQL}.
     * <p>
     * The default robust function is the identity.
     * 
     * @param modelFunction   {@link ModelFunction} that models the relation between input data and target data.
     */
    public RobustMeanSquaredErrorLQL( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
        // Default robust function is identity.
        this.robustFunction = new IdentityRobustFunction();
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
    // PRIVATE METHODS
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
        double errorNormSquared = error.normFrobeniusSquared();
        double robustWeight = this.robustFunction.f1( errorNormSquared );
        // Add contribution to the jacobian.
        this.J.addProduct( error.transpose().scaleInplace( robustWeight ) , functionJacobian );
        // Add contribution to Gauss-Newton matrix.
        this.JTWJ.addProduct( functionJacobian.transpose().scaleInplace( robustWeight ) , functionJacobian );
        // TODO: check if 2 scaleInplace are more efficient than 1 scale functionJacobian.
        // Add contribution to loss.
        this.cost += this.robustFunction.f( errorNormSquared );
    }
    
}
