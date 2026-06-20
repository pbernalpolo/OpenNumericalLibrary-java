package numericalLibrary.functions;



/**
 * Computes associated Legendre polynomials.
 * <p>
 * The recurrence relations are:
 * <ul>
 * <li> P_{l+1}^{m}(x) = alpha_{l-1}^m x P_l^m(x) - beta_{l-1}^m P_{l-1}^m(x)
 * <li> P_{l+1}^{l}(x) = nu_l x P_l^l(x)
 * <li> P_{l+1}^{l+1}(x) = - mu_l sqrt( 1 - x^2 ) P_l^l(x)
 * </ul>
 * with:
 * <ul>
 * <li> alpha_l^m = ( 2 l' + 1 ) / ( l' - m )
 * <li> beta_l^m = ( l' + m - 1 ) / ( l' - m )
 * <li> mu_l = 2 l' - 1
 * <li> nu_l = 2 l' - 1
 * </ul>
 * where l' = l + 1.
 * The initial Legendre polynomial is the fixed constant  P_0^0 = 1 / sqrt( 4 pi ) .
 * <p>
 * These relations have been found in a great text written by Justin Willmert:
 * https://justinwillmert.com/articles/2020/pre-normalizing-legendre-polynomials/
 */
public class AssociatedLegendrePolynomialEvaluator
	extends GeneralizedAssociatedLegendrePolynomialEvaluator
{
	////////////////////////////////////////////////////////////////
	/// PUBLIC CONSTRUCTORS
	////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs an {@link AssociatedLegendrePolynomialEvaluator}.
	 * 
	 * @param lMaximum	maximum degree  l  to be evaluated. The degree  l  will range in  l = 0 , 1 , ... , lMaximum
	 */
	public AssociatedLegendrePolynomialEvaluator( int lMaximum )
	{
		// Allocate
		super( lMaximum );
		for( int l=0; l<lMaximum; l++ ) {
			// For each l, m ranges from -l to l.
			int lp = l + 1;
			double two_lp_minus_1 = lp + lp - 1.0;
			this.mu[l] = two_lp_minus_1;
			this.nu[l] = two_lp_minus_1;
		}
		for( int l=0; l<lMaximum-1; l++ ) {
			// For each l, m ranges from -l to l.
			int lp = l + 2;
			double two_lp_minus_1 = lp + lp - 1.0;
			for( int m=0; m<=l; m++ ) {
				double lp_minus_m = lp - m;
				this.alpha[l][m] = two_lp_minus_1 / lp_minus_m;
				this.beta[l][m] = ( lp + m - 1.0 ) / lp_minus_m;
			}
		}
		this.p[0][0] = 1.0;
	}
	
}
