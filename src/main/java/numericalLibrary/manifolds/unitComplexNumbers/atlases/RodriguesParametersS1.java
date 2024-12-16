package numericalLibrary.manifolds.unitComplexNumbers.atlases;


import numericalLibrary.types.ComplexNumber;
import numericalLibrary.types.RealNumber;



/**
 * Implements the Rodrigues Parameters projection that maps {@link ComplexNumber}s in the S2 sphere to {@link RealNumber}s in the Euclidean space.
 * <p>
 * The Rodrigues Parameters projection is a particular case of a stereographic projection in which the center of projection is at infinity in the center of the sphere.
 * <p>
 * Chart definition:
 * <ul>
 *  <li> e = \phi( z ) = Im(z) / Re(z)
 *  <li> z = \phi^{-1}( e ) = (  1  +  e i  ) / \sqrt( 1 + e^2 )
 *  <li> Domain: { z \in C : ||z|| = 1, Re(z) > 0 }
 *  <li> Image: R
 * </ul>
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 * @see <a href>https://en.wikipedia.org/wiki/Stereographic_projection</a>
 */
public class RodriguesParametersS1
    extends UnitComplexNumberAtlas
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE CONSTANTS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Small value used to define the domain of the chart.
     */
    private static final double EPSILON = 1.0e-4;
    
    /**
     * Value of e obtained for a {@link ComplexNumber} that is outside of the domain of the chart and has been saturated.
     * 
     * @see #toChartCenteredAtIdentity(ComplexNumber)
     */
    private static final double E_SATURATED = Math.sqrt( 1.0 - EPSILON * EPSILON )/EPSILON;
    
    
    
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
        if( !this.isContainedInChartCenteredAtIdentityDomain( z ) ) {
            // Clip z to be in the domain of the chart.
            return new RealNumber( Math.signum( z.im() ) * E_SATURATED );
        }
        return new RealNumber( z.im()/z.re() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image is R, so there is no need to saturate the input {@link RealNumber}.
     */
    public ComplexNumber toManifoldFromChartCenteredAtIdentity( RealNumber e )
    {
        double eValue = e.toDouble();
        double alpha = 1.0/Math.sqrt( 1.0 + eValue * eValue );
        return ComplexNumber.fromRealPartAndImaginaryPart(
                1.0 ,
                eValue )
                .scaleInplace( alpha );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The domain is S1 with real part greater than 0.
     * Otherwise, z and -z would be mapped with the same element in the Euclidean space.
     * Using {@link #EPSILON} to define the domain avoids the points in the domain for which the chart is not defined.
     */
    public boolean isContainedInChartCenteredAtIdentityDomain( ComplexNumber z )
    {
        return ( z.re() >= EPSILON );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is R: the entire set of real numbers.
     */
    public boolean isContainedInChartImage( RealNumber e )
    {
        return true;
    }
    
}
