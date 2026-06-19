package numericalLibrary.functions;



public class AssociatedLegendrePolynomialEvaluator
	extends GeneralizedAssociatedLegendrePolynomialEvaluator
{
	
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
