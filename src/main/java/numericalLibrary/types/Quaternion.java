package types;


import java.util.Random;

import algebraicStructures.AdditiveAbelianGroupElement;
import algebraicStructures.MetricSpaceElement;
import algebraicStructures.MultiplicativeGroupElement;
import algebraicStructures.VectorSpaceElement;



/**
 * Implements quaternions.
 * <p>
 * Quaternion multiplication uses Hamilton product.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Quaternion</a>
 */
public class Quaternion
    implements
        AdditiveAbelianGroupElement<Quaternion>,
        MultiplicativeGroupElement<Quaternion>,
        VectorSpaceElement<Quaternion>,
        MetricSpaceElement<Quaternion>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Scalar part of the {@link Quaternion}.
     */
    private double qw;
    
    /**
     * First component of vector part of the {@link Quaternion}.
     */
    private double qx;
    
    /**
     * Second component of vector part of the {@link Quaternion}.
     */
    private double qy;
    
    /**
     * Third component of vector part of the {@link Quaternion}.
     */
    private double qz;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link Quaternion}.
     * 
     * @param qr    real part of the {@link Quaternion}.
     * @param qi    first imaginary part of the {@link Quaternion}.
     * @param qj    second imaginary part of the {@link Quaternion}.
     * @param qk    third imaginary part of the {@link Quaternion}.
     */
    public Quaternion( double qr , double qi , double qj , double qk )
    {
        this.qw = qr;
        this.qx = qi;
        this.qy = qj;
        this.qz = qk;
    }



    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns real part of the {@link Quaternion}.
     * 
     * @return  real part of the {@link Quaternion}.
     */
    public double w()
    {
        return this.qw;
    }
    
    
    /**
     * Returns x component of the vector part of the {@link Quaternion}.
     * 
     * @return  x component of the vector part of the {@link Quaternion}.
     */
    public double x()
    {
        return this.qx;
    }


    /**
     * Returns y component of the vector part of the {@link Quaternion}.
     * 
     * @return  y component of the vector part of the {@link Quaternion}.
     */
    public double y()
    {
        return this.qy;
    }


    /**
     * Returns z component of the vector part of the {@link Quaternion}.
     * 
     * @return  z component of the vector part of the {@link Quaternion}.
     */
    public double z()
    {
        return this.qz;
    }

    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return String.format( "%2.16f + %2.16f i + %2.16f j + %2.16f k" , this.w() , this.x() , this.y() , this.z() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean equals( Quaternion other )
    {
        return (  this.w() == other.w()  &&
                  this.x() == other.x()  &&
                  this.y() == other.y()  &&
                  this.z() == other.z()  );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion setTo( Quaternion other )
    {
        this.qw = other.w();
        this.qx = other.x();
        this.qy = other.y();
        this.qz = other.z();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion copy()
    {
        return new Quaternion( this.w() , this.x() , this.y() , this.z() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion add( Quaternion other )
    {
        return new Quaternion( this.w() + other.w() ,
                               this.x() + other.x() ,
                               this.y() + other.y() ,
                               this.z() + other.z() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public Quaternion addInplace( Quaternion other )
    {
        this.qw += other.w();
        this.qx += other.x();
        this.qy += other.y();
        this.qz += other.z();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion identityAdditive()
    {
        return Quaternion.zero();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public Quaternion setToZero()
    {
        this.qw = 0.0;
        this.qx = 0.0;
        this.qy = 0.0;
        this.qz = 0.0;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion inverseAdditive()
    {
        return new Quaternion( -this.w() , -this.x() , -this.y() , -this.z() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public Quaternion inverseAdditiveInplace()
    {
        this.qw = -this.w();
        this.qx = -this.x();
        this.qy = -this.y();
        this.qz = -this.z();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion subtract( Quaternion other )
    {
        return new Quaternion( this.w() - other.w() ,
                               this.x() - other.x() ,
                               this.y() - other.y() ,
                               this.z() - other.z() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public Quaternion subtractInplace( Quaternion other )
    {
        this.qw -= other.w();
        this.qx -= other.x();
        this.qy -= other.y();
        this.qz -= other.z();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion multiply( Quaternion other )
    {
        return new Quaternion( this.w() * other.w()  - ( this.x() * other.x()  +  this.y() * other.y()  +  this.z() * other.z() ) ,
                               this.w() * other.x()  +  other.w() * this.x()  +  ( this.y() * other.z() - this.z() * other.y() ) ,
                               this.w() * other.y()  +  other.w() * this.y()  +  ( this.z() * other.x() - this.x() * other.z() ) ,
                               this.w() * other.z()  +  other.w() * this.z()  +  ( this.x() * other.y() - this.y() * other.x() ) );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public Quaternion multiplyInplace( Quaternion other )
    {
        double w = this.w();
        double x = this.x();
        double y = this.y();
        double z = this.z();
        this.qw  =  w * other.w()  - ( x * other.x()  +  y * other.y()  +  z * other.z() );
        this.qx  =  w * other.x()  +  other.w() * x  +  ( y * other.z() - z * other.y() );
        this.qy  =  w * other.y()  +  other.w() * y  +  ( z * other.x() - x * other.z() );
        this.qz  =  w * other.z()  +  other.w() * z  +  ( x * other.y() - y * other.x() );
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion identityMultiplicative()
    {
        return Quaternion.one();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public Quaternion setToOne()
    {
        this.qw = 1.0;
        this.qx = 0.0;
        this.qy = 0.0;
        this.qz = 0.0;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion inverseMultiplicative()
    {
        double normSquared = this.normSquared();
        if( normSquared == 0.0 ) {
            throw new IllegalArgumentException( "Zero has no reciprocal." );
        }
        return this.conjugate().scaleInplace( 1.0/normSquared );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public Quaternion inverseMultiplicativeInplace()
    {
        double normSquared = this.normSquared();
        if( normSquared == 0.0 ) {
            throw new IllegalArgumentException( "Zero has no reciprocal." );
        }
        return this.conjugateInplace().scaleInplace( 1.0/normSquared );
    }
    
    /*
    public Quaternion divideLeft( Quaternion other )
    {
        return other.inverseMultiplicative().multiply( this );
    }
    
    
    public Quaternion divideRight( Quaternion other )
    {
        return this.multiply( other.inverseMultiplicative() );
    }
    
    
    public Quaternion divideLeftInplace( Quaternion other )
    {
        return this.setTo( this.divideLeft( other ) );
    }
    
    
    public Quaternion divideRightInplace( Quaternion other )
    {
        return this.multiplyInplace( other.inverseMultiplicative() );
    }
    */
    
    
    /**
     * {@inheritDoc}
     */
    public Quaternion scale( double scalar )
    {
        return new Quaternion( this.w() * scalar ,
                               this.x() * scalar ,
                               this.y() * scalar ,
                               this.z() * scalar );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public Quaternion scaleInplace( double scalar )
    {
        this.qw *= scalar;
        this.qx *= scalar;
        this.qy *= scalar;
        this.qz *= scalar;
        return this;
    }
    
    
    public double distanceFrom( Quaternion other )
    {
        Quaternion delta = this.subtract( other );
        return delta.norm();
    }
    
    
    public double dot( Quaternion other )
    {
        return ( this.w() * other.w() +
                 this.x() * other.x() +
                 this.y() * other.y() +
                 this.z() * other.z() );
    }


    public double normSquared()
    {
        return this.dot( this );
    }
    
    
    public double norm()
    {
        return Math.sqrt( this.normSquared() );
    }


    public Quaternion normalize()
    {
        return this.scale( 1.0/this.norm() );
    }


    public Quaternion normalizeInplace()
    {
        return this.scaleInplace( 1.0/this.norm() );
    }
    
/*
    public double real()
    {
        return this.w();
    }


    public Quaternion imag()
    {
        return new Quaternion( 0.0 , this.x() , this.y() , this.z() );
    }
*/

    public Quaternion conjugate()
    {
        return new Quaternion( this.w() , -this.x() , -this.y() , -this.z() );
    }


    public Quaternion conjugateInplace()
    {
        // this.w = this.w;  this stays the same
        this.qx = -this.x();
        this.qy = -this.y();
        this.qz = -this.z();
        return this;
    }
    
    
    public Vector3 vectorPart()
    {
        return new Vector3( this.x() , this.y() , this.z() );
    }
    
    
    public double vectorPartNorm() {
        return Math.sqrt(  this.x() * this.x()  +  this.y() * this.y()  +  this.z() * this.z()  );
    }
    
    
    public double angle()
    {
        return 2.0 * Math.atan( this.vectorPartNorm()/this.w() );
    }
    

    public Quaternion exp()
    {
        double vnorm = this.vectorPartNorm();
        if( vnorm > 0.0 ) {
            double sincvnorm = Math.sin( vnorm )/vnorm;
            Quaternion r = new Quaternion( Math.cos( vnorm ) , this.x() * sincvnorm , this.y() * sincvnorm , this.z() * sincvnorm );
            return r.scaleInplace( Math.exp( this.w() ) );
        } else {
            return new Quaternion( Math.exp( this.w() ) , 0.0 , 0.0 , 0.0 );
        }
    }


    public Quaternion log()
    {
        double vnorm = this.vectorPartNorm();
        if( vnorm > 0.0 ) {
            double qnorm = this.norm();
            double aux = this.angle() / vnorm;
            return new Quaternion( Math.log( qnorm ) , this.x() * aux , this.y() * aux , this.z() * aux );
        } else {
            return new Quaternion( Math.log( this.norm() ) , 0.0 , 0.0 , 0.0 );
        }
    }


    public Quaternion power( double exponent )
    {
        double vnorm = this.vectorPartNorm();
        if( vnorm > 0.0 ) {
            double angle = this.angle();
            double xphi = exponent * angle;
            double aux = Math.sin( xphi )/vnorm;
            Quaternion r = new Quaternion( Math.cos( xphi ) , this.x() * aux , this.y() * aux , this.z() * aux );
            return r.scaleInplace( Math.pow( this.w() , exponent ) );
        } else {
            return new Quaternion( Math.pow( this.w() , exponent ) , 0.0 , 0.0 , 0.0 );
        }
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the zero element stored in a new instance.
     * <p>
     * That is {@code 0  +  0 · i  +  0 · j  +  0 · k }.
     * 
     * @return   zero element stored in a new instance.
     */
    public static Quaternion zero()
    {
        return new Quaternion( 0.0 , 0.0 , 0.0 , 0.0 );
    }
    
    
    /**
     * Returns the identity element stored in a new instance.
     * <p>
     * That is {@code 1  +  0 · i  +  0 · j  +  0 · k }.
     * 
     * @return  identity element stored in a new instance.
     */
    public static Quaternion one()
    {
        return new Quaternion( 1.0 , 0.0 , 0.0 , 0.0 );
    }
    
    
    /**
     * Returns the i {@link Quaternion} stored in a new instance.
     * <p>
     * That is {@code 0  +  1 · i  +  0 · j  +  0 · k }.
     * 
     * @return  i {@link Quaternion} stored in a new instance.
     */
    public static Quaternion i()
    {
        return new Quaternion( 0.0 , 1.0 , 0.0 , 0.0 );
    }


    /**
     * Returns the j {@link Quaternion} stored in a new instance.
     * <p>
     * That is {@code 0  +  0 · i  +  1 · j  +  0 · k }.
     * 
     * @return  j {@link Quaternion} stored in a new instance.
     */
    public static Quaternion j()
    {
        return new Quaternion( 0.0 , 0.0 , 1.0 , 0.0 );
    }


    /**
     * Returns the k {@link Quaternion} stored in a new instance.
     * <p>
     * That is {@code 0  +  0 · i  +  0 · j  +  1 · k }.
     * 
     * @return  k {@link Quaternion} stored in a new instance.
     */
    public static Quaternion k()
    {
        return new Quaternion( 0.0 , 0.0 , 0.0 , 1.0 );
    }
    
    
    /**
     * Returns a new {@link Quaternion} from a scalar and a {@link Vector3}.
     * 
     * @param scalar    scalar part of the {@link Quaternion}.
     * @param vector    vector part of the {@link Quaternion} stored in a {@link Vector3}.
     * @return  new {@link Quaternion} from a scalar and a {@link Vector3}.
     */
    public static Quaternion fromScalarAndVectorPart( double scalar , Vector3 vector )
    {
        return new Quaternion( scalar , vector.x() , vector.y() , vector.z() );
    }
    
    
    /**
     * Returns a random {@link Quaternion} stored in a new instance.
     * <p>
     * The random {@link Quaternion} is extracted from a Gaussian distribution with mean 0 and standard distribution 1.
     * 
     * @param randomNumberGenerator     random number generator used to extract the next normally distributed sample.
     * @return  random {@link Quaternion} stored in a new instance.
     */
    public static Quaternion random( Random randomNumberGenerator )
    {
        return new Quaternion(
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() );
    }
    
}
