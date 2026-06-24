package numericalLibrary.functions;



/**
 * Computes spherical harmonics  Y_l^m( theta , phi )  and their colatitude derivatives.
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
 * <br>
 *   Y_l^{-m}( theta , phi ) = (-1)^m conjugate( Y_l^m( theta , phi ) )
 * <p>
 * The real and imaginary parts are exposed directly (instead of a complex type)
 * so that callers can accumulate large sums without allocating temporaries.
 * <p>
 * The colatitude derivative is  dY_l^m/dtheta = - sin( theta ) [ dP_l^m/dx(x) ]_{x=cos theta} e^{ i m phi } .
 * This evaluator exposes it in the scaled, pole-safe form (multiplied by  sin( theta ) ):
 * <br>
 *   sin( theta ) dY_l^m/dtheta = [ ( x^2 - 1 ) dP_l^m/dx(x) ]_{x=cos theta} e^{ i m phi }
 * <br>
 * (using  x^2 - 1 = -sin^2( theta ) ).
 * The extra  sin( theta )  factor removes the  0/0  limit at the poles.
 * 
 * See {@link #evaluateDerivatives()}.
 */
public class SphericalHarmonicsEvaluator
{
	////////////////////////////////////////////////////////////////
	/// PRIVATE VARIABLES
	////////////////////////////////////////////////////////////////

	/**
	 * {@link PreNormalizedAssociatedLegendrePolynomialEvaluator} used to compute the Legendre polynomials contribution and its derivative.
	 */
	private final PreNormalizedAssociatedLegendrePolynomialEvaluator legendre;

	/**
	 * Real part of the complex exponential  e^{ i m phi } , i.e.  cos( m phi ) , for the last {@link #evaluate(double, double, double)} call.
	 */
	private final double[] cos_mPhi;

	/**
	 * Imaginary part of the complex exponential  e^{ i m phi } , i.e.  sin( m phi ) , for the last {@link #evaluate(double, double, double)} call.
	 */
	private final double[] sin_mPhi;



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
		this.legendre = new PreNormalizedAssociatedLegendrePolynomialEvaluator( lMaximum );
		this.cos_mPhi = new double[ lMaximum + 1 ];
		this.sin_mPhi = new double[ lMaximum + 1 ];
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
	public void evaluateWithThetaAndPhi( double theta , double phi )
	{
		if(  theta < 0.0  ||  Math.PI < theta  ) {
			throw new IllegalArgumentException( "theta must be in [0, pi]; found " + theta );
		}
		if(  phi < 0.0  ||  2.0 * Math.PI < phi  ) {
			throw new IllegalArgumentException( "phi must be in [0, 2 pi]; found " + phi );
		}
		this.evaluate( Math.cos( theta ) , Math.cos( phi ) , Math.sin( phi ) );
	}


	/**
	 * Evaluates the spherical harmonics at ( theta , phi ), taking  cos( theta ) ,  cos( phi )  and  sin( phi )  directly.
	 * <p>
	 * Prefer this method when these values are already available (for example, as direction cosines): it avoids recovering
	 * the angles only to take their cosine and sine again, which is wasteful and, for  theta , ill-conditioned near the poles.
	 *
	 * @param cosTheta	cosine of the polar angle. It must be in the interval [-1,1].
	 * @param cosPhi	cosine of the azimuth angle.
	 * @param sinPhi	sine of the azimuth angle.  ( cosPhi , sinPhi )  must be a unit vector.
	 * @throws IllegalArgumentException if cosTheta is not in [-1,1], or ( cosPhi , sinPhi ) is not a unit vector.
	 */
	public void evaluateWithCosThetaCosPhiAndSinPhi( double cosTheta , double cosPhi , double sinPhi )
	{
		if(  cosTheta < -1.0  ||  1.0 < cosTheta  ) {
			throw new IllegalArgumentException( "cosTheta must be in [-1, 1]; found " + cosTheta );
		}
		if(  Math.abs( cosPhi * cosPhi + sinPhi * sinPhi - 1.0 ) > 1.0e-9  ) {
			throw new IllegalArgumentException( "( cosPhi , sinPhi ) must be a unit vector; found ( " + cosPhi + " , " + sinPhi + " )" );
		}
		this.evaluate( cosTheta , cosPhi , sinPhi );
	}


	/**
	 * Evaluates the colatitude derivatives of the spherical harmonics at the point set by the last call to either:
	 * <ul>
	 * <li> {@link #evaluateWithThetaAndPhi(double, double)}
	 * <li> {@link #evaluateWithCosThetaCosPhiAndSinPhi(double, double, double)}
	 * </ul>
	 * <p>
	 * The derivative is provided in the scaled, pole-safe form  sin( theta ) dY_l^m/dtheta = [ ( x^2 - 1 ) dP_l^m/dx(x) ]_{x=cos theta} e^{ i m phi } ,
	 * accessed through {@link #getSphericalHarmonicsDerivativeRealPart(int, int)} and
	 * {@link #getSphericalHarmonicsDerivativeImaginaryPart(int, int)}.
	 */
	public void evaluateDerivatives()
	{
		this.legendre.evaluateDerivatives();
	}


