package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * <br>
 * L(\theta) = \sum_i F( x_i , \theta )
 * <br>
 * where:
 * <ul>
 *  <li> F is a {@link SquaredErrorFromTargetLoss},
 *  <li> x_i is the i-th input to the {@link SquaredErrorFromTargetLoss} F,
 *  <li> \theta is the parameter vector.
 * </ul>
 * 
 * @param <T>   type of inputs to the {@link SquaredErrorFromTargetLoss} F.
 */
public class MeanSquaredErrorFromTargetsLocallyQuadraticLoss<T>
    extends MeanSquaredErrorFromTargetsDifferentiableLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Last computed Gauss-Newton matrix from this {@link MeanSquaredErrorFromTargetsLocallyQuadraticLoss}.
     */
    private Matrix gaussNewtonMatrix;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredErrorFromTargetsLocallyQuadraticLoss}.
     * 
     * @param squaredErrorFromTargetLoss   {@link SquaredErrorFromTargetLoss} used to define the loss.
     */
    public MeanSquaredErrorFromTargetsLocallyQuadraticLoss( SquaredErrorFromTargetLoss<T> squaredErrorFromTargetLoss )
    {
        super( squaredErrorFromTargetLoss );
        this.gaussNewtonMatrix = squaredErrorFromTargetLoss.getGaussNewtonMatrix().copy();
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
        // For each input-target pair...
        for( int i=0; i<this.inputList.size(); i++ ) {
            // Set the input and target.
            T input = this.inputList.get( i );
            this.squaredErrorLoss.setInput( input );
            Matrix target = this.targetList.get( i );
            this.squaredErrorLoss.setTarget( target );
            // Add contribution to cost, gradient and Gauss-Newton matrix.
            this.cost += this.squaredErrorLoss.getCost();
            this.gradient.addInplace( this.squaredErrorLoss.getGradient() );
            this.gaussNewtonMatrix.addInplace( this.squaredErrorLoss.getGaussNewtonMatrix() );
        }
        // At this point, it is not dirty.
        this.dirtyFlag = false;
    }
    
}
