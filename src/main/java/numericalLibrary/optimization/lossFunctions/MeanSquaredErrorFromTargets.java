package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L(\theta) = \sum_i || f( x_i , \theta ) - y_i ||^2
 * <br>
 * where:
 * <ul>
 *  <li> f is a {@link ModelFunction},
 *  <li> x_i is the i-th input to the {@link ModelFunction} f,
 *  <li> y_i is the i-th target of the {@link ModelFunction} f,
 *  <li> \theta is the parameter vector.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class MeanSquaredErrorFromTargets<T>
    extends AbstractLocallyQuadraticLossDefinedWithModelFunction<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * List of inputs to the {@link ModelFunction}.
     */
    protected List<T> inputList;
    
    /**
     * List of targets of the {@link ModelFunction}.
     */
    protected List<Matrix> targetList;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredErrorFromTargets}.
     * 
     * @param modelFunction   {@link ModelFunction} to be optimized.
     */
    public MeanSquaredErrorFromTargets( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs and the list of targets of the {@link ModelFunction}.
     * 
     * @param modelFunctionInputList    list of inputs to the {@link ModelFunction}.
     * @param modelFunctionTargetList   list of targets of the {@link ModelFunction}.
     */
    public void setInputListAndTargetList( List<T> modelFunctionInputList , List<Matrix> modelFunctionTargetList )
    {
        if( modelFunctionInputList.size() != modelFunctionTargetList.size() ) {
            throw new IllegalArgumentException( "Incompatible list sizes: modelFunctionInputList has " + modelFunctionInputList.size() + " elements; modelFunctionTargetList has " + modelFunctionTargetList.size() + " elements." );
        }
        this.inputList = modelFunctionInputList;
        this.targetList = modelFunctionTargetList;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateCost()
    {
        // Initialize cost, and gradient.
        this.cost = 0.0;
        // For each input-target pair...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input and target.
            T input = this.inputList.get( i );
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix target = this.targetList.get( i );
            Matrix outputMinusTarget = modelFunctionOutput.subtractInplace( target );
            // Add contribution to cost, and gradient.
            this.cost += outputMinusTarget.normFrobeniusSquared();
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateCostAndGradient()
    {
        // Initialize cost, and gradient.
        this.cost = 0.0;
        this.gradient.setToZero();
        // For each input-target pair...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input and target.
            T input = this.inputList.get( i );
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix target = this.targetList.get( i );
            Matrix outputMinusTarget = modelFunctionOutput.subtractInplace( target );
            Matrix J = this.modelFunction.getJacobian();
            Matrix JT = J.transpose();
            Matrix gradient_i = JT.multiply( modelFunctionOutput );
            // Add contribution to cost, and gradient.
            this.cost += outputMinusTarget.normFrobeniusSquared();
            this.gradient.addInplace( gradient_i );
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateCostGradientAndGaussNewtonMatrix()
    {
        // Initialize cost, and gradient.
        this.cost = 0.0;
        this.gradient.setToZero();
        this.gaussNewtonMatrix.setToZero();
        // For each input-target pair...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input and target.
            T input = this.inputList.get( i );
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix target = this.targetList.get( i );
            Matrix outputMinusTarget = modelFunctionOutput.subtractInplace( target );
            Matrix J = this.modelFunction.getJacobian();
            Matrix JT = J.transpose();
            Matrix gradient_i = JT.multiply( modelFunctionOutput );
            Matrix gaussNewtonMatrix_i = JT.multiply( J );
            // Add contribution to cost, and gradient.
            this.cost += outputMinusTarget.normFrobeniusSquared();
            this.gradient.addInplace( gradient_i );
            this.gaussNewtonMatrix.addInplace( gaussNewtonMatrix_i );
        }
    }
    
}
