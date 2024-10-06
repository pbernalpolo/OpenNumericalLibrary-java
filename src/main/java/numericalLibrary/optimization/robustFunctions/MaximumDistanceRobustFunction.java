package numericalLibrary.optimization.robustFunctions;



public class MaximumDistanceRobustFunction
    implements RobustFunction
{
    private double k;
    private double kSquared;
    
    
    public MaximumDistanceRobustFunction( double kParameter )
    {
        this.k = kParameter;
        this.kSquared = kParameter * kParameter;
    }
    
    
    public double f( double xSquared )
    {
        if( xSquared < this.kSquared ) {
            return xSquared;
        } else {
            return this.kSquared;
        }
    }
    
    
    public double f1( double xSquared )
    {
        if( Math.sqrt( xSquared ) < this.k ) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
    
}
