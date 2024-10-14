package numericalLibrary.types;


import java.util.List;
import java.util.Random;

import numericalLibrary.algebraicStructures.AdditiveAbelianGroupElement;
import numericalLibrary.algebraicStructures.MetricSpaceElement;
import numericalLibrary.algebraicStructures.VectorSpaceElement;



/**
 * Implements matrix with real entries.
 */
public class Matrix
    implements
        AdditiveAbelianGroupElement<Matrix>,
        VectorSpaceElement<Matrix>,
        MetricSpaceElement<Matrix>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    private final double[][] x;  // matrix values
    private final int Nrows;
    private final int Ncols;
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE STATIC VARIABLES
    ////////////////////////////////////////////////////////////////
    private static boolean assertionsOn = true;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public int rows()
    {
        return this.Nrows;
    }
    
    
    public int cols()
    {
        return this.Ncols;
    }
    
    
    public String size()
    {
        return ( this.rows() + " x " + this.cols() );
    }
    
    
    public void setEntry( int i , int j , double value )
    {
        this.assertIndexBounds( i , j );
        this.setEntryUnchecked( i,j , value );
    }
    
    
    public double entry( int i , int j )
    {
        this.assertIndexBounds( i , j );
        return this.entryUnchecked( i , j );
    }
    
    
    public Matrix transpose()
    {
        Matrix m = Matrix.empty( this.cols() , this.rows() );
        for( int i=0; i<this.cols(); i++ ) {
            for( int j=0; j<this.rows(); j++ ) {
                m.setEntryUnchecked( i,j , this.entryUnchecked(j,i) );
            }
        }
        return m;
    }
    
    
    /*public Matrix transposeInplace()
    {
        this.setTo( this.transpose() );
        return this;
    }*/
    
    
    /*public double trace()
    {
        double theTrace = 0.0;
        int minDimension = ( this.rows() < this.cols() )? this.rows() : this.cols();
        for( int i=0; i<minDimension; i++ ) {
            theTrace += this.getEntry(i,i);
        }
        return theTrace;
    }
    */
    
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                sb.append( String.format( " %15.6e" , this.entryUnchecked(i,j) ) );
            }
            sb.append( "\n" );
        }
        sb.append( "\n" );
        return sb.toString();
    }
    
    
    public boolean equals( Matrix other )
    {
        if( this.rows() == other.rows()  &&  this.cols() == other.cols()  ) {
            for( int i=0; i<this.rows(); i++ ) {
                for( int j=0; j<this.cols(); j++ ) {
                    if( this.entryUnchecked(i,j) != other.entryUnchecked(i,j) ) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException     if {@code other} has not the same size as {@code this}.
     */
    public Matrix setTo( Matrix other )
    {
        this.assertEqualSize( other );
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                this.setEntryUnchecked( i,j , other.entryUnchecked(i,j) );
            }
        }
        return this;
    }
    
    
    public Matrix copy()
    {
        Matrix c = Matrix.emptyWithSizeOf( this );
        c.setTo( this );
        return c;
    }
    
    
    public boolean equalsApproximately( Matrix other , double tolerance )
    {
        this.assertEqualSize( other );
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                double dif = this.entryUnchecked(i,j) - other.entryUnchecked(i,j);
                if( Math.abs( dif ) > tolerance ) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    public Matrix add( Matrix other )
    {
        this.assertEqualSize( other );
        Matrix m = Matrix.emptyWithSizeOf( this );
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                m.setEntryUnchecked( i,j , this.entryUnchecked(i,j) + other.entryUnchecked(i,j) );
            }
        }
        return m;
    }


    public Matrix addInplace( Matrix other )
    {
        this.assertEqualSize( other );
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                this.x[i][j] += other.entryUnchecked(i,j);
            }
        }
        return this;
    }
    
    
    public Matrix identityAdditive()
    {
        return Matrix.zero( this.rows() , this.cols() );
    }

    
    public Matrix inverseAdditive()
    {
        Matrix m = Matrix.emptyWithSizeOf( this );
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                m.setEntryUnchecked( i,j , -this.entryUnchecked(i,j) );
            }
        }
        return m;
    }


    public Matrix inverseAdditiveInplace()
    {
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                this.x[i][j] = -this.entryUnchecked(i,j);
            }
        }
        return this;
    }


    public Matrix subtract( Matrix other )
    {
        this.assertEqualSize( other );
        Matrix m = Matrix.emptyWithSizeOf( this );
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                m.setEntryUnchecked( i,j , this.entryUnchecked(i,j) - other.entryUnchecked(i,j) );
            }
        }
        return m;
    }


    public Matrix subtractInplace( Matrix other )
    {
        this.assertEqualSize( other );
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                this.x[i][j] -= other.entryUnchecked(i,j);
            }
        }
        return this;
    }
    
    
    /**
     * Adds the product of first {@link Matrix} and second {@link Matrix}.
     * <p>
     * The multiplication is performed as  {@code first * second}.
     * {@code this} must have the same rows as {@code first}, and same columns as {@code second}.
     * {@code this} must also be different from {@code first} and {@code second}.
     * 
     * @param first     first factor of the {@link Matrix} multiplication.
     * @param second    second factor of the {@link Matrix} multiplication.
     * @return  {@code this} reference to which the product of {@code first} and {@code second} is added.
     * 
     * @throws IllegalArgumentException     if {@code this} is {@code first} or {@code second}.
     * @throws IllegalArgumentException     if {@code second} does not have same rows as {@code first} columns.
     * @throws IllegalArgumentException     if {@code this} does not have same rows as {@code first}, or columns as {@code second}.
     */
    public Matrix addProduct( Matrix first , Matrix second )
    {
        if(  this == first  ||  this == second  ) {
            throw new IllegalArgumentException( "\"this\" must be different from \"first\" and \"second\"." );
        }
        second.assertRows( first.cols() );
        this.assertSize( first.rows() , second.cols() );
        return this.addProductPrivate( first , second );
    }
    
    
    /**
     * Computes matrix multiplication.
     * <p>
     * The multiplication is performed as  this * other.
     * 
     * @param other     second factor in the matrix multiplication operation.
     * @return  new {@link Matrix} that contains the multiplication result.
     * 
     * @throws IllegalArgumentException     if {@code other} does not have same rows as {@code this} columns.
     */
    public Matrix multiply( Matrix other )
    {
        other.assertRows( this.cols() );
        Matrix output = Matrix.zero( this.rows() , other.cols() );
        output.addProductPrivate( this , other );
        return output;
    }
    
    
    public Matrix identityMultiplicativeLeft()
    {
        return Matrix.one( this.Nrows );
    }
    
    
    public Matrix identityMultiplicativeRight()
    {
        return Matrix.one( this.Ncols );
    }
    
    
    /*public Matrix inverseMultiplicative()
    {
        
    }


    public Matrix inverseMultiplicativeInplace()
    {
        return this.set( this.inverseMultiplicative() );
    }


    public Matrix divideLeft( Matrix other )
    {
        return other.inverseMultiplicative().multiply( this );
    }


    public Matrix divideLeftInplace( Matrix other )
    {
        return this.set( this.divideLeft( other ) );
    }
    
    
    public Matrix divideRight( Matrix other )
    {
        return this.multiply( other.inverseMultiplicative() );
    }


    public Matrix divideRightInplace( Matrix other )
    {
        return this.multiplyInplace( other.inverseMultiplicative() );
    }*/

    
    public Matrix scale( double scalar )
    {
        Matrix m = Matrix.emptyWithSizeOf( this );
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                m.setEntryUnchecked( i,j , this.entryUnchecked(i,j) * scalar );
            }
        }
        return m;
    }


    public Matrix scaleInplace( double scalar )
    {
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                this.x[i][j] *= scalar;
            }
        }
        return this;
    }
    
    
    /*public double dot( Matrix other )
    {
        return this.transpose().multiply( other ).trace();
    }*/
    
    
    public double normFrobeniusSquared()
    {
        double norm2 = 0.0;
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                norm2 += this.entryUnchecked(i,j) * this.entryUnchecked(i,j);
            }
        }
        return norm2;
    }
    
    
    public double normFrobenius()
    {
        return Math.sqrt( this.normFrobeniusSquared() );
    }


    public Matrix normalizeFrobenius()
    {
        return this.scale( 1.0/this.normFrobenius() );
    }


    public Matrix normalizeFrobeniusInplace()
    {
        return this.scaleInplace( 1.0/this.normFrobenius() );
    }



    public double distanceFrom( Matrix other )
    {
        this.assertEqualSize( other );
        double dist2 = 0.0;
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                double dif = this.entryUnchecked(i,j) - other.entryUnchecked(i,j);
                dist2 += dif * dif;
            }
        }
        return Math.sqrt( dist2 );
    }
    
    
    public Matrix choleskyDecomposition()
    {
        this.assertIsSquare();
        Matrix m = Matrix.emptyWithSizeOf( this );
        Matrix.choleskyDecompositionAlgorithm( this , m );
        return m;
    }
    
    
    public Matrix choleskyDecompositionInplace()
    {
        this.assertIsSquare();
        Matrix.choleskyDecompositionAlgorithm( this , this );
        return this;
    }
    
    
    public Matrix LDLTDecomposition()
    {
        this.assertIsSquare();
        Matrix m = Matrix.emptyWithSizeOf( this );
        Matrix.ldltDecompositionAlgorithm( this , m );
        return m;
    }
    
    
    public Matrix LDLTDecompositionInplace()
    {
        this.assertIsSquare();
        Matrix.ldltDecompositionAlgorithm( this , this );
        return this;
    }
    
    
    public Matrix divideRightByPositiveDefiniteUsingItsCholeskyDecomposition( Matrix L )
    {
        L.assertIsSquare();
        Matrix output = Matrix.emptyWithSizeOf( this );
        Matrix.divideRightByPositiveDefiniteUsingItsCholeskyDecompositionAlgorithm( output , L , this );
        return output;
    }


    public Matrix divideRightByPositiveDefiniteUsingItsCholeskyDecompositionInplace( Matrix L )
    {
        L.assertIsSquare();
        Matrix.divideRightByPositiveDefiniteUsingItsCholeskyDecompositionAlgorithm( this , L , this );
        return this;
    }
    
    
    public Matrix divideLeftByPositiveDefiniteUsingItsCholeskyDecomposition( Matrix L )
    {
        L.assertIsSquare();
        Matrix output = Matrix.emptyWithSizeOf( this );
        Matrix.divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionAlgorithm( L , output , this );
        return output;
    }


    public Matrix divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( Matrix L )
    {
        L.assertIsSquare();
        Matrix.divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionAlgorithm( L , this , this );
        return this;
    }
    
    
    public Matrix divideRightByPositiveDefiniteUsingItsLDLTDecomposition( Matrix LD )
    {
        LD.assertIsSquare();
        Matrix m = Matrix.emptyWithSizeOf( this );
        Matrix.divideRightByPositiveDefiniteUsingItsLDLTDecompositionAlgorithm( this , LD , m );
        return m;
    }

    
    public Matrix divideRightByPositiveDefiniteUsingItsLDLTDecompositionInplace( Matrix LD )
    {
        LD.assertIsSquare();
        Matrix.divideRightByPositiveDefiniteUsingItsLDLTDecompositionAlgorithm( this , LD , this );
        return this;
    }
    
    
    public double[] diagonalElements()
    {
        int nElements = ( this.rows() < this.cols() )? this.rows() : this.cols() ;
        double[] d = new double[ nElements ];
        for( int i=0; i<nElements; i++ ) {
            d[i] = this.entryUnchecked( i , i );
        }
        return d;
    }
    
    
    public double trace()
    {
        this.assertIsSquare();
        double sum = 0.0;
        for( int i=0; i<this.rows(); i++ ) {
            sum += this.entryUnchecked( i , i );
        }
        return sum;
    }
    
    
    public Matrix setSubmatrix( int i0 , int j0 , Matrix other )
    {
        this.assertIndexBounds( i0 , j0 );
        this.assertIndexBounds( i0 + other.rows() , j0 + other.cols() );
        for( int i=0; i<other.rows(); i++ ) {
            for( int j=0; j<other.cols(); j++ ) {
                this.setEntryUnchecked( i0+i , j0+j , other.entryUnchecked(i,j) );
            }
        }
        return this;
    }
    
    
    public Matrix submatrix( int i , int j , int numberOfRows , int numberOfColumns )
    {
        this.assertIndexBounds( i , j );
        this.assertIndexBounds( i+numberOfRows , j+numberOfColumns );
        return this.submatrixFast( i , j , numberOfRows , numberOfColumns );
    }
    
    
    public Matrix submatrixFromRow( int i )
    {
        this.assertRowIndexBounds( i );
        Matrix output = Matrix.empty( 1 , this.cols() );
        for( int j=0; j<this.cols(); j++ ) {
            output.setEntryUnchecked( 0,j , this.entryUnchecked( i , j ) );
        }
        return output;
    }
    
    
    public Matrix submatrixFromRows( int i , int numberOfRows )
    {
        this.assertRowIndexBounds( i );
        this.assertRowIndexBounds( i+numberOfRows );
        return this.submatrixFast( i , 0 , numberOfRows , this.cols() );
    }
    
    
    public Matrix submatrixFromColumn( int j )
    {
        this.assertColumnIndexBounds( j );
        Matrix output = Matrix.empty( this.rows() , 1 );
        for( int i=0; i<this.rows(); i++ ) {
            output.setEntryUnchecked( i,0 , this.entryUnchecked( i , j ) );
        }
        return output;
    }
    
    
    public Matrix submatrixFromColumns( int j , int numberOfColumns )
    {
        this.assertColumnIndexBounds( j );
        this.assertColumnIndexBounds( j+numberOfColumns );
        return this.submatrixFast( 0 , j , this.rows() , numberOfColumns );
    }
    
    
    public Vector2 submatrixToVector2( int i , int j )
    {
        this.assertIndexBounds( i , j );
        this.assertIndexBounds( i+2 , j );
        return new Vector2( this.entryUnchecked(i,j) , this.entryUnchecked(i+1,j) );
    }
    
    
    public Vector3 submatrixToVector3( int i , int j )
    {
        this.assertIndexBounds( i , j );
        this.assertIndexBounds( i+3 , j );
        return new Vector3( this.entryUnchecked(i,j) , this.entryUnchecked(i+1,j) , this.entryUnchecked(i+2,j) );
    }
    
    
    /**
     * Returns the {@link Matrix} elements stored in a flat array.
     * <p>
     * The {@link Matrix} elements are stored row after row, so the output array will be:
     * output = { a11 , a12 , ... , a1m , a21 , a22 , ... , a2m , an1 , an2 , ... , anm }
     * 
     * @return  {@link Matrix} elements stored in a flat array.
     */
    public double[] toFlatArray()
    {
        double[] output = new double[ this.rows() * this.cols() ];
        int c = 0;
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                output[c++] = this.entryUnchecked( i , j );
            }
        }
        return output;
    }
    
    
    public double[] rowAsArray( int i )
    {
        double[] row = new double[this.cols()];
        for( int j=0; j<this.cols(); j++ ) {
            row[j] = this.entryUnchecked(i,j);
        }
        return row;
    }
    
    
    public double[] columnAsArray( int j )
    {
        double[] column = new double[this.rows()];
        for( int i=0; i<this.rows(); i++ ) {
            column[i] = this.entryUnchecked(i,j);
        }
        return column;
    }
    
    
    public Vector2 applyToVector2( Vector2 v )
    {
        this.assertSize( 2 , 2 );
        return new Vector2(
                this.entryUnchecked(0,0) * v.x() + this.entryUnchecked(0,1) * v.y() ,
                this.entryUnchecked(1,0) * v.x() + this.entryUnchecked(1,1) * v.y() );
    }
    
    
    /**
     * Returns result of applying the {@link Matrix} transformation to a {@link Vector3} in a new {@link Vector3}.
     * 
     * @param v     {@link Vector3} to which the {@link Matrix} transformation is applied.
     * @return  result of applying the {@link Matrix} transformation to a {@link Vector3} in a new {@link Vector3}.
     */
    public Vector3 applyToVector3( Vector3 v )
    {
        this.assertSize( 3 , 3 );
        return new Vector3(
                this.entryUnchecked(0,0) * v.x() + this.entryUnchecked(0,1) * v.y() + this.entryUnchecked(0,2) * v.z() ,
                this.entryUnchecked(1,0) * v.x() + this.entryUnchecked(1,1) * v.y() + this.entryUnchecked(1,2) * v.z() ,
                this.entryUnchecked(2,0) * v.x() + this.entryUnchecked(2,1) * v.y() + this.entryUnchecked(2,2) * v.z() );
    }
    
    
    public boolean isSquare()
    {
        return ( this.rows() == this.cols() );
    }
    
    
    public Matrix abs()
    {
        Matrix output = Matrix.emptyWithSizeOf( this );
        return Matrix.absPrivate( this , output );
    }
    
    
    public Matrix absInplace()
    {
        return Matrix.absPrivate( this , this );
    }
    
    
    public double maxEntry()
    {
        double output = Double.NEGATIVE_INFINITY;
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                double entry = this.entryUnchecked( i , j );
                if( entry > output ) {
                    output = entry;
                }
            }
        }
        return output;
    }
    
    
    public double minEntry()
    {
        double output = Double.POSITIVE_INFINITY;
        for( int i=0; i<this.rows(); i++ ) {
            for( int j=0; j<this.cols(); j++ ) {
                double entry = this.entryUnchecked( i , j );
                if( entry < output ) {
                    output = entry;
                }
            }
        }
        return output;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Creates an empty matrix with a given size.
     * 
     * @param numberOfRows  number of rows of the empty matrix.
     * @param numberOfColumns   number of columns of the empty matrix.
     * @return  new empty matrix with the given size.
     */
    public static Matrix empty( int numberOfRows , int numberOfColumns )
    {
        return new Matrix( numberOfRows , numberOfColumns );
    }
    
    
    /**
     * Creates an empty matrix with same size as {@code other}
     * 
     * @param other     matrix whose size will be copied.
     * @return  new empty matrix with size as {@code other}.
     */
    private static Matrix emptyWithSizeOf( Matrix other )
    {
        return Matrix.empty( other.rows() , other.cols() );
    }
    
    
    /**
     * Creates a matrix full of zeros.
     * 
     * @param numberOfRows  number of rows for the returned matrix.
     * @param numberOfColumns   number of columns for the returned matrix.
     * @return  matrix full of zeros with the given size.
     */
    public static Matrix zero( int numberOfRows , int numberOfColumns )
    {
        return Matrix.empty( numberOfRows , numberOfColumns );
    }
    
    
    public static Matrix one( int dimension )
    {
        Matrix one = Matrix.empty( dimension , dimension );
        for( int i=0; i<dimension; i++ ) {
            one.setEntryUnchecked( i,i , 1.0 );
        }
        return one;
    }
    
    
    public static Matrix random( int numberOfRows , int numberOfColumns , Random randomNumberGenerator )
    {
        Matrix m = Matrix.empty( numberOfRows , numberOfColumns );
        for( int i=0; i<numberOfRows; i++ ) {
            for( int j=0; j<numberOfColumns; j++ ) {
                m.setEntryUnchecked( i,j , randomNumberGenerator.nextGaussian() );
            }
        }
        return m;
    }
    
    
    /**
     * Returns a new {@link Matrix} build from a flat array.
     * <p>
     * The {@link Matrix} elements must be stored row after row, so the flatArray array must have the form:
     * flatArray = { a11 , a12 , ... , a1m , a21 , a22 , ... , a2m , an1 , an2 , ... , anm }
     * 
     * @param flatArray     flat array that contains the {@link Matrix} elements row after row.
     * @param numberOfRows  number of rows of the output {@link Matrix}.
     * @param numberOfColumns   number of columns of the output {@link Matrix}.
     * @return  new {@link Matrix} build from a flat array.
     * 
     * @throws IllegalArgumentException if length of flatArray does not match provided {@link Matrix} size.
     */
    public static Matrix fromFlatArray( double[] flatArray , int numberOfRows , int numberOfColumns )
    {
        if( numberOfRows * numberOfColumns != flatArray.length ) {
            throw new IllegalArgumentException( "Length of flatArray (" + flatArray.length + ") does not match size of matrix ( " + numberOfRows + " x " + numberOfColumns + " )" );
        }
        Matrix output = Matrix.empty( numberOfRows , numberOfColumns );
        int c = 0;
        for( int i=0; i<output.rows(); i++ ) {
            for( int j=0; j<output.cols(); j++ ) {
                output.setEntryUnchecked( i , j , flatArray[c++] );
            }
        }
        return output;
    }
    
    
    public static Matrix rowFromArray( double[] theArray )
    {
        Matrix m = Matrix.empty( 1 , theArray.length );
        for( int j=0; j<theArray.length; j++ ) {
            m.setEntryUnchecked( 0,j , theArray[j] );
        }
        return m;
    }
    
    
    public static Matrix columnFromArray( double[] theArray )
    {
        Matrix m = Matrix.empty( theArray.length , 1 );
        for( int i=0; i<theArray.length; i++ ) {
            m.setEntryUnchecked( i,0 , theArray[i] );
        }
        return m;
    }
    
    
    public static Matrix vector2( double v1 , double v2 )
    {
        return Matrix.fromArray2D( new double[][] { { v1 } , { v2 } } );
    }
    
    
    public static Matrix vector3( double v1 , double v2 , double v3 )
    {
        return Matrix.fromArray2D( new double[][] { { v1 } , { v2 } , { v3 } } );
    }
    
    
    public static Matrix matrix2x2( double m11 , double m12 , double m21 , double m22 )
    {
        return Matrix.fromArray2D( new double[][] { { m11 , m12 } ,
                                                        { m21 , m22 } } );
    }
    
    
    public static Matrix matrix3x3( double m11 , double m12 , double m13 , double m21 , double m22 , double m23 , double m31 , double m32 , double m33 )
    {
        return Matrix.fromArray2D( new double[][] { { m11 , m12 , m13 } ,
                                                        { m21 , m22 , m23 } ,
                                                        { m31 , m32 , m33 } } );
    }
    
    
    public static Matrix fromVector2AsColumn( Vector2 v )
    {
        Matrix output = Matrix.empty( 2 , 1 );
        output.setEntry( 0,0 , v.x() );
        output.setEntry( 1,0 , v.y() );
        return output;
    }
    
    
    public static Matrix fromVector2AsRow( Vector2 v )
    {
        Matrix output = Matrix.empty( 1 , 2 );
        output.setEntry( 0,0 , v.x() );
        output.setEntry( 0,1 , v.y() );
        return output;
    }
    
    
    public static Matrix fromVector3AsColumn( Vector3 v )
    {
        Matrix output = Matrix.empty( 3 , 1 );
        output.setEntry( 0,0 , v.x() );
        output.setEntry( 1,0 , v.y() );
        output.setEntry( 2,0 , v.z() );
        return output;
    }
    
    
    public static Matrix fromVector3AsRow( Vector3 v )
    {
        Matrix output = Matrix.empty( 1 , 3 );
        output.setEntry( 0,0 , v.x() );
        output.setEntry( 0,1 , v.y() );
        output.setEntry( 0,2 , v.z() );
        return output;
    }
    
    
    public static Matrix diagonal( double[] diagonalElements )
    {
        Matrix m = Matrix.zero( diagonalElements.length , diagonalElements.length );
        for( int i=0; i<diagonalElements.length; i++ ) {
            m.setEntryUnchecked( i,i , diagonalElements[i] );
        }
        return m;
    }
    
    
    public static Matrix blockDiagonal( List<Matrix> lrm )
    {
        // first we obtain the size of the matrix
        int nrows = 0;
        int ncols = 0;
        for( Matrix rm : lrm ) {
            nrows += rm.rows();
            ncols += rm.cols();
        }
        // then, we create the matrix, and we fill the content
        Matrix m = Matrix.zero( nrows , ncols );
        int i0 = 0;
        int j0 = 0;
        for( Matrix rm : lrm ) {
            for( int i=0; i<rm.rows(); i++ ) {
                for( int j=0; j<rm.cols(); j++ ) {
                    m.setEntryUnchecked( i0+i , j0+j , rm.entryUnchecked(i,j) );
                }
            }
            i0 += rm.rows();
            j0 += rm.cols();
        }
        return m;
    }
    
    
    public static Matrix rotation2d( double rotationAngle )
    {
        return Matrix.matrix2x2(
                Math.cos( rotationAngle ) , -Math.sin( rotationAngle ) ,
                Math.sin( rotationAngle ) ,  Math.cos( rotationAngle ) );
    }
    
    
    public static Matrix LfromLDLTDecomposition( Matrix LDLTDecomposition )
    {
        Matrix L = LDLTDecomposition.copy();
        for( int i=0; i<L.cols(); i++ ) {
            L.setEntryUnchecked( i,i , 1.0 );
        }
        return L;
    }
    
    
    public static Matrix DfromLDLTDecomposition( Matrix LDLTDecomposition )
    {
        return Matrix.diagonal( LDLTDecomposition.diagonalElements() );
    }
    
    
    public static void setAssertions( boolean areAssertionsActive )
    {
        Matrix.assertionsOn = areAssertionsActive;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link Matrix}.
     * 
     * @param numberOfRows  number of rows for the new matrix.
     * @param numberOfColumns   number of columns for the new matrix.
     */
    private Matrix( int numberOfRows , int numberOfColumns )
    {
        this.Nrows = numberOfRows;
        this.Ncols = numberOfColumns;
        this.x = new double[numberOfRows][numberOfColumns];
    }



    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    private static Matrix fromArray2D( double[][] theArray )
    {
        Matrix output = new Matrix( theArray.length , theArray[0].length );
        for( int i=0; i<output.rows(); i++ ) {
            for( int j=0; j<output.cols(); j++ ) {
                output.setEntryUnchecked( i,j , theArray[i][j] );
            }
        }
        return output;
    }
    
    
    private void setEntryUnchecked( int i , int j , double value )
    {
        this.x[i][j] = value;
    }
    
    
    private double entryUnchecked( int i , int j )
    {
        return this.x[i][j];
    }
    
    
    private Matrix submatrixFast( int i , int j , int numberOfRows , int numberOfColumns )
    {
        Matrix m = Matrix.empty( numberOfRows , numberOfColumns );
        for( int ii=0; ii<numberOfRows; ii++ ) {
            for( int jj=0; jj<numberOfColumns; jj++ ) {
                m.setEntryUnchecked( ii,jj , this.entryUnchecked( i+ii , j+jj ) );
            }
        }
        return m;
    }
    
    
    private void assertRowIndexBounds( int i )
    {
        if(  Matrix.assertionsOn  &&  (  i < 0  ||  this.rows() < i  )  ) {
            throw new IllegalArgumentException( "Row index out of bounds: " + i + " not in 0..." + this.rows() );
        }
    }
    
    
    private void assertColumnIndexBounds( int j )
    {
        if(  Matrix.assertionsOn  &&  (  j < 0  ||  this.cols() < j  )  ) {
            throw new IllegalArgumentException( "Column index out of bounds: " + j + " not in 0..." + this.cols() );
        }
    }
    
    
    private void assertIndexBounds( int i , int j )
    {
        this.assertRowIndexBounds( i );
        this.assertColumnIndexBounds( j );
    }
    
    
    /**
     * Checks the number of rows of this matrix.
     * <p>
     * <ul>
     *  <li> If this has the number of rows, nothing happens.
     *  <li> If this does not have the number of rows, an {@link IllegalArgumentException} is thrown.
     * </ul>
     * 
     * @param rowsExpected  expected number of rows.
     * 
     * @throws IllegalArgumentException     if {@code this} does not have the expected number of rows.
     */
    private void assertRows( int rowsExpected )
    {
        if(  Matrix.assertionsOn  &&  this.rows() != rowsExpected  ) {
            throw new IllegalArgumentException( "Rows required: " + rowsExpected + " . Rows found: " + this.rows() );
        }
    }
    
    
    /**
     * Checks the size of this matrix.
     * <p>
     * <ul>
     *  <li> If this has the size, nothing happens.
     *  <li> If this does not have the size, an {@link IllegalArgumentException} is thrown.
     * </ul>
     * 
     * @param rowsExpected  expected number of rows.
     * @param colsExpected  expected number of columns.
     * 
     * @throws IllegalArgumentException     if {@code this} does not have the expected size.
     */
    private void assertSize( int rowsExpected , int colsExpected )
    {
        if(  Matrix.assertionsOn  &&  (  this.rows() != rowsExpected  ||  this.cols() != colsExpected  )  ) {
            throw new IllegalArgumentException( "Size required: " + rowsExpected + " x " + colsExpected +
                                                " . Size found: " + this.size() );
        }
    }
    
    
    /**
     * Checks that the size of {@code other} matches the size of {@code this}.
     * 
     * @param other     matrix whose size is checked to be equal to the size of {@code this}.
     * 
     * @throws IllegalArgumentException     if the sizes of {@code this} and {@code other} do not match.
     */
    private void assertEqualSize( Matrix other )
    {
        if(  Matrix.assertionsOn  &&  (  this.rows() != other.rows()  ||  this.cols() != other.cols()  )  ) {
            throw new IllegalArgumentException( "Same matrix size is required: " + this.size() + " != " + other.size() );
        }
    }
    
    
    private void assertIsSquare()
    {
        if(  Matrix.assertionsOn  &&  !this.isSquare()  ) {
            throw new IllegalArgumentException( "Square matrix is required: " + this.size() + " is not square." );
        }
    }
    
    
    /**
     * Adds the product of first {@link Matrix} and second {@link Matrix}.
     * <p>
     * The multiplication is performed as  {@code first * second}.
     * {@code this} must have the same rows as {@code first}, and same columns as {@code second}.
     * {@code this} must also be different from {@code first} and {@code second}.
     * This method does not perform any checks because it assumes that the developer will take care of the inputs or implement the checks in the methods using this method.
     * 
     * @param first     first factor of the {@link Matrix} multiplication.
     * @param second    second factor of the {@link Matrix} multiplication.
     * @return  {@code this} reference to which the product of {@code first} and {@code second} is added.
     */
    private Matrix addProductPrivate( Matrix first , Matrix second )
    {
        for( int i=0; i<first.rows(); i++ ) {
            for( int j=0; j<second.cols(); j++ ) {
                for( int k=0; k<first.cols(); k++ ) {
                    this.x[i][j] += first.entryUnchecked(i,k) * second.entryUnchecked(k,j);
                }
            }
        }
        return this;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE STATIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Performs a Cholesky decomposition of the form A = L * L^T.
     * <p>
     * This algorithm allows to perform an in-place decomposition.
     * That means that both inputs can be the same {@link Matrix}, and the output would be stored in the input.
     * 
     * @param A     input square {@link Matrix} from which the Cholesky decomposition is requested.
     * @param L     output square {@link Matrix} containing the Cholesky decomposition of input A.
     */
    private static void choleskyDecompositionAlgorithm( Matrix A , Matrix L )
    {
        // for each column
        for( int j=0; j<A.cols(); j++ ) {
            double sumD = 0.0;  // sum for the diagonal term
            // we first fill with 0.0 until diagonal
            for( int i=0; i<j; i++ ) {
                L.setEntryUnchecked( i,j , 0.0 );
                // we can compute this sum at the same time
                sumD -= L.entryUnchecked(j,i) * L.entryUnchecked(j,i);
            }
            sumD += A.entryUnchecked(j,j);
            // now we compute the diagonal term
            if( sumD <= 0.0 ) {
                throw new IllegalArgumentException( "Matrix must be positive-definite." );
            }
            L.setEntryUnchecked( j,j , Math.sqrt(sumD) );
            // we compute the terms below the diagonal
            for( int i=j+1; i<A.rows(); i++ ) {
                // first the sum
                double sumL = 0.0;
                for( int k=0; k<j; k++ ) {
                    sumL -= L.entryUnchecked(j,k) * L.entryUnchecked(i,k);
                }
                sumL += A.entryUnchecked(i,j);
                // then the division
                L.setEntryUnchecked( i,j , sumL/L.entryUnchecked(j,j) );
            }
        }
    }
    
    
    private static void ldltDecompositionAlgorithm( Matrix A , Matrix LD )
    {
        // for each column
        for( int j=0; j<A.cols(); j++ ) {
            double sumD = A.entryUnchecked(j,j);  // sum for the diagonal term
            // we first fill with 0.0 until diagonal
            for( int i=0; i<j; i++ ) {
                LD.setEntryUnchecked( i,j , 0.0 );
                // we can compute this sum at the same time
                sumD -= LD.entryUnchecked(j,i) * LD.entryUnchecked(j,i) * LD.entryUnchecked(i,i);
            }
            // now we set the diagonal term
            LD.setEntryUnchecked( j,j , sumD );
            // depending on the value of the diagonal term
            if( LD.entryUnchecked(j,j) != 0.0 ) {  // if m(j,j) is not zero
                // we compute the terms below the diagonal
                for( int i=j+1; i<A.rows(); i++ ) {
                    // first the sum
                    double sumL = A.entryUnchecked(i,j);
                    for( int k=0; k<j; k++ ) {
                        sumL -= LD.entryUnchecked(j,k) * LD.entryUnchecked(i,k) * LD.entryUnchecked(k,k);
                    }
                    // then the division
                    LD.setEntryUnchecked( i,j , sumL/LD.entryUnchecked(j,j) );
                }
            }else{
                // or we set them to zero
                for(int i=j+1; i<LD.rows(); i++){
                    LD.setEntryUnchecked( i,j , 0.0 );
                }
            }
        }
    }
    
    
    /**
     * Solves the equation  X * L * L^T = B  for X.
     * <p>
     * L must be a lower triangular square {@link Matrix}.
     * 
     * @param X     output {@link Matrix} that will contain the solution to  X * L * L^T = B . It must have the same size as B.
     * @param L     Cholesky decomposition of positive definite {@link Matrix} that will right divide  B.
     * @param B     {@link Matrix} to be right divided by the positive definite {@link Matrix} whose Cholesky decomposition is  L .
     */
    private static void divideRightByPositiveDefiniteUsingItsCholeskyDecompositionAlgorithm( Matrix X , Matrix L , Matrix B )
    {
        // we take each row from X and B independently
        for( int i=0; i<B.rows(); i++ ) {
            // first we solve ( y * L^T = B_i )
            for( int j=0; j<B.cols(); j++ ) {
                double sum = B.entryUnchecked(i,j);
                for( int k=0; k<j; k++ ) {
                    sum -= X.entryUnchecked(i,k) * L.entryUnchecked(j,k);
                }
                X.setEntryUnchecked( i,j , sum/L.entryUnchecked(j,j) );
            }
            // now we solve ( X_i * L = y )
            for( int j=X.cols()-1; j>-1; j-- ) {
                double sum = X.entryUnchecked(i,j);
                for( int k=j+1; k<X.cols(); k++ ) {
                    sum -= X.entryUnchecked(i,k) * L.entryUnchecked(k,j);
                }
                X.setEntryUnchecked( i,j , sum/L.entryUnchecked(j,j) );
            }
        }
    }
    
    
    private static void divideRightByPositiveDefiniteUsingItsLDLTDecompositionAlgorithm( Matrix A , Matrix LD , Matrix B )
    {
        // we take each pair of rows of K and M independently
        for( int i=0; i<A.rows(); i++ ) {
            // first we solve (y*D*L' = M)
            for( int j=0; j<LD.rows(); j++ ) {
                double sum = A.entryUnchecked(i,j);
                for( int k=0; k<j; k++ ) {
                    sum -= B.entryUnchecked(i,k) * LD.entryUnchecked(k,k) * LD.entryUnchecked(j,k);
                }
                B.setEntryUnchecked( i,j , sum/LD.entryUnchecked(j,j) );
            }
            // now we solve (K_i*L = y)
            for( int j=A.cols()-1; j>-1; j-- ) {
                double sum = B.entryUnchecked(i,j);
                for( int k=j+1; k<LD.rows(); k++ ) {
                    sum -= B.entryUnchecked(i,k) * LD.entryUnchecked(k,j);
                }
                B.setEntryUnchecked( i,j , sum );
            }
        }
    }
    
    
    /**
     * Solves the equation  L * L^T * X = B  for X.
     * <p>
     * L must be a lower triangular square {@link Matrix}.
     * 
     * @param X     output {@link Matrix} that will contain the solution to  L * L^T * X = B . It must have the same size as B.
     * @param L     Cholesky decomposition of positive definite {@link Matrix} that will left divide  B.
     * @param B     {@link Matrix} to be left divided by the positive definite {@link Matrix} whose Cholesky decomposition is  L .
     */
    private static void divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionAlgorithm( Matrix L , Matrix X , Matrix B )
    {
        // Take each column from X and B independently.
        for( int j=0; j<B.cols(); j++ ) {
            // Solve ( L * y = B_j ).
            for( int i=0; i<B.rows(); i++ ) {
                double sum = B.entryUnchecked(i,j);
                for( int k=0; k<i; k++ ) {
                    sum -= L.entryUnchecked(i,k) * X.entryUnchecked(k,j);
                }
                X.setEntryUnchecked( i,j , sum/L.entryUnchecked(i,i) );
            }
            // Solve ( y = L^T * X_j ).
            for( int i=X.rows()-1; i>-1; i-- ) {
                double sum = X.entryUnchecked(i,j);
                for( int k=i+1; k<X.rows(); k++ ) {
                    sum -= L.entryUnchecked(k,i) * X.entryUnchecked(k,j);
                }
                X.setEntryUnchecked( i,j , sum/L.entryUnchecked(i,i) );
            }
        }
    }
    
    
    private static Matrix absPrivate( Matrix m , Matrix output )
    {
        for( int i=0; i<m.rows(); i++ ) {
            for( int j=0; j<m.cols(); j++ ) {
                output.setEntryUnchecked( i,j , Math.abs( m.entryUnchecked(i,j) ) );
            }
        }
        return output;
    }


}

