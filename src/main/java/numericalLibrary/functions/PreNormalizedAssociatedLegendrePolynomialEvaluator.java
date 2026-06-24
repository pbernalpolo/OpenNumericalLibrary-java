package numericalLibrary.functions;



/**
 * Computes pre-normalized associated Legendre polynomials.
 * <p>
 * The recurrence relations are:
 * <ul>
 * <li> P_{l+1}^{m}(x) = alpha_{l-1}^m x P_l^m(x) - beta_{l-1}^m P_{l-1}^m(x)
 * <li> P_{l+1}^{l}(x) = nu_l x P_l^l(x)
 * <li> P_{l+1}^{l+1}(x) = - mu_l sqrt( 1 - x^2 ) P_l^l(x)
 * </ul>
 * with:
 * <ul>
 * <li> alpha_l^m = sqrt( [ ( 2 l' + 1 ) ( 2 l' - 1 ) ] / [ ( l' + m ) ( l' - m ) ] )
 * <li> beta_l^m = sqrt( ( [ ( 2 l' + 1 ) / ( 2 l' - 3 ) ] [ ( l' + m - 1 ) ( l' - m - 1 ) ] / [ ( l' + m ) ( l' - m ) ] )
 * <li> mu_l = sqrt( [ 2 l' + 1 ] / [ 2 l' ] )
 * <li> nu_l = sqrt( 2 l' + 1 )
 * </ul>
 * where l' = l + 1.
 * The initial Legendre polynomial is the fixed constant  P_0^0 = 1 / sqrt( 4 pi ) .
 * <p>
 * These relations have been found in a great text written by Justin Willmert:
 * https://justinwillmert.com/articles/2020/pre-normalizing-legendre-polynomials/
 */
public class PreNormalizedAssociatedLegendrePolynomialEvaluator
	extends GeneralizedAssociatedLegendrePolynomialEvaluator
{
	////////////////////////////////////////////////////////////////
	/// PUBLIC CONSTRUCTORS
	////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs an {@link PreNormalizedAssociatedLegendrePolynomialEvaluator}.
	 * 
	 * @param lMaximum	maximum degree  l  to be evaluated. The degree  l  will range in  l = 0 , 1 , ... , lMaximum
	 */
	public PreNormalizedAssociatedLegendrePolynomialEvaluator( int lMaximum )
	{
		// Allocate space for variables.
		super( lMaximum );
		// Set mu_l and nu_l.
		for( int l=0; l<lMaximum; l++ ) {
			int lp = l + 1;
			double two_lp = lp + lp;
			double two_lp_plus_1 = two_lp + 1.0;
			// Set mu_l = sqrt( [ 2 l' + 1 ] / [ 2 l' ] )  with  l'=l+1.
			this.mu[l] = Math.sqrt( two_lp_plus_1 / two_lp );
			// Set nu_l = sqrt( 2 l' + 1 )  with  l'=l+1.
			this.nu[l] = Math.sqrt( two_lp_plus_1 );
		}
		// Set mu; when  l  is even: mu[l-1] contains the product mu_{l-1} * mu_{l-2}.
		for( int l=1; l<lMaximum; l+=2 ) {
			int lp = l + 1;
			double mu_l_squared = ( lp + lp + 1.0 ) / ( lp + lp );
			double mu_l_minus1_squared = ( l + l + 1.0 ) / ( l + l );
			this.mu[l] = Math.sqrt( mu_l_squared * mu_l_minus1_squared );
		}
		// Set values for alpha and beta.
		for( int l=0; l<lMaximum-1; l++ ) {
			int lp = l + 2;
			double two_lp = lp + lp;
			for( int m=0; m<=l; m++ ) {
				double lp_plus_m = lp + m;
				double lp_minus_m = lp - m;
				// alpha = sqrt( [ ( 2 l' + 1 ) ( 2 l' - 1 ) ] / [ ( l' + m ) ( l' - m ) ] )
				this.alpha[l][m] = Math.sqrt( ( ( two_lp + 1.0 ) / lp_plus_m ) * ( ( two_lp - 1.0 ) / lp_minus_m ) );
				// beta = sqrt( [ ( 2 l' + 1 ) ( l' + m - 1 ) ( l' - m - 1 ) ] / [ ( 2 l' - 3 ) ( l' + m ) ( l' - m ) ] )
				this.beta[l][m] = Math.sqrt( ( ( two_lp + 1.0 ) / ( two_lp - 3.0 ) ) * ( ( lp_plus_m - 1.0 ) / lp_plus_m ) * ( ( lp_minus_m - 1.0 ) / lp_minus_m ) );
			}
		}
		// Set the derivative-recurrence coefficient.
		// For the pre-normalized polynomials:
		// gamma_l^m = sqrt( ( 2 l + 1 ) ( l + m ) ( l - m ) / ( 2 l - 1 ) ), stored at index l-1.
		for( int lMinus1=0; lMinus1<lMaximum; lMinus1++ ) {
			int l = lMinus1 + 1;
			double ratio = ( 2.0 * l + 1.0 ) / ( 2.0 * l - 1.0 );
			for( int m=0; m<=lMinus1; m++ ) {
				this.gamma[lMinus1][m] = Math.sqrt( ratio * ( l + m ) * ( l - m ) );
			}
		}
		this.p[0][0] = 1.0 / Math.sqrt( 4.0 * Math.PI );
	}
	
}
