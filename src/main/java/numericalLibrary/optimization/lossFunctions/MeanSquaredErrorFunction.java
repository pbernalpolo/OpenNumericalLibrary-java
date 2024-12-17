package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L(\theta) = \sum_i || f( x_i , \theta ) ||^2
 * <br>
 * where:
 * <ul>
 *  <li> f is a {@link ModelFunction},
 *  <li> x_i is the i-th input to the {@link ModelFunction} f,
 *  <li> \theta is the parameter vector.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class MeanSquaredErrorFunction<T>
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
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredErrorFunction}.
     * 
     * @param modelFunction   {@link ModelFunction} to be optimized.
     */
    public MeanSquaredErrorFunction( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs to the {@link ModelFunction}.
     * 
     * @param modelFunctionInputList    list of inputs to the {@link ModelFunction}.
     */
    public void setInputList( List<T> modelFunctionInputList )
    {
        this.inputList = modelFunctionInputList;
        this.dirtyFlag = true;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    protected MeanSquaredErrorFunctionDifferentiableLossUpdateStrategy getDifferentiableLossUpdateStrategy()
    {
        return new MeanSquaredErrorFunctionDifferentiableLossUpdateStrategy();
    }
    
    
    /**
     * {@inheritDoc}
     */
    protected MeanSquaredErrorFunctionLocallyQuadraticLossUpdateStrategy getLocallyQuadraticLossUpdateStrategy()
    {
        return new MeanSquaredErrorFunctionLocallyQuadraticLossUpdateStrategy();
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE CLASSES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Implements the strategy that offers better performance at the expense of only being able to use the class as a {@link DifferentiableLoss}.
     */
    private class MeanSquaredErrorFunctionDifferentiableLossUpdateStrategy
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
            // For each input...
            for( T input : inputList ) {
                // Set the input.
                loss.modelFunction.setInput( input );
                // Compute quantities involved in the cost and gradient.
                Matrix modelFunctionOutput = loss.modelFunction.getOutput();
                Matrix J = loss.modelFunction.getJacobian();
                // Add contribution to cost, and gradient.
                loss.cost += modelFunctionOutput.normFrobeniusSquared();
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
    private class MeanSquaredErrorFunctionLocallyQuadraticLossUpdateStrategy
        implements LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy<T>
    {
        /**
         * {@inheritDoc}
         */
        public void update( EfficientLocallyQuadraticLossDefinedWithModelFunction<T> loss )
        {
            // Initialize cost, gradient, and Gauss-Newton matrix.
            loss.cost = 0.0;
            loss.gradient.setToZero();
            loss.gaussNewtonMatrix.setToZero();
            // For each input...
            for( T input : inputList ) {
                // Set the input.
                loss.modelFunction.setInput( input );
                // Compute quantities involved in the cost and gradient.
                Matrix modelFunctionOutput = loss.modelFunction.getOutput();
                Matrix J = loss.modelFunction.getJacobian();
                // Add contribution to cost, gradient, and Gauss-Newton matrix.
                loss.cost += modelFunctionOutput.normFrobeniusSquared();
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
