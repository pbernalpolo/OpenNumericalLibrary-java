package numericalLibrary.manifolds;


import numericalLibrary.types.MatrixReal;



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
     * Returns the Jacobian of the chart, e = phi(x).
     * <p>
     * The Jacobian is returned in the form:
     * ( de_0/dx_0  de_0/dx_1   ... de_0/dx_m )
     * ( de_1/dx_0  de_1/dx_1   ... de_1/dx_m )
     * (  ...        ...             ...      )
     * ( de_n/dx_0  de_n/dx_1   ... de_n/dx_m )
     * 
     * @param x     manifold element at which the Jacobian is evaluated.
     * @return  Jacobian of the chart, e = phi(x).
     */
    public MatrixReal jacobianOfChart( ManifoldElementClass x );
    
    
    /**
     * Returns the Jacobian of the chart inverse, x = phi^{-1}(e).
     * <p>
     * The Jacobian is returned in the form:
     * ( dx_0/de_0  dx_0/de_1   ... dx_0/de_n )
     * ( dx_1/de_0  dx_1/de_1   ... dx_1/de_n )
     * (  ...        ...             ...      )
     * ( dx_m/de_0  dx_m/de_1   ... dx_m/de_n )
     * 
     * @param e     chart element at which the Jacobian is evaluated.
     * @return  Jacobian of the chart inverse, x = phi^{-1}(e).
     */
    public MatrixReal jacobianOfChartInverse( ChartElementClass e );
    
    
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
    public MatrixReal jacobianOfTransitionMap( ManifoldElementClass initialChartSelector , ManifoldElementClass finalChartSelector );
    
}
