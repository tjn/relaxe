/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
/**
 *
 */
package com.appspot.relaxe.pg.pp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.appspot.relaxe.Counter;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film;
import com.appspot.relaxe.rpc.IntegerHolder;


public class PagilaInspector {


//	private Object root = null;


	// private Map<>
	private IdentityHashMap<Object, Counter> instanceMap = new IdentityHashMap<Object, Counter>();
	private Map<Class<?>, Counter> typeCountMap = new HashMap<Class<?>, Counter>();

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {

		Film film = Film.Type.TYPE.getMetaData().getFactory().newEntity();
		film.setInteger(Film.FILM_ID, IntegerHolder.valueOf(1));

		PagilaInspector i = new PagilaInspector();
		i.inspect(film);

		System.err.println("instance-count: " + i.getInstanceCount());
		System.err.println("reference-count: " + i.getReferenceCount());

	}



	public void inspect(Object o) throws IllegalArgumentException, IllegalAccessException {

		boolean visited = false;

		if (o != null) {
			visited = instanceMap.containsKey(o);
			getCounter(instanceMap, o).increment();
		}

		Class<?> type = (o == null) ? null : o.getClass();
		getCounter(typeCountMap, type).increment();

		if (visited || type == null) {
			// skip:
		}
		else {
			List<Field> fl = new ArrayList<Field>();
			collect(type, fl);

			for (Field f : fl) {
				f.setAccessible(true);

				if (f.getType().isPrimitive()) {
					getCounter(typeCountMap, f.getType()).increment();
				}
				else {
					Object v = f.get(o);
					inspect(v);
				}
			}

			if (type.isArray()) {
				if (o instanceof Object[]) {
					Object[] elems = (Object[]) o;

					for (Object elem : elems) {
						inspect(elem);
					}
				}
			}
		}



	}




	private void collect(Class<?> type, List<Field> fl) {
		Field[] fields = type.getDeclaredFields();

 		for (Field f : fields) {
			int mods = f.getModifiers();

			if ((mods & Modifier.TRANSIENT) != 0) {
				if (type.getName().startsWith("fi.tnie")) {
					continue;
				}
			}

			if ((mods & Modifier.STATIC) != 0) {
				continue;
			}

//			System.err.println(type.getName() + ":" + f.toString());

//			String n = f.getName();
			fl.add(f);
		}

		Class<?> stype = type.getSuperclass();

		if (stype != null) {
			collect(stype, fl);
		}
	}



	private <K> Counter getCounter(Map<K, Counter> map, K key) {
		Counter c = map.get(key);

		if (c == null) {
			map.put(key, c = new Counter());
		}

		return c;

	}


	public int getInstanceCount() {
		return this.instanceMap.size();
	}

	public int getReferenceCount() {
		int c = 0;

		for (Counter counter : instanceMap.values()) {
			c += counter.value();
		}

		return c;
	}



	@Override
	public String toString() {

		StringBuilder buf = new StringBuilder(100);
		buf.append("instance-count:\n\t").append(getInstanceCount()).append("\n");
		buf.append("reference-count:\n\t").append(getReferenceCount()).append("\n");
		buf.append("\n");

		// Map<Class<?>, Counter> m = new TreeMap<Class<?>, Counter>(typeCountMap);

		buf.append("\nreferences by type:\n");

		for (Class<?> t : typeCountMap.keySet()) {
			Counter c = typeCountMap.get(t);
			buf.append("\t");
			buf.append((t == null) ? "<null>" : t.getName()).append(": ").append(c.value()).append("\n");
		}

		Map<Class<?>, Counter> im = new HashMap<Class<?>, Counter>();

		for (Object o : instanceMap.keySet()) {
			Class<?> type = (o == null) ? null : o.getClass();
			getCounter(im, type).increment();
		}

		buf.append("\ninstances by type:\n");

		for (Class<?> t : im.keySet()) {
			Counter c = im.get(t);
			buf.append("\t");
			buf.append((t == null) ? "<null>" : t.getName()).append(": ").append(c.value()).append("\n");
		}

		buf.append("\n");
		buf.append("\n");

		return buf.toString();
	}
}

