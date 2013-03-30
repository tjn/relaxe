/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import java.sql.Connection;
import java.util.List;

import fi.tnie.db.AbstractUnitTest;
import fi.tnie.db.EntityQueryExecutor;
import fi.tnie.db.SimpleUnificationContext;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQueryExpressionSortKey;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.EntityQueryTemplateAttribute;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.ent.PredicateAttributeTemplate;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.expr.OrderBy;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.gen.pg.ent.pub.Film;
import fi.tnie.db.gen.pg.ent.pub.Language;
import fi.tnie.db.query.QueryResult;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class EntityQueryExecutorTest<I extends Implementation<I>> extends AbstractUnitTest<I> {
	
		
}
