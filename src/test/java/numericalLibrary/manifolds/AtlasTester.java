package numericalLibrary.manifolds;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import numericalLibrary.algebraicStructures.MetricSpaceElement;



/**
 * Implements test methods for {@link Atlas}.
 * 
 * @param <ManifoldElement>     concrete type of manifold elements.
 * @param <ChartElement>        concrete type of chart elements.
 */
public interface AtlasTester<ManifoldElement extends MetricSpaceElement<ManifoldElement>, ChartElement extends MetricSpaceElement<ChartElement>>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the {@link Atlas} to be tested.
     * 
     * @return  {@link Atlas} to be tested.
     */
    public Atlas<ManifoldElement, ChartElement> getAtlas();
    
    
    /**
     * Returns a list of manifold elements that will be used to perform the tests.
     * <p>
     * The list has the same size as the one returned by {@link #getChartElementList()}.
     * 
     * @return  list of manifold elements that will be used to perform the tests.
     */
    public List<ManifoldElement> getManifoldElementList();
    
    
    /**
     * Returns a list of chart elements that will be used to perform the tests.
     * <p>
     * The list has the same size as the one returned by {@link #getManifoldElementList()}.
     * 
     * @return  list of chart elements that will be used to perform the tests.
     */
    public List<ChartElement> getChartElementList();
    
    
    
    ////////////////////////////////////////////////////////////////
    // DEFAULT TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link #getManifoldElementList()} and {@link #getChartElementList()} return lists with same number of elements.
     */
    @Test
    default void manifoldElementListAndChartElementListHaveSameSize()
    {
        assertEquals( this.getManifoldElementList().size() , this.getChartElementList().size() );
    }
    
    
    /**
     * Tests that {@link Atlas#toChart(Object)} returns an element in the chart image.
     */
    @Test
    default void toChartReturnsElementInChartImage()
    {
        Atlas<ManifoldElement, ChartElement> atlas = this.getAtlas();
        List<ManifoldElement> manifoldElementList = this.getManifoldElementList();
        List<ManifoldElement> chartSelectorList = this.getManifoldElementList();
        Collections.shuffle( chartSelectorList );
        for( int i=0; i<manifoldElementList.size(); i++ ) {
            // Set chart selector.
            ManifoldElement chartSelector = chartSelectorList.get( i );
            atlas.setChartSelector( chartSelector );
            // Take element in the domain of the chart.
            ManifoldElement x = manifoldElementList.get( i );
            // We transform it to the chart.
            ChartElement e = atlas.toChart( x );
            // We check that it is in the chart image.
            /*if( !atlas.isContainedInChartImage( e ) ) {
                x.print();
                e.print();
            }*/
            assertTrue( atlas.isContainedInChartImage( e ) );
        }
    }
    
    
    /**
     * Tests that mapping an element in the image of the chart to an element in the manifold, and then back to the chart image results in the same element we started with.
     */
    @Test
    default void chartToManifoldToChartResultsInitialChartElement()
    {
        Atlas<ManifoldElement, ChartElement> atlas = this.getAtlas();
        List<ChartElement> chartElementList = this.getChartElementList();
        List<ManifoldElement> chartSelectorList = this.getManifoldElementList();
        for( int i=0; i<chartElementList.size(); i++ ) {
            // Set chart selector.
            ManifoldElement chartSelector = chartSelectorList.get( i );
            atlas.setChartSelector( chartSelector );
            // Take element in the image of the chart,
            ChartElement e0 = chartElementList.get( i );
            if( !atlas.isContainedInChartImage( e0 ) ) {
                continue;
            }
            // transform it to the manifold,
            ManifoldElement x = atlas.toManifold( e0 );
            // and then back to the chart.
            ChartElement e = atlas.toChart( x );
            // We should obtain the same element we started with.
            double distance = e.distanceFrom( e0 );
            assertTrue( distance < 1.0e-8 );
        }
    }
    
    
    /**
     * Tests that mapping an element in the domain of the chart to an element in the image of the chart, and then back to the manifold results in the same element we started with.
     */
    @Test
    default void manifoldToChartToManifoldResultsInitialManifoldElement()
    {
        Atlas<ManifoldElement, ChartElement> atlas = this.getAtlas();
        List<ManifoldElement> manifoldElementList = this.getManifoldElementList();
        List<ManifoldElement> chartSelectorList = this.getManifoldElementList();
        Collections.shuffle( chartSelectorList );
        for( int i=0; i<manifoldElementList.size(); i++ ) {
            // Set chart selector.
            ManifoldElement chartSelector = chartSelectorList.get( i );
            atlas.setChartSelector( chartSelector );
            // If we take an element in the domain of the chart,
            ManifoldElement x0 = manifoldElementList.get( i );
            if( !atlas.isContainedInChartDomain( x0 ) ) {
                continue;
            }
            // we transform it to the chart,
            ChartElement e = atlas.toChart( x0 );
            // and we transform it back to the manifold
            ManifoldElement x = atlas.toManifold( e );
            // then, we should obtain the same element we started with.
            double distance = x.distanceFrom( x0 );
            /*if( distance >= 1.0e-7 ) {
                chartSelector.print();
                x0.print();
                e.print();
                x.print();
                System.out.println( distance );
            }*/
            assertTrue( distance < 1.0e-7 );
        }
    }
    
}
