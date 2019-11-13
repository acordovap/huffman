package pkg;

import java.util.Arrays;

public class NodeH implements Comparable<NodeH>{
    private byte [] b;
    private NodeH left;
    private NodeH right;
    private int freq;

    @Override
    public int compareTo(NodeH o){
        return (this.freq - o.freq);
    }

    public boolean equals(Object o){
        if (o == null) return false;
	    if (o == this) return true;
	    if (!(o instanceof NodeH)) return false;
	    NodeH oC = (NodeH)o;
			int i = 0;
			if(this.b.length == oC.b.length){
				while(i < this.b.length){
					if(this.b[i ] != oC.b[i]){
						break;
					}
					i++;
				}
            }
	    return (i == this.b.length);
    }

    public NodeH(byte b[], int freq) {
		this.b = b;
		this.freq =  freq;
		this.left = null;
		this.right = null;
	}

    public NodeH(NodeH left, NodeH right) {
        this.left = left;
        this.right = right;
        this.freq = left.getFreq() + right.getFreq();
    }

    public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Arrays.toString(this.b) + " : " + this.freq );
		return sb.toString();
	}

	public void printTree() {
		if(this.left != null)
			this.left.printTree();
		System.out.println(this.toString());
		if(this.right != null)
			this.right.printTree();
	}

    public NodeH getLeft(){
        return this.left;
    }

    public NodeH getRight(){
        return this.right;
    }
    public byte [] getB(){
        return b;
    }

    public void addFreqByOne() {
		freq++;
	}

    public int getFreq() {
		return freq;
	}

}
