package numericalLibrary.optimization.lossFunctions;


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
    extends MeanSquaredErrorFunction<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
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
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            double errorSquared = modelFunctionOutput.normFrobeniusSquared();
            // Add contribution to cost, and gradient.
            this.cost += this.robustFunction.f( errorSquared );
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
            double errorSquared = modelFunctionOutput.normFrobeniusSquared();
            double robustWeight = this.robustFunction.f1( errorSquared );
            Matrix J = this.modelFunction.getJacobian();
            Matrix JWT = J.transpose().scaleInplace( robustWeight );
            Matrix gradient_i = JWT.multiply( modelFunctionOutput );
            // Add contribution to cost, and gradient.
            this.cost += this.robustFunction.f( errorSquared );
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
