package numericalLibrary.util;


import numericalLibrary.optimization.LevenbergMarquardtModelFunction;
import numericalLibrary.types.Matrix;



public class GaussianFunction
    implements LevenbergMarquardtModelFunction<Double>
{
    
    private double a;
    private double b;
    private double c;
    private double x;
    private double diff;
    private double oneOverCSquared;
    private double dfda;
    
    private boolean dirtyFlag;

    
    public void setParameters( Matrix theta )
    {
        this.a = theta.entry( 0 , 0 );
        this.b = theta.entry( 1 , 0 );
        this.c = theta.entry( 2 , 0 );
    }
    
    
    public void setInput( Double input )
    {
        this.x = input;
        this.dirtyFlag = true;
    }
    
    
    public double getOutput()
    {
        this.clean();
        return ( a * this.dfda );
    }
    
    
    public Matrix getJacobian()
    {
        this.clean();
        Matrix J = Matrix.empty( 1 , 3 );
        J.setEntry( 0,0 , dfda );
        double dfdb = a * dfda * diff * oneOverCSquared;
        J.setEntry( 0,1 , dfdb );
        J.setEntry( 0,2 , dfdb * diff / c );
        return J;
    }
    
    
    private void clean()
    {
        if( this.dirtyFlag )
        {
            diff = x - b;
            oneOverCSquared = 1.0 / ( c * c );
            this.dfda = Math.exp( -diff * diff * oneOverCSquared / 2.0 );
            this.dirtyFlag = false;
        }
    }
    
}
