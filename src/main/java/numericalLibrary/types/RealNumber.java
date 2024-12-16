package numericalLibrary.types;


import java.util.Random;

import numericalLibrary.algebraicStructures.FieldElement;
import numericalLibrary.algebraicStructures.MetricSpaceElement;
import numericalLibrary.algebraicStructures.VectorSpaceElement;



/**
 * Implements real numbers.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Real_number</a>
 */
public class RealNumber
    implements
        FieldElement<RealNumber>,
        VectorSpaceElement<RealNumber>,
        MetricSpaceElement<RealNumber>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * The {@link RealNumber}.
     */
    private double x;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link RealNumber}.
     * 
     * @param realNumber  double that represents the {@link RealNumber}.
     */
    public RealNumber( double realNumber )
    {
        this.x = realNumber;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the {@link RealNumber} as a double.
     * 
     * @return  {@link RealNumber} as a double.
     */
    public double toDouble()
    {
        return this.x;
    }
    
    
    /**
     * Sets the value of the {@link RealNumber}.
     * 
     * @param value     value to be set.
     * @return  this {@link RealNumber}.
     */
    public RealNumber setValue( double value )
    {
        this.x = value;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return String.format( "%f" , this.toDouble() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean equals( RealNumber other )
    {
        return ( this.toDouble() == other.toDouble() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber setTo( RealNumber other )
    {
        this.x = other.toDouble();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber copy()
    {
        return new RealNumber( this.toDouble() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber add( RealNumber other )
    {
        return new RealNumber( this.toDouble() + other.toDouble() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber addInplace( RealNumber other )
    {
        this.x += other.toDouble();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber setToSum( RealNumber first , RealNumber second )
    {
        this.x = first.toDouble() + second.toDouble();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber identityAdditive()
    {
        return RealNumber.zero();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber setToZero()
    {
        this.x = 0.0;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber inverseAdditive()
    {
        return new RealNumber( -this.toDouble() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber inverseAdditiveInplace()
    {
        this.x = -this.toDouble();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber subtract( RealNumber other )
    {
        return new RealNumber( this.toDouble() - other.toDouble() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber subtractInplace( RealNumber other )
    {
        this.x -= other.toDouble();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber multiply( RealNumber other )
    {
        return new RealNumber( this.toDouble() * other.toDouble() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber multiplyInplace( RealNumber other )
    {
        this.x *= other.toDouble();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber setToProduct( RealNumber first , RealNumber second )
    {
        this.x = first.toDouble() * second.toDouble();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber identityMultiplicative()
    {
        return RealNumber.one();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber setToOne()
    {
        this.x = 1.0;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber inverseMultiplicative()
    {
        double value = this.toDouble();
        if( value == 0.0 ) {
            throw new IllegalArgumentException( "Zero has no reciprocal." );
        }
        return new RealNumber( 1.0/value );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber inverseMultiplicativeInplace()
    {
        double value = this.toDouble();
        if( value == 0.0 ) {
            throw new IllegalArgumentException( "Zero has no reciprocal." );
        }
        this.x = 1.0/this.x;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber divide( RealNumber other )
    {
        double otherValue = other.toDouble();
        if( otherValue == 0 ) {
            throw new IllegalArgumentException( "Division by zero is undefined." );
        }
        return new RealNumber( this.toDouble() / otherValue );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber divideInplace( RealNumber other )
    {
        double otherValue = other.toDouble();
        if( otherValue == 0 ) {
            throw new IllegalArgumentException( "Division by zero is undefined." );
        }
        this.x /= other.toDouble();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber scale( double scalar )
    {
        return new RealNumber( this.toDouble() * scalar );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public RealNumber scaleInplace( double scalar )
    {
        this.x *= scalar;
        return this;
    }
    
    
    /*
    public Complex absSquared()
    {
        return new Complex( this.normSquared() , 0.0 );
    }
    
    
    public Complex abs()
    {
        return new Complex( this.norm() , 0.0 );
    }
    */
    
    public RealNumber sqrt()
    {
        return new RealNumber( Math.sqrt( this.toDouble() ) );
    }
    
    
    public RealNumber exp() {
        return new RealNumber( Math.exp( this.toDouble() ) );
    }


    public RealNumber log() {
        return new RealNumber( Math.log( this.toDouble() ) );
    }

    
    
    
    public double dot( RealNumber other )
    {
        return ( this.toDouble() * other.toDouble() );
    }
    
    
    public double normSquared()
    {
        return ( this.toDouble() * this.toDouble() );
    }
    
    
    public double norm()
    {
        return Math.abs( this.toDouble() );
    }


    public RealNumber normalize()
    {
        return new RealNumber( Math.signum( this.toDouble() ) );
    }


    public RealNumber normalizeInplace()
    {
        this.x = Math.signum( this.toDouble() );
        return this;
    }


    public double distanceFrom( RealNumber other )
    {
        return Math.abs( this.toDouble() - other.toDouble() );
    }
    

/*    public Complex real()
    {
        return new Complex( this.re() , 0.0 );
    }


    public Complex imag()
    {
        return new Complex( 0.0 , this.im() );
    }
*/

    public RealNumber conjugate()
    {
        return this.copy();
    }
    
    
    public RealNumber conjugateInplace()
    {
        // this.x = this.x;  this stays the same
        return this;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the zero element stored in a new instance.
     * <p>
     * That is {@code 0  +  0 · i }.
     * 
     * @return   zero element stored in a new instance.
     */
    public static RealNumber zero()
    {
        return new RealNumber( 0.0 );
    }
    
    
    /**
     * Returns the identity element stored in a new instance.
     * <p>
     * That is {@code 1  +  0 · i }.
     * 
     * @return  identity element stored in a new instance.
     */
    public static RealNumber one()
    {
        return new RealNumber( 1.0 );
    }
    
    
    /**
     * Returns a random {@link RealNumber} stored in a new instance.
     * <p>
     * The random {@link RealNumber} is extracted from a Gaussian distribution with mean 0 and standard distribution 1.
     * 
     * @param randomNumberGenerator     random number generator used to extract the next normally distributed sample.
     * @return  random {@link RealNumber} stored in a new instance.
     */
    public static RealNumber random( Random randomNumberGenerator )
    {
        return new RealNumber( randomNumberGenerator.nextGaussian() );
    }
    
}
