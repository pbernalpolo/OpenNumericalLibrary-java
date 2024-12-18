package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.types.MatrixReal;



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
    private MatrixReal x;
    
    /**
     * Last computed gradient.
     */
    public MatrixReal gradient;
    
    /**
     * Last computed Gauss-Newton matrix.
     */
    public MatrixReal gaussNewtonMatrix;
    
    
    
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
        this.x = MatrixReal.empty( dimension , 1 );
        this.gradient = MatrixReal.empty( dimension , 1 );
        /**
         * The Jacobian of f(\theta) in 1/2 ||f(\theta)||^2 is the identity.
         * From that, we obtain that J^T J = I * I = I
         */
        this.gaussNewtonMatrix = MatrixReal.one( this.x.rows() );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public void setParameters( MatrixReal theta )
    {
        this.x.setTo( theta );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal getParameters()
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
    public MatrixReal getGradient()
    {
        this.gradient.setTo( this.x );
        return this.gradient;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal getGaussNewtonMatrix()
    {
        return this.gaussNewtonMatrix;
    }
    
}
