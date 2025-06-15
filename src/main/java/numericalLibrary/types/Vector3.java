package numericalLibrary.types;


import java.util.Random;

import numericalLibrary.algebraicStructures.AdditiveAbelianGroupElement;
import numericalLibrary.algebraicStructures.MetricSpaceElement;
import numericalLibrary.algebraicStructures.VectorSpaceElement;



/**
 * Implements 3d vectors.
 */
public class Vector3
    implements
        AdditiveAbelianGroupElement<Vector3>,
        VectorSpaceElement<Vector3>,
        MetricSpaceElement<Vector3>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * First component of the 3d vector.
     */
    private double vx;
    
    /**
     * Second component of the 3d vector.
     */
    private double vy;
    
    /**
     * Third component of the 3d vector.
     */
    private double vz;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a 3d vector.
     * 
     * @param x     first component of the 3d vector.
     * @param y     second component of the 3d vector.
     * @param z     third component of the 3d vector.
     */
    public Vector3( double x , double y , double z )
    {
        this.vx = x;
        this.vy = y;
        this.vz = z;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the x component of the {@link Vector3}.
     * 
     * @param x     value for the x component to be set.
     */
    public void setX( double x )
    {
        this.vx = x;
    }
    
    
    /**
     * Sets the y component of the {@link Vector3}.
     * 
     * @param y     value for the y component to be set.
     */
    public void setY( double y )
    {
        this.vy = y;
    }
    
    
    /**
     * Sets the z component of the {@link Vector3}.
     * 
     * @param z     value for the z component to be set.
     */
    public void setZ( double z )
    {
        this.vz = z;
    }
    
    
    /**
     * Returns the value of the x component.
     * 
     * @return  value of the x component.
     */
    public double x()
    {
        return vx;
    }
    
    
    /**
     * Returns the value of the y component.
     * 
     * @return  value of the y component.
     */
    public double y()
    {
        return vy;
    }
    
    
    /**
     * Returns the value of the z component.
     * 
     * @return  value of the z component.
     */
    public double z()
    {
        return vz;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return String.format( "( %2.16f , %2.16f , %2.16f )" , this.x() , this.y() , this.z() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean equals( Vector3 other )
    {
        return (  this.x() == other.x()  &&
                  this.y() == other.y()  &&
                  this.z() == other.z()  );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean equalsApproximately( Vector3 other , double tolerance )
    {
        return ( this.distanceFrom( other ) < tolerance );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean isNaN()
    {
        return (  Double.isNaN( this.x() )  ||
                  Double.isNaN( this.y() )  ||
                  Double.isNaN( this.z() )  );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 copy()
    {
        return new Vector3( this.x() , this.y() , this.z() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 setTo( Vector3 other )
    {
        this.vx = other.x();
        this.vy = other.y();
        this.vz = other.z();
        return this;
    }
    
    
    public Vector3 setToZero()
    {
        this.vx = 0.0;
        this.vy = 0.0;
        this.vz = 0.0;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 add( Vector3 other )
    {
        return new Vector3( this.x() + other.x() ,
                                this.y() + other.y() ,
                                this.z() + other.z() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 addInplace( Vector3 other )
    {
        this.vx += other.x();
        this.vy += other.y();
        this.vz += other.z();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 identityAdditive()
    {
        return Vector3.zero();
    }

    
    /**
     * {@inheritDoc}
     */
    public Vector3 inverseAdditive()
    {
        return new Vector3( -this.x() , -this.y() , -this.z() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 inverseAdditiveInplace()
    {
        this.vx = -this.x();
        this.vy = -this.y();
        this.vz = -this.z();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 subtract( Vector3 other )
    {
        return new Vector3( this.x() - other.x() ,
                                this.y() - other.y() ,
                                this.z() - other.z() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 subtractInplace( Vector3 other )
    {
        this.vx -= other.x();
        this.vy -= other.y();
        this.vz -= other.z();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 scale( double scalar )
    {
        return new Vector3( this.x() * scalar ,
                                this.y() * scalar ,
                                this.z() * scalar );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 scaleInplace( double scalar )
    {
        this.vx *= scalar;
        this.vy *= scalar;
        this.vz *= scalar;
        return this;
    }
    
    
    public double dot( Vector3 other )
    {
        return ( this.x() * other.x() +
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


    public Vector3 normalize()
    {
        return this.scale( 1.0/this.norm() );
    }


    public Vector3 normalizeInplace()
    {
        return this.scaleInplace( 1.0/this.norm() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public double distanceFrom( Vector3 other )
    {
        return this.subtract( other ).norm();
    }
    
    
    public double angleFrom( Vector3 other )
    {
        double dot = this.normalize().dot( other.normalize() );
        return ( ( dot < -1.0 )? Math.PI : ( ( 1.0 < dot )? 0.0 : Math.acos( dot ) ) );
    }
    
    
    public Vector3 crossProduct( Vector3 other )
    {
        return new Vector3( this.y() * other.z() - this.z() * other.y() ,
                                this.z() * other.x() - this.x() * other.z() ,
                                this.x() * other.y() - this.y() * other.x() );
    }
    
    
    public MatrixReal crossProductMatrix()
    {
        return MatrixReal.fromEntries3x3(
                0.0      , -this.z() ,  this.y() ,
                this.z() ,  0.0      , -this.x() ,
               -this.y() ,  this.x() ,  0.0      );
    }
    
    
    public MatrixReal outerProduct( Vector3 other )
    {
        return MatrixReal.fromEntries3x3(
                this.x() * other.x() , this.x() * other.y() , this.x() * other.z() ,
                this.y() * other.x() , this.y() * other.y() , this.y() * other.z() ,
                this.z() * other.x() , this.z() * other.y() , this.z() * other.z() );
    }
    
    
    public MatrixReal toMatrixAsColumn()
    {
        return MatrixReal.fromVector3AsColumn( this );
    }
    
    
    public MatrixReal toMatrixAsRow()
    {
        return MatrixReal.fromVector3AsRow( this );
    }
    
    
    /**
     * Returns the {@link Vector3} as a flat array.
     * <p>
     * The output is:
     * new double[] { this.x() , this.y() , this.z() };
     * 
     * @return  {@link Vector3} as a flat array.
     */
    public double[] toFlatArray()
    {
        return new double[] { this.x() , this.y() , this.z() };
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the zero element stored in a new instance.
     * <p>
     * That is the vector (0,0,0).
     * 
     * @return   zero element stored in a new instance.
     */
    public static Vector3 zero()
    {
        return new Vector3( 0.0 , 0.0 , 0.0 );
    }
    
    
    /**
     * Returns the i unit vector stored in a new instance.
     * <p>
     * That is the vector (1,0,0).
     * 
     * @return   i unit vector stored in a new instance.
     */
    public static Vector3 i()
    {
        return new Vector3( 1.0 , 0.0 , 0.0 );
    }
    
    
    /**
     * Returns the j unit vector stored in a new instance.
     * <p>
     * That is the vector (0,1,0).
     * 
     * @return   j unit vector stored in a new instance.
     */
    public static Vector3 j()
    {
        return new Vector3( 0.0 , 1.0 , 0.0 );
    }


    /**
     * Returns the k unit vector stored in a new instance.
     * <p>
     * That is the vector (0,0,1).
     * 
     * @return   k unit vector stored in a new instance.
     */
    public static Vector3 k()
    {
        return new Vector3( 0.0 , 0.0 , 1.0 );
    }
    
    
    /**
     * Returns a new {@link Vector3} constructed from a flat array.
     * 
     * @param flatArray     array that contains the {@link Vector3} elements.
     * @return  new {@link Vector3} constructed from a flat array.
     */
    public static Vector3 fromFlatArray( double[] flatArray )
    {
        if( flatArray.length < 3 ) {
            throw new IllegalArgumentException( "Not enough elements in flatArray; found " + flatArray.length + "; expected 3 or more." );
        }
        return new Vector3( flatArray[0] , flatArray[1] , flatArray[2] );
    }
    
    
    /**
     * Returns a new random {@link Vector3} with normally distributed components sampled from {@link Random}.
     * <p>
     * The components of the {@link Vector3} are normally distributed with mean {@code 0.0} and standard deviation {@code 1.0}.
     * 
     * @param randomNumberGenerator {@link Random} used to generate the components of the returned {@link Vector3}.
     * @return  new random {@link Vector3} with normally distributed components sampled from {@link Random}.
     */
    public static Vector3 random( Random randomNumberGenerator )
    {
        return new Vector3(
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() );
    }
    
}

