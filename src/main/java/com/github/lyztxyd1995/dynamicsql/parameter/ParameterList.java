package com.github.lyztxyd1995.dynamicsql.parameter;
import com.github.lyztxyd1995.dynamicsql.parameter.exceptions.IllegalParameterIndexException;
import com.github.lyztxyd1995.dynamicsql.parameter.exceptions.NoParameterFoundForMarkException;

class ParameterList {

    private int curIndex;

    ParamNode dummyHead;

    ParamNode dummayTail;

    private int numOfNodes;

    ParameterList(){
        this.curIndex = 1;
        this.dummyHead = new ParamNode(0, null);
        this.dummayTail = new ParamNode(Integer.MAX_VALUE, null);
        this.dummyHead.next = this.dummayTail;
        this.dummayTail.prev = this.dummyHead;
    }


    public void put(int position, String value){
        if (position <= 0 || position == Integer.MAX_VALUE) {
            throw new IllegalParameterIndexException();
        }
        ParamNode cur = dummyHead;
        while (cur.index < position) {
            cur = cur.next;
        }
        if (cur.index == position) {
            cur.value = value;
        } else {
            ParamNode newNode = new ParamNode(position, value);
            newNode.prev = cur.prev;
            newNode.next = cur;
            cur.prev.next = newNode;
            cur.prev = newNode;
        }
        numOfNodes++;
    }

    public String peek() {
        if (isEmpty()) {
            return null;
        }
        return dummyHead.next.index == curIndex ? dummyHead.next.value : null;
    }

    public String poll() {
        if (isEmpty()) {
            throw new NoParameterFoundForMarkException(curIndex);
        }
        if (dummyHead.next.index == curIndex) {
            String value = dummyHead.next.value;
            remove(dummyHead.next);
            curIndex++;
            return value;
        } else {
            throw new NoParameterFoundForMarkException(curIndex);
        }
    }

    private void remove(ParamNode paramNode) {
        ParamNode prev = paramNode.prev;
        ParamNode next = paramNode.next;
        prev.next = next;
        next.prev = prev;
        this.numOfNodes--;
    }


    public boolean isEmpty() {
        return numOfNodes == 0;
    }


    static class ParamNode{

        int index;

        String value;

        ParamNode next;

        ParamNode prev;

        ParamNode(int index, String value){
            this.index = index;
            this.value = value;
        }
    }
}
