package numericalLibrary.manifolds.unitQuaternions.atlases;



/**
 * Implements tests methods for {@link ModifiedRodriguesParametersS3}.
 */
class ModifiedRodriguesParametersS3Test
    extends UnitQuaternionDifferentiableAtlasTester
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public UnitQuaternionDifferentiableAtlas getAtlas()
    {
        return new ModifiedRodriguesParametersS3();
    }
    
}
