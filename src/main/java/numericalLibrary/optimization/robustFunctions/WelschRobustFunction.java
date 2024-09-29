package numericalLibrary.optimization.robustFunctions;



public class WelschRobustFunction
    implements RobustFunction
{
    
    private double kSquared;
    
    public WelschRobustFunction( double kParameter )
    {
        this.kSquared = kParameter * kParameter;
    }
    
    public double f( double xSquared )
    {
        return this.kSquared / 2.0 * ( 1.0 - Math.exp( - xSquared / this.kSquared ) );
    }
    
    public double f1( double xSquared )
    {
        return Math.exp( - xSquared / this.kSquared ) / 2.0;
    }
    
}
