package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.optimization.robustFunctions.RobustFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L(\theta) = \sum_i g( || f( x_i , \theta ) - y_i ||^2 )
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
public class RobustMeanSquaredErrorFromTargets<T>
    extends MeanSquaredErrorFromTargets<T>
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
     * Constructs a {@link RobustMeanSquaredErrorFromTargets}.
     * 
     * @param modelFunction   {@link ModelFunction} to be optimized.
     */
    public RobustMeanSquaredErrorFromTargets( ModelFunction<T> modelFunction )
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
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input and target.
            T input = this.inputList.get( i );
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix target = this.targetList.get( i );
            Matrix outputMinusTarget = modelFunctionOutput.subtractInplace( target );
            double errorSquared = outputMinusTarget.normFrobeniusSquared();
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
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input and target.
            T input = this.inputList.get( i );
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix target = this.targetList.get( i );
            Matrix outputMinusTarget = modelFunctionOutput.subtractInplace( target );
            double errorSquared = outputMinusTarget.normFrobeniusSquared();
            double robustWeight = this.robustFunction.f1( errorSquared );
            Matrix J = this.modelFunction.getJacobian();
            Matrix JWT = J.transpose().scaleInplace( robustWeight );
            Matrix gradient_i = JWT.multiply( outputMinusTarget );
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
        // Initialize cost, and gradient.
        this.cost = 0.0;
        this.gradient.setToZero();
        this.gaussNewtonMatrix.setToZero();
        // For each input...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input and target.
            T input = this.inputList.get( i );
            this.modelFunction.setInput( input );
            // Compute quantities involved in the cost and gradient.
            Matrix modelFunctionOutput = this.modelFunction.getOutput();
            Matrix target = this.targetList.get( i );
            Matrix outputMinusTarget = modelFunctionOutput.subtractInplace( target );
            double errorSquared = outputMinusTarget.normFrobeniusSquared();
            double robustWeight = this.robustFunction.f1( errorSquared );
            Matrix J = this.modelFunction.getJacobian();
            Matrix JWT = J.transpose().scaleInplace( robustWeight );
            Matrix gradient_i = JWT.multiply( outputMinusTarget );
            Matrix gaussNewtonMatrix_i = JWT.multiply( J );
            // Add contribution to cost, and gradient.
            this.cost += this.robustFunction.f( errorSquared );
            this.gradient.addInplace( gradient_i );
            this.gaussNewtonMatrix.addInplace( gaussNewtonMatrix_i );
        }
    }
    
}
