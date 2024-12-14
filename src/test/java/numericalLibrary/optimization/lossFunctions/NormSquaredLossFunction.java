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
     * Last computed cost.
     */
    public double cost;
    
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
        return this.cost;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGradient()
    {
        return this.gradient;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Matrix getGaussNewtonMatrix()
    {
        return this.gaussNewtonMatrix;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void allocateGradient()
    {
        this.gradient = this.x.copy();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The Jacobian of f(\theta) in 1/2 ||f(\theta)||^2 is the identity.
     * From that, we obtain that J^T J = I * I = I
     */
    public void allocateGaussNewtonMatrix()
    {
        this.gaussNewtonMatrix = Matrix.one( this.x.rows() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateCost()
    {
        this.cost = this.x.normFrobeniusSquared();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The Jacobian of f(\theta) in ||f(\theta)||^2 is the identity.
     * From that, we obtain that the gradient of 1/2 ||f(\theta)||^2 is
     * J^T * f(\theta) = I * \theta = \theta
     */
    public void updateCostAndGradient()
    {
        this.updateCost();
        this.gradient.setTo( this.x );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void updateCostGradientAndGaussNewtonMatrix()
    {
        this.updateCostAndGradient();
        // Gauss-Newton matrix is always the identity.
    }
    
}
