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
    /// PRIVATE VARIABLES
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
    /// PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the x component of the {@link Vector2}.
     * 
     * @param x     value for the x component to be set.
     */
    public void setX( double x )
    {
        this.vx = x;
    }
    
    
    /**
     * Sets the y component of the {@link Vector2}.
     * 
     * @param y     value for the y component to be set.
     */
    public void setY( double y )
    {
        this.vy = y;
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
     * {@inheritDoc}
     */
    public String toString()
    {
        return String.format( "( %2.16f , %2.16f )" , this.x() , this.y() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean equals( Vector2 other )
    {
        return (  this.x() == other.x()  &&
                  this.y() == other.y()  );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * In particular, it is considered equal if for each component, the absolute error | this_i - other_i | is below one of the thresholds:
     * <ul>
     * <li> toleranceAbsolute
     * <li> toleranceRelative * 0.5 * ( |this_i| + |other_i| )
     * </ul>
     */
    public boolean equalsApproximately( Vector2 other , double toleranceAbsolute , double toleranceRelative )
    {
    	double dx = Math.abs( this.x() - other.x() );
    	double dy = Math.abs( this.y() - other.y() );
    	boolean withinAbsoluteTolerance = (  dx <= toleranceAbsolute  &&  dy <= toleranceAbsolute  );
    	boolean withinRelativeTolerance = (
    			dx <= toleranceRelative * 0.5 * ( Math.abs( this.x() ) + Math.abs( other.x() ) )  &&
    			dy <= toleranceRelative * 0.5 * ( Math.abs( this.y() ) + Math.abs( other.y() ) )  );
    	return ( withinAbsoluteTolerance || withinRelativeTolerance );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean isNaN()
    {
        return (  Double.isNaN( this.x() )  ||
                  Double.isNaN( this.y() )  );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector2 copy()
    {
        return new Vector2( this.x() , this.y() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector2 setTo( Vector2 other )
    {
        this.vx = other.x();
        this.vy = other.y();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector2 add( Vector2 other )
    {
        return new Vector2( this.x() + other.x() ,
                                this.y() + other.y() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector2 addInplace( Vector2 other )
    {
        this.vx += other.x();
        this.vy += other.y();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector2 identityAdditive()
    {
        return Vector2.zero();
    }

    
    /**
     * {@inheritDoc}
     */
    public Vector2 inverseAdditive()
    {
        return new Vector2( -this.x() , -this.y() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector2 inverseAdditiveInplace()
    {
        this.vx = -this.x();
        this.vy = -this.y();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector2 subtract( Vector2 other )
    {
        return new Vector2( this.x() - other.x() ,
                                this.y() - other.y() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector2 subtractInplace( Vector2 other )
    {
        this.vx -= other.x();
        this.vy -= other.y();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector2 scale( double scalar )
    {
        return new Vector2( this.x() * scalar ,
                                this.y() * scalar );
    }
    
    
    /**
     * {@inheritDoc}
     */
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
    
    
    /**
     * {@inheritDoc}
     */
    public double distanceFrom( Vector2 other )
    {
        return this.subtract( other ).norm();
    }
    
    
    public MatrixReal outerProduct( Vector2 other )
    {
        return MatrixReal.fromEntries2x2(
                this.x() * other.x() , this.x() * other.y() ,
                this.y() * other.x() , this.y() * other.y() );
    }
    
    
    public MatrixReal toMatrixAsColumn()
    {
        return MatrixReal.fromVector2AsColumn( this );
    }
    
    
    public MatrixReal toMatrixAsRow()
    {
        return MatrixReal.fromVector2AsRow( this );
    }
    
    
    /**
     * Returns the {@link Vector2} as a flat array.
     * <p>
     * The output is:
     * new double[] { this.x() , this.y() };
     * 
     * @return  {@link Vector2} as a flat array.
     */
    public double[] toFlatArray()
    {
        return new double[] { this.x() , this.y() };
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    /// PUBLIC STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the zero element stored in a new instance.
     * <p>
     * That is the vector (0,0).
     * 
     * @return   zero element stored in a new instance.
     */
    public static Vector2 zero()
    {
        return new Vector2( 0.0 , 0.0 );
    }
    
    
    /**
     * Returns the i unit vector stored in a new instance.
     * <p>
     * That is the vector (1,0).
     * 
     * @return   i unit vector stored in a new instance.
     */
    public static Vector2 i()
    {
        return new Vector2( 1.0 , 0.0 );
    }
    
    
    /**
     * Returns the j unit vector stored in a new instance.
     * <p>
     * That is the vector (0,1).
     * 
     * @return   j unit vector stored in a new instance.
     */
    public static Vector2 j()
    {
        return new Vector2( 0.0 , 1.0 );
    }
    
    
    /**
     * Returns a new {@link Vector2} constructed from its components.
     * 
     * @param xComponent	x-component of the {@link Vector2}.
     * @param yComponent	y-component of the {@link Vector2}.
     * @return	new {@link Vector2} constructed from its components.
     */
    public static Vector2 fromComponents( double xComponent , double yComponent )
    {
    	return new Vector2( xComponent , yComponent );
    }
    
    
    /**
     * Returns a new {@link Vector2} constructed from a flat array.
     * 
     * @param flatArray     array that contains the {@link Vector2} elements as {@code double[] { v.x() , v.y() }}.
     * @return  new {@link Vector2} constructed from a flat array.
     */
    public static Vector2 fromFlatArray( double[] flatArray )
    {
        if( flatArray.length < 2 ) {
            throw new IllegalArgumentException( "Not enough elements in flatArray; found " + flatArray.length + "; expected 2 or more." );
        }
        return new Vector2( flatArray[0] , flatArray[1] );
    }
    
    
    /**
     * Returns a new random {@link Vector2} with normally distributed components sampled from {@link Random}.
     * <p>
     * The components of the {@link Vector2} are normally distributed with mean {@code 0.0} and standard deviation {@code 1.0}.
     * 
     * @param randomNumberGenerator     {@link Random} used to generate the components of the returned {@link Vector2}.
     * @return  new random new random {@link Vector2} with normally distributed components sampled from {@link Random}.
     */
    public static Vector2 random( Random randomNumberGenerator )
    {
        return new Vector2(
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    /// PRIVATE CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a 2d vector.
     * 
     * @param x     first component of the 2d vector.
     * @param y     second component of the 2d vector.
     */
    private Vector2( double x , double y )
    {
        this.vx = x;
        this.vy = y;
    }
    
}

