/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.meta.Column;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityDiff<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	implements EntityDiff<A, R, T, E>
{
	private E original;
	private E modified;

//	private static Logger logger = Logger.getLogger(AbstractEntityDiff.class);

	protected AbstractEntityDiff(E original, E modified) {
		super();
		this.original = original;
		this.modified = modified;
	}

	public E getOriginal() {
		return original;
	}

	public E getModified() {
		return modified;
	}

	@Override
	public Change change() {
		if (original == null && modified == null) {
			return null;
		}

		if (original == null) {
			return Change.ADDITION;
		}

		if (modified == null) {
			return Change.DELETION;
		}

		if (!attributes().isEmpty()) {
			return Change.MODIFICATION;
		}

		return null;
	}

	/**
	 * TODO: Make definition clearer.
	 *
	 * @param original
	 * @param modified
	 * @return
	 * @throws EntityException 
	 */

	protected Map<A, Change> attributes(E original, E modified) throws EntityRuntimeException {				
		EntityMetaData<A, R, T, E, ?, ?, ?> meta = original.getMetaData();
		Map<A, Change> cm = new HashMap<A, Change>();

		for (A a : meta.attributes()) {
			PrimitiveHolder<?, ?> o = original.value(a);
			PrimitiveHolder<?, ?> m = modified.value(a);

			if ((o == null && m == null) || (o == m)) {
				continue;
			}

			if (o == null || o.isNull()) {
				cm.put(a, Change.ADDITION);
				continue;
			}

			if (m == null || m.isNull()) {
				cm.put(a, Change.DELETION);
				continue;
			}

			try {
				if (!o.contentEquals(m)) {
					cm.put(a, Change.MODIFICATION);
				}
			}
			catch (ClassCastException e) {
				cm.put(a, Change.MODIFICATION);
				// TODO: should't we throw an exception
//				logger().info(e.getMessage());
			}
		}

		return cm;
	}

	protected Map<R, Change> references(E original, E modified) throws EntityRuntimeException {
		M meta = original.getMetaData();
		Map<R, Change> cm = new HashMap<R, Change>();

		for (R r : meta.relationships()) {
			Entity<?,?,?,?,?,?,?> o = original.ref(r).value();
			Entity<?,?,?,?,?,?,?> m = modified.ref(r).value();

			if ((o == null && m == null) || (o == m)) {
				continue;
			}

			if (o == null) {
				cm.put(r, Change.ADDITION);
				continue;
			}

			if (m == null) {
				cm.put(r, Change.DELETION);
				continue;
			}

			if (o != m && primaryKeyDiffers(o, m)) {
				cm.put(r, Change.MODIFICATION);
				continue;
			}
		}

		return cm;
	}

	private
	<P extends Entity<?, ?, ?, ?, ?, ?, ?>>
	boolean primaryKeyDiffers(P o, P m) throws EntityRuntimeException {
		Map<Column, PrimitiveHolder<?,?>> a = o.getPrimaryKey();
		Map<Column, PrimitiveHolder<?,?>> b = m.getPrimaryKey();

		if (a == null || b == null) {
			return a != b;
		}

		return (!a.equals(b));
	}

//	public static Logger logger() {
//		return AbstractEntityDiff.logger;
//	}
}
