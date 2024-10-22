package numericalLibrary.manifolds.unitComplexNumbers.atlases;


import numericalLibrary.types.ComplexNumber;
import numericalLibrary.types.RealNumber;



/**
 * Implements the Exponential Map projection that maps {@link ComplexNumber}s in the S1 sphere to {@link RealNumber}s in the Euclidean space.
 * <p>
 * The exponential map of the unit {@link ComplexNumber} manifold relates each {@link ComplexNumber} that represents a 2d rotation transformation to the angle that represents such rotation.
 * <p>
 * Chart definition:
 * <ul>
 *  <li> e = \phi( z ) = atan2( Im(z) , Re(z) )
 *  <li> z = \phi^{-1}( e ) =  cos(e)  +  i sin(e)
 *  <li> Domain: { z \in C : ||z|| = 1 }
 *  <li> Image: (-pi,pi]
 * </ul>
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 * @see <a href>https://en.wikipedia.org/wiki/Exponential_map_(Riemannian_geometry)</a>
 */
public class ExponentialMapS1
    extends UnitComplexNumberAtlas
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE CONSTANTS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Maximum allowed norm for chart elements to be transformed by {@link #toManifoldFromChartCenteredAtIdentity(RealNumber)}.
     * The {@link #MAX_NORM} value comes from the image of the chart being the ball of radius pi.
     * Note that both pi and -pi rotations represent the same 1d rotation, so we will choose as domain the interval (-pi,pi] for the chart to be bijective.
     * 
     * @see #toManifoldFromChartCenteredAtIdentity(RealNumber)
     */
    private static final double MAX_NORM = Math.PI;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * <p>
     * Any {@link UnitQuaternion} can be mapped to an element in the chart.
     * This is possible because both q and -q represent the same rotation transformation.
     */
    public RealNumber toChartCenteredAtIdentity( ComplexNumber z )
    {
        return new RealNumber( z.argument() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is the interval (-pi,pi].
     * Any input outside of the chart image will be saturated.
     * Saturating to {@link Math#PI} makes the map defined by the chart bijective:
     * each {@link RealNumber} within the chart image is mapped exactly with one 2d rotation transformation, or one {@link ComplexNumber} in the unit circle.
     */
    public ComplexNumber toManifoldFromChartCenteredAtIdentity( RealNumber e )
    {
        if( !this.isContainedInChartImage( e ) ) {
            // Clip the norm to be in the image of the chart.
            e.setValue( MAX_NORM );
        }
        return ComplexNumber.fromModulusAndArgument( 1.0 , e.toDouble() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Any {@link ComplexNumber} can be mapped to an element in the chart.
     * This is possible because we choose the domain to be the interval (-pi,pi] so the chart is bijective.
     */
    public boolean isContainedInChartCenteredAtIdentityDomain( ComplexNumber z )
    {
        return true;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is the interval (-pi,pi] so the chart is bijective.
     */
    public boolean isContainedInChartImage( RealNumber e )
    {
        double eValue = e.toDouble();
        return ( -MAX_NORM < eValue  &&  eValue <= MAX_NORM );
    }
    
}
