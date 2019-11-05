package com.mmorpg.framework.asyncdb;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AsynDBEntity {

    private static final AtomicReferenceFieldUpdater<AsynDBEntity, AsynDBState> stateUpdater =
            AtomicReferenceFieldUpdater.newUpdater(AsynDBEntity.class, AsynDBState.class, "state");

    /**
     * 实体状态
     */
    private transient volatile AsynDBState state = AsynDBState.NOMAL;

    private transient Synchronizer synchronizer;

    private transient SyncQueue suncQueue;

    public boolean submit(Operation operation) {
        AsynDBState currentState = null;

        do {
            currentState = state;
            if (!operation.isCanOperationAt(currentState)) {
                throw new RuntimeException("[" + this + "] submit exception" + currentState + " " + operation);
            }
        } while (operation.isNeedToChangeAt(currentState) && !stateUpdater.compareAndSet(this, currentState, operation.STATE));

        return currentState == AsynDBState.NOMAL;
    }

    public boolean trySync(int maxTime) {
        int trySyncCount = 0;

        AsynDBState currentState;
        do {
            currentState = this.state;
        } while (!stateUpdater.compareAndSet(this, currentState, currentState != AsynDBState.DELETE ? AsynDBState.NOMAL : AsynDBState.DELETED));

        while (trySyncCount++ < maxTime) {
            if (currentState.doOperation(this.synchronizer, this)) {
                return true;
            }
        }

        return false;
    }

    public Synchronizer getSynchronizer() {
        return this.synchronizer;
    }

    public void setSynchronizer(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }

    public void serialize() {

    }

    public int getHash() {
        return hashCode();
    }

    public AsynDBState getAsynDBState() {
        return state;
    }

    public SyncQueue getSuncQueue() {
        return suncQueue;
    }

    public void setSuncQueue(SyncQueue suncQueue) {
        this.suncQueue = suncQueue;

    }
}
