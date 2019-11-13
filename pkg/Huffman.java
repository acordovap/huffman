package pkg;

import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collections;
import java.io.Serializable;

public class Huffman implements Serializable{
    private static final long serialVersionUID = 1L;

    private HashMap<String, String> map;
    private byte [] encoded;

    public HashMap<String, String> getMap(){
        return this.map;
    }

    public byte [] getEncoded(){
        return this.encoded;
    }



    public Huffman(HashMap<String, String> map, byte [] encoded){
        this.map = map;
        this.encoded = encoded;
    }

    public static ArrayList<NodeH> textToList(byte [] text, int ws){
        ArrayList<NodeH> arr = new ArrayList<NodeH>();
        int ind;
        ByteBuffer bb = ByteBuffer.wrap(text);
        bb.rewind();
        while(bb.remaining() >= ws){
            byte [] ba = new byte[ws];
            bb.get(ba, 0,ws);
            NodeH a = new NodeH(ba, 1);
            ind =  arr.indexOf(a) ;
            if( ind != -1 )
                arr.get(ind).addFreqByOne();
            else
                arr.add(a);
        }
        if(bb.hasRemaining())
        {
            byte [] ba = new byte[bb.remaining()];
            bb.get(ba);
            NodeH a = new NodeH(ba,1);
            arr.add(a);
        }
        byte [] ba = new byte [0];
        NodeH eof = new NodeH(ba, 1);
        arr.add(eof);
        return arr;
    }

    public static NodeH getHuffmanTree(ArrayList<NodeH> nodeList) {
        while(nodeList.size() > 1) {
            Collections.sort(nodeList);
            NodeH x = nodeList.remove(0);
            NodeH y = nodeList.remove(0);
            NodeH z = new NodeH(x, y);
            nodeList.add(z);
        }
        return nodeList.get(0);
    }

    public static HashMap<String, String> getMapping(NodeH root){
        HashMap<String, String> map = new HashMap<String, String>();
        getMappingSub(root, map, "");
        return map;
    }

    private static void getMappingSub(NodeH n, HashMap<String, String> map, String path ){
        if(n.getLeft() == null && n.getRight() == null){
            map.put(Arrays.toString(n.getB()), path);
        }
        else{
            if(n.getLeft() != null){
                getMappingSub(n.getLeft(), map, path+"0");
            }
            if(n.getRight() != null){
                getMappingSub(n.getRight(), map, path+"1");
            }
        }
    }

    public static Huffman compress(byte [] corpus, HashMap<String, String> map, int ws ){
        ArrayList<Byte> auxA = new ArrayList<Byte>();
        HashMap<String, String> decodeMap = new HashMap<String, String>();
        int auxB = 0;
        ByteBuffer bb = ByteBuffer.wrap(corpus);
        StringBuffer toEncode = new StringBuffer();
        bb.rewind();
        while(bb.remaining() >= ws){
            byte [] ba = new byte[ws];
            bb.get(ba, 0,ws);
            toEncode.append( map.get(Arrays.toString(ba)) );
        }
        if(bb.hasRemaining())
        {
            byte [] ba = new byte[bb.remaining()];
            bb.get(ba);
            toEncode.append( map.get(Arrays.toString(ba)) );
        }
        toEncode.append(map.get(Arrays.toString(new byte[0])));
        int nb = 0;
        String tE = toEncode.toString();
        CharacterIterator it = new StringCharacterIterator(tE);

		while (it.current() != CharacterIterator.DONE) {
			auxB += Character.getNumericValue(it.current());
			nb++;
            if(nb == 8){
                auxA.add((byte)auxB);
                nb = 0;
                auxB = 0;
            }
            else{
                auxB = auxB << 1;
            }
			it.next();
		}
        if (nb > 0){
            auxB = auxB << (7-nb);
            auxA.add((byte)auxB);
        }
        map.forEach((k,v) -> decodeMap.put(v,k));
        byte[] byteArray = new byte[auxA.size()];
        for(int i = 0; i < auxA.size(); i++) byteArray[i] = auxA.get(i).byteValue();

        return new Huffman(decodeMap, byteArray);

    }

    public static int getBit(byte b, int p){
        int r = 0;
        r = b >> (7 - p) & 1;
        return r;
    }

    public static byte [] decompress(Huffman h){
        HashMap<String, String> map = h.getMap();
        byte [] encoded = h.getEncoded();
        ArrayList<Byte> decompressed  = new ArrayList<Byte>();
        StringBuffer sba = new StringBuffer();

        ByteBuffer bb = ByteBuffer.wrap(encoded);
        bb.rewind();

        while(bb.hasRemaining()){
            byte b = bb.get();
            for(int i = 0; i < 8; i++){
                sba.append(getBit(b, i));
                String v = map.get(sba.toString());
                if(v != null){
                    if(v.equals("[]") )
                        break;
                    String ss = v.substring(1,v.length() - 1);
                    StringTokenizer st = new StringTokenizer(ss, ", ");
                    while(st.hasMoreTokens()){
                        decompressed.add((byte)Integer.parseInt(st.nextToken()));
                    }
                    sba = new StringBuffer();
                }
            }
        }
        byte [] ret = new byte[decompressed.size()];
        for(int i = 0; i < decompressed.size(); i++) ret[i] = decompressed.get(i).byteValue();
        return ret;
    }


    public static String bytesToString(byte [] ba){
    	System.out.println("bytesToString");
        StringBuffer sb = new StringBuffer();
        ByteBuffer bb = ByteBuffer.wrap(ba);
        bb.rewind();
        while(bb.hasRemaining()){
            byte b = bb.get();
            for(int i = 0; i < 8; i++)
                sb.append(getBit(b,i));
        }
        return sb.toString();
    }


}
