package numericalLibrary.algebraicStructures;



/**
 * {@link FieldElement} represents elements of a field.
 * 
 * @param <T>   concrete type of {@link FieldElement}. We use CRTP to bound the type to interfaces that extend this interface.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Field_(mathematics)</a>
 */
public interface FieldElement<T extends FieldElement<T>>
	extends AdditiveAbelianGroupElement<T>, MultiplicativeAbelianGroupElement<T>
{
}
