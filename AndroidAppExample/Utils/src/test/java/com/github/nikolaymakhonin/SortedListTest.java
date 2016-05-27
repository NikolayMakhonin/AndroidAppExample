package com.github.nikolaymakhonin;

import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.lists.list.CollectionChangedEventArgs;
import com.github.nikolaymakhonin.utils.lists.list.CollectionChangedType;
import com.github.nikolaymakhonin.utils.lists.list.SortedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SortedListTest {
    private final Random _rnd = new Random();
    
    @Test
    public void TestAutoSort() {
        final SortedList<Integer> list = new SortedList<>(true, false, new Integer[] { 5, 3, 2, 4, 1 });
        assertList(list, new Integer[] { 1, 2, 3, 4, 5 });
        list.Remove(2);
        Assert.assertEquals(list.CountSorted(), 4);
        assertList(list, new Integer[] { 1, 3, 4, 5 });
        list.Remove(5);
        Assert.assertEquals(list.CountSorted(), 3);
        assertList(list, new Integer[] { 1, 3, 4 });
        list.RemoveAt(0);
        Assert.assertEquals(list.CountSorted(), 2);
        assertList(list, new Integer[] { 3, 4 });
        list.add(1);
        Assert.assertEquals(list.CountSorted(), 3);
        assertList(list, new Integer[] { 1, 3, 4 });
        list.AddAll(new Integer[] { 0, 5, 2, 3 });
        Assert.assertEquals(list.CountSorted(), 7);
        assertList(list, new Integer[] { 0, 1, 2, 3, 3, 4, 5 });
        list.set(3, 7);
        Assert.assertEquals(list.CountSorted(), 7);
        list.set(3, -1);
        Assert.assertEquals(list.CountSorted(), 7);
        assertList(list, new Integer[] { -1, 0, 1, 2, 4, 5, 7 });
    }
    
    @Test 
    public void TestIterator() {
        final Integer[] checkArray = { 5, 3, 4, 1, 2 };
        final SortedList<Integer> list = new SortedList<>(false, false, checkArray);
        int index = 0;
        for (final Integer item : list) {
            Assert.assertEquals(checkArray[index++], item);
        }
        Assert.assertEquals(checkArray.length, index);
    }
    
    @Test
    public void TestIndexOf() {
        final SortedList<Integer> list = new SortedList<>(false, false, new Integer[] { 5, 3, 4, 1, 2 });
        Assert.assertEquals(list.IndexOf(2), 4);
        list.Sort();
        Assert.assertEquals(list.IndexOf(2), 1);
        list.AddAll(new Integer[] { 7, 3, 8, 9 });
        Assert.assertEquals(list.IndexOf(9), 8);
        Assert.assertEquals(list.IndexOf(7), 5);
        Assert.assertEquals(list.IndexOf(3), 2);
        Assert.assertEquals(list.IndexOf(5), 4);
        list.set(0, list.get(0));
        Assert.assertEquals(list.IndexOf(3), 2);
    }
    
    private final Queue<CollectionChangedEventArgs> _collectionChangedEventArgs = new LinkedList<>();
    
    @Test
    public void TestCollectionChanged() {
        final SortedList<Integer> list = new SortedList<>(true, false, new Integer[] { 5, 3, 2, 4, 1 });
        list.CollectionChanged().subscribe(e -> _collectionChangedEventArgs.offer(e));
        assertCollectionChangedEmpty();
        list.Remove(2);
        assertCollectionChanged(CollectionChangedType.Resorted, -1, -1, null, null);
        assertCollectionChanged(CollectionChangedType.Removed, 1, -1, new Integer[] { 2 }, null);
        assertCollectionChanged(CollectionChangedType.Shift, 2, 1, null, null);
        assertCollectionChangedEmpty();
        list.Remove(5);
        assertCollectionChanged(CollectionChangedType.Removed, 3, -1, new Integer[] { 5 }, null);
        assertCollectionChangedEmpty();
        list.RemoveAt(0);
        assertCollectionChanged(CollectionChangedType.Removed, 0, -1, new Integer[] { 1 }, null);
        assertCollectionChanged(CollectionChangedType.Shift, 1, 0, null, null);
        assertCollectionChangedEmpty();
        list.add(1);
        assertCollectionChanged(CollectionChangedType.Shift, 0, 1, null, null);
        assertCollectionChanged(CollectionChangedType.Added, -1, 0, null, new Integer[] { 1 });
        assertCollectionChangedEmpty();
        list.AddAll(new Integer[] { 0, 5, 2, -1 });
        assertCollectionChanged(CollectionChangedType.Shift, 0, 1, null, null);
        assertCollectionChanged(CollectionChangedType.Added, -1, 0, null, new Integer[] { 0 });
        assertCollectionChanged(CollectionChangedType.Added, -1, 4, null, new Integer[] { 5 });
        assertCollectionChanged(CollectionChangedType.Shift, 2, 3, null, null);
        assertCollectionChanged(CollectionChangedType.Added, -1, 2, null, new Integer[] { 2 });
        assertCollectionChanged(CollectionChangedType.Shift, 0, 1, null, null);
        assertCollectionChanged(CollectionChangedType.Added, -1, 0, null, new Integer[] { -1 });
        assertCollectionChangedEmpty();
        list.clear();
        assertCollectionChanged(CollectionChangedType.Removed, 0, -1, new Integer[] { -1, 0, 1, 2, 3, 4, 5 }, null);
        assertCollectionChangedEmpty();
        list.setAutoSort(false);
        list.AddAll(new Integer[] { 1, 3, 4 });
        assertCollectionChanged(CollectionChangedType.Added, -1, 0, null, new Integer[] { 1, 3, 4 });
        assertCollectionChangedEmpty();
        list.Sort();
        assertCollectionChanged(CollectionChangedType.Resorted, -1, -1, null, null);
        assertCollectionChangedEmpty();
        list.Sort();
        assertCollectionChangedEmpty();
        list.AddAll(new Integer[] { 0, 5, 2, -1 });
        assertCollectionChanged(CollectionChangedType.Added, -1, 3, null, new Integer[] { 0, 5, 2, -1 });
        assertCollectionChangedEmpty();
        list.setAutoSort(true);
        list.set(3, 7);
        assertCollectionChanged(CollectionChangedType.Resorted, -1, -1, null, null);
        assertCollectionChanged(CollectionChangedType.Setted, 3, 3, new Integer[] { 2 }, new Integer[] { 7 });
        assertCollectionChanged(CollectionChangedType.Moved, 3, 6, null, new Integer[] { 7 });
        assertCollectionChangedEmpty();
        list.setSize(list.size() + 3, 0);
        assertCollectionChanged(CollectionChangedType.Added, -1, 7, null, new Integer[] { 0, 0, 0 });
        assertCollectionChangedEmpty();
        list.setSize(list.size() - 2, 0);
        assertCollectionChanged(CollectionChangedType.Removed, 8, -1, new Integer[] { 0, 0 }, null);
        assertCollectionChangedEmpty();
        //noinspection UnusedAssignment
        int i = list.get(7);
        assertCollectionChanged(CollectionChangedType.Resorted, -1, -1, null, null);
        assertCollectionChangedEmpty();
        
        list.setComparator((i1, i2) -> -CompareUtils.Compare(i1, i2));
        
        assertCollectionChangedEmpty();
        //noinspection UnusedAssignment
        i = list.get(7);
        assertCollectionChanged(CollectionChangedType.Resorted, -1, -1, null, null);
        assertCollectionChangedEmpty();
        assertList(list, new Integer[] { 7, 5, 4, 3, 1, 0, 0, -1 });
        assertCollectionChangedEmpty();
        list.set(1, 2);
        assertCollectionChanged(CollectionChangedType.Setted, 1, 1, new Integer[] { 5 }, new Integer[] { 2 });
        assertCollectionChanged(CollectionChangedType.Moved, 1, 3, null, new Integer[] { 2 });
        assertCollectionChangedEmpty();
        list.set(6, 6);
        assertCollectionChanged(CollectionChangedType.Setted, 6, 6, new Integer[] { 0 }, new Integer[] { 6 });
        assertCollectionChanged(CollectionChangedType.Moved, 6, 1, null, new Integer[] { 6 });
        assertCollectionChangedEmpty();
    }
    
    //region Stress Test
    private static class ComparableObject implements Comparable {
        private static int _nextId = 1;
        public final int id = _nextId++;
        
        @Override
        public int compareTo(final Object another) {
            return CompareUtils.Compare(id, ((ComparableObject)another).id);
        }    
        
        @Override
        public String toString() {
            return Integer.toString(id);
        }
    }
    
    @Test
    public void testStressSimple() {
        final int itemsCount = 50;
        final Object[] objectItems = new Object[itemsCount];
        final Integer[] intItems = new Integer[itemsCount];
        final ComparableObject[] comparableObjectItems = new ComparableObject[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            objectItems[i] = new Object();
            intItems[i] = i/2;
            comparableObjectItems[i] = new ComparableObject();
        }
        
        for (int i = 0; i < itemsCount; i++) {
            Assert.assertEquals(indexOfArrayList(Arrays.asList(objectItems), objectItems[i], null), i);
            Assert.assertEquals(intItems[indexOfArrayList(Arrays.asList(intItems), intItems[i], CompareUtils.IntegerComparator)], intItems[i]);
            Assert.assertEquals(indexOfArrayList(Arrays.asList(comparableObjectItems), comparableObjectItems[i], null), i);
        }
        
        stressTest(objectItems, false, false, null);
        stressTest(objectItems, false, true, null);
        stressTest(objectItems, true, false, null);
        stressTest(objectItems, true, true, null);
        stressTest(comparableObjectItems, false, false, null);
        stressTest(comparableObjectItems, false, true, null);
        stressTest(comparableObjectItems, true, false, null);
        stressTest(comparableObjectItems, true, true, null);
        stressTest(intItems, false, false, CompareUtils.IntegerComparator);
        stressTest(intItems, false, true, CompareUtils.IntegerComparator);
        stressTest(intItems, true, false, CompareUtils.IntegerComparator);
        stressTest(intItems, true, true, CompareUtils.IntegerComparator);
    }
    
    private <T> String checkList(final List<T> list1, final List<T> list2, final Comparator<T> comparator) {
        final int length = list1.size();
        if (length != list2.size()) {
            return String.format("list1.size(%s) != list2.size(%s)", length, list2.size());
        }
        for (int i = 0; i < length; i++) {
            final T item2 = list2.get(i);
            if (indexOfArrayList(list1, item2, comparator) < 0) {
                return String.format("list2[%s] (%s) not found in list1", i, item2);
            }
        }
        return null;
    }
    
    private <T> void stressTest(final T[] items, final boolean autoSort, final boolean notAddIfExists, final Comparator<T> comparator) {
        final List<T> list1 = new ArrayList<>();
        final List<T> list2 = new SortedList<>(autoSort, notAddIfExists, comparator);
        String errorMessage;
        for (int i = 0; i < 100000; i++) {
            final int numAction = _rnd.nextInt(5);
            final T item = items[_rnd.nextInt(items.length)];
            final int length = list1.size();
            Assert.assertEquals(length, list2.size());
            if (length == 0 && numAction >= 2) {
                continue;
            }
            int index1, index2;
            switch (numAction) {
                case 0:
                    if (!notAddIfExists || indexOfArrayList(list1, item, comparator) < 0) {
                        list1.add(item);
                    }
                    list2.add(item);
                    if ((errorMessage = checkList(list1, list2, comparator)) != null) {
                        list2.add(item);
                        Assert.fail(errorMessage);
                    }
                    break;
                case 1:
                    list1.remove(item);
                    list2.remove(item);
                    if ((errorMessage = checkList(list1, list2, comparator)) != null) {
                        list2.remove(item);
                        Assert.fail(errorMessage);
                    }
                    break;
                case 2:
                    index2 = _rnd.nextInt(length);
                    if (autoSort) {
                        final T indexItem = list2.get(index2);
                        index1 = indexOfArrayList(list1, indexItem, comparator);
                        Assert.assertTrue(index1 >= 0);
                    } else {
                        index1 = index2;
                    }
                    list1.remove(index1);
                    list2.remove(index2);
                    if ((errorMessage = checkList(list1, list2, comparator)) != null) {
                        list2.remove(index2);
                        Assert.fail(errorMessage);
                    }
                    break;
                case 3:
                    index1 = indexOfArrayList(list1, item, comparator);
                    index2 = list2.indexOf(item);
                    if (autoSort) {
                        Assert.assertEquals(index1 < 0, index2 < 0);
                        if (index2 > 0) {
                            final T indexItem = list2.get(index2);
                            Assert.assertTrue(equalItems(item, indexItem, comparator));
                        }
                    } else {
                        Assert.assertEquals(index1, index2);
                    }
                    if ((errorMessage = checkList(list1, list2, comparator)) != null) {
                        list2.remove(index2);
                        Assert.fail(errorMessage);
                    }
                    break;
                case 4:
                    index2 = _rnd.nextInt(length);
                    if (autoSort) {
                        final T indexItem = list2.get(index2);
                        index1 = indexOfArrayList(list1, indexItem, comparator);
                        Assert.assertTrue(index1 >= 0);
                    } else {
                        index1 = index2;
                    }
                    if (notAddIfExists) {
                        final int existIndex = indexOfArrayList(list1, item, comparator);
                        if (existIndex < 0 || existIndex == index1) {
                            list1.set(index1, item);
                        } else {
                            list1.remove(index1);
                        }
                    } else {
                        list1.set(index1, item);
                    }
                    list2.set(index2, item);
                    if ((errorMessage = checkList(list1, list2, comparator)) != null) {
                        list2.set(index2, item);
                        Assert.fail(errorMessage);
                    }
                    break;                    
                default:
                    Assert.fail();
                    break;
            }
        }
        if ((errorMessage = checkList((List<T>)Arrays.asList(list1.toArray()), (List<T>)Arrays.asList(list2.toArray()), comparator)) != null) {
            Assert.fail(errorMessage);
        }
    }
    
    private <T> int indexOfArrayList(final List<T> arrayList, final T item, final Comparator comparator) {
        final int length = arrayList.size();
        for (int i = 0; i < length; i++) {
            final T listItem = arrayList.get(i);
            if (equalItems(listItem, item, comparator)) {
                return i;
            }
        }
        return -1;
    }
    
    private <T> boolean equalItems(final T item1, final T item2, final Comparator comparator) {
        if (comparator != null) {
            if (comparator.compare(item2, item1) == 0) {
                return true;
            }
        } else {
            if (item1 instanceof Comparable && ((Comparable)item1).compareTo(item2) == 0) {
                return true;
            }
            if (item2 instanceof Comparable && ((Comparable)item2).compareTo(item1) == 0) {
                return true;
            }
            if (item1 == item2 || (item1 != null && item1.equals(item2))) {
                return true;
            }
        }
        return false;
    }
    //endregion

    private void assertCollectionChangedEmpty() {
        Assert.assertEquals(_collectionChangedEventArgs.size(), 0);
    }
    
    private void assertCollectionChanged(final CollectionChangedType changedType, final int oldIndex,
        final int newIndex, final Integer[] oldItems, final Integer[] newItems) {
        Assert.assertTrue(_collectionChangedEventArgs.size() >= 1);
        final CollectionChangedEventArgs e = _collectionChangedEventArgs.poll();
        Assert.assertEquals(e.getChangedType(), changedType);
        Assert.assertEquals(e.getOldIndex(), oldIndex);
        Assert.assertEquals(e.getNewIndex(), newIndex);
        assertList(e.getOldItems(), oldItems);
        assertList(e.getNewItems(), newItems);
    }
    
    private <T> void assertList(final List<T> list, final T[] value) {
        Assert.assertEquals(list == null, value == null);
        if (list == null || value == null) { return; }
        Assert.assertEquals(list.size(), value.length);
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(list.get(i), value[i]);
        }
    }
    
    private <T> void assertList(final Object[] list, final T[] value) {
        Assert.assertEquals(list == null, value == null);
        if (list == null || value == null) { return; }
        Assert.assertEquals(list.length, value.length);
        for (int i = 0; i < list.length; i++) {
            Assert.assertEquals(list[i], value[i]);
        }
    }
}
