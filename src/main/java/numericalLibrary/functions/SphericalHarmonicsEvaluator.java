package numericalLibrary.functions;


import numericalLibrary.types.ComplexNumber;



/**
 * Computes spherical harmonics  Y_l^m( theta , phi ).
 * <p>
 * They are defined as:
 * <p>
 *   Y_l^m( theta , phi ) = P_l^m( cos( theta ) ) e^{ i m phi }
 * <p>
 * where  P_l^m  are the pre-normalized associated Legendre polynomials computed by the underlying
 * {@link PreNormalizedAssociatedLegendrePolynomialEvaluator}.
 * As a consequence:
 * <ul>
 * <li> The harmonics are fully (ortho)normalized: they integrate to 1 over the unit sphere, with  Y_0^0 = 1 / sqrt( 4 pi ) .
 * <li> The Condon-Shortley phase  (-1)^m  is included (inherited from the Legendre recurrence relations).
 * </ul>
 * Only non-negative orders  m = 0 , 1 , ... , l  are evaluated.
 * Negative orders can be obtained from the symmetry relation:
 * <p>
 *   Y_l^{-m}( theta , phi ) = (-1)^m conjugate( Y_l^m( theta , phi ) )
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
	 * @throws IllegalArgumentException if theta is not in [0,pi], or phi is not in [0,2pi].
	 */
	public void evaluate( double theta , double phi )
	{
		if(  theta < 0.0  ||  Math.PI < theta  ) {
			throw new IllegalArgumentException( "theta must be in [0, pi]; found " + theta );
		}
		if(  phi < 0.0  ||  2.0 * Math.PI < phi  ) {
			throw new IllegalArgumentException( "phi must be in [0, 2 pi]; found " + phi );
		}
		// Evaluate normalized associated Legendre polynomials.
		double x = Math.cos( theta );
		this.normalizedAssociatedLegendrePolynomialEvaluator.evaluate( x );
		// Evaluate complex exponentials  e^{ i m phi } in place, without allocating temporaries.
		for( int m=0; m<this.complexExponentials.length; m++ ) {
			this.complexExponentials[m].setModulusAndArgument( 1.0 , m * phi );
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
