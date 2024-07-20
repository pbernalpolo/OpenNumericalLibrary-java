package numericalLibrary.types;


import java.util.Random;

import numericalLibrary.algebraicStructures.MetricSpaceElement;
import numericalLibrary.algebraicStructures.MultiplicativeGroupElement;



/**
 * Implements unit quaternions.
 * <p>
 * 
 */
public class UnitQuaternion
    implements
        MultiplicativeGroupElement<UnitQuaternion>,
        MetricSpaceElement<UnitQuaternion>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    private Quaternion q;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////

    public double w()
    {
        return this.q.w();
    }


    public double x()
    {
        return this.q.x();
    }


    public double y()
    {
        return this.q.y();
    }


    public double z()
    {
        return this.q.z();
    }
    
    
    public Quaternion quaternion()
    {
        return this.q;
    }

    
    public String toString()
    {
        return this.q.toString();
    }

    
    public UnitQuaternion print()
    {
        this.q.print();
        return this;
    }
    
    
    public UnitQuaternion copy()
    {
        return new UnitQuaternion( this.q.copy() );
    }

    
    public UnitQuaternion setTo( UnitQuaternion other )
    {
        this.q.setTo( other.q );
        return this;
    }
    
    
    public boolean equals( UnitQuaternion other )
    {
        return this.q.equals( other.q );
    }

    
    public UnitQuaternion opposite()
    {
        return new UnitQuaternion( this.q.inverseAdditive() );
    }
    
    
    public UnitQuaternion oppositeInplace()
    {
        this.q.inverseAdditiveInplace();
        return this;
    }


    public UnitQuaternion multiply( UnitQuaternion other )
    {
        return new UnitQuaternion( this.q.multiply( other.q ).normalizeInplace() );
    }


    public UnitQuaternion multiplyInplace( UnitQuaternion other )
    {
        this.q = this.q.multiplyInplace( other.q ).normalizeInplace();
        return this;
    }
    
    
    public UnitQuaternion identityMultiplicative()
    {
        return UnitQuaternion.one();
    }


    public UnitQuaternion inverseMultiplicative()
    {
        return new UnitQuaternion( this.q.conjugate() );
    }


    public UnitQuaternion inverseMultiplicativeInplace()
    {
        this.q.conjugateInplace();
        return this;
    }
    
    
    public double dot( UnitQuaternion other )
    {
        return this.q.dot( other.q );
    }
    
    
    public UnitQuaternion delta( UnitQuaternion other )
    {
        return other.inverseMultiplicative().multiply( this );
    }


    public double distanceFrom( UnitQuaternion other )
    {
        // delta_0 = ( other^* * this )_0 = this · other
        double delta0 = Math.abs( this.dot( other ) );
        return ( ( delta0 < 1.0 )?  2.0 * Math.acos( delta0 )  :  0.0 );
    }
    
    
    public Vector3 vectorPart()
    {
        return this.q.vectorPart();
    }
    
    
    public double vectorPartNorm() {
        return Math.sqrt(  1.0  -  this.w() * this.w()  );
    }
    
    
    public double angle()
    {
        return 2.0 * Math.atan( this.vectorPartNorm() / this.w() );
    }


    public Vector3 axis()
    {
        Vector3 vp = this.vectorPart();
        double norm = vp.norm();
        return ( norm > 0.0 )?  vp.scaleInplace( 1.0/norm )  :  Vector3.k() ;
    }
    
    
    public Vector3 toRotationVector()
    {
        double qvnorm = this.vectorPartNorm();
        if( qvnorm > 0.0 ) {
            return this.vectorPart().scaleInplace( 2.0 * Math.atan( qvnorm / this.w() ) / qvnorm );
            // the version above is numerically more stable than using 2.0 * Math.asin( qvnorm ) or 2.0 * Math.acos( this.w() )
        } else {
            return Vector3.zero();
        }
    }
    
    
    public Vector3 errorFrom( UnitQuaternion other )
    {
        return this.delta( other ).toRotationVector();
    }
    
    
    public UnitQuaternion positiveScalarPartForm()
    {
        return ( this.w() > 0.0 )? this : this.opposite();
    }
    
    
    public Matrix toRotationMatrix()
    {
        double ri = this.w() * this.x();
        ri += ri;
        double rj = this.w() * this.y();
        rj += rj;
        double rk = this.w() * this.z();
        rk += rk;
        double ij = this.x() * this.y();
        ij += ij;
        double ik = this.x() * this.z();
        ik += ik;
        double jk = this.y() * this.z();
        jk += jk;
        double ii = this.x() * this.x();
        ii += ii;
        double jj = this.y() * this.y();
        jj += jj;
        double kk = this.z() * this.z();
        kk += kk;
        
        return Matrix.matrix3x3( 1.0 -jj -kk  , ij - rk      , ik + rj ,
                                     ij + rk      , 1.0 -ii -kk  , jk - ri ,
                                     ik - rj      , jk + ri      , 1.0 -ii -jj );
    }
    
    
    public Vector3 rotate( Vector3 v )
    {
        // https://en.wikipedia.org/wiki/Quaternions_and_spatial_rotation#Used_methods
        Vector3 r = this.vectorPart();
        Vector3 dv = r.crossProduct( r.crossProduct( v ).addInplace( v.scale( this.w() ) ) );
        dv.addInplace( dv );
        return dv.addInplace( v );
    }
    
    
    public Vector3 rotateInplace( Vector3 v )
    {
        // https://en.wikipedia.org/wiki/Quaternions_and_spatial_rotation#Used_methods
        Vector3 r = this.vectorPart();
        Vector3 dv = r.crossProduct( r.crossProduct( v ).addInplace( v.scale( this.w() ) ) );
        dv.addInplace( dv );
        return v.addInplace( dv );
    }
    
    
    public Vector3 rotateWithInverse( Vector3 v )
    {
        // https://en.wikipedia.org/wiki/Quaternions_and_spatial_rotation#Used_methods
        Vector3 r = this.vectorPart();
        Vector3 dv = r.crossProduct( r.crossProduct( v ).subtractInplace( v.scale( this.w() ) ) );
        dv.addInplace( dv );
        return dv.addInplace( v );
    }


    public Vector3 rotateWithInverseInplace( Vector3 v )
    {
        // https://en.wikipedia.org/wiki/Quaternions_and_spatial_rotation#Used_methods
        Vector3 r = this.vectorPart();
        Vector3 dv = r.crossProduct( r.crossProduct( v ).subtractInplace( v.scale( this.w() ) ) );
        dv.addInplace( dv );
        return v.addInplace( dv );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC STATIC METHODS
    ////////////////////////////////////////////////////////////////

    public static UnitQuaternion one()
    {
        return new UnitQuaternion( Quaternion.one() );
    }
    

    public static UnitQuaternion i()
    {
        return new UnitQuaternion( Quaternion.i() );
    }


    public static UnitQuaternion j()
    {
        return new UnitQuaternion( Quaternion.j() );
    }


    public static UnitQuaternion k()
    {
        return new UnitQuaternion( Quaternion.k() );
    }
    
    
    public static UnitQuaternion fromNormalizedQuaternion( Quaternion theNormalizedQuaternion )
    {
        return new UnitQuaternion( theNormalizedQuaternion );
    }
    
    
    public static UnitQuaternion fromAngleAndUnitVector( double angle , Vector3 axis )
    {
        double angle05 = 0.5 * angle;
        Quaternion q = Quaternion.fromScalarAndVectorPart( Math.cos( angle05 ) ,
                                                           axis.scale( Math.sin( angle05 ) ) );
        return new UnitQuaternion( q );
    }

    
    public static UnitQuaternion fromAngleAxis( double angle , Vector3 axis )
    {
        return UnitQuaternion.fromAngleAndUnitVector( angle , axis.scale( 1.0/axis.norm() ) );
    }
    
    
    public static UnitQuaternion fromRotationVector( Vector3 v )
    {
        double vnorm = v.norm();
        if( vnorm > 0.0 ) {
            double vnorm05 = 0.5 * vnorm;
            double sincvnorm = Math.sin( vnorm05 )/vnorm;
            Quaternion q = Quaternion.fromScalarAndVectorPart( Math.cos( vnorm05 ) ,
                                                               v.scale( sincvnorm ) );
            return new UnitQuaternion( q );
        } else {
            return new UnitQuaternion( Quaternion.one() );
        }
    }
    
    
    public static UnitQuaternion fromRotationMatrix( Matrix R )
    {
        if(  R.rows() != 3  ||  R.cols() != 3  ) {
            throw new IllegalArgumentException( "3D rotation matrix expected." );
        }
        double R11 = R.entry(0,0);
        double R22 = R.entry(1,1);
        double R33 = R.entry(2,2);
        double trace = R11 + R22 + R33;
        double[] values = new double[] { trace , R11 , R22 , R33 };
        int index = 0;
        for( int i=1; i<values.length; i++ ) {
            index = ( values[i] > values[index] )? i : index ;
        }
        Quaternion q = null;
        switch( index ) {
            case 0:
                q = new Quaternion( 1.0 + trace ,
                                    R.entry(2,1) - R.entry(1,2) ,
                                    R.entry(0,2) - R.entry(2,0) ,
                                    R.entry(1,0) - R.entry(0,1) );
                break;
            case 1:
                q = new Quaternion( R.entry(2,1) - R.entry(1,2) ,
                                    1.0 + R11 - R22 - R33 ,
                                    R.entry(1,0) + R.entry(0,1) ,
                                    R.entry(2,0) + R.entry(0,2) );
                break;
            case 2:
                q = new Quaternion( R.entry(0,2) - R.entry(2,0) ,
                                    R.entry(1,0) + R.entry(0,1) ,
                                    1.0 - R11 + R22 - R33 ,
                                    R.entry(2,1) + R.entry(1,2) );
                break;
            case 3:
                q = new Quaternion( R.entry(1,0) - R.entry(0,1) ,
                                    R.entry(2,0) + R.entry(0,2) ,
                                    R.entry(2,1) + R.entry(1,2) ,
                                    1.0 - R11 - R22 + R33 );
                break;
        }
        return UnitQuaternion.fromNormalizedQuaternion( q.normalizeInplace() );
    }
    
    
    public static UnitQuaternion random( Random randomNumberGenerator )
    {
        return new UnitQuaternion( Quaternion.random( randomNumberGenerator ).normalizeInplace() );
    }
    
    
    public static UnitQuaternion thatRotatesMinimumAngleFirstToSecondVector( Vector3 first , Vector3 second )
    {
        if(  first.norm() > 0.0  &&  second.norm() > 0.0  ) {
            // https://www.xarg.org/proof/quaternion-from-two-vectors/
            double d = first.dot( second );
            Vector3 w = first.crossProduct( second );
            Quaternion q = Quaternion.fromScalarAndVectorPart(
                    d  +  Math.sqrt(  d * d  +  w.dot( w )  ) ,
                    w  );
            double qnorm = q.norm();
            if( qnorm > 0.0 ) {
                return UnitQuaternion.fromNormalizedQuaternion( q.scaleInplace( 1.0/qnorm ) );
            } else {
                return UnitQuaternion.thatRotatesPiAroundAxisOrthogonalTo( first );
            }
        } else {
            throw new IllegalArgumentException( "[UnitQuaternion.thatRotatesMinimumAngleFirstToSecondVector] Vectors must be different than 0." );
        }
    }
    
    
    public static UnitQuaternion thatRotatesMinimumAngle001To( Vector3 v )
    {
        if( v.norm() > 0.0 ) {
            // https://www.xarg.org/proof/quaternion-from-two-vectors/
            Quaternion q = Quaternion.fromScalarAndVectorPart(
                    v.z() + v.norm() ,
                    new Vector3( -v.y() , v.x() , 0.0 )  );
            double qnorm = q.norm();
            if( qnorm > 0.0 ) {
                return UnitQuaternion.fromNormalizedQuaternion( q.scaleInplace( 1.0/qnorm ) );
            } else {
                // This happens when v = (-1,0,0). Any pi rotation around an orthogonal axis works. 
                return UnitQuaternion.i();
            }
        } else {
            throw new IllegalArgumentException( "[UnitQuaternion.thatRotatesMinimumAngle001To] Vector must be different than 0 and NaN." );
        }
    }
    
    
    public static UnitQuaternion thatRotatesMaximumAngleFirstToSecondVector( Vector3 first , Vector3 second )
    {
        double firstNormSquared = first.normSquared();
        double secondNormSquared = second.normSquared();
        if(  firstNormSquared > 0.0  &&  secondNormSquared > 0.0  ) {
            // https://stackoverflow.com/questions/1171849/finding-quaternion-representing-the-rotation-from-one-vector-to-another
            Vector3 halfWayUnitVector = UnitQuaternion.halfWayUnitVectorBetween(
                    first.scale( 1.0/Math.sqrt( firstNormSquared ) ) ,
                    second.scale( 1.0/Math.sqrt( secondNormSquared ) ) );
            Quaternion q = Quaternion.fromScalarAndVectorPart( 0.0 , halfWayUnitVector );
            return UnitQuaternion.fromNormalizedQuaternion( q );
        } else {
            throw new IllegalArgumentException( "[UnitQuaternion.thatRotatesMaximumAngleFirstToSecondVector] Vectors must be different than 0." );
        }
    }
    
    
    public static UnitQuaternion thatRotatesMaximumAngle001To( Vector3 v )
    {
        double vNormSquared = v.normSquared();
        if( vNormSquared > 0.0 ) {
            // https://stackoverflow.com/questions/1171849/finding-quaternion-representing-the-rotation-from-one-vector-to-another
            Vector3 halfWayUnitVector = UnitQuaternion.halfWayUnitVectorBetween001And(
                    v.scale( 1.0/Math.sqrt( vNormSquared ) ) );
            Quaternion q = Quaternion.fromScalarAndVectorPart( 0.0 , halfWayUnitVector );
            return UnitQuaternion.fromNormalizedQuaternion( q );
        } else {
            throw new IllegalArgumentException( "[UnitQuaternion.thatRotatesMaximumAngle001To] Vector must be different than 0." );
        }
    }
    
    
    public static UnitQuaternion thatRotates001To( Vector3 v , double axisParameter )
    {
        if(  axisParameter < 0.0  ||  1.0 < axisParameter  ) {
            throw new IllegalArgumentException( "[UnitQuaternion.thatRotates001To] axisParameter must take values in the interval [0,1] but " + axisParameter + " was given." );
        }
        double vNormSquared = v.normSquared();
        if( vNormSquared > 0.0 ) {
            // Compute vector orthogonal to both (0,0,1) and v using the cross product.
            Vector3 orthogonalVector = Vector3.k().crossProduct( v );
            double orthogonalVectorNormSquared = orthogonalVector.normSquared();
            if( orthogonalVectorNormSquared > 0.0 ) {
                orthogonalVector.normalizeInplace();
            } else {
                orthogonalVector = Vector3.j();  // i is taken by halfWayUnitVectorBetween001And.
            }
            // Compute the half way unit vector.
            Vector3 halfWayUnitVector = UnitQuaternion.halfWayUnitVectorBetween001And(
                    v.scale( 1.0/Math.sqrt( vNormSquared ) ) );
            // Compute orthonormal vector to both the initial orthogonal vector and the half way unit vector.
            Vector3 orthonormalToBoth = orthogonalVector.crossProduct( halfWayUnitVector ).normalizeInplace();
            // Get rotation axis from rotating the orthogonal vector some angle in the interval [0,pi/2].
            Vector3 axis = UnitQuaternion.fromAngleAndUnitVector(
                    axisParameter * Math.PI/2.0 , orthonormalToBoth ).rotate( orthogonalVector );
            // Get sin(theta/2)^2 from  R(q) * (0,0,1) = v ,  with  q = ( cos(theta/2) , axis * sin(theta/2) ) .
            double sin2HalfTheta = ( 1.0 - v.z() )/( 2 * ( axis.x() * axis.x() + axis.y() * axis.y() ) );
            // Clamp to [0,1] to avoid numerical inaccuracies.
            sin2HalfTheta = ( 0.0 <= sin2HalfTheta )? sin2HalfTheta : 0.0;
            sin2HalfTheta = ( sin2HalfTheta <= 1.0 )? sin2HalfTheta : 1.0;
            // Build quaternion using  q = ( cos(theta/2) , axis * sin(theta/2) ) .
            Quaternion q = Quaternion.fromScalarAndVectorPart(
                    Math.sqrt( 1.0 - sin2HalfTheta ) ,
                    axis.scaleInplace( Math.sqrt( sin2HalfTheta ) ) );
            return UnitQuaternion.fromNormalizedQuaternion( q );
        } else {
            throw new IllegalArgumentException( "[UnitQuaternion.thatRotates001To] Vector must be different than 0." );
        }
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
        
    private UnitQuaternion( Quaternion theQuaternion )
    {
        this.q = theQuaternion;
    }
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    private static UnitQuaternion thatRotatesPiAroundAxisOrthogonalTo( Vector3 v )
    {
        double unorm = Math.sqrt( v.x() * v.x() + v.y() * v.y() );
        Vector3 u = new Vector3( v.y()/unorm , -v.x()/unorm , 0.0 );
        Quaternion q = Quaternion.fromScalarAndVectorPart( 0.0 , u );
        return UnitQuaternion.fromNormalizedQuaternion( q );
    }
    
    
    // v is expected to be normalized
    private static Vector3 halfWayUnitVectorBetween001And( Vector3 v )
    {
        Vector3 halfWayVector = Vector3.k().addInplace( v ).scaleInplace( 0.5 );
        double halfWayVectorNormSquared = halfWayVector.normSquared();
        if( halfWayVectorNormSquared > 0.0 ) {
            return halfWayVector.scaleInplace( 1.0/Math.sqrt( halfWayVectorNormSquared ) );
        } else {
            // This happens when v = (0,0,-1). Any vector orthogonal to (0,0,1) works.
            return Vector3.i();
        }
    }
    
    
    // v1 and v2 are expected to be normalized
    private static Vector3 halfWayUnitVectorBetween( Vector3 v1 , Vector3 v2 )
    {
        Vector3 halfWayVector = v1.addInplace( v2 ).scaleInplace( 0.5 );
        double halfWayVectorNormSquared = halfWayVector.normSquared();
        if( halfWayVectorNormSquared > 0.0 ) {
            return halfWayVector.scaleInplace( 1.0/Math.sqrt( halfWayVectorNormSquared ) );
        } else {
            // This happens when v1 = -v2. Any vector orthogonal to v1 works.
            // As a consequence of the Hairy ball theorem, there is no single continuous function
            // that can generate a vector in R3 that is orthogonal to a given one for all vector inputs.
            // This makes the following branching necessary.
            if( Math.abs( v1.x() ) < Math.abs( v1.y() ) ) {
                return new Vector3( 0.0 , v1.z() , -v1.y() ).normalizeInplace();
            } else {
                return new Vector3( v1.z() , 0.0 , -v1.x() ).normalizeInplace();
            }
        }
    }
    
}
