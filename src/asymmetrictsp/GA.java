package asymmetrictsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GA {    
    private double mutationRate = 0.15;
    Random RNG = new Random();
    public Chromosome bestSoFar = new Chromosome();
    public Chromosome bestThisTurn = new Chromosome();
    
    public static int[][] cities = {{-1, 55, 34, 32, 54, 40, 36, 40, 37, 53},
                                    {64, -1, 54, 55, 73, 45, 71, 50, 53, 52},
                                    {51, 48, -1, 41, 40, 58, 55, 33, 35, 37},
                                    {47, 46, 55, -1, 49, 45, 56, 52, 57, 55},
                                    {50, 39, 43, 52, -1, 26, 40, 39, 38, 33},
                                    {60, 49, 48, 57, 58, -1, 48, 47, 48, 48},
                                    {51, 37, 44, 43, 38, 40, -1, 64, 48, 47},
                                    {58, 41, 53, 45, 47, 43, 74, -1, 43, 42},
                                    {53, 38, 40, 33, 36, 58, 35, 30, -1, 31},
                                    {60, 39, 40, 56, 41, 41, 45, 59, 19, -1}};
    
    public Chromosome[] population = new Chromosome[8];

    public GA() {
        initChromosomes();
//        startGA();

    }
    
    public void initChromosomes(){
        for(int i=0; i<8; i++) population[i] = new Chromosome();
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
    
    public int totalFitness(){
        int total=0;        
        for(int i=0; i<8; i++) total += population[i].fitness();
        return total;
    }    
    
    public Chromosome[] singlePointCrossover(Chromosome parent1, Chromosome parent2){
        
        Chromosome offSpringPair[] = new Chromosome[2];
        Chromosome offSpring1= new Chromosome();
        Chromosome offSpring2= new Chromosome();
        offSpring1.makeOffSpring();
        offSpring2.makeOffSpring();
        int randomIndex = RNG.nextInt(10);
        int buffer;
        
        for(int i=0; i<randomIndex; i++){
            offSpring1.setGene(i, parent1.getGene(i));
        }
        
        buffer = randomIndex;
        
        for(int i=0; i<10; i++){
            if(!offSpring1.getGenes().contains(parent2.getGene(i))){
                offSpring1.setGene(buffer, parent2.getGene(i));
                buffer++;
            }
        }
        
        for(int i=0; i<randomIndex; i++){
            offSpring2.setGene(i, parent2.getGene(i));
        }
        
        buffer = randomIndex;
        
        for(int i=0; i<10; i++){
            if(!offSpring2.getGenes().contains(parent1.getGene(i))){
                offSpring2.setGene(buffer, parent1.getGene(i));
                buffer++;
            }
        }
        
        offSpringPair[0] = offSpring1;
        offSpringPair[1] = offSpring2;
        
        mutate(offSpring1);
        mutate(offSpring2);
        
        return offSpringPair;        
    }
    
    public void generateNextGen(){        
        Chromosome candidate1;
        Chromosome candidate2;
        Chromosome[] offSprings = new Chromosome[8];
        Chromosome[] buffer = new Chromosome[2];
        
        for(int i=0; i<8; i+=2){
            candidate1 = tournamentSelection();
            candidate2 = tournamentSelection();

            buffer = singlePointCrossover(candidate1, candidate2);
            offSprings[i] = buffer[0];
            offSprings[i+1] = buffer[1];
            
            for(int x=0; x<8; x++) population[x].reset(); //reset all chromosomes to unselected. 
        }
        
        
        for(int j=0; j<8; j++) mutate(offSprings[j]);
        
        elitism(offSprings);
        
        bestSoFar = population[0];        
    }
    
    public void elitism(Chromosome[] offSprings){        
        List<Chromosome> offList = new ArrayList<>();
        List<Chromosome> popList = new ArrayList<>();
        
        for(int i=0; i<8; i++) offList.add(offSprings[i]);
        for(int i=0; i<8; i++) popList.add(population[i]);
        
        Collections.sort(offList);
        Collections.sort(popList);
        
        for(int i=0; i<2; i++) population[i] = popList.get(i);
        for(int i=0; i<6; i++) population[i+2] = offList.get(i);
        
        bestThisTurn = population[0];        
    }
        
    public Chromosome tournamentSelection(){
        Chromosome candidate1, candidate2;
        int random, random2;

        while(true){
            random = RNG.nextInt(8);
            if(!population[random].isSelected()){
                population[random].select();
                break;
            }
        }
        
        while(true){
            random2 = RNG.nextInt(8);
            if(!population[random2].isSelected()){
                population[random2].select();
                break;
            }
        }
        
        candidate1 = fittestOf(population[random], population[random2]);        
        
        while(true){
            random = RNG.nextInt(8);
            if(!population[random].isSelected()){
                population[random].select();
                break;
            }
        }
        
        while(true){
            random2 = RNG.nextInt(8);
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
        
    public void mutate(Chromosome offspring){
        double random = RNG.nextDouble();
        
        if(random < mutationRate){
            int index1, index2;
            int buff1, buff2;
            
            do{
                index1 = RNG.nextInt(10);
                index2 = RNG.nextInt(10);
            } while(index1 == index2);
            
            buff1 = offspring.getGene(index1);
            buff2 = offspring.getGene(index2);
            
            offspring.setGene(index1, buff2);
            offspring.setGene(index2, buff1);
        }        
        
    }

//    private void startGA() {
//        int counter = 0;
//        while(bestSoFar.fitness() > 319){
//        //for(int i=0; i<1000; i++){
//            generateNextGen();
//            if(counter % 10000 == 0){
//                System.out.println("Generation: " + counter +"     Best So Far: " + bestSoFar.fitness() + "    Best This Turn: " + bestThisTurn.fitness());
//                for(int j=0; j<8; j++) System.out.println(population[j] + "   " + population[j].fitness());
//            }
//            counter++;
//        }
//        
//        System.out.println("BEST:  " + bestSoFar + "    FITNESS: " + bestSoFar.fitness());        
//        for(int i=0; i<8; i++) System.out.println(population[i] + "   " + population[i].fitness());
//        
//        System.out.println("\nTOTAL GENERATIONS: " + counter);
//
//        
//        
//    }
}
