package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link DifferentiableLoss} defined as:
 * <br>
 * L(\theta) = \sum_i w_i || f( x_i , \theta ) ||^2
 * <br>
 * where:
 * <ul>
 *  <li> f is the {@link ModelFunction} that describes the error,
 *  <li> x_i is the i-th input to the {@link ModelFunction} f,
 *  <li> w_i is the weight that measures the importance of the error associated to x_i,
 *  <li> \theta is the parameter vector.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class WeightedMeanErrorFunction<T>
    extends EfficientLocallyQuadraticLossDefinedWithModelFunction<T>
    implements DifferentiableLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////

    /**
     * List of inputs to the {@link ModelFunction}.
     */
    private List<T> inputList;
    
    /**
     * List of weights that define the importance of each input in {@link #inputList}.
     */
    private List<Double> weightList;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link WeightedMeanErrorFunction}.
     * 
     * @param modelFunction   {@link ModelFunction} used to define the loss.
     */
    public WeightedMeanErrorFunction( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs to the {@link WeightedMeanErrorFunction} and their weights.
     * 
     * @param modelFunctionInputList    list of inputs to {@link WeightedMeanErrorFunction}.
     * @param weightList    list of weights that define the importance of each input.
     */
    public void setInputListAndWeightList( List<T> modelFunctionInputList , List<Double> weightList )
    {
        if( modelFunctionInputList.size() != weightList.size() ) {
            throw new IllegalArgumentException( "Incompatible list sizes: modelFunctionInputList has " + modelFunctionInputList.size() + " elements; weightList has " + weightList.size() + " elements." );
        }
        this.inputList = modelFunctionInputList;
        this.weightList = weightList;
        this.dirtyFlag = true;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    protected WeightedMeanErrorFunctionDifferentiableLossUpdateStrategy getDifferentiableLossUpdateStrategy()
    {
        return new WeightedMeanErrorFunctionDifferentiableLossUpdateStrategy();
    }
    
    
    /**
     * {@inheritDoc}
     */
    protected WeightedMeanErrorFunctionLocallyQuadraticLossUpdateStrategy getLocallyQuadraticLossUpdateStrategy()
    {
        return new WeightedMeanErrorFunctionLocallyQuadraticLossUpdateStrategy();
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE CLASSES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Implements the strategy that offers better performance at the expense of only being able to use the class as a {@link DifferentiableLoss}.
     */
    private class WeightedMeanErrorFunctionDifferentiableLossUpdateStrategy
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
            for( int i=0; i<inputList.size(); i++ ) {
                // Set the input.
                T input = inputList.get( i );
                loss.modelFunction.setInput( input );
                // Compute quantities involved in the cost and gradient.
                Matrix modelFunctionOutput = loss.modelFunction.getOutput();
                Matrix J = loss.modelFunction.getJacobian();
                double weight = weightList.get( i );
                // Add contribution to cost, and gradient.
                loss.cost += weight * modelFunctionOutput.normFrobeniusSquared();
                loss.gradient.addLeftTransposeTimesRight( J , modelFunctionOutput.scaleInplace( weight ) );
            }
            double oneOverInputListSize = 1.0/inputList.size();
            loss.cost *= oneOverInputListSize;
            loss.gradient.scaleInplace( oneOverInputListSize );
        }
    }
    
    
    /**
     * Implements the strategy that allows to use the class as a {@link LocallyQuadraticLoss}.
     */
    private class WeightedMeanErrorFunctionLocallyQuadraticLossUpdateStrategy
        implements LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy<T>
    {
        /**
         * {@inheritDoc}
         */
        public void update( EfficientLocallyQuadraticLossDefinedWithModelFunction<T> loss )
        {
            // Create matrix to speed up computations.
            Matrix JW = Matrix.emptyWithSizeOf( loss.modelFunction.getJacobian() );
            // Initialize cost, and gradient.
            loss.cost = 0.0;
            loss.gradient.setToZero();
            loss.gaussNewtonMatrix.setToZero();
            // For each input...
            for( int i=0; i<inputList.size(); i++ ) {
                // Set the input.
                T input = inputList.get( i );
                loss.modelFunction.setInput( input );
                // Compute quantities involved in the cost and gradient.
                Matrix modelFunctionOutput = loss.modelFunction.getOutput();
                Matrix J = loss.modelFunction.getJacobian();
                double weight = weightList.get( i );
                JW.setTo( J ).scaleInplace( weight );
                // Add contribution to cost, and gradient.
                loss.cost += weight * modelFunctionOutput.normFrobeniusSquared();
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
