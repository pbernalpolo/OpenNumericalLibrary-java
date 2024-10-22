package numericalLibrary.manifolds.unitQuaternions.atlases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.manifolds.AtlasTester;
import numericalLibrary.types.Quaternion;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;

/**
 * Implements test methods for {@link UnitQuaternionAtlas}.
 */
public abstract class UnitQuaternionAtlasTester
    implements AtlasTester<UnitQuaternion, Vector3>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public abstract UnitQuaternionAtlas getAtlas();
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public List<UnitQuaternion> getManifoldElementList()
    {
        List<UnitQuaternion> output = new ArrayList<UnitQuaternion>();
        Random rng = new Random( 42 );
        for( int i=0; i<100; i++ ) {
            output.add( UnitQuaternion.random( rng ) );
        }
        return output;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public List<Vector3> getChartElementList()
    {
        List<Vector3> output = new ArrayList<Vector3>();
        UnitQuaternionAtlas atlas = this.getAtlas();
        Random rng = new Random( 42 );
        for( int i=0; i<100; i++ ) {
            Vector3 e = Vector3.random( rng );
            while( !atlas.isContainedInChartImage( e ) ) {
                e = Vector3.random( rng );
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
        UnitQuaternionAtlas atlas = this.getAtlas();
        // Obtain the quaternion mapped with the origin.
        UnitQuaternion q = atlas.toManifoldFromChartCenteredAtIdentity( Vector3.zero() );
        // It should be the identity.
        double distance = q.distanceFrom( UnitQuaternion.one() );
        assertTrue( distance == 0.0 );
    }
    
    
    /**
     * Tests that q and -q are both mapped to the same chart element.
     * <p>
     * This test makes sense because both q and -q represent the same rotation transformation, and for that reason, the same chart element.
     */
    @Test
    public void toChartMapsQAndMinusQToSameChartElement()
    {
        UnitQuaternionAtlas atlas = this.getAtlas();
        List<UnitQuaternion> manifoldElementList = this.getManifoldElementList();
        for( UnitQuaternion q : manifoldElementList ) {
            // Take a random element in the domain of the chart.
            // The same rotation must be build with the opposite quaternion from the first one.
            UnitQuaternion qOpposite = q.opposite();
            // Transform both to the chart
            Vector3 e = atlas.toChartCenteredAtIdentity( q );
            Vector3 eOpposite = atlas.toChartCenteredAtIdentity( qOpposite );
            // Check that both q and -q are mapped to the same chart element.
            double distance = eOpposite.distanceFrom( e );
            assertTrue( distance < 1.0e-100 );
        }
    }
    
    
    /**
     * Tests that {@link Atlas#toManifold(Object)} satisfies the approximation for small deviations from the origin of the chart.
     * 
     * @see     eq. (10) in "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
     */
    @Test
    public void toManifoldQuaternionApproximationIsSatisfied()
    {
        UnitQuaternionAtlas atlas = this.getAtlas();
        List<Vector3> chartElementList = this.getChartElementList();
        for( Vector3 e0 : chartElementList ) {
            // Take a random element in the image of the chart,
            // but we want it close to the origin so that we can use the approximation.
            Vector3 e = e0.scaleInplace( 1.0e-3 );
            // Transform it to the manifold.
            UnitQuaternion deltaq = atlas.toManifoldFromChartCenteredAtIdentity( e );
            // Check if the approximation is satisfied.
            Quaternion delta = Quaternion.fromScalarAndVectorPart( 1.0-e.normSquared()/8.0 , e.scale(0.5) );
            double distance = deltaq.distanceFrom( UnitQuaternion.fromNormalizedQuaternion( delta ) );
            // The approximation should be of order O(||e||^2); we allow one order of magnitude more.
            assertTrue( distance < e.normSquared() * 10.0 );
        }
    }
    
    
    /**
     * Tests that {@link Atlas#toManifold(Object)} satisfies the small angle approximation.
     * 
     * @see     eq. (6) and (10) in "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
     */
    @Test
    public void toManifoldAngleAxispproximationIsSatisfied()
    {
        UnitQuaternionAtlas atlas = this.getAtlas();
        List<Vector3> chartElementList = this.getChartElementList();
        for( Vector3 e0 : chartElementList ) {
            // Take a random element in the image of the chart,
            // but we want it close to the origin so that we can use the approximation.
            Vector3 e = e0.scaleInplace( 1.0e-3 );
            // Transform it to the manifold.
            UnitQuaternion q = atlas.toManifoldFromChartCenteredAtIdentity( e );
            // Check if the approximation is satisfied.
            // First the angle;
            double angle = 2.0 * Math.acos( 1.0 - e.normSquared()/8.0 );
            assertEquals( angle , q.angle() , 1.0e-6 );
            // then the axis.
            Vector3 axisExpected = e.normalize();
            Vector3 axisActual = q.axis();
            double distanceAxis = axisExpected.distanceFrom( axisActual );
            // The approximation should be of order O(e); we allow one order of magnitude more.
            assertTrue( distanceAxis < e.norm() * 10.0 );
        }
    }
    
    
    /**
     * Tests that {@link Atlas#toManifold(Object)} clips chart elements that are not contained in the chart image.
     */
    @Test
    public void toManifoldClipsVectorsNotContainedInChartImage()
    {
        UnitQuaternionAtlas atlas = this.getAtlas();
        List<Vector3> chartElementList = this.getChartElementList();
        for( Vector3 e : chartElementList ) {
            // Obtain the identity quaternion,
            UnitQuaternion qOne = UnitQuaternion.one();
            // and a quaternion that represents a rotation of angle pi around the axis defined by e.
            UnitQuaternion qPi = UnitQuaternion.fromRotationVector( e.scale( Math.PI/e.norm() ) );
            // Compute the quaternion mapped with e,
            UnitQuaternion q = atlas.toManifoldFromChartCenteredAtIdentity( e );
            // and measure the distance between the rotations represented by q and qOne,
            double thetaOne = q.distanceFrom( qOne );
            // and the distance between the rotations represented by q and qPi.
            double thetaPi = q.distanceFrom( qPi );
            // If we repeatedly scale the element in the chart,
            for( int i=0; i<10; i++ ) {
                e.scaleInplace( 2.0 );
                // we should get thetaOne greater than or equal to the previous one,
                q = atlas.toManifoldFromChartCenteredAtIdentity( e );
                double thetaOneNew = q.distanceFrom( qOne );
                assertTrue( thetaOneNew >= thetaOne );
                thetaOne = thetaOneNew;
                // and we should get thetaPi less than or equal to the previous one.
                double thetaPiNew = q.distanceFrom( qPi );
                assertTrue( thetaPiNew <= thetaPi );
                thetaPi = thetaPiNew;
            }
        }
    }
    
}
