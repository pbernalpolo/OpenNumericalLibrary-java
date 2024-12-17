package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.optimization.robustFunctions.RobustFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L(\theta) = \sum_i g( || f( x_i , \theta ) ||^2 )
 * <br>
 * where:
 * <ul>
 *  <li> f is a {@link ModelFunction},
 *  <li> x_i is the i-th input to the {@link ModelFunction} f,
 *  <li> y_i is the i-th target of the {@link ModelFunction} f,
 *  <li> \theta is the parameter vector,
 *  <li> g is a {@link RobustFunction} that shapes the error.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class RobustMeanSquaredErrorFunction<T>
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
     * {@link RobustFunction} used to define the {@link LocallyQuadraticLoss}.
     */
    private RobustFunction robustFunction;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link RobustMeanSquaredErrorFunction}.
     * 
     * @param modelFunction   {@link ModelFunction} to be optimized.
     */
    public RobustMeanSquaredErrorFunction( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the {@link RobustFunction} that defines the cost.
     * 
     * @param robustFunction    {@link RobustFunction} that defines the cost.
     */
    public void setRobustFunction( RobustFunction robustFunction )
    {
        this.robustFunction = robustFunction;
    }
    
    
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
    protected RobustMeanSquaredErrorFunctionDifferentiableLossUpdateStrategy getDifferentiableLossUpdateStrategy()
    {
        return new RobustMeanSquaredErrorFunctionDifferentiableLossUpdateStrategy();
    }
    
    
    /**
     * {@inheritDoc}
     */
    protected RobustMeanSquaredErrorFunctionLocallyQuadraticLossUpdateStrategy getLocallyQuadraticLossUpdateStrategy()
    {
        return new RobustMeanSquaredErrorFunctionLocallyQuadraticLossUpdateStrategy();
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE CLASSES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Implements the strategy that offers better performance at the expense of only being able to use the class as a {@link DifferentiableLoss}.
     */
    private class RobustMeanSquaredErrorFunctionDifferentiableLossUpdateStrategy
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
                double errorSquared = modelFunctionOutput.normFrobeniusSquared();
                double robustWeight = robustFunction.f1( errorSquared );
                Matrix J = loss.modelFunction.getJacobian();
                // Add contribution to cost, and gradient.
                loss.cost += robustFunction.f( errorSquared );
                loss.gradient.addLeftTransposeTimesRight( J , modelFunctionOutput.scaleInplace( robustWeight ) );
            }
            double oneOverInputListSize = 1.0/inputList.size();
            loss.cost *= oneOverInputListSize;
            loss.gradient.scaleInplace( oneOverInputListSize );
        }
    }
    
    
    /**
     * Implements the strategy that allows to use the class as a {@link LocallyQuadraticLoss}.
     */
    private class RobustMeanSquaredErrorFunctionLocallyQuadraticLossUpdateStrategy
        implements LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy<T>
    {
        /**
         * {@inheritDoc}
         */
        public void update( EfficientLocallyQuadraticLossDefinedWithModelFunction<T> loss )
        {
            // Create matrix to speed up computations.
            Matrix JW = Matrix.emptyWithSizeOf( loss.modelFunction.getJacobian() );
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
                double errorSquared = modelFunctionOutput.normFrobeniusSquared();
                double robustWeight = robustFunction.f1( errorSquared );
                Matrix J = loss.modelFunction.getJacobian();
                JW.setTo( J ).scaleInplace( robustWeight );
                // Add contribution to cost, gradient, and Gauss-Newton matrix.
                loss.cost += modelFunctionOutput.normFrobeniusSquared();
                loss.gradient.addLeftTransposeTimesRight( JW , modelFunctionOutput );
                loss.gaussNewtonMatrix.addLeftTransposeTimesRight( JW , J );
            }
            double oneOverInputListSize = 1.0/inputList.size();
            loss.cost *= oneOverInputListSize;
            loss.gradient.scaleInplace( oneOverInputListSize );
            loss.gaussNewtonMatrix.scaleInplace( oneOverInputListSize );
        }
    }
    
}
