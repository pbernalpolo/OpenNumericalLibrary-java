package numericalLibrary.types;


import java.util.Random;

import numericalLibrary.algebraicStructures.MetricSpaceElement;
import numericalLibrary.algebraicStructures.MultiplicativeGroupElement;



/**
 * Implements unit quaternions.
 * <p>
 * This class acts as a Proxy for the {@link Quaternion} class;
 * it delegates much of the implementation to that class, and hides the parts that do not make sense for unit quaternions.
 * It also re-implements the parts that can be optimized.
 */
public class UnitQuaternion
    implements
        MultiplicativeGroupElement<UnitQuaternion>,
        MetricSpaceElement<UnitQuaternion>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Wrapped {@link Quaternion} that provides quaternion functionality.
     */
    private final Quaternion q;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the real part of the {@link UnitQuaternion}.
     * 
     * @return  real part of the {@link UnitQuaternion}.
     */
    public double w()
    {
        return this.q.w();
    }
    
    
    /**
     * Returns the coefficient multiplying the imaginary unit i of the {@link UnitQuaternion}.
     * 
     * @return  coefficient multiplying the imaginary unit i of the {@link UnitQuaternion}.
     */
    public double x()
    {
        return this.q.x();
    }
    
    
    /**
     * Returns the coefficient multiplying the imaginary unit j of the {@link UnitQuaternion}.
     * 
     * @return  coefficient multiplying the imaginary unit j of the {@link UnitQuaternion}.
     */
    public double y()
    {
        return this.q.y();
    }
    
    
    /**
     * Returns the coefficient multiplying the imaginary unit k of the {@link UnitQuaternion}.
     * 
     * @return  coefficient multiplying the imaginary unit k of the {@link UnitQuaternion}.
     */
    public double z()
    {
        return this.q.z();
    }
    
    
    /**
     * Returns the {@link Quaternion} wrapped by this {@link UnitQuaternion}.
     * 
     * @return  {@link Quaternion} wrapped by this {@link UnitQuaternion}.
     */
    public Quaternion quaternion()
    {
        return this.q;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return this.q.toString();
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean equals( UnitQuaternion other )
    {
        return this.q.equals( other.q );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean isNaN()
    {
        return this.q.isNaN();
    }
    
    
    /**
     * {@inheritDoc}
     */
    public UnitQuaternion copy()
    {
        return new UnitQuaternion( this.q.copy() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public UnitQuaternion setTo( UnitQuaternion other )
    {
        this.q.setTo( other.q );
        return this;
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

    
    /**
     * {@inheritDoc}
     * <p>
     * Each product of {@link UnitQuaternion}s is renormalized so that another {@link UnitQuaternion} is returned.
     */
    public UnitQuaternion multiply( UnitQuaternion other )
    {
        Quaternion product = this.q.multiply( other.q );
        UnitQuaternion.normalizeInplace( product );
        return UnitQuaternion.fromNormalizedQuaternion( product );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Each product of {@link UnitQuaternion}s is renormalized so that another {@link UnitQuaternion} is returned.
     */
    public UnitQuaternion multiplyInplace( UnitQuaternion other )
    {
        this.q.multiplyInplace( other.q );
        UnitQuaternion.normalizeInplace( this.q );
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public UnitQuaternion identityMultiplicative()
    {
        return UnitQuaternion.one();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The multiplicative inverse of a unit quaternion is just its conjugate.
     */
    public UnitQuaternion inverseMultiplicative()
    {
        return new UnitQuaternion( this.q.conjugate() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The multiplicative inverse of a unit quaternion is just its conjugate.
     */
    public UnitQuaternion inverseMultiplicativeInplace()
    {
        this.q.conjugateInplace();
        return this;
    }
    
    
    public double dot( UnitQuaternion other )
    {
        return this.q.dot( other.q );
    }
    
    
    /**
     * Returns the unit quaternion {@code delta} that satisfies {@code this} = {@code other} * {@code delta}.
     * 
     * @param other     unit quaternion from which the {@code delta} must be computed.
     * @return  unit quaternion {@code delta} that satisfies {@code this} = {@code other} * {@code delta}.
     */
    public UnitQuaternion delta( UnitQuaternion other )
    {
        return other.inverseMultiplicative().multiply( this );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double distanceFrom( UnitQuaternion other )
    {
        // delta_0 = ( other^* * this )_0 = this · other
        double delta0 = Math.abs( this.dot( other ) );
        return ( ( delta0 < 1.0 )?  2.0 * Math.acos( delta0 )  :  0.0 );
    }
    
    
    /**
     * Returns the vector part of {@code this} stored in a new {@link Vector3}.
     * 
     * @return  vector part of {@code this} stored in a new {@link Vector3}.
     */
    public Vector3 vectorPart()
    {
        return this.q.vectorPart();
    }
    
    
    /**
     * Returns the norm of the vector part of {@code this} {@link Quaternion}.
     * 
     * @return  norm of the vector part of {@code this} {@link Quaternion}.
     */
    public double vectorPartNorm() {
        return this.q.vectorPartNorm();
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
    
    
    /**
     * Returns the rotation vector that represents the same rotation transformation than {@code this} unit quaternion.
     * <p>
     * The output is stored in a new {@link Vector3}.
     * 
     * @return  rotation vector that represents the same rotation transformation than {@code this} unit quaternion.
     */
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
    
    
    /**
     * Returns the rotation matrix that represents the same rotation transformation than {@code this} unit quaternion.
     * <p>
     * The output is stored in a new {@link MatrixReal}.
     * 
     * @return  rotation matrix that represents the same rotation transformation than {@code this} unit quaternion.
     */
    public MatrixReal toRotationMatrix()
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
        
        return MatrixReal.fromEntries3x3(
                1.0 -jj -kk  , ij - rk      , ik + rj ,
                ij + rk      , 1.0 -ii -kk  , jk - ri ,
                ik - rj      , jk + ri      , 1.0 -ii -jj );
    }
    
    
    /**
     * Returns the Tait–Bryan angles that represent the orientation described by the {@link UnitQuaternion}.
     * <p>
     * In particular, if the {@link UnitQuaternion} rotates vectors from a frame A to a frame B,
     * xA = R(qAB) * xB
     * then, the same rotation matrix will be obtained by the product of 3 successive rotations:
     * xA = R_z( output[2] ) * R_y( output[1] ) * R_x( output[0] ) * xB
     * 
     * @return  Tait–Bryan angles that represent the orientation described by the {@link UnitQuaternion}.
     * 
     * @see #fromRollPitchYawZYX(double, double, double)
     */
    public double[] toRollPitchYawZYX()
    {
        double y2 = this.y() * this.y();
        double phi = Math.atan2( 2.0 * ( this.w() * this.x() + this.y() * this.z() ) , 1.0 - 2.0 * ( this.x() * this.x() + y2 ) );
        double twice_wy_minus_xz = 2.0 * ( this.w() * this.y() - this.x() * this.z() );
        double theta = 2.0 * Math.atan2( Math.sqrt( 1.0 + twice_wy_minus_xz ) , Math.sqrt( 1.0 - twice_wy_minus_xz ) ) - Math.PI/2.0;
        double psi = Math.atan2( 2.0 * ( this.w() * this.z() + this.x() * this.y() ) , 1.0 - 2.0 * ( y2 + this.z() * this.z() ) );
        return new double[] { phi , theta , psi };
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
    
    /**
     * Returns the identity element stored in a new instance.
     * <p>
     * That is {@code 1  +  0 · i  +  0 · j  +  0 · k }.
     * 
     * @return  identity element stored in a new instance.
     */
    public static UnitQuaternion one()
    {
        return new UnitQuaternion( Quaternion.one() );
    }
    

    /**
     * Returns the i {@link UnitQuaternion} stored in a new instance.
     * <p>
     * That is {@code 0  +  1 · i  +  0 · j  +  0 · k }.
     * 
     * @return  i {@link UnitQuaternion} stored in a new instance.
     */
    public static UnitQuaternion i()
    {
        return new UnitQuaternion( Quaternion.i() );
    }
    
    
    /**
     * Returns the j {@link UnitQuaternion} stored in a new instance.
     * <p>
     * That is {@code 0  +  0 · i  +  1 · j  +  0 · k }.
     * 
     * @return  j {@link UnitQuaternion} stored in a new instance.
     */
    public static UnitQuaternion j()
    {
        return new UnitQuaternion( Quaternion.j() );
    }
    
    
    /**
     * Returns the k {@link UnitQuaternion} stored in a new instance.
     * <p>
     * That is {@code 0  +  0 · i  +  0 · j  +  1 · k }.
     * 
     * @return  k {@link UnitQuaternion} stored in a new instance.
     */
    public static UnitQuaternion k()
    {
        return new UnitQuaternion( Quaternion.k() );
    }
    
    
    /**
     * Returns a new {@link UnitQuaternion} that wraps a normalized {@link Quaternion}.
     * <p>
     * Note that no checks are performed on the input {@link Quaternion},
     * and the {@link UnitQuaternion} class assumes the wrapped {@link Quaternion} to be normalized,
     * so the user is responsible for introducing a normalized {@link Quaternion}.
     * Failing to introduce a quaternion with unit norm will make the {@link UnitQuaternion} class to behave in an unexpected way.
     * 
     * @param normalizedQuaternion  normalized {@link Quaternion} to be wrapped by the returned {@link UnitQuaternion}
     * @return  new {@link UnitQuaternion} that wraps a normalized {@link Quaternion}.
     */
    public static UnitQuaternion fromNormalizedQuaternion( Quaternion normalizedQuaternion )
    {
        return new UnitQuaternion( normalizedQuaternion );
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
    
    
    /**
     * Returns a new {@link UnitQuaternion} that represents the same rotation transformation as the input rotation vector.
     * 
     * @param v     rotation vector that represents the same rotation transformation as the {@link UnitQuaternion} returned by this method.
     * @return  new {@link UnitQuaternion} that represents the same rotation transformation as the input rotation vector.
     */
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
    
    
    /**
     * Returns a new {@link UnitQuaternion} that represents the same rotation transformation as the input rotation matrix.
     * 
     * @param R     rotation matrix that represents the same rotation transformation as the {@link UnitQuaternion} returned by this method.
     * @return  new {@link UnitQuaternion} that represents the same rotation transformation as the input rotation matrix.
     */
    public static UnitQuaternion fromRotationMatrix( MatrixReal R )
    {
        if(  R.rows() != 3  ||  R.columns() != 3  ) {
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
        UnitQuaternion.normalizeInplace( q );
        return UnitQuaternion.fromNormalizedQuaternion( q );
    }
    
    
    /**
     * Returns a new {@link UnitQuaternion} from the Tait–Bryan angles that represent an orientation.
     * <p>
     * In particular, if the Tait-Bryan angles rotate vectors from a frame A to a frame B,
     * xA = R_z( psi ) * R_y( theta ) * R_x( phi ) * xB
     * then, the same rotation matrix will be obtained from the {@link UnitQuaternion} that results from this method:
     * xA = R(qAB) * xB
     * 
     * @return  new {@link UnitQuaternion} from the Tait–Bryan angles that represent an orientation.
     * 
     * @see #toRollPitchYawZYX()
     */
    public static UnitQuaternion fromRollPitchYawZYX( double phi , double theta , double psi )
    {
        UnitQuaternion qz = UnitQuaternion.fromAngleAndUnitVector( psi , Vector3.k() );
        UnitQuaternion qy = UnitQuaternion.fromAngleAndUnitVector( theta , Vector3.j() );
        UnitQuaternion qx = UnitQuaternion.fromAngleAndUnitVector( phi , Vector3.i() );
        return qz.multiplyInplace( qy ).multiplyInplace( qx );
    }
    
    
    public static UnitQuaternion random( Random randomNumberGenerator )
    {
        return new UnitQuaternion( Quaternion.random( randomNumberGenerator ).normalizeInplace() );
    }
    
    
    /**
     * Returns the {@link UnitQuaternion} that rotates a {@code first} unit {@link Vector3} into a {@code second} unit {@link Vector3}.
     * <p>
     * The {@link UnitQuaternion} will perform rotations around the axis that is orthogonal to both {@code first} and {@code second},
     * so that the rotation angle will be minimum.
     * 
     * @param first     unit {@link Vector3} that the returned {@link UnitQuaternion} will rotate into the {@code second} one.
     * @param second    unit {@link Vector3} into which the returned {@link UnitQuaternion} will rotate the {@code first} one.
     * @return  {@link UnitQuaternion} that rotates {@code first} unit {@link Vector3} into {@code second} unit {@link Vector3}.
     * 
     * @throws IllegalArgumentException if any of the input unit {@link Vector3}s are zero or NaN.
     */
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
    
    
    /**
     * Returns the {@link UnitQuaternion} that rotates (0,0,1) into a unit {@link Vector3}.
     * <p>
     * The {@link UnitQuaternion} will perform rotations around the axis that is orthogonal to both (0,0,1) and {@code v},
     * so that the rotation angle will be minimum.
     * 
     * @param v     unit {@link Vector3} into which the returned {@link UnitQuaternion} will rotate the (0,0,1) {@link Vector3}.
     * @return  {@link UnitQuaternion} that rotates (0,0,1) into the unit {@link Vector3} {@code v}.
     * 
     * @throws IllegalArgumentException if the input unit {@link Vector3} is zero or NaN.
     */
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
    
    
    /**
     * Returns the {@link UnitQuaternion} that rotates a {@code first} unit {@link Vector3} into a {@code second} unit {@link Vector3}.
     * <p>
     * The {@link UnitQuaternion} will perform rotations around the axis between {@code first} and {@code second},
     * so that the rotation angle will be maximum and equal to pi.
     * 
     * @param first     unit {@link Vector3} that the returned {@link UnitQuaternion} will rotate into the {@code second} one.
     * @param second    unit {@link Vector3} into which the returned {@link UnitQuaternion} will rotate the {@code first} one.
     * @return  {@link UnitQuaternion} that rotates {@code first} unit {@link Vector3} into {@code second} unit {@link Vector3}.
     * 
     * @throws IllegalArgumentException if any of the input unit {@link Vector3}s are zero or NaN.
     */
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
    
    
    /**
     * Returns the {@link UnitQuaternion} that rotates (0,0,1) into a unit {@link Vector3}.
     * <p>
     * The {@link UnitQuaternion} will perform rotations around the axis between {@code first} and {@code second},
     * so that the rotation angle will be maximum and equal to pi.
     * 
     * @param v     unit {@link Vector3} into which the returned {@link UnitQuaternion} will rotate the (0,0,1) {@link Vector3}.
     * @return  {@link UnitQuaternion} that rotates (0,0,1) into the unit {@link Vector3} {@code v}.
     * 
     * @throws IllegalArgumentException if the input unit {@link Vector3} is zero or NaN.
     */
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
    
    
    /**
     * Returns the {@link UnitQuaternion} that rotates (0,0,1) into a unit {@link Vector3}.
     * <p>
     * The {@link UnitQuaternion} will perform rotations around the axis defined by {@code axisParameter} which is defined in the interval [0,1]:
     * <ul>
     *  <li> A value of 0 will result in the rotation being performed around the axis orthogonal to both (0,0,1) and {@code v}, so that the rotation angle will be minimum.
     *  <li> A value of 1 will result in the rotation being performed around the axis between (0,0,1) and {@code v}, so that the rotation angle will be maximum and equal to pi.
     * </ul>
     * Note that this method requires {@code v} to be a unit {@link Vector3}.
     * 
     * @param v     unit {@link Vector3} into which the returned {@link UnitQuaternion} will rotate the (0,0,1) {@link Vector3}.
     * @param axisParameter     parameter used to select the rotation axis.
     * @return  {@link UnitQuaternion} that rotates (0,0,1) into the unit {@link Vector3} {@code v}.
     * 
     * @throws IllegalArgumentException if the input unit {@link Vector3} is zero or NaN.
     */
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
    
    /**
     * Constructs a new {@link UnitQuaternion} wrapping a {@link Quaternion}.
     * <p>
     * Note that the input {@link Quaternion} must be normalized for this class to work properly.
     * That is the reason for this method to be private.
     * On the other hand, the public alternative {@link #fromNormalizedQuaternion(Quaternion)} is provided to make it explicit that a normalized quaternion is required.
     * 
     * @param theQuaternion     {@link Quaternion} to be wrapped by the constructed {@link UnitQuaternion}.
     * 
     * @see #fromNormalizedQuaternion(Quaternion)
     */
    private UnitQuaternion( Quaternion theQuaternion )
    {
        this.q = theQuaternion;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Normalizes in place a {@link Quaternion} without checking if it has zero norm.
     * <p>
     * Since the norm of a {@link UnitQuaternion} is 1, we avoid the unnecessary check for a zero {@link Quaternion}.
     * 
     * @param q     {@link Quaternion} to be normalized in place.
     * 
     * @see Quaternion#normalizeInplace()
     */
    private static void normalizeInplace( Quaternion q )
    {
        q.scaleInplace( 1.0/q.norm() );
    }
    
    
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
