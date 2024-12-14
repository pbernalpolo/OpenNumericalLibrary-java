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
     * Sets the list of inputs to the {@link #modelFunction}.
     * 
     * @param modelFunctionInputList    list of inputs to the {@link #modelFunction}.
     */
    public void setInputList( List<T> modelFunctionInputList )
    {
        this.inputList = modelFunctionInputList;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateCost()
    {
        // Initialize cost, and gradient.
        this.cost = 0.0;
        // For each input...
        for( T input : this.inputList ) {
            // Set the input.
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            // Add contribution to cost, and gradient.
            this.cost += modelFunctionOutput.normFrobeniusSquared();
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
        for( T input : this.inputList ) {
            // Set the input.
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix J = this.modelFunction.getJacobian();
            Matrix JT = J.transpose();
            Matrix gradient_i = JT.multiply( modelFunctionOutput );
            // Add contribution to cost, and gradient.
            this.cost += modelFunctionOutput.normFrobeniusSquared();
            this.gradient.addInplace( gradient_i );
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateCostGradientAndGaussNewtonMatrix()
    {
        // Initialize cost, gradient, and Gauss-Newton matrix.
        this.cost = 0.0;
        this.gradient.setToZero();
        this.gaussNewtonMatrix.setToZero();
        // For each input...
        for( T input : this.inputList ) {
            // Set the input.
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix J = this.modelFunction.getJacobian();
            Matrix JT = J.transpose();
            Matrix gradient_i = JT.multiply( modelFunctionOutput );
            Matrix gaussNewtonMatrix_i = JT.multiply( J );
            // Add contribution to cost, gradient, and Gauss-Newton matrix.
            this.cost += modelFunctionOutput.normFrobeniusSquared();
            this.gradient.addInplace( gradient_i );
            this.gaussNewtonMatrix.addInplace( gaussNewtonMatrix_i );
        }
    }
    
}
