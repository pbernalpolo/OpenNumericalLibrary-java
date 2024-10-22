package numericalLibrary.manifolds;


import numericalLibrary.types.Matrix;



/**
 * {@link DifferentiableAtlas} is an {@link Atlas} whose charts are differentiable.
 * 
 * @param <ManifoldElementClass>    type of manifold elements.
 * @param <ChartElementClass>   type of chart elements.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Differentiable_manifold</a>
 */
public interface DifferentiableAtlas<ManifoldElementClass, ChartElementClass>
    extends Atlas<ManifoldElementClass, ChartElementClass>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the Jacobian of the transition map.
     * <p>
     * The transition map is the function that takes an element in an initial chart, maps it to the manifold, and from the manifold maps it to an element in a final chart.
     * The Jacobian is evaluated at the final chart selector.
     * 
     * @param initialChartSelector  chart element that selects the initial chart.
     * @param finalChartSelector    chart element that selects the final chart.
     * @return  Jacobian of the transition map evaluated at the final chart selector.
     */
    public Matrix jacobianOfTransitionMap( ManifoldElementClass initialChartSelector , ManifoldElementClass finalChartSelector );
    
}
