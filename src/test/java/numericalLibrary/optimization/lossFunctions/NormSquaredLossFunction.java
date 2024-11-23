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
        this.x = Matrix.empty( 1 , dimension );
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
     * Here we can think that although \theta is the parameter vector as a row matrix,
     * it appears inside the norm squared as a column matrix.
     * Then, the Jacobian of f(\theta) in ||f(\theta)||^2 is the identity.
     * From that, we obtain that the Jacobian of ||f(\theta)||^2 is
     * (\theta^T)^T * I = \theta
     */
    public Matrix getJacobian()
    {
        return this.x;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Here we can think that although \theta is the parameter vector as a row matrix,
     * it appears inside the norm squared as a column matrix.
     * Then, the Jacobian of f(\theta) in ||f(\theta)||^2 is the identity.
     * From that, we obtain that J^T J = I * I = I
     */
    public Matrix getGaussNewtonMatrix()
    {
        return Matrix.one( this.x.cols() );
    }
    
}
