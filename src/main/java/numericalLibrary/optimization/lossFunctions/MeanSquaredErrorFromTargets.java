package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.MatrixReal;



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
    extends EfficientLocallyQuadraticLossDefinedWithModelFunction<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * List of inputs to the {@link ModelFunction}.
     */
    private List<T> inputList;
    
    /**
     * List of targets of the {@link ModelFunction}.
     */
    private List<MatrixReal> targetList;
    
    
    
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
    public void setInputListAndTargetList( List<T> modelFunctionInputList , List<MatrixReal> modelFunctionTargetList )
    {
        if( modelFunctionInputList.size() != modelFunctionTargetList.size() ) {
            throw new IllegalArgumentException( "Incompatible list sizes: modelFunctionInputList has " + modelFunctionInputList.size() + " elements; modelFunctionTargetList has " + modelFunctionTargetList.size() + " elements." );
        }
        this.inputList = modelFunctionInputList;
        this.targetList = modelFunctionTargetList;
        this.dirtyFlag = true;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    protected MeanSquaredErrorFromTargetsDifferentiableLossUpdateStrategy getDifferentiableLossUpdateStrategy()
    {
        return new MeanSquaredErrorFromTargetsDifferentiableLossUpdateStrategy();
    }
    
    
    /**
     * {@inheritDoc}
     */
    protected MeanSquaredErrorFromTargetsLocallyQuadraticLossUpdateStrategy getLocallyQuadraticLossUpdateStrategy()
    {
        return new MeanSquaredErrorFromTargetsLocallyQuadraticLossUpdateStrategy();
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE CLASSES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Implements the strategy that offers better performance at the expense of only being able to use the class as a {@link DifferentiableLoss}.
     */
    private class MeanSquaredErrorFromTargetsDifferentiableLossUpdateStrategy
        implements LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy<T>
    {
        /**
         * {@inheritDoc}
         */
        public void update( EfficientLocallyQuadraticLossDefinedWithModelFunction<T> loss )
        {
            // Initialize cost, and gradient.
            loss.cost = 0.0;
            loss.gradient.setToZero();
            // For each input-target pair...
            for( int i=0; i<inputList.size(); i++ ) {
                // Set the input and target.
                T input = inputList.get( i );
                loss.modelFunction.setInput( input );
                // Compute quantities involved in the cost and gradient.
                MatrixReal modelFunctionOutput = loss.modelFunction.getOutput();
                MatrixReal target = targetList.get( i );
                MatrixReal outputMinusTarget = modelFunctionOutput.subtractInplace( target );
                MatrixReal J = loss.modelFunction.getJacobian();
                // Add contribution to cost, and gradient.
                loss.cost += outputMinusTarget.normFrobeniusSquared();
                loss.gradient.addLeftTransposeTimesRight( J , modelFunctionOutput );
            }
            double oneOverInputListSize = 1.0/inputList.size();
            loss.cost *= oneOverInputListSize;
            loss.gradient.scaleInplace( oneOverInputListSize );
        }
    }
    
    
    /**
     * Implements the strategy that allows to use the class as a {@link LocallyQuadraticLoss}.
     */
    private class MeanSquaredErrorFromTargetsLocallyQuadraticLossUpdateStrategy
        implements LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy<T>
    {
        /**
         * {@inheritDoc}
         */
        public void update( EfficientLocallyQuadraticLossDefinedWithModelFunction<T> loss )
        {
            // Initialize cost, and gradient.
            loss.cost = 0.0;
            loss.gradient.setToZero();
            loss.gaussNewtonMatrix.setToZero();
            // For each input-target pair...
            for( int i=0; i<inputList.size(); i++ ) {
                // Set the input and target.
                T input = inputList.get( i );
                loss.modelFunction.setInput( input );
                // Compute quantities involved in the cost and gradient.
                MatrixReal modelFunctionOutput = loss.modelFunction.getOutput();
                MatrixReal target = targetList.get( i );
                MatrixReal outputMinusTarget = modelFunctionOutput.subtractInplace( target );
                MatrixReal J = loss.modelFunction.getJacobian();
                // Add contribution to cost, and gradient.
                loss.cost += outputMinusTarget.normFrobeniusSquared();
                loss.gradient.addLeftTransposeTimesRight( J , modelFunctionOutput );
                loss.gaussNewtonMatrix.addLeftTransposeTimesRight( J , J );
            }
            double oneOverInputListSize = 1.0/inputList.size();
            loss.cost *= oneOverInputListSize;
            loss.gradient.scaleInplace( oneOverInputListSize );
            loss.gaussNewtonMatrix.scaleInplace( oneOverInputListSize );
        }
    }
    
}
