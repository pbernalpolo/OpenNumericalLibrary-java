package numericalLibrary.manifolds;


/**
 * {@link Atlas} represents a set of charts that map elements from a manifold to the Euclidean space.
 * <p>
 * Charts are used to update a state defined in a manifold using the state correction vector in a Kalman filter framework.
 * 
 * @param <ManifoldElementClass>    type of manifold elements.
 * @param <ChartElementClass>   type of chart elements.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Atlas_(topology)</a>
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 */
public interface Atlas<ManifoldElementClass, ChartElementClass>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the element of the manifold that will be used to select the chart of the atlas.
     * 
     * @param theChartSelector  element of the manifold that will be used to select the chart of the atlas.
     */
    public void setChartSelector( ManifoldElementClass theChartSelector );
    
    
    /**
     * Maps a manifold element to a chart element.
     * <p>
     * This method implements the chart function (usually called phi).
     * The chart used to map manifold elements to chart elements is selected using the chart selector specified in {@link #setChartSelector(Object)}.
     * 
     * @param x     manifold element to be mapped to a chart element.
     * @return  chart element mapped from the manifold element.
     */
    public ChartElementClass toChart( ManifoldElementClass x );
    
    
    /**
     * Maps a chart element to a manifold element.
     * <p>
     * This method implements the inverse of the chart function (usually called phi^{-1}).
     * The chart used to map manifold elements to chart elements is selected using the chart selector specified in {@link #setChartSelector(Object)}.
     * 
     * @param e     chart element to be mapped to a manifold element.
     * @return  manifold element mapped from the chart element.
     */
    public ManifoldElementClass toManifold( ChartElementClass e );
    
    
    /**
     * Returns true if the manifold element is contained in the chart domain.
     * <p>
     * The chart whose domain is checked is selected using the chart selector specified in {@link #setChartSelector(Object)}.
     * 
     * @param x     manifold element on which the check is performed.
     * @return  true if the manifold element is contained in the chart domain.
     */
    public boolean isContainedInChartDomain( ManifoldElementClass x );
    
    
    /**
     * Returns true if the chart element is contained in the chart image.
     * <p>
     * The chart whose image is checked is selected using the chart selector specified in {@link #setChartSelector(Object)}.
     * 
     * @param e     chart element on which the check is performed.
     * @return  true if the chart element is contained in the chart image.
     */
    public boolean isContainedInChartImage( ChartElementClass e );
    
}
