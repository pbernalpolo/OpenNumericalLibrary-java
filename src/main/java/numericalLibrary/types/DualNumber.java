package numericalLibrary.types;


import java.util.Random;

import numericalLibrary.algebraicStructures.FieldElement;
import numericalLibrary.algebraicStructures.MetricSpaceElement;
import numericalLibrary.algebraicStructures.VectorSpaceElement;



/**
 * Implements dual numbers.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Dual_number</a>
 */
public class DualNumber
    implements
        FieldElement<DualNumber>,
        VectorSpaceElement<DualNumber>,
        MetricSpaceElement<DualNumber>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    /**
     * Real part of the {@link DualNumber}.
     */
    private double a;
    
    /**
     * Imaginary part of the {@link DualNumber}.
     */
    private double b;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link DualNumber}.
     * 
     * @param real  real part of the {@link DualNumber}.
     * @param imag  imaginary part of the {@link DualNumber}.
     */
    public DualNumber( double real , double imag )
    {
        this.a = real;
        this.b = imag;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the real part of the {@link DualNumber}.
     * 
     * @return  real part of the {@link DualNumber}.
     */
    public double re()
    {
        return this.a;
    }
    
    
    /**
     * Returns the imaginary part of the {@link DualNumber}.
     * 
     * @return  imaginary part of the {@link DualNumber}.
     */
    public double im()
    {
        return this.b;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return String.format( "%f + i %f" , this.re() , this.im() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean equals( DualNumber other )
    {
        return ( this.re() == other.re() &&
                 this.im() == other.im() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber setTo( DualNumber other )
    {
        this.a = other.re();
        this.b = other.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber copy()
    {
        return new DualNumber( this.re() , this.im() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber add( DualNumber other )
    {
        return new DualNumber(
                this.re() + other.re() ,
                this.im() + other.im() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber addInplace( DualNumber other )
    {
        this.a += other.re();
        this.b += other.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber setToSum( DualNumber first , DualNumber second )
    {
        this.a = first.re() + second.re();
        this.b = first.im() + second.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber identityAdditive()
    {
        return DualNumber.zero();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber setToZero()
    {
        this.a = 0.0;
        this.b = 0.0;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber inverseAdditive()
    {
        return new DualNumber( -this.re() , -this.im() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber inverseAdditiveInplace()
    {
        this.a = -this.re();
        this.b = -this.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber subtract( DualNumber other )
    {
        return new DualNumber(
                this.re() - other.re() ,
                this.im() - other.im() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber subtractInplace( DualNumber other )
    {
        this.a -= other.re();
        this.b -= other.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber multiply( DualNumber other )
    {
        return new DualNumber(
                this.re() * other.re() ,
                this.re() * other.im()  +  this.im() * other.re() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber multiplyInplace( DualNumber other )
    {
        this.b = this.re() * other.im()  +  this.im() * other.re();
        this.a = this.re() * other.re();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber setToProduct( DualNumber first , DualNumber second )
    {
        this.a = first.re() * second.re();
        this.b = first.re() * second.im()  +  first.im() * second.re();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber identityMultiplicative()
    {
        return DualNumber.one();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber setToOne()
    {
        this.a = 1.0;
        this.b = 0.0;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber inverseMultiplicative()
    {
        if( this.re() == 0.0 ) {
            throw new IllegalArgumentException( "Reciprocal is not defined when the real part is zero." );
        }
        return this.conjugate().scaleInplace( 1.0/this.normSquared() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber inverseMultiplicativeInplace()
    {
        if( this.re() == 0.0 ) {
            throw new IllegalArgumentException( "Reciprocal is not defined when the real part is zero." );
        }
        return this.conjugateInplace().scaleInplace( 1.0/this.normSquared() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber divide( DualNumber other )
    {
        if( other.re() == 0.0 ) {
            throw new IllegalArgumentException( "Division is undefined when the real part of the divisor is zero." );
        }
        return new DualNumber(
                this.re() / other.re() ,
                ( this.im() * other.re() - this.re() * other.im() ) / ( other.re() * other.re() ) );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public DualNumber divideInplace( DualNumber other )
    {
        if( other.re() == 0.0 ) {
            throw new IllegalArgumentException( "Division is undefined when the real part of the divisor is zero." );
        }
        this.b = ( this.im() * other.re() - this.re() * other.im() ) / ( other.re() * other.re() );
        this.a = this.re() / other.re();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber scale( double scalar )
    {
        return new DualNumber(
                this.re() * scalar ,
                this.im() * scalar );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public DualNumber scaleInplace( double scalar )
    {
        this.a *= scalar;
        this.b *= scalar;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method because the distance between dual numbers should not be defined through {@link DualNumber#norm()}.
     */
    public double distanceFrom( DualNumber other )
    {
        DualNumber difference = this.subtract( other );
        return Math.sqrt( difference.re() * difference.re() + difference.im() * difference.im() );
    }
    
    
    public double normSquared()
    {
        return ( this.re() * this.re() );
    }
    
    
    public double norm()
    {
        return Math.abs( this.re() );
    }
    
    
    public DualNumber conjugate()
    {
        return new DualNumber( this.re() , -this.im() );
    }
    
    
    public DualNumber conjugateInplace()
    {
        // this.a = this.re();  this stays the same
        this.b = -this.im();
        return this;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the zero element stored in a new instance.
     * <p>
     * That is {@code 0  +  0 · epsilon}.
     * 
     * @return   zero element stored in a new instance.
     */
    public static DualNumber zero()
    {
        return new DualNumber( 0.0 , 0.0 );
    }
    
    
    /**
     * Returns the identity element stored in a new instance.
     * <p>
     * That is {@code 1  +  0 · epsilon}.
     * 
     * @return  identity element stored in a new instance.
     */
    public static DualNumber one()
    {
        return new DualNumber( 1.0 , 0.0 );
    }
    
    
    /**
     * Returns epsilon stored in a new instance.
     * <p>
     * That is {@code 0  +  1 · epsilon}.
     * 
     * @return  epsilon stored in a new instance.
     */
    public static DualNumber epsilon()
    {
        return new DualNumber( 0.0 , 1.0 );
    }
    
    
    /**
     * Returns a random {@link DualNumber} stored in a new instance.
     * <p>
     * The random {@link DualNumber} is extracted from a Gaussian distribution with mean 0 and standard distribution 1.
     * 
     * @param randomNumberGenerator     random number generator used to extract the next normally distributed sample.
     * @return  random {@link DualNumber} stored in a new instance.
     */
    public static DualNumber random( Random randomNumberGenerator )
    {
        return new DualNumber(
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() );
    }
    
}
