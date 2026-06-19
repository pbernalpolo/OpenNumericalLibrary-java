package numericalLibrary.functions;



public class NormalizedAssociatedLegendrePolynomialEvaluator
	extends GeneralizedAssociatedLegendrePolynomialEvaluator
{
	
	public NormalizedAssociatedLegendrePolynomialEvaluator( int lMaximum )
	{
		// Allocate
		super( lMaximum );
		for( int l=0; l<lMaximum; l++ ) {
			int lp = l + 1;
			double two_lp = lp + lp;
			double two_lp_plus_1 = two_lp + 1.0;
			this.mu[l] = Math.sqrt( two_lp_plus_1 / two_lp );
			this.nu[l] = Math.sqrt( two_lp_plus_1 );
		}
		for( int l=0; l<lMaximum-1; l++ ) {
			// For each l, m ranges from -l to l.
			int lp = l + 2;
			double two_lp = lp + lp;
			for( int m=0; m<=l; m++ ) {
				double lp_plus_m = lp + m;
				double lp_minus_m = lp - m;
				this.alpha[l][m] = Math.sqrt( ( ( two_lp + 1.0 ) / lp_plus_m ) * ( ( two_lp - 1.0 ) / lp_minus_m ) );
				this.beta[l][m] = Math.sqrt( ( ( two_lp + 1.0 ) / ( two_lp - 3.0 ) ) * ( ( lp_plus_m - 1.0 ) / lp_plus_m ) * ( ( lp_minus_m - 1.0 ) / lp_minus_m ) );
			}
		}
		this.p[0][0] = 0.5 / Math.sqrt( Math.PI );
	}
	
}
