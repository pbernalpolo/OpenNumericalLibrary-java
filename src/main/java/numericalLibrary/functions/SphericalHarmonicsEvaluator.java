package numericalLibrary.functions;


import numericalLibrary.types.ComplexNumber;



/**
 * Computes spherical harmonics.
 * <p>
 * It is composed of a {@link PreNormalizedAssociatedLegendrePolynomialEvaluator}.
 */
public class SphericalHarmonicsEvaluator
{
	////////////////////////////////////////////////////////////////
	/// PRIVATE VARIABLES
	////////////////////////////////////////////////////////////////
	
	/**
	 * {@link PreNormalizedAssociatedLegendrePolynomialEvaluator} used to compute the Legendre polynomials contribution.
	 */
	private PreNormalizedAssociatedLegendrePolynomialEvaluator normalizedAssociatedLegendrePolynomialEvaluator;
	
	/**
	 * Complex exponentials resulting from evaluating  e^{ i m phi }  being  phi  the azimuth in the interval [0,2pi].
	 */
	private ComplexNumber[] complexExponentials;
	
	
	
	////////////////////////////////////////////////////////////////
	/// PUBLIC CONSTRUCTORS
	////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@link SphericalHarmonicsEvaluator}.
	 * 
	 * @param lMaximum		maximum degree  l  to be evaluated. The degree  l  will range in  l = 0 , 1 , ... , lMaximum
	 */
	public SphericalHarmonicsEvaluator( int lMaximum )
	{
		// Create normalized associated Legendre polynomials.
		this.normalizedAssociatedLegendrePolynomialEvaluator = new PreNormalizedAssociatedLegendrePolynomialEvaluator( lMaximum );
		// Create array of complex exponentials.
		this.complexExponentials = new ComplexNumber[ lMaximum + 1 ];
		for( int m=0; m<this.complexExponentials.length; m++ ) {
			this.complexExponentials[m] = ComplexNumber.one();
		}
	}
	
	
	
	////////////////////////////////////////////////////////////////
	/// PUBLIC METHODS
	////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the spherical harmonics at ( theta , phi ).
	 * 
	 * @param theta		polar angle. It must be in the interval [0,pi].
	 * @param phi	azimuth angle. It must be in the interval [0,2pi].
	 */
	public void evaluate( double theta , double phi )
	{
		// Evaluate normalized associated Legendre polynomials.
		double x = Math.cos( theta );
		this.normalizedAssociatedLegendrePolynomialEvaluator.evaluate( x );
		// Evaluate complex exponentials.
		for( int m=0; m<this.complexExponentials.length; m++ ) {
			this.complexExponentials[m].setTo( ComplexNumber.i().scaleInplace( m * phi ).exp() );
		}
	}
	
	
	/**
	 * Returns the spherical harmonic  Y_l^m( theta , phi ).
	 * <p>
	 * The evaluation point is set from the last {@link #evaluate(double, double)} call.
	 * 
	 * @param l		polynomial degree in the range l = 0 , 1 , ... , lMaximum
	 * @param m		polynomial order in the range m = 0 , 1 , ... , l
	 * @return	{@link ComplexNumber} that results from evaluating the spherical harmonic  Y_l^m( theta , phi ).
	 */
	public ComplexNumber getSphericalHarmonicsValue( int l , int m )
	{
		return this.complexExponentials[m].scale( this.normalizedAssociatedLegendrePolynomialEvaluator.getPolynomialValue( l , m ) );
	}
	
}
