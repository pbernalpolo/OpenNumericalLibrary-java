package numericalLibrary.manifolds.unitComplexNumbers.atlases;


import numericalLibrary.types.ComplexNumber;
import numericalLibrary.types.RealNumber;



/**
 * Implements the Orthographic projection that maps {@link ComplexNumber}s in the S1 sphere to {@link RealNumber}s in the Euclidean space.
 * <p>
 * The Orthographic projection is a particular case of a stereographic projection in which the center of projection is at -infinity in the imaginary axis.
 * <p>
 * Chart definition:
 * <ul>
 *  <li> e = \phi( z ) = Im(z)
 *  <li> z = \phi^{-1}( e ) =  \sqrt{ 1 - e^2 }  +  e i
 *  <li> Domain: { z \in C : ||z|| = 1, Re(z) > 0 }
 *  <li> Image: [-1,1]
 * </ul>
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 * @see <a href>https://en.wikipedia.org/wiki/Stereographic_projection</a>
 * @see <a href>https://en.wikipedia.org/wiki/Orthographic_projection</a>
 */
public class OrthographicS1
    extends UnitComplexNumberAtlas
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * <p>
     * The domain of the chart are the unit {@link ComplexNumber}s with real part greater or equal than 0.
     * Any input outside of the chart domain will be saturated.
     * Saturating to the border of the domain makes the map defined by the chart bijective:
     * each {@link ComplexNumber} within the domain is mapped exactly with one real number.
     */
    public RealNumber toChartCenteredAtIdentity( ComplexNumber z )
    {
        double e = z.im();
        if( !this.isContainedInChartCenteredAtIdentityDomain( z ) ) {
            // Clip z to be in the domain of the chart.
            return new RealNumber( Math.signum( e ) );
        }
        return new RealNumber( e );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is the interval [-1,1].
     * Any input outside of the chart image will be saturated.
     * Saturating to the image interval makes the map defined by the chart bijective:
     * each {@link RealNumber} within the chart image is mapped exactly with one 2d rotation transformation, or one {@link ComplexNumber} in the hemisphere of the S1 sphere with positive real part.
     */
    public ComplexNumber toManifoldFromChartCenteredAtIdentity( RealNumber e )
    {
        double eValue = e.toDouble();
        if( !this.isContainedInChartImage( e ) ) {
            // Clip e to be in the image of the chart.
            eValue = Math.signum( eValue );
        }
        ComplexNumber z = ComplexNumber.fromRealPartAndImaginaryPart(
                Math.sqrt( 1.0 - eValue * eValue ) ,
                eValue );
        return z;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The domain is S1 with real part greater or equal than 0.
     */
    public boolean isContainedInChartCenteredAtIdentityDomain( ComplexNumber z )
    {
        return ( z.re() >= 0.0 );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is the interval [-1,1].
     */
    public boolean isContainedInChartImage( RealNumber e )
    {
        double eValue = e.toDouble();
        return (  -1.0 <= eValue  &&  eValue <= 1.0  );
    }
    
}
