package part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Part2 {    
    
    private double mutationRate = 0.05;
    Random RNG = new Random();
    public int bestCounter;
    public Chromosome bestSoFar = new Chromosome();
    public Chromosome bestThisTurn = new Chromosome();    
    public Chromosome[] population = new Chromosome[6];
    
    

    public Part2(){
        initChromosomes();
        startGA();
    }
    
    public void initChromosomes(){
        for(int i=0; i<6; i++) population[i] = new Chromosome();
    }
    
    public Chromosome getFittest() {
        Chromosome fittest = population[0];

        for (int i = 0; i < population.length; i++) {
            if (fittest.fitness() >= population[i].fitness()) {
                fittest = population[i];
            }
        }
        
        return fittest;
    }
    
    public Chromosome tournamentSelection(){
        Chromosome candidate1, candidate2;
        int random, random2;

        while(true){
            random = RNG.nextInt(6);
            if(!population[random].isSelected()){
                population[random].select();
                break;
            }
        }
        
        while(true){
            random2 = RNG.nextInt(6);
            if(!population[random2].isSelected()){
                population[random2].select();
                break;
            }
        }
        
        candidate1 = fittestOf(population[random], population[random2]);
        
        while(true){
            random = RNG.nextInt(6);
            if(!population[random].isSelected()){
                population[random].select();
                break;
            }
        }
        
        while(true){
            random2 = RNG.nextInt(6);
            if(!population[random2].isSelected()){
                population[random2].select();
                break;
            }
        }

        
        candidate2 = fittestOf(population[random], population[random2]);        
        return fittestOf(candidate1, candidate2);        
    }
    
    public Chromosome fittestOf(Chromosome ch1, Chromosome ch2){
        if(ch1.fitness() <= ch2.fitness()){ ch2.reset(); return ch1; }
        ch1.reset();
        return ch2;
    }
    
    public Chromosome[] singlePointCrossover(Chromosome ch1, Chromosome ch2){
        Chromosome offSpringPair[] = new Chromosome[2];        
        Chromosome offSpring1= new Chromosome();
        Chromosome offSpring2= new Chromosome();
        int random;
        
        do{ random = RNG.nextInt(6); }while(random != 0);   
        for(int i=0; i<random; i++) offSpring1.setGene(i, ch1.getGene(i));
        for(int i=random; i<6; i++) offSpring1.setGene(i, ch2.getGene(i));
          
        for(int i=0; i<random; i++) offSpring2.setGene(i, ch2.getGene(i));
        for(int i=random; i<6; i++) offSpring2.setGene(i, ch1.getGene(i));
        
        offSpringPair[0] = offSpring1;
        offSpringPair[1] = offSpring2;
        
        return offSpringPair;
    }
    
    public void generateNextGen(){        
        Chromosome candidate1;
        Chromosome candidate2;
        Chromosome[] offSprings = new Chromosome[6];
        Chromosome[] buffer = new Chromosome[2];
        
        for(int i=0; i<6; i+=2){
            candidate1 = tournamentSelection();
            candidate2 = tournamentSelection();
            
            
            buffer = singlePointCrossover(candidate1, candidate2);
            offSprings[i] = buffer[0];
            offSprings[i+1] = buffer[1];
            
            for(int x=0; x<6; x++) population[x].reset(); //reset all chromosomes to unselected. 
        }
        
       
        
        for(int j=0; j<6; j++) mutate(offSprings[j]);
        
        elitism(offSprings);
        bestSoFar = population[0];
    }
    
    public void mutate(Chromosome ch){
        double random = RNG.nextDouble();
        
        for(int i=0; i<8; i++){
            if(random < mutationRate){
                ch.bitFlip(i);
            }
        }
        
    }
    
    public void elitism(Chromosome[] offSprings){
        
        List<Chromosome> wholeList = new ArrayList<>();
        
        for(int i=0; i<6; i++) wholeList.add(offSprings[i]);
        for(int i=0; i<6; i++) wholeList.add(population[i]);
        
        Collections.sort(wholeList);
        
        
        for(int i=0; i<6; i++) population[i] = wholeList.get(i);
        
        bestThisTurn = wholeList.get(0);        
    }

    private void startGA() {
        int counter = 0;
        while(bestSoFar.fitness() > -64){
            generateNextGen();         
            counter++;
        }
        
        bestCounter = counter;
    }
}
