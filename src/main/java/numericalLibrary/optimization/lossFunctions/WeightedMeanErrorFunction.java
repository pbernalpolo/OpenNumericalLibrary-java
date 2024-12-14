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
    extends AbstractLocallyQuadraticLossDefinedWithModelFunction<T>
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
     * @param squaredErrorFunctionLoss   {@link SquaredErrorFunctionLoss} used to define the loss.
     */
    public WeightedMeanErrorFunction( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
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
        if( modelFunctionInputList.size() != weightList.size() ) {
            throw new IllegalArgumentException( "Incompatible list sizes: modelFunctionInputList has " + modelFunctionInputList.size() + " elements; weightList has " + weightList.size() + " elements." );
        }
        this.inputList = modelFunctionInputList;
        this.weightList = weightList;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateCost()
    {
        // Initialize cost, and gradient.
        this.cost = 0.0;
        // For each input...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input.
            T input = this.inputList.get( i );
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            double weight = this.weightList.get( i );
            // Add contribution to cost, and gradient.
            this.cost += weight * modelFunctionOutput.normFrobeniusSquared();
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
        // For each input...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input.
            T input = this.inputList.get( i );
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix J = this.modelFunction.getJacobian();
            double weight = this.weightList.get( i );
            Matrix JWT = J.transpose().scaleInplace( weight );
            Matrix gradient_i = JWT.multiply( modelFunctionOutput );
            // Add contribution to cost, and gradient.
            this.cost += weight * modelFunctionOutput.normFrobeniusSquared();
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
        // For each input...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input.
            T input = this.inputList.get( i );
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix J = this.modelFunction.getJacobian();
            double weight = this.weightList.get( i );
            Matrix JWT = J.transpose().scaleInplace( weight );
            Matrix gradient_i = JWT.multiply( modelFunctionOutput );
            Matrix gaussNewtonMatrix_i = JWT.multiply( J );
            // Add contribution to cost, and gradient.
            this.cost += weight * modelFunctionOutput.normFrobeniusSquared();
            this.gradient.addInplace( gradient_i );
            this.gaussNewtonMatrix.addInplace( gaussNewtonMatrix_i );
        }
    }
    
}
