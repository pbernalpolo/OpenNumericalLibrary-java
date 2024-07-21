package numericalLibrary.types;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.algebraicStructures.MetricSpaceElementTester;
import numericalLibrary.algebraicStructures.MultiplicativeGroupElementTester;



/**
 * Implements test methods for {@link UnitQuaternion}.
 */
class UnitQuaternionTest
    implements
        MultiplicativeGroupElementTester<UnitQuaternion>,
        MetricSpaceElementTester<UnitQuaternion>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    private Random randomNumberGenerator;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    public UnitQuaternionTest()
    {
        this.randomNumberGenerator = new Random( 42 );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public List<UnitQuaternion> getElementList()
    {
        List<UnitQuaternion> output = new ArrayList<UnitQuaternion>();
        output.add( UnitQuaternion.one() );
        output.add( UnitQuaternion.one() );
        output.add( UnitQuaternion.i() );
        output.add( UnitQuaternion.i() );
        output.add( UnitQuaternion.j() );
        output.add( UnitQuaternion.j() );
        output.add( UnitQuaternion.k() );
        output.add( UnitQuaternion.k() );
        Random randomNumberGenerator = new Random( 42 );
        for( int i=0; i<1000; i++ ) {
            UnitQuaternion r = UnitQuaternion.random( randomNumberGenerator );
            output.add( r );
            output.add( r.copy() );
        }
        return output;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    @Test
    void distanceToTest()
    {
        List<UnitQuaternion> luq1 = this.getUnitQuaternionList( 1 );
        List<UnitQuaternion> luq2 = this.getUnitQuaternionList( 2 );
        List<UnitQuaternion> luq3 = this.getUnitQuaternionList( 3 );
        for( int n=0; n<luq1.size(); n++ ) {
            UnitQuaternion q = luq1.get( n );
            UnitQuaternion p = luq2.get( n );
            UnitQuaternion r = luq3.get( n );
            // distance between an element and itself is zero
            assertEquals( 0.0 , q.distanceFrom( q ) , 1.0e-7 );
            assertEquals( 0.0 , p.distanceFrom( p ) , 1.0e-7 );
            // distance between 2 different elements is positive or zero
            assertTrue( q.distanceFrom( p ) >= 0.0 );
            // distance is symmetric: d(x,y) = d(y,x)
            assertEquals( q.distanceFrom( p ) , p.distanceFrom( q ) );
            // triangle inequality holds: d(x,z) <= d(x,y) + d(y,z)
            assertTrue( q.distanceFrom( r ) <= q.distanceFrom( p ) + p.distanceFrom( r ) );
            // distance between q and -q is 0
            assertEquals( 0.0 , q.distanceFrom( q.opposite() ) , 1.0e-7 );
        }
    }
    
    
    @Test
    void toRotationVectorBehavior()
    {
        // quaternion 1 -> null rotation vector
        assertTrue( UnitQuaternion.one().toRotationVector().equals( Vector3.zero() ) );
        // quaternion i -> rotation of pi around x
        assertTrue( UnitQuaternion.i().toRotationVector().equals( Vector3.i().scaleInplace( Math.PI ) ) );
        // quaternion j -> rotation of pi around y
        assertTrue( UnitQuaternion.j().toRotationVector().equals( Vector3.j().scaleInplace( Math.PI ) ) );
        // quaternion k -> rotation of pi around z
        assertTrue( UnitQuaternion.k().toRotationVector().equals( Vector3.k().scaleInplace( Math.PI ) ) );
        // quaternion -1 -> null rotation vector
        assertTrue( UnitQuaternion.one().toRotationVector().equals( Vector3.zero() ) );
        // quaternion i^{-1} -> rotation of -pi around x
        assertTrue( UnitQuaternion.i().inverseMultiplicativeInplace().toRotationVector().equals( Vector3.i().scaleInplace( -Math.PI ) ) );
        // quaternion j^{-1} -> rotation of -pi around y
        assertTrue( UnitQuaternion.j().inverseMultiplicativeInplace().toRotationVector().equals( Vector3.j().scaleInplace( -Math.PI ) ) );
        // quaternion k^{-1} -> rotation of -pi around z
        assertTrue( UnitQuaternion.k().inverseMultiplicativeInplace().toRotationVector().equals( Vector3.k().scaleInplace( -Math.PI ) ) );
    }
    
    
    @Test
    void toRotationVectorCompatibleWithAxisAngle()
    {
        List<UnitQuaternion> luq = this.getUnitQuaternionList( 1 );
        for( int n=0; n<luq.size(); n++ ) {
            UnitQuaternion q = luq.get( n );
            Vector3 axisAngle = q.axis().scale( q.angle() );
            Vector3 rv = q.toRotationVector();
            assertTrue( axisAngle.equalsApproximately( rv , 1.0e-12 ) );
        }
    }
    
    
    @Test
    void toRotationVectorReturnsSameVectorFromQAndMinusQTest()
    {
        List<UnitQuaternion> luq = this.getUnitQuaternionList( 1 );
        for( int n=0; n<luq.size(); n++ ) {
            // q and -q produce the same rotation vector
            UnitQuaternion q = luq.get( n );
            UnitQuaternion qopposite = q.opposite();
            Vector3 rv1 = q.toRotationVector();
            Vector3 rv2 = qopposite.toRotationVector();
            assertTrue( rv1.equals( rv2 ) );
        }
    }
    
    
    @Test
    void toRotationVectorCompatibleWithFromRotationVector()
    {
        List<Vector3> lrv = getVectorList( 1 );
        for(int n=0; n<lrv.size(); n++) {
            // vector --fromRotationVector--> quaternion --toRotationVector--> vector
            Vector3 rv0 = Vector3.random( this.randomNumberGenerator );
            while( rv0.norm() > Math.PI ) {
                rv0 = Vector3.random( this.randomNumberGenerator );
            }
            UnitQuaternion q = UnitQuaternion.fromRotationVector( rv0 );
            Vector3 rv = q.toRotationVector();
            assertTrue( rv.equalsApproximately( rv0 , 1.0e-14 ) );
        }
        List<UnitQuaternion> luq = this.getUnitQuaternionList( 1 );
        for( int n=0; n<luq.size(); n++ ) {
            // quaternion --toRotationVector--> vector --fromRotationVector--> quaternion
            UnitQuaternion q0 = luq.get( n );
            Vector3 rv = q0.toRotationVector();
            UnitQuaternion q = UnitQuaternion.fromRotationVector( rv );
            assertTrue( q.equalsApproximately( q0 , 1.0e-7 ) );
        }
    }
    
    
    @Test
    void fromAngleAxisCompatibleWithAngleAndAxis()
    {
        List<UnitQuaternion> luq = this.getUnitQuaternionList( 1 );
        for( int n=0; n<luq.size(); n++ ) {
            UnitQuaternion q0 = luq.get( n );
            double angle = q0.angle();
            Vector3 axis = q0.axis();
            UnitQuaternion q = UnitQuaternion.fromAngleAxis( angle , axis );
            assertTrue( q.equalsApproximately( q0 , 1.0e-7 ) );
        }
    }
    
    
    @Test
    void toRotationMatrixCompatibleWithFromRotationMatrix()
    {
        List<UnitQuaternion> luq = this.getUnitQuaternionList( 1 );
        for( int n=0; n<luq.size(); n++ ) {
            UnitQuaternion q0 = luq.get( n );
            Matrix R = q0.toRotationMatrix();
            UnitQuaternion q = UnitQuaternion.fromRotationMatrix( R );
            assertTrue( q.equalsApproximately( q0 , 1.0e-7 ) );
        }
    }
    
    
    @Test
    void thatRotatesFirstToSecondVector()
    {
        List<Vector3> lrv1 = getVectorList( 1 );
        List<Vector3> lrv2 = getVectorList( 2 );
        for(int n=0; n<lrv1.size(); n++) {
            Vector3 first = lrv1.get( n );
            Vector3 second = lrv2.get( n );
            if(  first.norm() > 0.0  &&  second.norm() > 0.0  ) {
                first = first.normalize();
                second = second.normalize();
            } else {
                continue;
            }
            UnitQuaternion qMinAngle = UnitQuaternion.thatRotatesMinimumAngleFirstToSecondVector( first , second );
            assertTrue( qMinAngle.rotate( first ).equalsApproximately( second , 1.0e-12 ) );
            UnitQuaternion qMaxAngle = UnitQuaternion.thatRotatesMaximumAngleFirstToSecondVector( first , second );
            assertTrue( qMaxAngle.rotate( first ).equalsApproximately( second , 1.0e-13 ) );
        }
    }
    
    
    @Test
    void thatRotates001To()
    {
        List<Vector3> lrv = getVectorList( 1 );
        for(int n=0; n<lrv.size(); n++) {
            Vector3 v = lrv.get( n );
            if( v.norm() > 0.0 ) {
                v = v.normalize();
            } else {
                continue;
            }
            UnitQuaternion qMinAngle = UnitQuaternion.thatRotatesMinimumAngle001To( v );
            assertTrue( qMinAngle.rotate( Vector3.k() ).equalsApproximately( v , 1.0e-13 ) );
            UnitQuaternion qMaxAngle = UnitQuaternion.thatRotatesMaximumAngle001To( v );
            assertTrue( qMaxAngle.rotate( Vector3.k() ).equalsApproximately( v , 1.0e-13 ) );
        }
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    private List<UnitQuaternion> getUnitQuaternionList( int shuffleSeed )
    {
        List<UnitQuaternion> luq = new ArrayList<UnitQuaternion>();
        luq.add( UnitQuaternion.one() );
        luq.add( UnitQuaternion.i() );
        luq.add( UnitQuaternion.j() );
        luq.add( UnitQuaternion.k() );
        luq.add( UnitQuaternion.one().inverseMultiplicativeInplace() );
        luq.add( UnitQuaternion.i().inverseMultiplicativeInplace() );
        luq.add( UnitQuaternion.j().inverseMultiplicativeInplace() );
        luq.add( UnitQuaternion.k().inverseMultiplicativeInplace() );
        luq.add( UnitQuaternion.fromRotationVector( new Vector3( 1.0 , 1.0 , 0.0 ).scaleInplace( Math.PI/2.0 ) ) );
        luq.add( UnitQuaternion.fromRotationVector( new Vector3( 0.0 , 1.0 , 1.0 ).scaleInplace( Math.PI/2.0 ) ) );
        luq.add( UnitQuaternion.fromRotationVector( new Vector3( 1.0 , 0.0 , 1.0 ).scaleInplace( Math.PI/2.0 ) ) );
        luq.add( UnitQuaternion.fromRotationVector( new Vector3( 1.0 , 1.0 , 0.0 ).scaleInplace( -Math.PI/2.0 ) ) );
        luq.add( UnitQuaternion.fromRotationVector( new Vector3( 0.0 , 1.0 , 1.0 ).scaleInplace( -Math.PI/2.0 ) ) );
        luq.add( UnitQuaternion.fromRotationVector( new Vector3( 1.0 , 0.0 , 1.0 ).scaleInplace( -Math.PI/2.0 ) ) );
        for( int n=0; n<10000; n++ ) {
            luq.add( UnitQuaternion.random( this.randomNumberGenerator ) );
        }
        Collections.shuffle( luq , new Random( shuffleSeed ) );
        return luq;
    }
    
    
    private List<Vector3> getVectorList( int shuffleSeed )
    {
        List<Vector3> lrv = new ArrayList<Vector3>();
        lrv.add( Vector3.zero() );
        lrv.add( Vector3.i() );
        lrv.add( Vector3.j() );
        lrv.add( Vector3.k() );
        lrv.add( Vector3.i().inverseAdditiveInplace() );
        lrv.add( Vector3.j().inverseAdditiveInplace() );
        lrv.add( Vector3.k().inverseAdditiveInplace() );
        for( int n=0; n<10000; n++ ) {
            lrv.add( Vector3.random( this.randomNumberGenerator ) );
        }
        Collections.shuffle( lrv , new Random( shuffleSeed ) );
        return lrv;
    }
    
}
