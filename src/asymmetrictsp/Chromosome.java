package asymmetrictsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Chromosome implements Comparator<Chromosome>, Comparable<Chromosome> {
    
    private List<Integer> genes;
    Random RNG = new Random();
    private boolean Selected = false;

    public Chromosome() {
        this.genes = new ArrayList<>();
        init();
    }

    public List<Integer> getGenes() {
        return genes;
    }
   
    public int fitness(){
        int fitness = 0;
        
        for(int i=1; i<genes.size(); i++){
            fitness += GA.cities[genes.get(i-1)][genes.get(i)];
            //System.out.printf(GA.cities[genes.get(i-1)][genes.get(i)] + " ");
        }
        
        return fitness;
    }
    
    public void init(){        
        for(int i=0; i<10; i++) genes.add(i);
        Collections.shuffle(genes);       
    }
    
    public void setGene(int index, int newGene){
        this.genes.set(index, newGene);
    }
    
    public int getGene(int index){
        return this.genes.get(index);
    }

    @Override
    public String toString() {
        return "" + genes;
    }
    
    public void makeOffSpring(){
        for(int i=0; i<10; i++) this.genes.set(i, -1);
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
