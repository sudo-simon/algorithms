package Structs;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {

    public enum Type { MINIMAL, MAXIMAL };

    private ArrayList<T> heap;
    private int size;
    private Type type;

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
                //TODO here
            }
            else{
                //TODO here
            }
            return;
        
        }
        else{
            
            if (rightChild != null){
                //TODO here
            }
            else{
                //TODO here
            }
            return;
        
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

    public boolean contains(T o){ return binarySearch(o,0,size-1); }

    private boolean binarySearch(T o, int startIndex, int endIndex){
        
        if (startIndex > endIndex) return false;
        
        int middle = (startIndex+endIndex)/2;
        if (heap.get(middle) == o) return true;

        if (o.compareTo(heap.get(middle)) < 0) return binarySearch(o, startIndex, middle);
        return binarySearch(o, middle+1, endIndex);
    
    }


}