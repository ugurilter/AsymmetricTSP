package part2;

import java.util.Comparator;
import java.util.Random;

public class Chromosome implements Comparator<Chromosome>, Comparable<Chromosome>{
    
    private String genes = "";
    private boolean Selected = false;
    Random RNG = new Random();

    public Chromosome() {        
        init();
    }
    
    public int fitness(){        
        return (int) Math.pow(binaryToInt(genes), 2) + (16 * binaryToInt(genes));
    }
    
    public void init(){        
        for(int i=0; i<8; i++){
            if(RNG.nextBoolean()) genes+= "1";
            else genes+= "0";
        }
    }
    
    public void setGene(int index, char c){
        StringBuilder builder = new StringBuilder(genes);
        builder.setCharAt(index, 'c');
    }
    
    public void bitFlip(int index){
        StringBuilder builder = new StringBuilder(genes);
        if(builder.charAt(index) == '1') builder.setCharAt(index, '0');
        else builder.setCharAt(index, '1');        
    }
    
    public char getGene(int index){
        return genes.charAt(index);
    }
    
    public int binaryToInt(String number){
        String buffer = number.substring(1);        
        if(number.charAt(0) == '0') return Integer.parseInt(buffer, 2);
        return Integer.parseInt(buffer, 2) - 128;     
    }

    @Override
    public String toString() {
        return genes;
    }
    
    @Override
    public int compare(Chromosome arg0, Chromosome arg1) {
        if(arg0.fitness() < arg1.fitness()) return -1;
        else if(arg0.fitness() > arg1.fitness()) return 1;
        return 0;
    }

    @Override
    public int compareTo(Chromosome arg0) {
        if(this.fitness() < arg0.fitness()) return -1;
        else if(this.fitness() > arg0.fitness()) return 1;
        return 0;    
    }
    
    public void reset(){
        Selected = false;
    }
    
    public void select(){
        Selected = true;
    }

    public boolean isSelected() {
        return Selected;
    }
    
    
}
