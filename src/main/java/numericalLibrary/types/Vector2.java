package numericalLibrary.types;


import java.util.Random;

import numericalLibrary.algebraicStructures.AdditiveAbelianGroupElement;
import numericalLibrary.algebraicStructures.MetricSpaceElement;
import numericalLibrary.algebraicStructures.VectorSpaceElement;



/**
 * Implement 2d vectors.
 */
public class Vector2
    implements
        AdditiveAbelianGroupElement<Vector2>,
        VectorSpaceElement<Vector2>,
        MetricSpaceElement<Vector2>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * First component of the 2d vector.
     */
    private double vx;
    
    /**
     * Second component of the 2d vector.
     */
    private double vy;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a 2d vector.
     * 
     * @param x     first component of the 2d vector.
     * @param y     second component of the 2d vector.
     */
    public Vector2( double x , double y )
    {
        this.vx = x;
        this.vy = y;
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
    
    
    public double x()
    {
        return vx;
    }
    
    
    public double y()
    {
        return vy;
    }
    
    
    public String toString()
    {
        return String.format( "( %2.16f , %2.16f )" , this.x() , this.y() );
    }
    
    
    public Vector2 print()
    {
        System.out.println( this.toString() );
        return this;
    }
    
    
    public Vector2 copy()
    {
        return new Vector2( this.x() , this.y() );
    }
    
    
    public Vector2 setTo( Vector2 other )
    {
        this.vx = other.x();
        this.vy = other.y();
        return this;
    }
    
    
    public boolean equals( Vector2 other )
    {
        return (  this.x() == other.x()  &&
                  this.y() == other.y()  );
    }
    
    
    public boolean equalsApproximately( Vector2 other , double tolerance )
    {
        return ( this.distanceFrom( other ) < tolerance );
    }
    
    
    public Vector2 add( Vector2 other )
    {
        return new Vector2( this.x() + other.x() ,
                                this.y() + other.y() );
    }


    public Vector2 addInplace( Vector2 other )
    {
        this.vx += other.x();
        this.vy += other.y();
        return this;
    }
    
    
    public Vector2 identityAdditive()
    {
        return Vector2.zero();
    }

    
    public Vector2 inverseAdditive()
    {
        return new Vector2( -this.x() , -this.y() );
    }


    public Vector2 inverseAdditiveInplace()
    {
        this.vx = -this.x();
        this.vy = -this.y();
        return this;
    }


    public Vector2 subtract( Vector2 other )
    {
        return new Vector2( this.x() - other.x() ,
                                this.y() - other.y() );
    }


    public Vector2 subtractInplace( Vector2 other )
    {
        this.vx -= other.x();
        this.vy -= other.y();
        return this;
    }
    
    
    public Vector2 scale( double scalar )
    {
        return new Vector2( this.x() * scalar ,
                                this.y() * scalar );
    }


    public Vector2 scaleInplace( double scalar )
    {
        this.vx *= scalar;
        this.vy *= scalar;
        return this;
    }
    
    
    public double dot( Vector2 other )
    {
        return ( this.x() * other.x() +
                 this.y() * other.y() );
    }
    
    
    public double normSquared()
    {
        return this.dot( this );
    }
    
    
    public double norm()
    {
        return Math.sqrt( this.normSquared() );
    }


    public Vector2 normalize()
    {
        return this.scale( 1.0/this.norm() );
    }


    public Vector2 normalizeInplace()
    {
        return this.scaleInplace( 1.0/this.norm() );
    }



    public double distanceFrom( Vector2 other )
    {
        return this.subtract( other ).norm();
    }
    
    
    public Matrix outerProduct( Vector2 other )
    {
        return Matrix.fromEntries2x2(
                this.x() * other.x() , this.x() * other.y() ,
                this.y() * other.x() , this.y() * other.y() );
    }
    
    
    public Matrix toMatrixAsColumn()
    {
        return Matrix.fromVector2AsColumn( this );
    }
    
    
    public Matrix toMatrixAsRow()
    {
        return Matrix.fromVector2AsRow( this );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public static Vector2 zero()
    {
        return new Vector2( 0.0 , 0.0 );
    }
    
    
    public static Vector2 i()
    {
        return new Vector2( 1.0 , 0.0 );
    }


    public static Vector2 j()
    {
        return new Vector2( 0.0 , 1.0 );
    }


    public static Vector2 random( Random randomNumberGenerator )
    {
        return new Vector2(
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() );
    }
    
}

