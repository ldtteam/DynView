package com.dynamic_view.ViewDistHandler;

/**
 * Interface for dynamic view distance managers.
 */
public interface IDynamicViewDistanceManager
{
    void initViewDist();

    /**
     * Updates the current view distance based on server load.
     *
     * @param meanTick mean tick time
     */
    void updateViewDistForMeanTick(final int meanTick);

    void setCurrentChunkViewDist(int currentChunkViewDist);

    void setCurrentChunkUpdateDist(int currentChunkUpdateDist);
}
