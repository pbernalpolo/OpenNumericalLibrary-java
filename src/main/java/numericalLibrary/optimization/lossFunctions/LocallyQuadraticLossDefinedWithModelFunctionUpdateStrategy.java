package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;



/**
 * Represents a strategy used to update the internal representation of an {@link EfficientLocallyQuadraticLossDefinedWithModelFunction}.
 * 
 * @param <T>   concrete type of inputs to the {@link ModelFunction} defined within {@link EfficientLocallyQuadraticLossDefinedWithModelFunction}.
 */
interface LocallyQuadraticLossDefinedWithModelFunctionUpdateStrategy<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Updates the cost, gradient, and Gauss-Newton matrix of the {@link EfficientLocallyQuadraticLossDefinedWithModelFunction}.
     * 
     * @param loss  {@link EfficientLocallyQuadraticLossDefinedWithModelFunction} to be updated.
     */
    public abstract void update( EfficientLocallyQuadraticLossDefinedWithModelFunction<T> loss );
    
}
