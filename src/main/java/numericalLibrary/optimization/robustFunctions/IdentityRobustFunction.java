package numericalLibrary.optimization.robustFunctions;



public class IdentityRobustFunction
    implements RobustFunction
{
    
    
    
    public double f( double xSquared )
    {
        return xSquared;
    }
    
    public double f1( double xSquared )
    {
        return 1.0;
    }
    
}
