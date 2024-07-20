package numericalLibrary.types;


import java.util.Random;

import numericalLibrary.algebraicStructures.FieldElement;
import numericalLibrary.algebraicStructures.MetricSpaceElement;
import numericalLibrary.algebraicStructures.VectorSpaceElement;



/**
 * Implements complex numbers.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Complex_number</a>
 */
public class ComplexNumber
    implements
        FieldElement<ComplexNumber>,
        VectorSpaceElement<ComplexNumber>,
        MetricSpaceElement<ComplexNumber>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Real part of the {@link ComplexNumber}.
     */
    private double x;
    
    /**
     * Imaginary part of the {@link ComplexNumber}.
     */
    private double y;  // imaginary part
    
    

    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link ComplexNumber}.
     * 
     * @param real  real part of the {@link ComplexNumber}.
     * @param imag  imaginary part of the {@link ComplexNumber}.
     */
    public ComplexNumber( double real , double imag )
    {
        this.x = real;
        this.y = imag;
    }



    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns real part of the {@link ComplexNumber}.
     * 
     * @return  real part of the {@link ComplexNumber}.
     */
    public double re()
    {
        return this.x;
    }
    
    
    /**
     * Returns imaginary part of the {@link ComplexNumber}.
     * 
     * @return  imaginary part of the {@link ComplexNumber}.
     */
    public double im()
    {
        return this.y;
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
    public boolean equals( ComplexNumber other )
    {
        return ( this.re() == other.re() &&
                 this.im() == other.im() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber setTo( ComplexNumber other )
    {
        this.x = other.re();
        this.y = other.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber copy()
    {
        return new ComplexNumber( this.re() , this.im() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber add( ComplexNumber other )
    {
        return new ComplexNumber( this.re() + other.re() ,
                            this.im() + other.im() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber addInplace( ComplexNumber other )
    {
        this.x += other.re();
        this.y += other.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber setToSum( ComplexNumber first , ComplexNumber second )
    {
        this.x = first.re() + second.re();
        this.y = first.im() + second.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber identityAdditive()
    {
        return ComplexNumber.zero();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber setToZero()
    {
        this.x = 0.0;
        this.y = 0.0;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber inverseAdditive()
    {
        return new ComplexNumber( -this.re() , -this.im() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber inverseAdditiveInplace()
    {
        this.x = -this.re();
        this.y = -this.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber subtract( ComplexNumber other )
    {
        return new ComplexNumber( this.re() - other.re() ,
                            this.im() - other.im() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber subtractInplace( ComplexNumber other )
    {
        this.x -= other.re();
        this.y -= other.im();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber multiply( ComplexNumber other )
    {
        return new ComplexNumber(  this.re() * other.re()  -  this.im() * other.im()  ,
                             this.re() * other.im()  +  this.im() * other.re()  );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber multiplyInplace( ComplexNumber other )
    {
        double real = this.re();
        double imag = this.im();
        this.x = real * other.re()  -  imag * other.im();
        this.y = real * other.im()  +  imag * other.re();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber setToProduct( ComplexNumber first , ComplexNumber second )
    {
        this.x = first.re() * second.re()  -  first.im() * second.im();
        this.y = first.re() * second.im()  +  first.im() * second.re();
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber identityMultiplicative()
    {
        return ComplexNumber.one();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber setToOne()
    {
        this.x = 1.0;
        this.y = 0.0;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber inverseMultiplicative()
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
    public ComplexNumber inverseMultiplicativeInplace()
    {
        double normSquared = this.normSquared();
        if( normSquared == 0.0 ) {
            throw new IllegalArgumentException( "Zero has no reciprocal." );
        }
        return this.conjugateInplace().scaleInplace( 1.0/normSquared );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber divide( ComplexNumber other )
    {
        double normSquared = this.normSquared();
        if( normSquared == 0 ) {
            throw new IllegalArgumentException( "Division by zero is undefined." );
        }
        double oneOverNormSquared = 1.0/normSquared;
        return new ComplexNumber(
                ( this.re() * other.re() + this.im() * other.im() ) * oneOverNormSquared ,
                ( this.re() * other.im() - this.im() * other.re() ) * oneOverNormSquared );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber divideInplace( ComplexNumber other )
    {
        double normSquared = this.normSquared();
        if( normSquared == 0 ) {
            throw new IllegalArgumentException( "Division by zero is undefined." );
        }
        double re = this.re();
        double im = this.im();
        double oneOverNormSquared = 1.0/normSquared;
        this.x = ( re * other.re() + im * other.im() ) * oneOverNormSquared;
        this.y = ( re * other.im() - im * other.re() ) * oneOverNormSquared;
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber scale( double scalar )
    {
        return new ComplexNumber(  this.re() * scalar  ,  this.im() * scalar  );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Overridden method to make it more efficient.
     */
    public ComplexNumber scaleInplace( double scalar )
    {
        this.x *= scalar;
        this.y *= scalar;
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
    
    public ComplexNumber sqrt()
    {
        double modulus = this.norm();
        return new ComplexNumber( Math.sqrt( 0.5 * ( modulus + this.re() ) ) ,
                            Math.sqrt( 0.5 * ( modulus - this.re() ) ) * Math.signum( this.im() ) );
    }
    
    
    public ComplexNumber exp() {
        return new ComplexNumber( Math.cos( this.im() ) , Math.sin( this.im() ) ).scaleInplace( Math.exp( this.re() ) );
    }


    public ComplexNumber log() {
        return new ComplexNumber( Math.log( this.re() ) , this.argument() );
    }

    
    
    
    public double dot( ComplexNumber other )
    {
        return ( this.re() * other.re() +
                 this.im() * other.im() );
    }
    

    public double normSquared()
    {
        return this.dot( this );
    }


    public double norm()
    {
        return Math.sqrt( this.normSquared() );
    }


    public ComplexNumber normalize()
    {
        return this.scale( 1.0/this.norm() );
    }


    public ComplexNumber normalizeInplace()
    {
        return this.scaleInplace( 1.0/this.norm() );
    }


    public double distanceFrom( ComplexNumber other )
    {
        ComplexNumber delta = this.subtract( other );
        return delta.norm();
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

    public ComplexNumber conjugate()
    {
        return new ComplexNumber( this.re() , -this.im() );
    }
    
    
    public ComplexNumber conjugateInplace()
    {
        // this.x = this.x;  this stays the same
        this.y = -this.im();
        return this;
    }
    
    
    public double argument()
    {
        return Math.atan2( this.im() , this.re() );
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
    public static ComplexNumber zero()
    {
        return new ComplexNumber( 0.0 , 0.0 );
    }
    
    
    /**
     * Returns the identity element stored in a new instance.
     * <p>
     * That is {@code 1  +  0 · i }.
     * 
     * @return  identity element stored in a new instance.
     */
    public static ComplexNumber one()
    {
        return new ComplexNumber( 1.0 , 0.0 );
    }
    
    
    /**
     * Returns i {@link ComplexNumber} stored in a new instance.
     * <p>
     * That is {@code 0  +  1 · epsilon}.
     * 
     * @return  i {@link ComplexNumber} stored in a new instance.
     */
    public static ComplexNumber i()
    {
        return new ComplexNumber( 0.0 , 1.0 );
    }
    
    
    /**
     * Returns a {@link ComplexNumber} created from its modulus and argument.
     * 
     * @param modulus   modulus of the {@link ComplexNumber} to be created.
     * @param argument  argument of the {@link ComplexNumber} to be created.
     * @return
     */
    public static ComplexNumber fromModulusAndArgument( double modulus , double argument )
    {
        return new ComplexNumber( Math.cos( argument ) , Math.sin( argument ) ).scaleInplace( modulus );
    }
    
    
    /**
     * Returns a random {@link ComplexNumber} stored in a new instance.
     * <p>
     * The random {@link ComplexNumber} is extracted from a Gaussian distribution with mean 0 and standard distribution 1.
     * 
     * @param randomNumberGenerator     random number generator used to extract the next normally distributed sample.
     * @return  random {@link ComplexNumber} stored in a new instance.
     */
    public static ComplexNumber random( Random randomNumberGenerator )
    {
        return new ComplexNumber(
                randomNumberGenerator.nextGaussian() ,
                randomNumberGenerator.nextGaussian() );
    }
    
}
