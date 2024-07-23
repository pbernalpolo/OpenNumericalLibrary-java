package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Represents a function used to model the relation between {@link LevenbergMarquardtEmpiricalPair}s.
 */
public interface LevenbergMarquardtModelFunction<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    public void setParameters( Matrix theta );
    
    public void setInput( T x );
    
    public double getOutput();
    
    public Matrix getJacobian();
    
}
