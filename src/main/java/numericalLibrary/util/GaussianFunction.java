package numericalLibrary.util;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;
import numericalLibrary.types.MatrixReal;



/**
 * Implements a Gaussian function to fit data using a {@link IterativeOptimizationAlgorithm}.
 * <p>
 * Concretely, the Gaussian function is given by:
 *      a exp( -( x - b )^2/( 2 * c^2 )
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Gaussian_function</a>
 */
public class GaussianFunction
    implements ModelFunction<Double>
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
     * {@inheritDoc}
     */
    public void setParameters( MatrixReal theta )
    {
        this.a = theta.entry( 0 , 0 );
        this.b = theta.entry( 1 , 0 );
        this.c = theta.entry( 2 , 0 );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal getParameters()
    {
        MatrixReal output = MatrixReal.empty( 3 , 1 );
        output.setEntry( 0,0 , this.a );
        output.setEntry( 1,0 , this.b );
        output.setEntry( 2,0 , this.c );
        return output;
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
    public MatrixReal getOutput()
    {
        this.clean();
        return MatrixReal.one( 1 ).scaleInplace( a * this.dfda );
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
