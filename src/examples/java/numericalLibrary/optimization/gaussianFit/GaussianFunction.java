package numericalLibrary.optimization.gaussianFit;


import numericalLibrary.types.MatrixReal;



/**
 * Implements a parameterized Gaussian function.
 * <p>
 * Concretely, the Gaussian function is given by:
 *      a exp( -( x - b )^2/( 2 * c^2 )
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Gaussian_function</a>
 */
public class GaussianFunction
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Height of the curve's peak.
     */
    private double a;
    
    /**
     * Position of the peak.
     */
    private double b;
    
    /**
     * Standard deviation; controls the width of the "bell".
     */
    private double c;
    
    /**
     * Independent variable set by {@link #setInput(Double)}.
     */
    private double x;
    
    /**
     * Difference between {@link #x} and {@link #b}: that is  x - b .
     */
    private double diff;
    
    /**
     * One over {@link #c} squared: that is  1/c^2 .
     */
    private double oneOverCSquared;
    
    /**
     * Derivative of the function with respect to the parameter {@link #a}.
     */
    private double dfda;
    
    /**
     * Flag used to implement the Dirty Flag Optimization Pattern.
     */
    private boolean dirtyFlag;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the height of the curve's peak.
     * 
     * @param height	height to be set.
     */
    public void setHeight( double height )
    {
    	this.a = height;
    }
    
    
    /**
     * Returns the height of the curve's peak.
     * 
     * @return	height of the curve's peak.
     */
    public double getHeight()
    {
    	return this.a;
    }
    
    
    /**
     * Sets the position of the peak.
     * 
     * @param center	position of the peak.
     */
    public void setCenter( double center )
    {
    	this.b = center;
    }
    
    
    /**
     * Returns the position of the peak.
     * 
     * @return	position of the peak.
     */
    public double getCenter()
    {
    	return this.b;
    }
    
    
    /**
     * Sets the standard deviation of the Gaussian curve.
     * 
     * @param standardDeviation		standard deviation of the Gaussian curve.
     */
    public void setStandardDeviation( double standardDeviation )
    {
    	this.c = standardDeviation;
    }
    
    
    /**
     * Returns the standard deviation of the Gaussian curve.
     * 
     * @return	standard deviation of the Gaussian curve.
     */
    public double getStandardDeviation()
    {
    	return this.c;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void setInput( Double input )
    {
        this.x = input;
        this.dirtyFlag = true;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double getOutput()
    {
        this.clean();
        return a * this.dfda;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal getJacobian()
    {
        this.clean();
        MatrixReal J = MatrixReal.empty( 1 , 3 );
        J.setEntry( 0,0 , dfda );
        double dfdb = a * dfda * diff * oneOverCSquared;
        J.setEntry( 0,1 , dfdb );
        J.setEntry( 0,2 , dfdb * diff / c );
        return J;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Implements the Dirty Flag Optimization Pattern.
     * <p>
     * It computes common stuff and avoids to recompute it in other methods.
     */
    private void clean()
    {
        if( !this.dirtyFlag )
        {
            return;
        }
        diff = x - b;
        oneOverCSquared = 1.0 / ( c * c );
        this.dfda = Math.exp( -diff * diff * oneOverCSquared / 2.0 );
        this.dirtyFlag = false;
    }
    
}
