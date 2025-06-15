package numericalLibrary.types;


import java.util.ArrayList;
import java.util.List;

import numericalLibrary.algebraicStructures.VectorSpaceElement;



/**
 * Example that shows how to set up and use the Levenberg-Marquardt algorithm to fit a Gaussian function to empirical data.
 */
class TypesExample
{
    ////////////////////////////////////////////////////////////////
    // ENTRY POINT
    ////////////////////////////////////////////////////////////////
    
    public static void main( String[] args )
    {
        // Create a list of real numbers.
        List<RealNumber> realNumberList = new ArrayList<RealNumber>();
        for( int i=0; i<7; i++ ) {
            realNumberList.add( new RealNumber( i ) );
        }
        
        // Create a list of complex numbers.
        List<ComplexNumber> complexNumberList = new ArrayList<ComplexNumber>();
        for( int i=0; i<5; i++ ) {
            complexNumberList.add( ComplexNumber.fromRealPartAndImaginaryPart( i , - 3 * i ) );
        }
        
        // Create a list of quaternions.
        List<Quaternion> quaternionList = new ArrayList<Quaternion>();
        for( int i=0; i<3; i++ ) {
            quaternionList.add( Quaternion.fromScalarAndVectorPart( i , new Vector3( -2 * i , 3 * i , 5 * i ) ) );
        }
        
        // Create a list of matrices.
        List<MatrixReal> matrixList = new ArrayList<MatrixReal>();
        for( int i=0; i<2; i++ ) {
            matrixList.add( MatrixReal.one( i+2 ) );
        }
        
        // Put all elements in a list of VectorSpaceElements.
        List<VectorSpaceElement<?>> vectorSpaceElementList = new ArrayList<VectorSpaceElement<?>>();
        vectorSpaceElementList.addAll( realNumberList );
        vectorSpaceElementList.addAll( complexNumberList );
        vectorSpaceElementList.addAll( quaternionList );
        vectorSpaceElementList.addAll( matrixList );
        
        // Print VectorSpaceElements before and after scaling.
        for( VectorSpaceElement<?> x : vectorSpaceElementList ) {
            System.out.println( "Before it was:\n" + x.toString() + "\n and after scaling:\n" + x.scaleInplace( 2 ).toString() + "\n" );
        }
        
        // Print all VectorSpaceElements before scaling them once again.
        for( VectorSpaceElement<?> x : vectorSpaceElementList ) {
            x.print().scaleInplace( 3.0 );
        }
    }
    
}
