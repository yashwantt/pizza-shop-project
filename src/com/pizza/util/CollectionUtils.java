package com.pizza.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CollectionUtils extends org.springframework.util.CollectionUtils {
	
	private CollectionUtils() {
		// static only
	}

	/**
	 * Utility method to avoid specifying the types both in the variable and the instance.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return the map
	 */
	public static <K, V> Map<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * Utility method to avoid specifying the types both in the variable and the instance.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @param value the value
	 * @return the map
	 */
	public static <K, V> Map<K, V> newHashMap(final K key, final V value) {
		final HashMap<K, V> map = new HashMap<K, V>();
		map.put(key, value);
		return map;
	}

	/**
	 * Utility method to avoid specifying the types both in the variable and the instance.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param initialCapacity the initial capacity
	 * @return the map
	 */
	public static <K, V> Map<K, V> newHashMap(final int initialCapacity) {
		return new HashMap<K, V>(initialCapacity);
	}

	/**
	 * Utility method to avoid specifying the types both in the variable and the instance.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param map the map whose mappings are to be placed in this map
	 * @return the map
	 */
	public static <K, V> Map<K, V> newHashMap(final Map<? extends K, ? extends V> map) {
		return new HashMap<K, V>(map);
	}

	/**
	 * Combine keys and values from two arrays into one map. The key at each array index is paired with the
	 * value at the same index. Map entries are added in index order, which means if multiple key objects
	 * are {@link Object#equals(Object) equal}, the last matching value is the only one retained in the map.
	 * If the length of the input arrays are not equal, an exception is thrown.
	 * @param <K> The key type.
	 * @param <V> The value type.
	 * @param keys Array of keys.
	 * @param values Arrey of values.
	 * @return Combined keys and values. The count of entries will be equal to the number of elements in
	 *   each array only if all keys are distinct.
	 */
	public static <K, V> Map<K, V> zip(final K[] keys, final V[] values) {
		if (keys == null || values == null) {
			throw new IllegalArgumentException("Object cannot be null.");
		}
		if (keys.length != values.length) {
			throw new IllegalArgumentException("Array lengths must be equal.");
		}

		Map<K, V> result = new HashMap<K, V>();
		for (int i = 0; i < keys.length; i++) {
			result.put(keys[i], values[i]);
		}
		return result;
	}


	/**
	 * Utility method to avoid specifying the types both in the variable and the instance, and pre-populate the
	 * collection.
	 * @param <T> the set type
	 * @param elements the initial elements
	 * @return the set
	 */
	public static <T> Set<T> newHashSet(final T... elements) {
		return new HashSet<T>(Arrays.asList(elements));
	}

	/**
	 * Utility method to avoid specifying the type both in the variable and the instance.
	 * @param <T> the list type
	 * @return the list
	 */
	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}

	/**
	 * Utility method to avoid specifying the type both in the variable and the instance, and pre-populate the
	 * collection.
	 * @param <T> the list type
	 * @param elements the initial elements
	 * @return the list
	 */
	public static <T> List<T> newArrayList(final T... elements) {
		return new ArrayList<T>(Arrays.asList(elements));
	}

	/**
	 * Utility method to avoid specifying the type both in the variable and the instance, and pre-set the size.
	 * @param <T> the list type
	 * @param size the size of the array
	 * @return the list
	 */
	public static <T> List<T> newArrayList(final int size) {
		return new ArrayList<T>(size);
	}

	/**
	 * Returns a formatted string listing each element of the given collection.
	 * @param collection the collection
	 * @return a formatted string listing each element of the given collection
	 */
	public static String toString(final Collection<?> collection) {
		final StringBuilder buff = new StringBuilder();
		if (collection != null) {
			for (final Object o : collection) {
				buff.append(o).append(',');
			}
			if (buff.length() > 0) {
				buff.deleteCharAt(buff.length() - 1);
			}
		}
		return buff.insert(0, '{').append('}').toString();
	}

	/**
	 * @param map map to make into a string
	 * @return make a string out of a map, for debugging
	 */
	public static String mapToString(final Map<?, ?> map) {
		final StringBuilder builder = new StringBuilder();
		for (final Entry<?, ?> e : map.entrySet()) {
			builder.append(e.getKey()).append("=").append(e.getValue()).append(",");
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}

}
