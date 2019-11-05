package com.mmorpg.framework.asyncdb;

public enum AsynDBState {

    NOMAL() {
        @Override
        public boolean doOperation(Synchronizer<AsynDBEntity> synchronizer, AsynDBEntity asynDBEntity) {
            return true;
        }
    },

    DELETED() {
        @Override
        public boolean doOperation(Synchronizer<AsynDBEntity> synchronizer, AsynDBEntity asynDBEntity) {
            return true;
        }
    },

    DELETE() {
        @Override
        public boolean doOperation(Synchronizer<AsynDBEntity> synchronizer, AsynDBEntity asynDBEntity) {
            return synchronizer.deleteData(asynDBEntity);
        }
    },

    UPDATE() {
        @Override
        public boolean doOperation(Synchronizer<AsynDBEntity> synchronizer, AsynDBEntity asynDBEntity) {
            return synchronizer.updateData(asynDBEntity);
        }
    },

    INSERT() {
        @Override
        public boolean doOperation(Synchronizer<AsynDBEntity> synchronizer, AsynDBEntity asynDBEntity) {
            return synchronizer.insertData(asynDBEntity);
        }
    },
    ;

    public abstract boolean doOperation(Synchronizer<AsynDBEntity> var1, AsynDBEntity var2);
}
