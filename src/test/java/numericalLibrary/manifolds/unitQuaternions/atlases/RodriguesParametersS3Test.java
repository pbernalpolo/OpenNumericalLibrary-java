package numericalLibrary.manifolds.unitQuaternions.atlases;



/**
 * Implements tests methods for {@link RodriguesParametersS3}.
 */
class RodriguesParametersS3Test
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
        return new RodriguesParametersS3();
    }
    
}
