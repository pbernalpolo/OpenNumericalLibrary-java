package numericalLibrary.types;


import java.util.Random;

import numericalLibrary.algebraicStructures.AdditiveAbelianGroupElement;
import numericalLibrary.algebraicStructures.MetricSpaceElement;
import numericalLibrary.algebraicStructures.VectorSpaceElement;



public class Vector3
    implements
        AdditiveAbelianGroupElement<Vector3>,
        VectorSpaceElement<Vector3>,
        MetricSpaceElement<Vector3>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    private double vx;
    private double vy;
    private double vz;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    public Vector3( double x , double y , double z )
    {
        this.vx = x;
        this.vy = y;
        this.vz = z;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public void setX( double x )
    {
        this.vx = x;
    }
    
    
    public void setY( double y )
    {
        this.vy = y;
    }
    
    
    public void setZ( double z )
    {
        this.vz = z;
    }
    
    
    public double x()
    {
        return vx;
    }
    
    
    public double y()
    {
        return vy;
    }
    
    
    public double z()
    {
        return vz;
    }
    
    
    public String toString()
    {
        return String.format( "( %2.16f , %2.16f , %2.16f )" , this.x() , this.y() , this.z() );
    }
    
    
    public Vector3 print()
    {
        System.out.println( this.toString() );
        return this;
    }
    
    
    public Vector3 copy()
    {
        return new Vector3( this.x() , this.y() , this.z() );
    }
    
    
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
    
    
    public boolean equals( Vector3 other )
    {
        return (  this.x() == other.x()  &&
                  this.y() == other.y()  &&
                  this.z() == other.z()  );
    }
    
    
    public boolean equalsApproximately( Vector3 other , double tolerance )
    {
        return ( this.distanceFrom( other ) < tolerance );
    }
    
    
    public Vector3 add( Vector3 other )
    {
        return new Vector3( this.x() + other.x() ,
                                this.y() + other.y() ,
                                this.z() + other.z() );
    }


    public Vector3 addInplace( Vector3 other )
    {
        this.vx += other.x();
        this.vy += other.y();
        this.vz += other.z();
        return this;
    }
    
    
    public Vector3 identityAdditive()
    {
        return Vector3.zero();
    }

    
    public Vector3 inverseAdditive()
    {
        return new Vector3( -this.x() , -this.y() , -this.z() );
    }


    public Vector3 inverseAdditiveInplace()
    {
        this.vx = -this.x();
        this.vy = -this.y();
        this.vz = -this.z();
        return this;
    }


    public Vector3 subtract( Vector3 other )
    {
        return new Vector3( this.x() - other.x() ,
                                this.y() - other.y() ,
                                this.z() - other.z() );
    }


    public Vector3 subtractInplace( Vector3 other )
    {
        this.vx -= other.x();
        this.vy -= other.y();
        this.vz -= other.z();
        return this;
    }
    
    
    public Vector3 scale( double scalar )
    {
        return new Vector3( this.x() * scalar ,
                                this.y() * scalar ,
                                this.z() * scalar );
    }


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
    
    
    public Matrix crossProductMatrix()
    {
        return Matrix.matrix3x3( 0.0      , -this.z() ,  this.y() ,
                                     this.z() ,  0.0      , -this.x() ,
                                    -this.y() ,  this.x() ,  0.0      );
    }
    
    
    public Matrix outerProduct( Vector3 other )
    {
        return Matrix.matrix3x3( this.x() * other.x() , this.x() * other.y() , this.x() * other.z() ,
                                     this.y() * other.x() , this.y() * other.y() , this.y() * other.z() ,
                                     this.z() * other.x() , this.z() * other.y() , this.z() * other.z() );
    }
    
    
    public Matrix toMatrixAsColumn()
    {
        return Matrix.fromVector3AsColumn( this );
    }
    
    
    public Matrix toMatrixAsRow()
    {
        return Matrix.fromVector3AsRow( this );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public static Vector3 zero()
    {
        return new Vector3( 0.0 , 0.0 , 0.0 );
    }
    
    
    public static Vector3 i()
    {
        return new Vector3( 1.0 , 0.0 , 0.0 );
    }


    public static Vector3 j()
    {
        return new Vector3( 0.0 , 1.0 , 0.0 );
    }


    public static Vector3 k()
    {
        return new Vector3( 0.0 , 0.0 , 1.0 );
    }
    
    
    public static Vector3 random( Random randomNumberGenerator )
    {
        return new Vector3(
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() );
    }
    
}

