package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L(\theta) = \sum_i F( x_i , \theta )
 * <br>
 * where:
 * <ul>
 *  <li> F is a {@link SquaredErrorFunctionLoss},
 *  <li> x_i is the i-th input to the {@link SquaredErrorFunctionLoss},
 *  <li> \theta is the parameter vector.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link SquaredErrorFunctionLoss}.
 */
public class MeanSquaredErrorFunctionLocallyQuadraticLoss<T>
    extends MeanSquaredErrorFunctionDifferentiableLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Last computed Gauss-Newton matrix from this {@link MeanSquaredErrorFunctionLocallyQuadraticLoss}.
     */
    private Matrix gaussNewtonMatrix;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredErrorFunctionLocallyQuadraticLoss}.
     * 
     * @param squaredErrorFunctionLoss   {@link SquaredErrorFunctionLoss} used to define the loss.
     */
    public MeanSquaredErrorFunctionLocallyQuadraticLoss( SquaredErrorFunctionLoss<T> squaredErrorFunctionLoss )
    {
        super( squaredErrorFunctionLoss );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public double getCost()
    {
        this.clean();
        return this.cost;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGradient()
    {
        this.clean();
        return this.gradient;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGaussNewtonMatrix()
    {
        this.clean();
        return this.gaussNewtonMatrix;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Implementation of the Dirty Flag Optimization Pattern.
     */
    private void clean()
    {
        // If not dirty, then there is nothing new to compute.
        if( !this.dirtyFlag ) {
            return;
        }
        // Initialize cost, gradient, and Gauss-Newton matrix.
        this.cost = 0.0;
        this.gradient.setToZero();
        this.gaussNewtonMatrix.setToZero();
        // For each input...
        for( T input : this.inputList ) {
            // Set the input.
            this.squaredErrorLoss.setInput( input );
            // Add contribution to cost, gradient, and Gauss-Newton matrix.
            this.cost += this.squaredErrorLoss.getCost();
            this.gradient.addInplace( this.squaredErrorLoss.getGradient() );
            this.gaussNewtonMatrix.addInplace( this.squaredErrorLoss.getGaussNewtonMatrix() );
        }
        // At this point, it is not dirty.
        this.dirtyFlag = false;
    }
    
}
