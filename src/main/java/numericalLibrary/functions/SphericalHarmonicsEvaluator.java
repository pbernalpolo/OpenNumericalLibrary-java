package numericalLibrary.functions;


import numericalLibrary.types.ComplexNumber;



public class SphericalHarmonicsEvaluator
{
	
	private NormalizedAssociatedLegendrePolynomialEvaluator normalizedAssociatedLegendrePolynomialEvaluator;
	private ComplexNumber[] complexExponentials;
	
	
	public SphericalHarmonicsEvaluator( int lMaximum )
	{
		// Create normalized associated Legendre polynomials.
		this.normalizedAssociatedLegendrePolynomialEvaluator = new NormalizedAssociatedLegendrePolynomialEvaluator( lMaximum );
		// Create array of complex exponentials.
		this.complexExponentials = new ComplexNumber[ lMaximum + 1 ];
		for( int m=0; m<this.complexExponentials.length; m++ ) {
			this.complexExponentials[m] = ComplexNumber.one();
		}
	}
	
	
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
	
	
	public ComplexNumber getSphericalHarmonicsValue( int l , int m )
	{
		return this.complexExponentials[m].scale( this.normalizedAssociatedLegendrePolynomialEvaluator.getPolynomialValue( l , m ) );
	}
	
}
