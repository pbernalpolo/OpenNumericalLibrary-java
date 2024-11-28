package numericalLibrary.manifolds.unitComplexNumbers.atlases;


import numericalLibrary.types.ComplexNumber;
import numericalLibrary.types.RealNumber;



/**
 * Implements the Modified Rodrigues Parameters projection that maps {@link ComplexNumber}s in the S1 sphere to {@link RealNumber}s in the Euclidean space.
 * <p>
 * The Modified Rodrigues Parameters is a particular case of a stereographic projection in which the center of projection is at the bottom of the sphere.
 * <p>
 * Chart definition:
 * <ul>
 *  <li> e = \phi( z ) = 2 Im(z) / ( 1 + Re(z) )
 *  <li> z = \phi^{-1}( e ) = (  4 - e^2  +  4 e i  ) / ( 4 + e^2 )
 *  <li> Domain: { z \in C : ||z|| = 1 } \ { -1 }
 *  <li> Image: R
 * </ul>
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 * @see <a href>https://en.wikipedia.org/wiki/Stereographic_projection</a>
 */
public class ModifiedRodriguesParametersS1
    extends UnitComplexNumberAtlas
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * <p>
     * Any {@link ComplexNumber} can be mapped to an element in the chart.
     */
    public RealNumber toChartCenteredAtIdentity( ComplexNumber z )
    {
        return new RealNumber( 2.0 * z.im() / ( 1.0 + z.re() ) );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is R: the entire set of real numbers.
     */
    public ComplexNumber toManifoldFromChartCenteredAtIdentity( RealNumber e )
    {
        double eValue = e.toDouble();
        double eSquared = eValue * eValue;
        double alpha = 1.0/( 4.0 + eSquared );
        ComplexNumber z = ComplexNumber.fromRealPartAndImaginaryPart( 4.0 - eSquared , 4.0 * eValue ).scaleInplace( alpha );
        return z;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Any {@link ComplexNumber} can be mapped to an element in the chart, except for z=-1.
     */
    public boolean isContainedInChartCenteredAtIdentityDomain( ComplexNumber z )
    {
        return (  z.re() != -1.0  ||  z.im() != 0.0  );
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
