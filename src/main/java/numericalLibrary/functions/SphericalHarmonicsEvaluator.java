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
 * <p>
 * Usage: set the evaluation point with the {@code setTheta}/{@code setCosTheta} and {@code setPhi}/{@code setCosPhiAndSinPhi}
 * methods, then call {@link #evaluate()} (for the values) and/or {@link #evaluateDerivatives()} (for the derivatives).
 * The colatitude (theta) and azimuth (phi) parts are cached and recomputed lazily, so sweeping a row at constant
 * colatitude while varying the azimuth only re-evaluates the inexpensive azimuth part. {@link #evaluate()} and
 * {@link #evaluateDerivatives()} are idempotent until the point changes.
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
	 * Real part of the complex exponential  e^{ i m phi } , i.e.  cos( m phi ) , for the current azimuth.
	 */
	private final double[] cos_mPhi;

	/**
	 * Imaginary part of the complex exponential  e^{ i m phi } , i.e.  sin( m phi ) , for the current azimuth.
	 */
	private final double[] sin_mPhi;

	/**
	 * Cosine of the current polar angle.
	 */
	private double cosTheta = 1.0;

	/**
	 * Cosine and sine of the current azimuth angle.
	 */
	private double cosPhi = 1.0;
	private double sinPhi = 0.0;

	/**
	 * Whether the Legendre polynomial values must be recomputed because {@link #cosTheta} changed.
	 */
	private boolean legendreValuesDirty = true;

	/**
	 * Whether the Legendre polynomial derivatives must be recomputed because {@link #cosTheta} changed.
	 */
	private boolean legendreDerivativesDirty = true;

	/**
	 * Whether the azimuth exponentials must be recomputed because {@link #cosPhi} or {@link #sinPhi} changed.
	 */
	private boolean exponentialsDirty = true;



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
	 * Sets the polar angle of the evaluation point.
	 * <p>
	 * Prefer {@link #setCosTheta(double)} when  cos( theta )  is already available: it avoids recovering  theta
	 * with an arc-cosine only to take its cosine again, which is wasteful and ill-conditioned near the poles.
	 *
	 * @param theta		polar angle. It must be in the interval [0,pi].
	 * @throws IllegalArgumentException if theta is not in [0,pi].
	 */
	public void setTheta( double theta )
	{
		if(  theta < 0.0  ||  Math.PI < theta  ) {
			throw new IllegalArgumentException( "theta must be in [0, pi]; found " + theta );
		}
		this.setCosTheta( Math.cos( theta ) );
	}


	/**
	 * Sets the cosine of the polar angle of the evaluation point.
	 *
	 * @param cosTheta	cosine of the polar angle. It must be in the interval [-1,1].
	 * @throws IllegalArgumentException if cosTheta is not in [-1,1].
	 */
	public void setCosTheta( double cosTheta )
	{
		if(  cosTheta < -1.0  ||  1.0 < cosTheta  ) {
			throw new IllegalArgumentException( "cosTheta must be in [-1, 1]; found " + cosTheta );
		}
		if( cosTheta == this.cosTheta ) {
			return;
		}
		this.cosTheta = cosTheta;
		this.legendreValuesDirty = true;
		this.legendreDerivativesDirty = true;
	}


	/**
	 * Sets the azimuth angle of the evaluation point.
	 * <p>
	 * Prefer {@link #setCosPhiAndSinPhi(double, double)} when these values are already available (for example, as direction cosines).
	 *
	 * @param phi	azimuth angle. It must be in the interval [0,2pi].
	 * @throws IllegalArgumentException if phi is not in [0,2pi].
	 */
	public void setPhi( double phi )
	{
		if(  phi < 0.0  ||  2.0 * Math.PI < phi  ) {
			throw new IllegalArgumentException( "phi must be in [0, 2 pi]; found " + phi );
		}
		this.setCosPhiAndSinPhi( Math.cos( phi ) , Math.sin( phi ) );
	}


	/**
	 * Sets the cosine and sine of the azimuth angle of the evaluation point.
	 *
	 * @param cosPhi	cosine of the azimuth angle.
	 * @param sinPhi	sine of the azimuth angle.  ( cosPhi , sinPhi )  must be a unit vector.
	 * @throws IllegalArgumentException if ( cosPhi , sinPhi ) is not a unit vector.
	 */
	public void setCosPhiAndSinPhi( double cosPhi , double sinPhi )
	{
		if(  Math.abs( cosPhi * cosPhi + sinPhi * sinPhi - 1.0 ) > 1.0e-9  ) {
			throw new IllegalArgumentException( "( cosPhi , sinPhi ) must be a unit vector; found ( " + cosPhi + " , " + sinPhi + " )" );
		}
		if(  cosPhi == this.cosPhi  &&  sinPhi == this.sinPhi  ) {
			return;
		}
		this.cosPhi = cosPhi;
		this.sinPhi = sinPhi;
		this.exponentialsDirty = true;
	}


	/**
	 * Evaluates the spherical harmonics at the current evaluation point.
	 * <p>
	 * Must be called before {@link #getSphericalHarmonicsRealPart(int, int)} and {@link #getSphericalHarmonicsImaginaryPart(int, int)}.
	 * Only the parts whose inputs changed since the last call are recomputed.
	 */
	public void evaluate()
	{
		this.cleanLegendreValues();
		this.cleanExponentials();
	}


	/**
	 * Evaluates the colatitude derivatives of the spherical harmonics at the current evaluation point.
	 * <p>
	 * Must be called before {@link #getSphericalHarmonicsDerivativeRealPart(int, int)} and
	 * {@link #getSphericalHarmonicsDerivativeImaginaryPart(int, int)}.
	 * The derivative is provided in the scaled, pole-safe form  sin( theta ) dY_l^m/dtheta = [ ( x^2 - 1 ) dP_l^m/dx(x) ]_{x=cos theta} e^{ i m phi } .
	 * Only the parts whose inputs changed since the last call are recomputed.
	 */
	public void evaluateDerivatives()
	{
		this.cleanLegendreValues();
		this.cleanLegendreDerivatives();
		this.cleanExponentials();
	}


	/**
	 * Returns the real part of the spherical harmonic  Y_l^m( theta , phi ):
	 * P_l^m( cos theta ) cos( m phi ) .
	 * <p>
	 * Requires a previous {@link #evaluate()} call at the current point.
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
	 * Requires a previous {@link #evaluate()} call at the current point.
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
	 * Requires a previous {@link #evaluateDerivatives()} call at the current point.
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
	 * Returns the imaginary part of the scaled colatitude derivative  sin( theta ) dY_l^m/dtheta:
	 * {@code [ ( x^2 - 1 ) dP_l^m/dx(x) ]_{x=cos theta} sin( m phi )}.
	 * <p>
	 * Requires a previous {@link #evaluateDerivatives()} call at the current point.
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
	 * Recomputes the Legendre polynomial values if the polar angle changed since the last computation.
	 */
	private void cleanLegendreValues()
	{
		if( !this.legendreValuesDirty ) {
			return;
		}
		this.legendre.evaluate( this.cosTheta );
		this.legendreValuesDirty = false;
	}


	/**
	 * Recomputes the Legendre polynomial derivatives if the polar angle changed since the last computation.
	 */
	private void cleanLegendreDerivatives()
	{
		this.cleanLegendreValues();
		if( !this.legendreDerivativesDirty ) {
			return;
		}
		this.legendre.evaluateDerivatives();
		this.legendreDerivativesDirty = false;
	}


	/**
	 * Recomputes the azimuth exponentials  cos( m phi )  and  sin( m phi )  if the azimuth changed since the last computation.
	 */
	private void cleanExponentials()
	{
		if( !this.exponentialsDirty ) {
			return;
		}
		this.cos_mPhi[0] = 1.0;
		this.sin_mPhi[0] = 0.0;
		for( int m=1; m<this.cos_mPhi.length; m++ ) {
			this.cos_mPhi[m] = this.cosPhi * this.cos_mPhi[m-1] - this.sinPhi * this.sin_mPhi[m-1];
			this.sin_mPhi[m] = this.sinPhi * this.cos_mPhi[m-1] + this.cosPhi * this.sin_mPhi[m-1];
		}
		this.exponentialsDirty = false;
	}

}