	/**
	 * Returns the real part of the spherical harmonic  Y_l^m( theta , phi ):
	 * P_l^m( cos theta ) cos( m phi ) .
	 * <p>
	 * The evaluation point is set from the last call to either:
	 * <ul>
	 * <li> {@link #evaluateWithThetaAndPhi(double, double)}
	 * <li> {@link #evaluateWithCosThetaCosPhiAndSinPhi(double, double, double)}
	 * </ul>
	 *
	 * @param l		polynomial degree in the range l = 0 , 1 , ... , lMaximum
	 * @param m		polynomial order in the range m = 0 , 1 , ... , l
	 * @return	real part of  Y_l^m( theta , phi ) .
	 */
	public double getSphericalHarmonicsRealPart( int l , int m )
	{
		return this.cos_mPhi[m] * this.legendre.getPolynomialValue( l , m );
	}


	/**
	 * Returns the imaginary part of the spherical harmonic  Y_l^m( theta , phi ):
	 * P_l^m( cos theta ) sin( m phi ) .
	 * <p>
	 * The evaluation point is set from the last call to either:
	 * <ul>
	 * <li> {@link #evaluateWithThetaAndPhi(double, double)}
	 * <li> {@link #evaluateWithCosThetaCosPhiAndSinPhi(double, double, double)}
	 * </ul>
	 *
	 * @param l		polynomial degree in the range l = 0 , 1 , ... , lMaximum
	 * @param m		polynomial order in the range m = 0 , 1 , ... , l
	 * @return	imaginary part of  Y_l^m( theta , phi ) .
	 */
	public double getSphericalHarmonicsImaginaryPart( int l , int m )
	{
		return this.sin_mPhi[m] * this.legendre.getPolynomialValue( l , m );
	}


	/**
	 * Returns the real part of the scaled colatitude derivative  sin( theta ) dY_l^m/dtheta:
	 * {@code [ ( x^2 - 1 ) dP_l^m/dx(x) ]_{x=cos theta} cos( m phi )}.
	 * <p>
	 * Requires a previous {@link #evaluateDerivatives()} call.
	 *
	 * @param l		polynomial degree in the range l = 0 , 1 , ... , lMaximum
	 * @param m		polynomial order in the range m = 0 , 1 , ... , l
	 * @return	real part of  sin( theta ) dY_l^m/dtheta .
	 */
	public double getSphericalHarmonicsDerivativeRealPart( int l , int m )
	{
		return this.cos_mPhi[m] * this.legendre.getPolynomialDerivativeTimesX2Minus1( l , m );
	}


	/**
	 * Returns the imaginary part of the scaled colatitude derivative  sin( theta ) dY_l^m/dtheta , i.e.
	 * {@code [ ( x^2 - 1 ) dP_l^m/dx(x) ]_{x=cos theta} sin( m phi )}.
	 * <p>
	 * Requires a previous {@link #evaluateDerivatives()} call.
	 *
	 * @param l		polynomial degree in the range l = 0 , 1 , ... , lMaximum
	 * @param m		polynomial order in the range m = 0 , 1 , ... , l
	 * @return	imaginary part of  sin( theta ) dY_l^m/dtheta .
	 */
	public double getSphericalHarmonicsDerivativeImaginaryPart( int l , int m )
	{
		return this.sin_mPhi[m] * this.legendre.getPolynomialDerivativeTimesX2Minus1( l , m );
	}



	////////////////////////////////////////////////////////////////
	/// PRIVATE METHODS
	////////////////////////////////////////////////////////////////

	/**
	 * Evaluates the spherical harmonics from the already-validated direction cosines.
	 * <p>
	 * This is the shared core of the public {@code evaluate...} methods; it performs no argument checks.
	 *
	 * @param cosTheta	cosine of the polar angle, assumed to be in [-1,1].
	 * @param cosPhi	cosine of the azimuth angle, with  ( cosPhi , sinPhi )  assumed to be a unit vector.
	 * @param sinPhi	sine of the azimuth angle, with  ( cosPhi , sinPhi )  assumed to be a unit vector.
	 */
	private void evaluate( double cosTheta , double cosPhi , double sinPhi )
	{
		// Evaluate normalized associated Legendre polynomials.
		this.legendre.evaluate( cosTheta );
		// Evaluate  cos( m phi )  and  sin( m phi )  by recurrence, without per-order trigonometric calls or temporaries.
		this.cos_mPhi[0] = 1.0;
		this.sin_mPhi[0] = 0.0;
		for( int m=1; m<this.cos_mPhi.length; m++ ) {
			this.cos_mPhi[m] = cosPhi * this.cos_mPhi[m-1] - sinPhi * this.sin_mPhi[m-1];
			this.sin_mPhi[m] = sinPhi * this.cos_mPhi[m-1] + cosPhi * this.sin_mPhi[m-1];
		}
	}

}
