package Structs.heap;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {

    public enum Type { MINIMAL, MAXIMAL };

    private ArrayList<T> heap;
    private int size;
    private Type type;

    public Heap(){
        this.heap = new ArrayList<T>();
        this.size = 0;
        this.type = Type.MINIMAL;
    }
    public Heap(Type t){
        this.heap = new ArrayList<T>();
        this.size = 0;
        this.type = t;
    }

    public int size(){ return this.size; }

    private void swap(int i1, int i2){
        T tmp = heap.get(i1);
        heap.set(i1, heap.get(i2));
        heap.set(i2,tmp);
    }

    private int parentIndex(int index){ return (index-1)/2; }
    private int leftChildIndex(int index){ return (index*2)+1; }
    private int rightChildIndex(int index){ return (index*2)+2; }

    private void upHeap(int index){
        
        int parentIndex = parentIndex(index);
        T parent;
        try{
            parent = heap.get(parentIndex);
        } catch (IndexOutOfBoundsException e){
            return;
        }
        T child = heap.get(index);
        
        if (this.type == Type.MINIMAL){

            if (parent.compareTo(child) <= 0) return;
            swap(index,parentIndex);
            upHeap(parentIndex);
            return;
        
        }
        else{
            
            if (parent.compareTo(child) >= 0) return;
            swap(index,parentIndex);
            upHeap(parentIndex);
            return;
        
        }

    }

    private void downHeap(int index){

        int leftChildIndex = leftChildIndex(index);
        int rightChildIndex = rightChildIndex(index);
        T leftChild, rightChild;
        try{
            leftChild = heap.get(leftChildIndex);
        } catch (IndexOutOfBoundsException e){
            return;
        }
        try{
            rightChild = heap.get(rightChildIndex);
        } catch (IndexOutOfBoundsException e){
            rightChild = null;
        }
        T parent = heap.get(index);
        

        if (this.type == Type.MINIMAL){
            
            if (rightChild != null){
                int swapIndex;
                if (leftChild.compareTo(rightChild) < 0)
                    swapIndex = leftChildIndex;
                else
                    swapIndex = rightChildIndex;
                swap(index,swapIndex);
                downHeap(swapIndex);
                return;
            }
            else{
                if (parent.compareTo(leftChild) <= 0) return;
                swap(index,leftChildIndex);
                downHeap(leftChildIndex);
                return;
            }
        
        }
        else{
            
            if (rightChild != null){
                int swapIndex;
                if (leftChild.compareTo(rightChild) > 0)
                    swapIndex = leftChildIndex;
                else
                    swapIndex = rightChildIndex;
                swap(index,swapIndex);
                downHeap(swapIndex);
                return;
            }
            else{
                if (parent.compareTo(leftChild) >= 0) return;
                swap(index,leftChildIndex);
                downHeap(leftChildIndex);
                return;
            }
        
        }
    }

    public void add(T o){
        this.heap.add(o);
        ++this.size;
        upHeap(size-1);
    }

    public T removeMin(){
        T ret = heap.get(0);
        swap(0,size-1);
        heap.remove(size-1);
        --this.size;
        downHeap(0);
        return ret;
    }

    public T peekMin(){ return this.heap.get(0); }

    public boolean contains(T o){ return heapSearch(o,0); }

    //! O(n) complexity
    private boolean heapSearch(T o, int index){

        if (index >= size) return false;

        T current = heap.get(index);
        if (o.compareTo(current) == 0) return true;

        int leftChildIndex = leftChildIndex(index);
        int rightChildIndex = rightChildIndex(index);
        T leftChild, rightChild;
        try{
            leftChild = heap.get(leftChildIndex);
        } catch (IndexOutOfBoundsException e){
            return false;
        }
        try{
            rightChild = heap.get(rightChildIndex);
        } catch (IndexOutOfBoundsException e){
            rightChild = null;
        }
        
        if (this.type == Type.MINIMAL){

            if (o.compareTo(current) < 0) return false;
            if (rightChild != null)
                return (heapSearch(o,leftChildIndex) || heapSearch(o,rightChildIndex));
            else
                return heapSearch(o,leftChildIndex);

        }

        else{

            if (o.compareTo(current) > 0) return false;
            if (rightChild != null)
                return (heapSearch(o,leftChildIndex) || heapSearch(o,rightChildIndex));
            else
                return heapSearch(o,leftChildIndex);

        }

    }

    //! O(log n) complexity on a sorted array of T
    /*
    private boolean binarySearch(T o, int startIndex, int endIndex){
        
        if (startIndex > endIndex) return false;
        if (startIndex == endIndex) return (o == heap.get(startIndex));
        
        int middle = (startIndex+endIndex)/2;
        if (heap.get(middle) == o) return true;

        if (o.compareTo(heap.get(middle)) < 0)
            return binarySearch(o, startIndex, middle);
        return binarySearch(o, middle+1, endIndex);
    
    }
    */

}