package numericalLibrary.manifolds.unitComplexNumbers.atlases;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.manifolds.AtlasTester;
import numericalLibrary.types.ComplexNumber;
import numericalLibrary.types.RealNumber;



/**
 * Implements test methods for {@link UnitComplexNumberAtlas}.
 */
public abstract class UnitComplexNumberAtlasTester
    implements AtlasTester<ComplexNumber, RealNumber>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public abstract UnitComplexNumberAtlas getAtlas();
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public List<ComplexNumber> getManifoldElementList()
    {
        List<ComplexNumber> output = new ArrayList<ComplexNumber>();
        Random rng = new Random( 42 );
        for( int i=0; i<100; i++ ) {
            output.add( ComplexNumber.random( rng ).normalizeInplace() );
        }
        return output;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public List<RealNumber> getChartElementList()
    {
        List<RealNumber> output = new ArrayList<RealNumber>();
        UnitComplexNumberAtlas atlas = this.getAtlas();
        Random rng = new Random( 42 );
        for( int i=0; i<100; i++ ) {
            RealNumber e = RealNumber.random( rng );
            while( !atlas.isContainedInChartImage( e ) ) {
                e = RealNumber.random( rng );
            }
            output.add( e );
        }
        return output;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that transforming zero from the chart to the manifold results in the identity quaternion.
     */
    @Test
    public void toManifoldFromZeroReturnsIdentity()
    {
        UnitComplexNumberAtlas atlas = this.getAtlas();
        // Obtain the ComplexNumber mapped with the origin.
        ComplexNumber z = atlas.toManifoldFromChartCenteredAtIdentity( RealNumber.zero() );
        // It should be the identity.
        double distance = z.distanceFrom( ComplexNumber.one() );
        assertTrue( distance == 0.0 );
    }
    
    
    /**
     * Tests that {@link Atlas#toManifold(Object)} satisfies the approximation for small deviations from the origin of the chart.
     */
    @Test
    public void toManifoldApproximationIsSatisfied()
    {
        UnitComplexNumberAtlas atlas = this.getAtlas();
        List<RealNumber> chartElementList = this.getChartElementList();
        for( RealNumber e0 : chartElementList ) {
            // Take a random element in the image of the chart,
            // but we want it close to the origin so that we can use the approximation.
            RealNumber e = e0.scaleInplace( 1.0e-3 );
            // Transform it to the manifold.
            ComplexNumber deltaz = atlas.toManifoldFromChartCenteredAtIdentity( e );
            // Check if the approximation is satisfied.
            ComplexNumber delta = ComplexNumber.fromRealPartAndImaginaryPart( 1.0-e.normSquared() , e.toDouble() );
            double distance = deltaz.distanceFrom( delta );
            /*if( distance >= 1.e-5 ) {
                e.print();
                deltaz.print();
                delta.print();
                System.out.println( distance );
            }*/
            // The approximation should be of order O(e^2); we allow one order of magnitude more.
            assertTrue( distance < e.normSquared() * 10.0 );
        }
    }
    
    
    /**
     * Tests that {@link Atlas#toManifold(Object)} satisfies the small angle approximation.
     */
    @Test
    public void toManifoldAnglepproximationIsSatisfied()
    {
        UnitComplexNumberAtlas atlas = this.getAtlas();
        List<RealNumber> chartElementList = this.getChartElementList();
        for( RealNumber e0 : chartElementList ) {
            // Take a random element in the image of the chart,
            // but we want it close to the origin so that we can use the approximation.
            RealNumber e = e0.scaleInplace( 1.0e-3 );
            // Transform it to the manifold.
            ComplexNumber z = atlas.toManifoldFromChartCenteredAtIdentity( e );
            // Check if the angle approximation is satisfied.
            double angle = Math.acos( 1.0 - e.normSquared() );
            /*if( angle > 1.0e-2 ) {
                e.print();
                z.print();
                System.out.println( angle );
            }*/
            // The approximation should be of order O(e); we allow one order of magnitude more.
            assertEquals( angle , z.argument() , e.norm() * 10.0 );
        }
    }
    
    
    /**
     * Tests that {@link Atlas#toManifold(Object)} clips chart elements that are not contained in the chart image.
     */
    @Test
    public void toManifoldClipsVectorsNotContainedInChartImage()
    {
        UnitComplexNumberAtlas atlas = this.getAtlas();
        List<RealNumber> chartElementList = this.getChartElementList();
        for( RealNumber e : chartElementList ) {
            // Obtain the identity complex,
            ComplexNumber zOne = ComplexNumber.one();
            // and a complex that represents a rotation of angle pi.
            ComplexNumber zPi = ComplexNumber.fromModulusAndArgument( 1.0 , Math.PI );
            // Compute the complex mapped with e,
            ComplexNumber z = atlas.toManifoldFromChartCenteredAtIdentity( e );
            // and measure the distance between the rotations represented by z and zOne,
            double thetaOne = z.distanceFrom( zOne );
            // and the distance between the rotations represented by z and zPi.
            double thetaPi = z.distanceFrom( zPi );
            // If we repeatedly scale the element in the chart,
            for( int i=0; i<10; i++ ) {
                e.scaleInplace( 2.0 );
                // we should get thetaOne greater than or equal to the previous one,
                z = atlas.toManifoldFromChartCenteredAtIdentity( e );
                double thetaOneNew = z.distanceFrom( zOne );
                assertTrue( thetaOneNew >= thetaOne );
                thetaOne = thetaOneNew;
                // and we should get thetaPi less than or equal to the previous one.
                double thetaPiNew = z.distanceFrom( zPi );
                assertTrue( thetaPiNew <= thetaPi );
                thetaPi = thetaPiNew;
            }
        }
    }
    
}
