/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityMetaData;

public abstract class MetaDataRegistry {    
    private Map<Class<?>, EntityMetaData<?, ?, ?, ?, ?>> metaDataMap = null;

    public EntityMetaData<?, ?, ?, ?, ?> getMetaData(
            Class<? extends EntityMetaData<?, ?, ?, ?, ?>> metaDataClass)
            throws EntityException {
        return getMetaDataMap().get(metaDataClass);
    }
 
    protected void register(EntityMetaData<?, ?, ?, ?, ?> m) {
        getMetaDataMap().put(m.getClass(), m);        
    }   
    
    private Map<Class<?>, EntityMetaData<?, ?, ?, ?, ?>> getMetaDataMap() {
        if (metaDataMap == null) {
            metaDataMap = new HashMap<Class<?>, EntityMetaData<?,?,?,?,?>>();            
        }

        return metaDataMap;
    }}