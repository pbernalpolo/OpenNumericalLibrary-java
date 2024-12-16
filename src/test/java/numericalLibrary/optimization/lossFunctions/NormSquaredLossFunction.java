package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.types.Matrix;



/**
 * {@link DifferentiableLoss} defined as:
 * L(\theta) = 1/2 || \theta ||^2
 * where:
 * - \theta is the parameter vector.
 * <p>
 * Function created for testing purposes.
 */
public class NormSquaredLossFunction
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Parameter vector.
     */
    private Matrix x;
    
    /**
     * Last computed gradient.
     */
    public Matrix gradient;
    
    /**
     * Last computed Gauss-Newton matrix.
     */
    public Matrix gaussNewtonMatrix;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link NormSquaredLossFunction}.
     * 
     * @param dimension     dimension of the parameter vector.
     */
    public NormSquaredLossFunction( int dimension )
    {
        this.x = Matrix.empty( dimension , 1 );
        this.gradient = Matrix.empty( dimension , 1 );
        /**
         * The Jacobian of f(\theta) in 1/2 ||f(\theta)||^2 is the identity.
         * From that, we obtain that J^T J = I * I = I
         */
        this.gaussNewtonMatrix = Matrix.one( this.x.rows() );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public void setParameters( Matrix theta )
    {
        this.x.setTo( theta );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getParameters()
    {
        return this.x;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double getCost()
    {
        return this.x.normFrobeniusSquared();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The Jacobian of f(\theta) in 1/2 ||f(\theta)||^2 is the identity.
     * From that, we obtain that the gradient of 1/2 ||f(\theta)||^2 is
     * J^T * f(\theta) = I * \theta = \theta
     */
    public Matrix getGradient()
    {
        this.gradient.setTo( this.x );
        return this.gradient;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGaussNewtonMatrix()
    {
        return this.gaussNewtonMatrix;
    }
    
}
