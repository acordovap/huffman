package pkg;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Paths;

public class Compressor{

    public static void compress(String fp, int ws){
        //Leemos el archivo y lo ponemos en un arreglo de bytes
        try{
            // FileInputStream fis = new FileInputStream(fp);
            InputStream is = new FileInputStream(fp);
            long fileSize = new File(fp).length();

            byte [] allBytes = new byte[(int)fileSize];

            is.read(allBytes);
            is.close();
            // fis.close();

            ArrayList<NodeH> freq = Huffman.textToList(allBytes, ws);
            HashMap<String, String> map = Huffman.getMapping(Huffman.getHuffmanTree(freq));
            Huffman h = Huffman.compress(allBytes, map, ws);


            FileOutputStream fos = new FileOutputStream(fp+".jhc");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(h);
            oos.close();
            fos.close();
            
            // para guardar el archivo sin mapa de descomrpesión
            fos = new FileOutputStream(fp+"_nomap.jhc");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(h.getEncoded());
            oos.close();
            fos.close();
            
            System.out.println("Compressed file: " + fp + ".jhc");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void decompress(String fp){
        try{
            FileInputStream fis = new FileInputStream(fp);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Huffman hr = (Huffman) ois.readObject();
            ois.close();
            fis.close();
            byte [] allBytes = Huffman.decompress(hr);

            String fn = Paths.get(fp).getFileName().toString();
            String of = fp.replaceFirst(fn, "jhc_" + fn.substring(0, fn.length() - 4));
            OutputStream os = new FileOutputStream(of);
            for(Byte it: allBytes) os.write( it.byteValue() );
            os.close();
            
            System.out.println("Inflated file: " + of);
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public static void main(String [] args){
    	/**
        String a = "C:\\Users\\Alan\\Desktop\\Alan_CV_English.pdf";
        int ws = 4;
        compress(a, ws);
        decompress(a+".jhc");
        **/
    	
    	int i = 0;
    	if (args.length == 2 && args[i].equals("-c")) {
    		System.out.println("Compress");
    		int ws = 1;
    		String filepath = args[++i];
    		//comprimir
    		compress(filepath, ws);
    	}
    	else if(args.length == 4 && args[i].equals("-c") && args[++i].equals("-w")){
    		System.out.println("Compress");
    		int ws = Integer.parseInt(args[++i]);
    		String filepath = args[++i];
    		//comprimir
    		compress(filepath, ws);
    	}
    	else if(args.length == 2 && args[i].equals("-d")){
    		System.out.println("Decompress");
    		String filepath = args[++i];
    		//descomprimir
    		decompress(filepath);
    	}
    	else{
    		System.out.println("Programa mal invocado.");
    		System.out.println("\nSintaxis:");
    		System.out.println("\tjava -jar huffman -c [-w <n>] <archivo_a_comprimir>");
    		System.out.println("\t\tó");
    		System.out.println("\tjava -jar huffman -d <archivo_a_descomprimir>");
    	}
    }
}
